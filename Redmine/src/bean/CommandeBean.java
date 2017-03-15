package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;

import model.Incident;
import model.enums.Champ;
import model.enums.Statut;
import model.system.ApplicationBDC;
import utilities.CellHelper;
import utilities.GrowlException;
import utilities.Utilities;
import utilities.enums.Side;
import utilities.interfaces.Instance;

@ManagedBean (name = "commande")
@ViewScoped
public class CommandeBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;

    /* ----- Bean de gestion ----- */
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    // Bean de chargement des incidents
    @ManagedProperty (value = "#{incident}")
    private IncidentBean incidentBean;

    /* ----- Attribus du Bean ----- */

    /** nom du p�le s�lectionn� */
    private String nomPole;
    /** Liste des incidents */
    private List<Incident> listIncidents;
    /** Liste des applications selectionn�es */
    private List<ApplicationBDC> applisSelect;
    /** Liste des applications s�lectionn�es pour le fichier Excel */
    private List<ApplicationBDC> listApplisBDC;

    /** Workbook du nouveau fichier excel */
    private Workbook wb;
    /** Helper pour la gestion des cellules */
    private CellHelper helper;
    /** Variables priv�es des indices des colonnes */
    private final int iApps = 2, iClos = 3, iResolv = 4, iCharge = 5, iBDC = 6, iAvanc = 7, iNoms = 2;
    /** Date pour le tri des incidents */
    private Date dateDebut, dateFin;
    
    private int iRows;

    /* ---------- CONSTUCTORS ---------- */

    public CommandeBean()
    {
        instanciation();
    }

    @Override
    public void instanciation()
    {
        wb = new HSSFWorkbook();
        helper = new CellHelper(wb);
        listApplisBDC = new ArrayList<>();
        applisSelect = new ArrayList<>();
        listIncidents = new ArrayList<>();
    }

    /* ---------- METHODS ---------- */

    public String charger()
    {
        try
        {
            listIncidents = incidentBean.chargerIncidents(nomPole);
        }
        catch (GrowlException e)
        {
            Utilities.updateGrowl(e.getMessage(), e.getSeverity(), e.getDetail());
            return "";
        }
        recuperationApplications();
        return "";
    }

    private void recuperationApplications()
    {
        List<String> listAppliAjoutees = new ArrayList<>();

        for (Incident incident : listIncidents)
        {
            // R�cup�ration du champ correspondant � l'application de l'incident
            String appli = incident.getMapValeurs().get(Champ.APPLICATION);

            if (appli != null && !listAppliAjoutees.contains(appli))
            {
                listAppliAjoutees.add(appli);
                listApplisBDC.add(new ApplicationBDC(appli));
            }
        }
        // Tri de la liste par ordre alphab�tique des trigrammes d'application
        Collections.sort(listApplisBDC);
    }

    /**
     * Permet de fichier le fichier excel avec le calcul de l'avanc�e du bon de commande.
     * 
     * @return
     */
    public String calcul()
    {
        // Cr�ation de la feuille de calcul - effacement si elle existe d�j�.
        Sheet sheet = wb.getSheet("Calcul");
        if (sheet == null)
            sheet = wb.createSheet("Calcul");
        else
        {
            Iterator<Row> rowIte = sheet.iterator();
            while (rowIte.hasNext())
            {
                rowIte.next();
                rowIte.remove();
            }
        }

        // Cr�ation des ent�tes de colonne
        structure(sheet);

        try
        {
            miseAJourDesDonnees(sheet);
        }
        catch (ParseException e)
        {
            Utilities.updateGrowl("Erreur dans la cr�ation du fichier Excel " + e.getClass().getSimpleName(), FacesMessage.SEVERITY_ERROR, null);
            return "";
        }
        
        miseAJourdesTotaux(sheet);
        
//        sheet.setAutoFilter(new CellRangeAddress(iNoms, iRows, iApps, iAvanc));

        // Sauvegarde du premier fichier sur C
        File newFile = new File("/ressources/test.xls");
        try
        {
            wb.write(new FileOutputStream(newFile.getName()));
            wb.close();
            listBean.setUpload(new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "BonDeDommande.xls"));
        }
        catch (IOException e)
        {
            Utilities.updateGrowl("Erreur dans la cr�ation du fichier Excel " + e.getClass().getSimpleName(), FacesMessage.SEVERITY_ERROR, null);
            return "";
        }

        return "";
    }

    /**
     * Mise des valeurs de chaque colonne avec les calculs
     * 
     * @param sheet
     * @throws ParseException
     */
    private void miseAJourDesDonnees(Sheet sheet) throws ParseException
    {
        // Variables
        Row row;
        Cell cell;
        iRows = iNoms;

        // It�ration sur les listes des applications s�l�ctionn�es
        for (ApplicationBDC appli : applisSelect)
        {
            iRows++; // Incr�mente le compteur depuis la premi�re ligne avec le nom des colonnes.
            int iformula = iRows + 1; // Il faut rajouter 1 au compteur pour les formules marchent.
            Statut statut;
            if (iRows % 2== 0)
                statut = Statut.NOUVEAU;
            else
                statut = Statut.PENDING;
            // Cr�ation d'une nouvelle ligne de cellules
            row = sheet.createRow(iRows);

            // Nom de chaque application
            cell = helper.recentrage(row.createCell(iApps));
            cell.setCellStyle(helper.getStyle(statut, Side.GAUCHE));
            cell.setCellValue(appli.getNom());

            // Nombre d'incidents clos
            cell = helper.recentrage(row.createCell(iClos));
            cell.setCellStyle(helper.getStyle(statut, null));
            cell.setCellValue(calculValeur(Statut.CLOSED, appli));

            // Nombre d'incidents resolved
            cell = helper.recentrage(row.createCell(iResolv));
            cell.setCellStyle(helper.getStyle(statut, null));
            cell.setCellValue(calculValeur(Statut.RESOLVED, appli));

            // Charge total des incidents clos et resolved
            cell = helper.recentrage(row.createCell(iCharge));
            cell.setCellStyle(formatStyle(statut, null, "0.0"));
            cell.setCellFormula("(" + CellReference.convertNumToColString(iClos) + iformula + "+" + CellReference.convertNumToColString(iResolv) + iformula + ")/" + appli.getTaux());

            // Nombre d'incidents du bon de commande
            cell = helper.recentrage(row.createCell(iBDC));
            cell.setCellStyle(helper.getStyle(statut, null));
            cell.setCellValue(appli.getBdc());

            // Pourcentage atteint du bon de commande
            cell = helper.recentrage(row.createCell(iAvanc));
            cell.setCellStyle(formatStyle(statut, Side.DROITE,"00.0%"));
            cell.setCellFormula("(" + CellReference.convertNumToColString(iClos) + iformula + "+" + CellReference.convertNumToColString(iResolv) + iformula + ")/" + CellReference.convertNumToColString(iBDC) + iformula);
        }
    }

    /**
     * Permet de calculer le nombre d'incidents cl�tur�s et resolved entre deux dates donn�es.
     * 
     * @param statut
     * @param appli
     * @return
     * @throws ParseException
     */
    private int calculValeur(Statut statut, ApplicationBDC appli) throws ParseException
    {
        int total = 0;

        for (Incident incident : listIncidents)
        {
            String appliIncident = incident.getMapValeurs().get(Champ.APPLICATION);
            String DA = incident.getMapValeurs().get(Champ.DA);
            if (appli.getNom().equals(appliIncident) && !DA.trim().equalsIgnoreCase("abandon"))
            {
                Statut statutIncident = incident.getStatut();
                if (statut == Statut.CLOSED && statut == statutIncident)
                {
                    Date dateCloture = incident.getDateCloture();

                    if (dateCloture != null && dateCloture.compareTo(dateFin) <= 0 && dateCloture.compareTo(dateDebut) >= 0)
                    	total++;
                }
                else if (statut == Statut.RESOLVED && statut == statutIncident && incident.getMapValeurs().get(Champ.DATERESOLUTION) != null)
                {

                    Date dateResolved = new SimpleDateFormat("dd/MM/yyyy").parse(incident.getMapValeurs().get(Champ.DATERESOLUTION).substring(0, 10));

                    if (dateResolved != null && dateResolved.compareTo(dateFin) <= 0 && dateResolved.compareTo(dateDebut) >= 0)
                        total++;
                }
            }
        }
        return total;
    }

    /**
     * Structure le tableau avec le nom des colonnes
     * 
     * @param sheet
     */
    private void structure(Sheet sheet)
    {
        Row row = sheet.createRow(iNoms);

        Cell cell = helper.recentrage(row.createCell(iApps));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUTGAUCHE)));
        cell.setCellValue("Applications");

        cell = helper.recentrage(row.createCell(iClos));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("Clos");

        cell = helper.recentrage(row.createCell(iResolv));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("R�solus");

        cell = helper.recentrage(row.createCell(iCharge));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("Charge/jour");

        cell = helper.recentrage(row.createCell(iBDC));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("BDC");

        cell = helper.recentrage(row.createCell(iAvanc));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUTDROITE)));
        cell.setCellValue("Avanc�e");
    }
    

    private void miseAJourdesTotaux(Sheet sheet)
    {
        
        Row row = sheet.createRow(++iRows);
        int iformula = iRows + 1; // Il faut rajouter 1 au compteur pour les formules marchent.
        
        Cell cell = helper.recentrage(row.createCell(iApps));
        cell.setCellStyle(formatStyle(Statut.WRKINPRG, Side.BAS, null));
        cell.getCellStyle().setBorderLeft(BorderStyle.THICK);
        cell.setCellValue("Totaux");
        sheet.autoSizeColumn(iApps);
        
        cell = helper.recentrage(row.createCell(iClos));
        cell.setCellStyle(helper.getStyle(Statut.WRKINPRG, Side.BAS));
        cell.setCellFormula(formuleAddition(iClos));
        sheet.autoSizeColumn(iClos);
        
        cell = helper.recentrage(row.createCell(iResolv));
        cell.setCellStyle(helper.getStyle(Statut.WRKINPRG, Side.BAS));
        cell.setCellFormula(formuleAddition(iResolv));
        sheet.autoSizeColumn(iResolv);
        
        cell = helper.recentrage(row.createCell(iCharge));
        cell.setCellStyle(formatStyle(Statut.WRKINPRG, Side.BAS, "0.0"));
        cell.setCellFormula(formuleAddition(iCharge));
        sheet.autoSizeColumn(iCharge);
        
        cell = helper.recentrage(row.createCell(iBDC));
        cell.setCellStyle(helper.getStyle(Statut.WRKINPRG, Side.BAS));
        cell.setCellFormula(formuleAddition(iBDC));
        sheet.autoSizeColumn(iBDC);
        
        cell = helper.recentrage(row.createCell(iAvanc));
        cell.setCellStyle(formatStyle(Statut.WRKINPRG, Side.BAS,"00.0%"));
        cell.getCellStyle().setBorderRight(BorderStyle.THICK);
        cell.setCellFormula("(" + CellReference.convertNumToColString(iClos) + iformula + "+" + CellReference.convertNumToColString(iResolv) + iformula + ")/"
                + CellReference.convertNumToColString(iBDC) + iformula); 
        sheet.autoSizeColumn(iAvanc);
    }
    
    private String formuleAddition(int colonne)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = iNoms+1; i < iRows; i++)
        {
            int j = i+1;
            builder.append(CellReference.convertNumToColString(colonne)).append(j).append("+");
        }
        builder.delete(builder.length()-1, builder.length());
        
        return builder.toString();
    }
    
    private CellStyle formatStyle(Statut statut, Side side, String format)
    {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(helper.getStyle(statut, side));
        if (format != null && !format.isEmpty())
        style.setDataFormat(wb.createDataFormat().getFormat(format));        
        return style;
    }
    
    private CellStyle formatBold(CellStyle style)
    {
        CellStyle retour = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        retour.cloneStyleFrom(style);
        style.setFont(font);
        return style;
        
    }


    /* ---------- ACCESS ---------- */

    /**
     * @return the listBean
     */
    public ListBean getListBean()
    {
        return listBean;
    }

    /**
     * @param listBean
     *            the listBean to set
     */
    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }

    /**
     * @return the listApplisBDC
     */
    public List<ApplicationBDC> getListApplisBDC()
    {
        return listApplisBDC;
    }

    /**
     * @return the nomPole
     */
    public String getNomPole()
    {
        return nomPole;
    }

    /**
     * @param nomPole
     *            the nomPole to set
     */
    public void setNomPole(String nomPole)
    {
        this.nomPole = nomPole;
    }

    /**
     * @return the incidentBean
     */
    public IncidentBean getIncidentBean()
    {
        return incidentBean;
    }

    /**
     * @param incidentBean
     *            the incidentBean to set
     */
    public void setIncidentBean(IncidentBean incidentBean)
    {
        this.incidentBean = incidentBean;
    }

    /**
     * @return the applisSelect
     */
    public List<ApplicationBDC> getApplisSelect()
    {
        return applisSelect;
    }

    /**
     * @param applisSelect
     *            the applisSelect to set
     */
    public void setApplisSelect(List<ApplicationBDC> applisSelect)
    {
        this.applisSelect = applisSelect;
    }

    /**
     * @return the dateDebut
     */
    public Date getDateDebut()
    {
        return dateDebut;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(Date dateDebut)
    {
        this.dateDebut = dateDebut;
    }

    /**
     * @return the dateFin
     */
    public Date getDateFin()
    {
        return dateFin;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(Date dateFin)
    {
        this.dateFin = dateFin;
    }
}