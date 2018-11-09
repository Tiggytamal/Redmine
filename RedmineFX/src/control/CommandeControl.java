package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import model.Incident;
import model.enums.Champ;
import model.enums.Statut;
import model.system.ApplicationBDC;
import utilities.CellHelper;
import utilities.enums.Side;
import utilities.interfaces.Instance;

public class CommandeControl implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;

    /* ----- Bean de gestion ----- */
    
    // Session Bean
    private ListControl listControl;

    /* ----- Attribus du Bean ----- */

    /** nom du pôle sélectionné */
    private String nomPole;
    /** Liste des applications selectionnées */
    private List<ApplicationBDC> applisSelect;
    /** Liste des applications sélectionnées pour le fichier Excel */
    private List<ApplicationBDC> listApplisBDC;

    /** Workbook du nouveau fichier excel */
    private Workbook wb;
    /** Helper pour la gestion des cellules */
    private CellHelper helper;
    /** Variables privées des indices des colonnes */
    private final int iApps = 2;
    private final int iClos = 3, iResolv = 4, iCharge = 5, iBDC = 6, iAvanc = 7, iNoms = 2;
    /** Date pour le tri des incidents */
    private Date dateDebut;
    private Date dateFin;
    
    private int iRows;

    /* ---------- CONSTUCTORS ---------- */

    public CommandeControl()
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
        listControl = ListControl.getInstance();
    }

    /* ---------- METHODS ---------- */

    /**
     * Permet de fichier le fichier excel avec le calcul de l'avancée du bon de commande.
     * 
     * @return
     */
    public String calcul(File file)
    {
        // Création de la feuille de calcul - effacement si elle existe déjà.
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

        // Création des entêtes de colonne
        structure(sheet);

        try
        {
            miseAJourDesDonnees(sheet);
        }
        catch (ParseException e)
        {
            //TODO
//            Utilities.updateGrowl("Erreur dans la création du fichier Excel " + e.getClass().getSimpleName(), Severity.SEVERITY_ERROR, null);
            return "";
        }
        
        miseAJourdesTotaux(sheet);
        
        // Sauvegarde du premier fichier sur C
//        File newFile = new File(listControl.getPath() + "bdc.xls");
        try
        {
            wb.write(new FileOutputStream(file));
            wb.close();
            listControl.setUpload(file);
        }
        catch (IOException e)
        {
            //TODO
//            Utilities.updateGrowl("Erreur dans la création du fichier Excel " + e.getClass().getSimpleName(), Severity.SEVERITY_ERROR, null);
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
        // Itération sur les listes des applications séléctionnées
        for (ApplicationBDC appli : applisSelect)
        {
            iRows++; // Incrémente le compteur depuis la première ligne avec le nom des colonnes.
            int iformula = iRows + 1; // Il faut rajouter 1 au compteur pour les formules marchent.
            Statut statut;
            if (iRows % 2== 0)
                statut = Statut.NOUVEAU;
            else
                statut = Statut.PENDING;
            // Création d'une nouvelle ligne de cellules
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
     * Permet de calculer le nombre d'incidents clôturés et resolved entre deux dates données.
     * 
     * @param statut
     * @param appli
     * @return
     * @throws ParseException
     */
    private int calculValeur(Statut statut, ApplicationBDC appli) throws ParseException
    {
        int total = 0;

        for (Incident incident : listControl.getListIncidents())
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
        cell.setCellValue("Résolus");

        cell = helper.recentrage(row.createCell(iCharge));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("Charge/jour");

        cell = helper.recentrage(row.createCell(iBDC));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUT)));
        cell.setCellValue("BDC");

        cell = helper.recentrage(row.createCell(iAvanc));
        cell.setCellStyle(formatBold(helper.getStyle(Statut.NOUVEAU, Side.HAUTDROITE)));
        cell.setCellValue("Avancée");
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