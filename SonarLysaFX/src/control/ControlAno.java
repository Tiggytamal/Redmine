package control;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.loginconnue;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import model.Anomalie;
import model.InfoClarity;
import model.RespService;
import model.enums.Environnement;
import model.enums.Matiere;
import model.enums.TypeCol;
import model.enums.TypeParam;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de getion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class ControlAno extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    // Liste des indices des colonnes
    private int colDir;
    private int colDepart;
    private int colService;
    private int colResp;
    private int colClarity;
    private int colLib;
    private int colCpi;
    private int colEdition;
    private int colLot;
    private int colEnv;
    private int colAno;
    private int colEtat;
    private int colSec;
    private int colRemarque;
    private int colVer;
    private int colDateCrea;
    private int colDateRel;
    private int colMatiere;

    // Liste des noms de colonnes
    private String direction;
    private String departement;
    private String service;
    private String respService;
    private String clarity;
    private String libelle;
    private String cpi;
    private String edition;
    private String lot;
    private String env;
    private String anomalie;
    private String etat;
    private String securite;
    private String remarque;
    private String traite;
    private String version;
    private String dateCreation;
    private String dateRelance;
    private String matiere;

    // Nom de la feuillle avec les naomalies en cours
    private static final String SQ = "SUIVI Qualit�";
    private static final String AC = "Anomalies closes";
    private static final String CLOSE = "Close";
    private static final String ABANDONNEE = "Abandonn�e";
    private static final String SECURITEKO = "X";
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";
    private static final int NOMBRECOL = 18;
    private static final String AVERIFIER = "A v�rifier";
    private String lienslots;
    private String liensAnos;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlAno(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * 
     * @return
     */
    public List<Anomalie> listAnomaliesSurLotsCrees()
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();

        // It�ration sur chaque ligne pour cr�er les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // Cr�ation de l'anomalie
            retour.add(creerAnodepuisExcel(row));
        }
        return retour;
    }

    /**
     * Permet de cr�er les feuille des anomalies par version et retourne les anomalies � cr�er
     * 
     * @param nomSheet
     * @param anoAcreer
     * @param anoDejacrees 
     * @return
     * @throws IOException
     */
    public List<Anomalie> createSheetError(String nomSheet, List<Anomalie> anoAcreer, List<Anomalie> anoDejacrees)
    {
        // 1. Variables
        
        // Cr�ation de la feuille de calcul
        Sheet sheet = wb.getSheet(nomSheet);

        // Liste retour des anomalies � cr�er
        List<Anomalie> retour = new ArrayList<>();

        // 2. Sauvegarde donn�es existantes. On it�re pour r�cup�rer tous les num�ros de lot d�j� abandon�s, puis suppression de la feuille
        List<String> lotsAbandon = new ArrayList<>();
        if (sheet != null)
        {
            Iterator<Row> iter = sheet.rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                Cell cellLot = row.getCell(Index.LOTI.ordinal());
                Cell cellT = row.getCell(Index.TRAITEI.ordinal());
                if (cellLot.getCellTypeEnum() == CellType.STRING && cellT.getCellTypeEnum() == CellType.STRING && cellT.getStringCellValue().equals("A"))
                    lotsAbandon.add(row.getCell(Index.LOTI.ordinal()).getStringCellValue());
            }
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        }

        // 3. G�n�ration de la nouvelle feuille
        
        // Recr�ation de la feuille
        sheet = wb.createSheet(nomSheet);
        Cell cell;
        Row row;

        // Cr�ation du style des titres
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);

        // Cr�ation des noms des colonnes
        Row titres = sheet.createRow(0);
        for (Index index : Index.values())
        {
            cell = titres.createCell(index.ordinal());
            cell.setCellStyle(styleTitre);
            switch (index)
            {
                case LOTI:
                    cell.setCellValue(lot);
                    break;
                case EDITIONI:
                    cell.setCellValue(edition);
                    break;
                case ENVI:
                    cell.setCellValue(env);
                    break;
                case TRAITEI:
                    cell.setCellValue(traite);
                    break;
            }
        }
        
        // 4. Ajout anomalies d�j� cr��es
        for (Anomalie ano : anoDejacrees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneVersion(row, ano, IndexedColors.WHITE, "O");
        }
        
        // 5. It�ration sur les anomalies � cr�er. Si elles sont d�j� dans les anomalies abadonn�es, on cr��e une ligne � l'�tat abandon, sinon on cr�e une ligne �
        // l'�tat non trait� et on ajoute celle-ci aux anomalies � cr�er.
        for (Anomalie ano : anoAcreer)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (lotsAbandon.contains(ano.getLot()))
            {
                creerLigneVersion(row, ano, IndexedColors.WHITE, "A");
            }
            else
            {
                creerLigneVersion(row, ano, IndexedColors.LIGHT_YELLOW, "N");
                retour.add(ano);
            }
        }

        sheet.autoSizeColumn(Index.LOTI.ordinal());
        sheet.autoSizeColumn(Index.EDITIONI.ordinal());
        sheet.autoSizeColumn(Index.ENVI.ordinal());
        return retour;
    }

    /**
     * 
     * @param fichier
     * @return
     * @throws IOException
     */
    public Sheet sauvegardeFichier(String fichier) throws IOException
    {
        // R�cup�ration feuille principale existante
        Sheet retour = wb.getSheet(SQ);
        if (retour != null)
        {
            // Cr�ation du fichier de sauvegarde et effacement de la feuille
            wb.write(new FileOutputStream(new StringBuilder(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString()));
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }

        // Cr�ation des lignes de titres
        retour = wb.createSheet(SQ);
        creerLigneTitres(retour);
        return retour;
    }

    /**
     * Gestion de la feuille principale des anomalies. Maj des anciennes plus cr�ation des nouvelles
     * 
     * @param anoAajouter
     * @param lotsSecurite
     * @param matiere2 
     * @param anoAajouter2
     * @param lotsEnErreur
     * @throws IOException
     * @throws InvalidFormatException
     * @throws EncryptedDocumentException
     */
    public void majFeuillePrincipale(List<Anomalie> lotsEnAno, List<Anomalie> anoAajouter, Set<String> lotsEnErreurSonar, Set<String> lotsSecurite, Set<String> lotsRelease, Sheet sheet, Matiere matiere) throws IOException
    {
        // R�cup�ration feuille et liste des anomalies closes
        Map<String, Anomalie> anoClose = new HashMap<>();
        Sheet sheetClose = saveAnomaliesCloses(anoClose);
        
        Map<String, RespService> mapRespService = fichiersXML.getMapRespService();

        // Mise � jour anomalies d�j� cr��es
        for (Anomalie ano : lotsEnAno)
        {
            Row row;
            ano.getMatieres().add(matiere);
            String anoLot = ano.getLot().substring(4);
            IndexedColors couleur;

            // Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(SECURITEKO);
            
            // Si une anomalie est close dans RTC, on la transfert sur l'autre feuille.
            if (CLOSE.equalsIgnoreCase(ano.getEtat()) || ABANDONNEE.equals(ano.getEtat()))
            {
                row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.WHITE);
                continue;
            }
            
            controleChefDeService(ano, mapRespService);

            // Mise en vert des anomalies avec un Quality Gate bon
            if (!lotsEnErreurSonar.contains(anoLot))
            {
                couleur = IndexedColors.LIGHT_GREEN;
            }
            else
            {
                // Les lots release sont en jaune
                if (lotsRelease.contains(anoLot))
                {
                    ano.setVersion(RELEASE);
                    couleur = IndexedColors.LIGHT_YELLOW;
                }
                else
                {
                    ano.setVersion(SNAPSHOT);
                    couleur = IndexedColors.WHITE;
                }
            }
            
            if (AVERIFIER.equals(ano.getEtat()))
                couleur = IndexedColors.GREY_25_PERCENT;
            
            // Remise de la couleur � orange si le lot n'a pas encore �t� trait�
            if(!ano.isTraitee())
                couleur = IndexedColors.LIGHT_ORANGE;
            
            // Cr�ation de la ligne
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            creerLigneSQ(row, ano, couleur);
        }

        ajouterNouvellesAnos(sheet, anoAajouter, anoClose, lotsSecurite, lotsRelease, matiere);
        ajouterAnomaliesCloses(sheetClose, anoClose);

        autosizeColumns(sheet);
        autosizeColumns(sheetClose);
        write();
    }

    /**
     * Mise � jour des fichiers pour les anomalies comprenant plusieures type de mati�re
     * 
     * @param anoMultiple
     */
    public void majMultiMatiere(List<String> anoMultiple)
    {
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Probl�me r�cup�ration feuillle excel principale");
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            if (anoMultiple.contains(sheet.getRow(i).getCell(colLot).getStringCellValue()))
                sheet.getRow(i).getCell(colMatiere).setCellValue(Matiere.JAVA.toString() + " - " + Matiere.DATASTAGE);
        }
        
    }

    @Override
    protected void initColonnes()
    {
        // Intialisation noms des colonnes
        Map<TypeCol, String> nomColonnes = proprietesXML.getMapColonnes();
        direction = nomColonnes.get(TypeCol.DIRECTION);
        departement = nomColonnes.get(TypeCol.DEPARTEMENT);
        service = nomColonnes.get(TypeCol.SERVICE);
        respService = nomColonnes.get(TypeCol.RESPSERVICE);
        clarity = nomColonnes.get(TypeCol.CLARITY);
        libelle = nomColonnes.get(TypeCol.LIBELLE);
        cpi = nomColonnes.get(TypeCol.CPI);
        edition = nomColonnes.get(TypeCol.EDITION);
        lot = nomColonnes.get(TypeCol.LOT);
        env = nomColonnes.get(TypeCol.ENV);
        anomalie = nomColonnes.get(TypeCol.ANOMALIE);
        etat = nomColonnes.get(TypeCol.ETAT);
        securite = nomColonnes.get(TypeCol.SECURITE);
        remarque = nomColonnes.get(TypeCol.REMARQUE);
        version = nomColonnes.get(TypeCol.VERSION);
        dateCreation = nomColonnes.get(TypeCol.DATECREATION);
        dateRelance = nomColonnes.get(TypeCol.DATERELANCE);
        traite = nomColonnes.get(TypeCol.TRAITE);
        matiere = nomColonnes.get(TypeCol.MATIERE);

        // Initialisation des parties constantes des liens
        Map<TypeParam, String> proprietes = proprietesXML.getMapParams();
        lienslots = proprietes.get(TypeParam.LIENSLOTS);
        liensAnos = proprietes.get(TypeParam.LIENSANOS);
    }

    @Override
    protected void calculIndiceColonnes()
    {
        // R�cup�ration de la premi�re feuille
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier n'a pas de page Suivi Qualit�");

        titres = sheet.getRow(0);
        int nbreCol = 0;

        // R�cup�ration des indices de colonnes
        for (Cell cell : titres)
        {
            if (cell.getCellTypeEnum() != CellType.STRING)
                continue;

            if (cell.getStringCellValue().equals(direction))
            {
                colDir = cell.getColumnIndex();
                testMax(colDir);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(departement))
            {
                colDepart = cell.getColumnIndex();
                testMax(colDepart);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(service))
            {
                colService = cell.getColumnIndex();
                testMax(colService);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(respService))
            {
                colResp = cell.getColumnIndex();
                testMax(colResp);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(clarity))
            {
                colClarity = cell.getColumnIndex();
                testMax(colClarity);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(libelle))
            {
                colLib = cell.getColumnIndex();
                testMax(colLib);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(cpi))
            {
                colCpi = cell.getColumnIndex();
                testMax(colCpi);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(edition))
            {
                colEdition = cell.getColumnIndex();
                testMax(colEdition);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(lot))
            {
                colLot = cell.getColumnIndex();
                testMax(colLot);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(env))
            {
                colEnv = cell.getColumnIndex();
                testMax(colEnv);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(anomalie))
            {
                colAno = cell.getColumnIndex();
                testMax(colAno);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(etat))
            {
                colEtat = cell.getColumnIndex();
                testMax(colEtat);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(securite))
            {
                colSec = cell.getColumnIndex();
                testMax(colSec);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(remarque))
            {
                colRemarque = cell.getColumnIndex();
                testMax(colRemarque);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(version))
            {
                colVer = cell.getColumnIndex();
                testMax(colVer);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(dateCreation))
            {
                colDateCrea = cell.getColumnIndex();
                testMax(colDateCrea);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(dateRelance))
            {
                colDateRel = cell.getColumnIndex();
                testMax(colDateRel);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(matiere))
            {
                colMatiere = cell.getColumnIndex();
                testMax(colMatiere);
                nbreCol++;
            }

        }
        if (nbreCol != NOMBRECOL)
        {
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier excel est mal configur�, v�rifier les colonnes de celui-ci");
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * 
     * @param sheetClose
     * @param anoClose
     */
    private void ajouterAnomaliesCloses(Sheet sheetClose, Map<String, Anomalie> anoClose)
    {
        Row row;
        for (Anomalie ano : anoClose.values())
        {
            row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
            creerLigneSQ(row, ano, IndexedColors.WHITE);
        }
    }
    
    /**
     * 
     * @param row
     * @param ano
     * @param couleur
     */
    private void creerLigneSQ(Row row, Anomalie ano, IndexedColors couleur)
    {
        // 1. Contr�les
        if (couleur == null || row == null || ano == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas �tre nuls");

        // 2. Helper
        CellHelper helper = new CellHelper(wb);

        // 3. Cr�ation des styles
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle date = wb.createCellStyle();
        date.cloneStyleFrom(centre);
        date.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        // 4. Valorisation des cellules avec les donn�es de l'anomalie
        
        // Contr�le Clarity et mise � jour donn�es
        controleClarity(ano);

        // Direction
        valoriserCellule(row, colDir, normal, ano.getDirection(), ano.getDirectionComment());
        
        // D�partement
        valoriserCellule(row, colDepart, normal, ano.getDepartement(), ano.getDepartementComment());
        
        // Service
        valoriserCellule(row, colService, normal, ano.getService(), ano.getServiceComment());
        
        // Responsable service
        valoriserCellule(row, colResp, normal, ano.getResponsableService(), ano.getResponsableServiceComment());
        
        // code projet Clarity
        valoriserCellule(row, colClarity, normal, ano.getProjetClarity(), ano.getProjetClarityComment());
        
        // libelle projet
        valoriserCellule(row, colLib, normal, ano.getLibelleProjet(), ano.getLibelleProjetComment());
        
        // Cpi du lot
        valoriserCellule(row, colCpi, normal, ano.getCpiProjet(), ano.getCpiProjetComment());
        
        // Edition
        valoriserCellule(row, colEdition, centre, ano.getEdition(), ano.getEditionComment());
        
        // Num�ro du lot
        Cell cell = valoriserCellule(row, colLot, centre, ano.getLot(), ano.getLotComment());
        ajouterLiens(cell, lienslots, ano.getLot().substring(4));

        // Environnement
        valoriserCellule(row, colEnv, centre, ano.getEnvironnement(), ano.getEnvironnementComment());

        // Num�ros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnomalie();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);
            // Rajout de "&id=", car cela fait planter la d�s�rialisation du fichier de param�tres
            ajouterLiens(cell, liensAnos + "&id=", String.valueOf(numeroAno));
        }
        
        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtat(), ano.getEtatComment());
        
        // Anomalie de s�curite
        valoriserCellule(row, colSec, centre, ano.getSecurite(), ano.getSecuriteComment());
        
        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque(), ano.getRemarqueComment());
        
        // Version composants
        valoriserCellule(row, colVer, centre, ano.getVersion(), ano.getVersionComment());

        // Date cr�ation
        valoriserCellule(row, colDateCrea, date, ano.getDateCreation(), ano.getDateCreationComment());

        // Date relance
        valoriserCellule(row, colDateRel, date, ano.getDateRelance(), ano.getDateRelanceComment());
        
        // Matiere
        valoriserCellule(row, colMatiere, centre, ano.getMatieresString(), ano.getMatieresComment());
    }

    /**
     * 
     * @param row
     * @param ano
     * @param couleur
     * @param traite
     */
    private void creerLigneVersion(Row row, Anomalie ano, IndexedColors couleur, String traite)
    {
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        valoriserCellule(row, Index.LOTI.ordinal(), centre, ano.getLot(), null);
        valoriserCellule(row, Index.EDITIONI.ordinal(), helper.getStyle(couleur), ano.getEdition(), null);
        valoriserCellule(row, Index.ENVI.ordinal(), centre, ano.getEnvironnement().toString(), null);
        valoriserCellule(row, Index.TRAITEI.ordinal(), centre, traite, null);
    }

    /**
     * 
     * @param sheet
     */
    private void creerLigneTitres(Sheet sheet)
    {
        // Cr�ation de la ligne de titres
        Row titresNew = sheet.createRow(0);

        // On it�re sur la ligne de titres
        for (int i = 0; i < titres.getLastCellNum(); i++)
        {
            Cell newCell = titresNew.createCell(i);
            Cell oldCell = titres.getCell(i);

            // On continue si la cellule du titre est nulle
            if (oldCell == null)
            {
                continue;
            }

            // Copie du style des cellules
            CellStyle newCellStyle = wb.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // Copie des titres
            newCell.setCellValue(oldCell.getStringCellValue());
            switch (oldCell.getCellTypeEnum())
            {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
                case _NONE:
                    break;
            }
        }
    }

    /**
     * 
     * @param cell
     * @param baseAdresse
     * @param variable
     */
    private void ajouterLiens(Cell cell, String baseAdresse, String variable)
    {
        if (cell == null || baseAdresse == null || baseAdresse.isEmpty())
            throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent �tre nulles");
        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFont(font);
        cell.setCellStyle(style);
        link.setAddress(baseAdresse + variable);
        cell.setHyperlink(link);
    }

    /**
     * 
     * @param sheet
     * @param anoAajouter
     * @param lotsSecurite
     * @param lotsRelease
     * @param matiere 
     */
    private void ajouterNouvellesAnos(Sheet sheet, List<Anomalie> anoAajouter, Map<String, Anomalie> mapAnoCloses, Set<String> lotsSecurite, Set<String> lotsRelease, Matiere matiere)
    {
        for (Anomalie ano : anoAajouter)
        {
            ano.getMatieres().add(matiere);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            String anoLot = ano.getLot().substring(4);

            // Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(SECURITEKO);

            // Ajout de la donn�e de version
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Ajout de la date de cr�ation � la date du jour
            ano.setDateCreation(LocalDate.now());

            // Cr�ation de la ligne
            if (mapAnoCloses.keySet().contains(ano.getLot()))
            {
                Anomalie anoClose = mapAnoCloses.get(ano.getLot());
                ano.setDateCreation(anoClose.getDateCreation());
                ano.setDateRelance(anoClose.getDateRelance());
                ano.setRemarque(anoClose.getRemarque());
                ano.setNumeroAnomalie(anoClose.getNumeroAnomalie());
                ano.setEtat(AVERIFIER);
                creerLigneSQ(row, ano, IndexedColors.GREY_25_PERCENT);
                mapAnoCloses.remove(ano.getLot());
            }
            else
                creerLigneSQ(row, ano, IndexedColors.LIGHT_ORANGE);
        }
    }

    /**
     * Enregistre toutes les anomalies de la feuille des anomalies closes, puis retourne une feuille vide pour les traitements suivants.
     * 
     * @param anoClose
     * @return
     */
    private Sheet saveAnomaliesCloses(Map<String, Anomalie> anoClose)
    {
        // R�cup�ration de la feuille des ano closes.
        Sheet retour = wb.getSheet(AC);
        if (retour != null)
        {
            // It�ration sur les lignes sauf la premi�re qui correspond aux titres. R�cup�ration des informations des anomalies
            for (Iterator<Row> iter = retour.rowIterator(); iter.hasNext();)
            {
                Row row = iter.next();
                if (row.getRowNum() == 0)
                    continue;
                Anomalie ano = creerAnodepuisExcel(row);
                anoClose.put(ano.getLot(), ano);
            }
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }
        retour = wb.createSheet(AC);
        creerLigneTitres(retour);
        return retour;
    }

    /**
     * Contr�le si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    private Anomalie controleClarity(Anomalie ano)
    {
        // R�cup�ration infox Clarity depuis fichier Excel
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();
        String anoClarity = ano.getProjetClarity();
        Set<String> keyset = map.keySet();

        // V�rification si le code Clarity de l'anomalie est bien dans la map
        if (keyset.contains(anoClarity))
        {
            InfoClarity info = map.get(anoClarity);
            ano.setDepartement(info.getDepartement());
            ano.setDirection(info.getDirection());
            ano.setService(info.getService());
            return ano;
        }

        // Sinon on it�re sur les clefs en supprimant les indices de lot, et on prend la premi�re clef correspondante
        for (String key : keyset)
        {
            // On retire les deux derni�res lettres pour les clefs de plus de 6 caract�res finissants par 0[1-9]
            if (controleKey(anoClarity, key))
            {
                InfoClarity info = map.get(key);
                ano.setDepartement(info.getDepartement());
                ano.setDirection(info.getDirection());
                ano.setService(info.getService());
                return ano;
            }
        }

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        loginconnue.warn("Code Clarity inconnu : " + anoClarity + " - Lot : " + ano.getLot());
        return ano;
    }
    
    /**
     * Met � jour le responsable de service depuis les informations du fichier XML, si le service est renseign�.<br>
     * Remonte un warning si le service n'est pas connu
     * @param ano
     * @param mapRespService
     */
    private void controleChefDeService(Anomalie ano, Map<String, RespService> mapRespService)
    {
        String anoServ = ano.getService();
        if (anoServ == null || anoServ.isEmpty())
            return;
        
        if (mapRespService.keySet().contains(anoServ))
            ano.setResponsableService(mapRespService.get(anoServ).getNom());
        else
            loginconnue.warn("Pas de responsable de service trouv� pour ce service : " + ano.getService());
    }

    /**
     * 
     * @param row
     * @return
     */
    private Anomalie creerAnodepuisExcel(Row row)
    {
        Anomalie retour = new Anomalie();
        retour.setDirection(getCellStringValue(row, colDir));
        retour.setDirectionComment(getCellComment(row, colDir));
        retour.setDepartement(getCellStringValue(row, colDepart));
        retour.setDepartementComment(getCellComment(row, colDepart));
        retour.setService(getCellStringValue(row, colService));
        retour.setServiceComment(getCellComment(row, colService));
        retour.setResponsableService(getCellStringValue(row, colResp));
        retour.setResponsableServiceComment(getCellComment(row, colResp));
        retour.setProjetClarity(getCellStringValue(row, colClarity));
        retour.setProjetClarityComment(getCellComment(row, colClarity));
        retour.setLibelleProjet(getCellStringValue(row, colLib));
        retour.setLibelleProjetComment(getCellComment(row, colLib));
        retour.setCpiProjet(getCellStringValue(row, colCpi));
        retour.setCpiProjetComment(getCellComment(row, colCpi));
        retour.setEdition(getCellStringValue(row, colEdition));
        retour.setEditionComment(getCellComment(row, colEdition));
        retour.setLot(getCellStringValue(row, colLot));
        retour.setLotComment(getCellComment(row, colLot));
        retour.setEnvironnement(Environnement.getEnvironnement(getCellStringValue(row, colEnv)));
        retour.setEnvironnementComment(getCellComment(row, colEnv));
        retour.setNumeroAnomalie(getCellNumericValue(row, colAno));
        retour.setNumeroAnomalieComment(getCellComment(row, colAno));
        retour.setEtat(getCellStringValue(row, colEtat));
        retour.setEtatComment(getCellComment(row, colEtat));
        retour.setSecurite(getCellStringValue(row, colSec));
        retour.setSecuriteComment(getCellComment(row, colSec));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setRemarqueComment(getCellComment(row, colRemarque));
        retour.setVersion(getCellStringValue(row, colVer));
        retour.setVersionComment(getCellComment(row, colVer));
        retour.setDateCreation(getCellDateValue(row, colDateCrea));
        retour.setDateCreationComment(getCellComment(row, colDateCrea));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setDateRelanceComment(getCellComment(row, colDateRel));
        retour.setMatieresString(getCellStringValue(row, colMatiere));
        retour.setMatieresComment(getCellComment(row, colMatiere));
        retour.calculTraitee();
        return retour;
    }
    
    
    private boolean controleKey(String anoClarity, String key)
    {
        // Controle si la clef n'a pas les indices de num�ro de lot
        String smallkey = key.length() > 5 && key.matches(".*0[0-9E]$") ? key.substring(0, 6) : key; 
        // Contr�le si la clef est de type T*.
        String newKey = key.length() == 9 && key.startsWith("T") ? key.substring(0, 8) : smallkey;     
        return anoClarity.equalsIgnoreCase(newKey);
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Liste des num�ros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Gr�goire mathon
     *
     */
    private enum Index {
        LOTI, EDITIONI, ENVI, TRAITEI;
    }
}