package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import model.Incident;
import model.User;
import model.enums.Champ;
import model.enums.Statut;
import model.enums.Tracker;
import utilities.CellHelper;
import utilities.DateConvert;
import utilities.GrowlException;
import utilities.Statics;
import utilities.Utilities;
import utilities.enums.Side;
import utilities.interfaces.Instance;

@ManagedBean (name = "excel")
@ViewScoped
public class ExcelBean implements Serializable, Instance
{

    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;

    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    // Attribut pour l'import

    /** Excel envoy� � l'application */
    private UploadedFile file;
    /** workbook correspondant au fichier envoy� */
    private Workbook wbIn;
    /** Workbook du nouveau fichier excel */
    private Workbook wbOut;

    /** Noms des colonnes de la page Avancement */
    private final static String OBJECTIF = "NbincidentsObjectif";
    private final static String ENTRANTS = "NbincidentsEntrants", CLOS = "NbincidentsClos", RESOLVED = "NbincidentsResolved", TRANSFERED = "NbincidentsTransf�r�s",
            ENCOURS = "NbincidentsEnCours", CIBLE = "Nbd'inc.�traiterpouratteindrelacible", AVANCEMENT = "Avancement";

    /** Identifiants pour les calculs des incidents */
    private final static String PENDING = "NbincidentsPending", PROBSRESOLVED = "NbprobsResolved", PROBSENCOURS = "Nbprobl�mesEnCours", LISTINCS = "Listeincidents",
            LISTINCSTRANS = "ListeIncidentsTransferes";

    /** Noms des colonnes de la page du stock SM9 */
    private final static String SM9NUMERO = "N� d'incident", SM9TRACKER = "Tracker", SM9APP = "Application", SM9BANQUE = "Banque", SM9ENVIRO = "Environnement",
            SM9PRIORITE = "Priorit�", SM9SUJET = "Sujet", SM9ASSIGNE = "Assign� �", SM9STATUT = "Statut de l'incident", SM9OUV = "Date d'ouverture",
            SM9PRISEENCHARGE = "Date de prise en charge", SM9RESOLUTION = "Date de r�solution", SM9REOUV = "R�-ouverture";

    /** index de la colonne avec les mois de l'ann�e */
    private final static int INDEXCOLMOIS = 1;

    /* ---------- CONSTUCTORS ---------- */

    public ExcelBean()
    {
        instanciation();
    }

    /* ---------- METHODS ---------- */

    @Override
    @PostConstruct
    public void instanciation()
    {
        wbIn = new HSSFWorkbook();
        wbOut = new HSSFWorkbook();
    }

    public String charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
    {
        // R�cup�ration du fichier envoy�
        file = event.getFile();

        // Cr�ation des deux workooks
        wbIn = WorkbookFactory.create(file.getInputstream());
        wbOut = WorkbookFactory.create(file.getInputstream());

        // Traitement de la page d'avancement
        List<Incident> incidentsATraiter = new ArrayList<>();
        try
        {
            incidentsATraiter = avancement(wbIn, wbOut);

            // Traitement de la page SM9
            sm9(incidentsATraiter, wbIn, wbOut);
        }
        catch (GrowlException e)
        {
            Utilities.updateGrowl(e.getMessage(), e.getSeverity(), e.getDetail());
            return "";
        }

        // Sauvegarde du premier fichier sur C
        File newFile = new File("/ressources/test.xls");
        wbOut.write(new FileOutputStream(newFile.getName()));
        wbIn.close();
        wbOut.close();
        listBean.setUpload(new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "test_workbook.xls"));
        return "";
    }

    /**
     * Mise � jour de la feuille d'avancement des incidents
     * 
     * @param wbIn
     * @param wbOut
     * @return
     * @throws GrowlException
     */
    private List<Incident> avancement(Workbook wbIn, Workbook wbOut) throws GrowlException
    {

        /* ------ Intialisation des variables ----- */

        // Index des cellules
        int moisEnCours = 0, iEntrants = 0, iResolved = 0, iClos = 0, iTransferes = 0, iEnCours = 0, iCible = 0, iAvancement = 0, iObjectif = 0, compteIndex = 0;

        // Cr�ation des feuilles de classeur
        Sheet sheetAvancementIn = wbIn.getSheet(Statics.sheetAvancement);
        Sheet sheetAvancementOut = wbOut.getSheet(Statics.sheetAvancement);

        CellHelper helper = new CellHelper(wbOut);

        /* ------ Calcul des variables ------ */

        // Calcul des index des lignes
        Integer[] retourIndex = recupIndexLignes(sheetAvancementIn);
        moisEnCours = retourIndex[1];

        // R�cup�ration des index des colonnes
        for (Cell cell : sheetAvancementIn.getRow(retourIndex[0]))
        {
            if (CellType.STRING == cell.getCellTypeEnum())
            {
                String value = cell.getStringCellValue().replaceAll("\\s", "").replaceAll(Statics.NL, "");
                switch (value)
                {
                    case OBJECTIF :
                        iObjectif = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case ENTRANTS :
                        iEntrants = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case CLOS :
                        iClos = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case RESOLVED :
                        iResolved = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case ENCOURS :
                        iEnCours = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case TRANSFERED :
                        iTransferes = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case CIBLE :
                        iCible = cell.getColumnIndex();
                        compteIndex++;
                        break;

                    case AVANCEMENT :
                        iAvancement = cell.getColumnIndex();
                        compteIndex++;
                        break;
                }
            }
        }

        if (compteIndex != 8)
            throw new GrowlException(FacesMessage.SEVERITY_ERROR, "Erreur dans le format du fichier Excel", null);

        /* ------ Mise � jour du fichier Excel ------ */

        // r�cup�ration de la ligne excel � mettre � jour
        Row row = sheetAvancementOut.getRow(moisEnCours);

        // Calcul du nombre d'incident
        HashMap<String, Object> retourCalcul = calculNbreIncidents();

        // Mise � jour des cellules
        miseAJourCellule(helper.recentrage(row.getCell(iEntrants)), (int) retourCalcul.get(ENTRANTS));
        miseAJourCellule(helper.recentrage(row.getCell(iClos)), (int) retourCalcul.get(CLOS));
        miseAJourCellule(helper.recentrage(row.getCell(iResolved)), (int) retourCalcul.get(RESOLVED));
        helper.recentrage(row.getCell(iTransferes)).setCellValue(cellTransfere(retourCalcul));

        // Mise � jour de la cellule des incidents en cours
        row.getCell(iEnCours).setCellValue(cellEnCours(retourCalcul));

        int indexFormula = moisEnCours + 1;
        // Mise � jour de la cellule du nombre d'incidents cible
        String clos = "NUMBERVALUE(LEFT(" + CellReference.convertNumToColString(iClos) + indexFormula + "," + "FIND(\"(\"," + CellReference.convertNumToColString(iClos)
                + indexFormula + ")-1))";
        String objectif = CellReference.convertNumToColString(iObjectif) + indexFormula;
        helper.recentrage(row.getCell(iCible))
                .setCellFormula("IF(" + clos + ">" + objectif + ",0," + CellReference.convertNumToColString(iObjectif) + indexFormula + "-" + clos + ")");
        helper.recentrage(row.getCell(iAvancement)).setCellFormula("IF(" + objectif + "<>0," + clos + "/" + objectif + ",0)");

        // Renvoie la liste des incidents � traiter
        @SuppressWarnings ("unchecked")
        List<Incident> retour = (List<Incident>) retourCalcul.get(LISTINCS);

        return retour;
    }

    /**
     * Met � jour les cellules avec les nouveaux calculs en affichant la diff�rence avec le dernier point
     * 
     * @param cell
     * @param nouveau
     */
    private void miseAJourCellule(Cell cell, int nouveau)
    {
        int avant = 0;
        if (cell.getCellTypeEnum() == CellType.STRING)
        {
            try
            {
                avant = Integer.parseInt(cell.getStringCellValue());
            }
            catch (NumberFormatException e)
            {

            }

        }
        else if (cell.getCellTypeEnum() == CellType.NUMERIC)
        {
            avant = (int) cell.getNumericCellValue();
        }

        cell.setCellValue(nouveau + " (+" + (nouveau - avant) + ")");
    }

    private void sm9(List<Incident> list, Workbook wbIn, Workbook wbOut) throws GrowlException
    {
        /* ------ Cr�ation des variables ------ */

        // Cr�ation des feuilles de classeur
        Sheet sheetSM9In = wbIn.getSheet(Statics.sheetStockSM9);
        Sheet sheetSM9Out = wbOut.getSheet(Statics.sheetStockSM9);

        // ints
        int iNumero = 0, iTracker = 0, iApplication = 0, iBanque = 0, iEnvironnement = 0, iPriorite = 0, iSujet = 0, iAssigne = 0, iStatut = 0, iDateOuv = 0,
                iDatePriseEnCharge = 0, iDateReso = 0, iReouv = 0, iLigne1 = 0, totalIndex = 0;

        // Map des incident tri�s par num�ro.
        TreeMap<String, Incident> mapOut = new TreeMap<>();

        // Cr�ation de l'Helper pour mettre � jour les cellules
        CellHelper helper = new CellHelper(wbOut);

        /* ------ Traitement ------ */

        for (Incident incident : list)
        {
            mapOut.put(incident.getMapValeurs().get(Champ.NUMERO), incident);
        }

        // R�cup�ration des indices de la page
        for (Row row : sheetSM9In)
        {
            for (Cell cell : row)
            {
                if (cell.getCellTypeEnum() == CellType.STRING)
                {
                    switch (cell.getStringCellValue())
                    {
                        case SM9NUMERO :
                            iLigne1 = cell.getRowIndex() + 1;
                            iNumero = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9TRACKER :
                            iTracker = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9APP :
                            iApplication = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9BANQUE :
                            iBanque = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9ENVIRO :
                            iEnvironnement = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9PRIORITE :
                            iPriorite = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9SUJET :
                            iSujet = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9ASSIGNE :
                            iAssigne = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9STATUT :
                            iStatut = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9OUV :
                            iDateOuv = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9PRISEENCHARGE :
                            iDatePriseEnCharge = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9RESOLUTION :
                            iDateReso = cell.getColumnIndex();
                            totalIndex++;
                            break;

                        case SM9REOUV :
                            iReouv = cell.getColumnIndex();
                            totalIndex++;
                            break;
                    }
                }
            }
            if (totalIndex == 13)
                break;
        }

        if (totalIndex != 13)
            throw new GrowlException(FacesMessage.SEVERITY_ERROR, "Feuille Sm9 mal format�e", null);

        // Premi�re boucle - r�cup�ration des commentaires existants des incidents toujours en cours
        for (int i = iLigne1; i <= sheetSM9In.getLastRowNum(); i++)
        {
            Row row = sheetSM9In.getRow(i);
            String cle = row.getCell(iNumero).getStringCellValue();

            // Si l'incident est toujours dans la liste, on r�cup�re le commentaire du fichier
            if (mapOut.keySet().contains(cle))
                mapOut.get(cle).setCommentaire(row.getCell(iStatut).getStringCellValue());

            // Une fois le commentaire r�cup�r�, on efface toute la ligne.
            for (Cell cell : sheetSM9Out.getRow(i))
            {
                cell.setCellValue("");
                cell.setCellStyle(helper.getStyle(null, null));
            }
        }

        // Intialisation de la premi�re ligne d'incident � mettre � jour
        int ligne = iLigne1;

        // It�rateur sur les cl�s de la TreeMap
        for (Iterator<String> iter = mapOut.keySet().iterator(); iter.hasNext();)
        {
            /* ------ Initialisation des variables ------ */

            // R�cup�ration de la ligne � mettre � jour
            Row row = sheetSM9Out.getRow(ligne);
            // Cr�ation de l'incident
            Incident incident = mapOut.get(iter.next());

            /* ------ Mise � jour des donn�es depuis l'incident ------ */

            // Premi�re ligne
            if (ligne == iLigne1)
            {
                for (Cell cell : row)
                {
                    if (cell.getColumnIndex() == row.getFirstCellNum())
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), Side.HAUTGAUCHE));
                    else if (cell.getColumnIndex() == row.getLastCellNum() - 1)
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), Side.HAUTDROITE));
                    else
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), Side.HAUT));
                }
            }
            // Derni�re ligne
            else if (!iter.hasNext())
            {
                
                for (Cell cell : row)
                {
                    if (cell.getColumnIndex() == row.getFirstCellNum())
                    {
                        CellStyle style = wbOut.createCellStyle();
                        style.cloneStyleFrom(helper.getStyle(incident.getStatut(), Side.GAUCHE));
                        style.setBorderBottom(BorderStyle.THICK);
                        cell.setCellStyle(style);
                    }
                    else if (cell.getColumnIndex() == row.getLastCellNum() - 1)
                    {
                        CellStyle style = wbOut.createCellStyle();
                        style.cloneStyleFrom(helper.getStyle(incident.getStatut(), Side.DROITE));
                        style.setBorderBottom(BorderStyle.THICK);
                        cell.setCellStyle(style);
                    }
                    else
                    {
                        CellStyle style = wbOut.createCellStyle();
                        style.cloneStyleFrom(helper.getStyle(incident.getStatut(), null));
                        style.setBorderBottom(BorderStyle.THICK);
                        cell.setCellStyle(style);
                    }
                }
            }
            // Toutes les autres
            else
            {
                for (Cell cell : row)
                {
                    if (cell.getColumnIndex() == row.getFirstCellNum())
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), Side.GAUCHE));
                    else if (cell.getColumnIndex() == row.getLastCellNum() - 1)
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), Side.DROITE));
                    else
                        cell.setCellStyle(helper.getStyle(incident.getStatut(), null));
                }
            }

            // Mise � jour des donn�es dans chaque cellule
            row.getCell(iNumero).setCellValue(incident.getMapValeurs().get(Champ.NUMERO));
            row.getCell(iTracker).setCellValue(incident.getStatut().toString());
            row.getCell(iApplication).setCellValue(incident.getMapValeurs().get(Champ.APPLICATION));
            row.getCell(iBanque).setCellValue(incident.getMapValeurs().get(Champ.BANQUE));
            row.getCell(iEnvironnement).setCellValue(incident.getMapValeurs().get(Champ.ENVIRONNEMENT));
            row.getCell(iPriorite).setCellValue(incident.getPriorite());
            row.getCell(iSujet).setCellValue(incident.getSujet());
            User responsable = incident.getResponsable();
            row.getCell(iAssigne).setCellValue(responsable.getPrenom() + " " + responsable.getNom());
            helper.setFontColor(row.getCell(iStatut), IndexedColors.RED).setCellValue(incident.getCommentaire());
            row.getCell(iDateOuv).setCellValue(incident.getMapValeurs().get(Champ.DATEOUVERTURE));
            row.getCell(iDatePriseEnCharge).setCellValue(incident.getMapValeurs().get(Champ.DATEPRISENCHARGE));
            row.getCell(iDateReso).setCellValue(incident.getMapValeurs().get(Champ.DATERESOLUTION));
            row.getCell(iReouv).setCellValue(incident.getMapValeurs().get(Champ.REOUVERTURE));

            // Incr�mentation du num�ro de ligne
            ligne++;
        }
    }

    /**
     * Calcul de la cellule d'affichage des incidents transf�r�s.
     * 
     * @param compte
     * @param listIncidents
     * @return
     */
    private String cellTransfere(HashMap<String, Object> map)
    {
        // Cr�ation du StringBuilder
        StringBuilder retour = new StringBuilder("Nombre d'incidents : ");

        // Ajout du nombre d'incident transf�r�
        retour.append(map.get(TRANSFERED)).append("\n");

        // It�ration de la liste des incidents transf�r�s

        @SuppressWarnings ("unchecked")
        List<Incident> list = (List<Incident>) map.get(LISTINCSTRANS);
        for (int i = 0; i < list.size(); i++)
        {
            Incident incident = list.get(i);
            // Ajout du num�ro de l'incident et du groupe de transfert
            retour.append(incident.getMapValeurs().get(Champ.NUMERO)).append(" - ").append(incident.getMapValeurs().get(Champ.GRTRANSFERT));
            if (i < list.size() - 1)
                retour.append(Statics.NL);
        }
        return retour.toString();
    }

    /**
     * Calcul de la cellule des incidents en cours
     * 
     * @param map
     * @return
     */
    private String cellEnCours(HashMap<String, Object> map)
    {
        // Cr�ation du StringBuilder
        StringBuilder retour = new StringBuilder();

        // R�cup�ration des comptes
        int enCours = (int) map.get(ENCOURS);
        int pending = (int) map.get(PENDING);
        int resolved = (int) map.get(RESOLVED);
        int total = enCours + pending + resolved;
        int probsEnCours = (int) map.get(PROBSENCOURS);
        int probsResolved = (int) map.get(PROBSRESOLVED);
        int totalProbs = probsEnCours + probsResolved;

        // Ajout des donn�es
        retour.append(total).append(" incidents ( ");
        retour.append(resolved).append(" resolved, ").append(enCours).append(" working, ").append(pending).append(" pending )\n");
        retour.append(totalProbs).append(" probl�mes dont ").append(probsResolved).append(" resolved ");

        return retour.toString();
    }

    /**
     * Permet de calculer les indes des lignes pour la mise � jour de la feuille excel
     * 
     * @param sheet
     * @return
     * @throws GrowlException
     */
    private Integer[] recupIndexLignes(Sheet sheet) throws GrowlException
    {
        Integer[] retour = new Integer[2];

        // It�ration sur les cellules de la colonne avec les noms des mois
        for (Row row : sheet)
        {
            Cell cell = row.getCell(INDEXCOLMOIS);
            if (cell != null && cell.getCellTypeEnum().equals(CellType.NUMERIC) && cell.getNumericCellValue() > 40000)
            {
                LocalDate date = (LocalDate) DateConvert.convert(LocalDate.class, cell.getDateCellValue());

                // lorsque que l'on trouve la premi�re ligne avec l'ann�e en cours, on prend l'index + 1 pour chercher
                // le nom des colonnes
                if (retour[0] == null && Statics.TODAY.getYear() == date.getYear())
                    retour[0] = cell.getRowIndex() - 1;

                // r�cup�ration de l'incdes de la ligne � mettre jour pour ce mois.
                if (Statics.TODAY.getMonth().equals(date.getMonth()) && Statics.TODAY.getYear() == date.getYear())
                    retour[1] = cell.getRowIndex();
            }
        }

        if (retour[0] == null || retour[1] == null)
            throw new GrowlException(null, "Mauvaise formation du fichier Excel - Il n'y a pas de ligne pour le mois en cours", null);

        return retour;
    }

    /**
     * M�thode de calcul du nombre d'incidents
     * 
     * @return
     *         Liste des r�sultats
     */
    private HashMap<String, Object> calculNbreIncidents()
    {
        /* ----- Variables locales ----- */

        // Formatteur de date
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // LocalDates
        LocalDate datePriseEnCharge = null, dateCloture = null, dateTransfert = null;
        // Strings
        String dateTransfertString = null, datePriseEnChargeString = null, da = null;
        // ints
        int totalDuMois = 0, totalResolved = 0, totalClosed = 0, totalWorkInPrg = 0, totalPending = 0, totalTransfered = 0, totalProbsResolved = 0, totalProbs = 0;
        // List<Incident>
        List<Incident> incsTransferes = new ArrayList<>(), incsATraiter = new ArrayList<>();
        // Enum�rations
        Tracker tracker = null;
        Statut statut = null;
        // Bool�ens
        boolean estAbandonne = false, estIncident = false, estProbleme = false;
        // Objet de retour
        HashMap<String, Object> retour = new HashMap<>();

        /* ----- It�ration sur les incidents ----- */

        for (Incident incident : listBean.getListIncidents())
        {
            // Initialisation des variables
            datePriseEnChargeString = incident.getMapValeurs().get(Champ.DATEPRISENCHARGE);
            da = incident.getMapValeurs().get(Champ.DA);
            dateTransfertString = incident.getMapValeurs().get(Champ.DATETRANSFERT);
            tracker = incident.getTracker();
            statut = incident.getStatut();
            estAbandonne = Statics.ABANDON.equalsIgnoreCase(da);
            estIncident = (tracker == Tracker.INCIDENT || tracker == Tracker.DEMANDE);
            estProbleme = (tracker == Tracker.PROBLEME);

            // Pas de d�compte si ce n'est pas un incident ou un probl�me et si l'incident a un num�ro de DA � abandon
            if (estAbandonne || (!estIncident && !estProbleme))
                continue;

            // Incr�mentation des compteurs et ajout dans les listes des incidents en cours.
            switch (statut)
            {
                case RESOLVED :
                    if (estIncident)
                    {
                        totalResolved++;
                        totalClosed++;
                    }
                    else if (estProbleme)
                        totalProbsResolved++;
                    incsATraiter.add(incident);
                    break;

                case NOUVEAU :
                    if (estIncident)
                        totalWorkInPrg++;
                    else if (estProbleme)
                        totalProbs++;
                    incsATraiter.add(incident);
                    break;

                case PENDING :
                    if (estIncident)
                        totalPending++;
                    else if (estProbleme)
                        totalProbs++;
                    incsATraiter.add(incident);
                    break;

                case WRKINPRG :
                    if (estIncident)
                        totalWorkInPrg++;
                    else if (estProbleme)
                        totalProbs++;
                    incsATraiter.add(incident);
                    break;

                case REFERRED :
                    if (estIncident)
                        totalWorkInPrg++;
                    else if (estProbleme)
                        totalProbs++;
                    incsATraiter.add(incident);
                    break;

                default :
                    break;
            }

            // Incr�mentation des incidents transf�r�s
            if (statut == Statut.TRANSFERED && dateTransfertString != null && dateTransfertString.length() > 9)
            {
                dateTransfert = LocalDate.parse(dateTransfertString.substring(0, 10), f);

                if (dateTransfert.getYear() == Statics.TODAY.getYear() && dateTransfert.getMonth().equals(Statics.TODAY.getMonth()))
                {
                    totalTransfered++;
                    incsTransferes.add(incident);
                }
            }

            // Elimination des incidents qui ont une date mal format�e
            if (datePriseEnChargeString != null && datePriseEnChargeString.length() > 9)
            {
                datePriseEnCharge = LocalDate.parse(datePriseEnChargeString.substring(0, 10), f);

                // Incr�mentation des incidents du mois
                if (estIncident && datePriseEnCharge.getYear() == Statics.TODAY.getYear() && datePriseEnCharge.getMonth().equals(Statics.TODAY.getMonth()))
                {
                    totalDuMois++;
                    continue;
                }

                // Incr�mentation des incidents clos
                if (estIncident && statut == Statut.CLOSED && incident.getDateCloture() != null)
                {
                    dateCloture = DateConvert.localDate(incident.getDateCloture());
                    if (dateCloture.getYear() == Statics.TODAY.getYear() && dateCloture.getMonth().equals(Statics.TODAY.getMonth()))
                        totalClosed++;
                }
            }
        }

        retour.put(ENTRANTS, totalDuMois);
        retour.put(RESOLVED, totalResolved);
        retour.put(CLOS, totalClosed);
        retour.put(ENCOURS, totalWorkInPrg);
        retour.put(PENDING, totalPending);
        retour.put(TRANSFERED, totalTransfered);
        retour.put(PROBSRESOLVED, totalProbsResolved);
        retour.put(PROBSENCOURS, totalProbs);
        retour.put(LISTINCS, incsATraiter);
        retour.put(LISTINCSTRANS, incsTransferes);
        return retour;
    }

    /* ---------- ACCESS ---------- */

    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public ListBean getListBean()
    {
        return listBean;
    }

    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }
}