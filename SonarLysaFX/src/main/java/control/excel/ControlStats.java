package control.excel;

import java.io.File;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.bdd.DefautQualite;
import utilities.CellHelper;
import utilities.TechnicalException;
import utilities.enums.Bordure;

/**
 * Classe d'écriture des statistiques des anomalies SonarQube
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ControlStats extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final String STATS = "Statistiques";
    
    /*---------- CONSTRUCTEURS ----------*/

    protected ControlStats(File file)
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public void calculStatistiques(Iterable<DefautQualite> dqsEnBase)
    {
        // Réinitialisation de la feuille
        Sheet sheet = wb.getSheet(STATS);
        if (sheet != null)
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        sheet = wb.createSheet(STATS);
        creerTitresStats(sheet);

        // Données calculées
        int[] valeurs = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursM = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursT = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] valeursE = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        // calcul des dates
        LocalDate today = LocalDate.now();
        LocalDate debutMois = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(1).minusDays(1L);
        LocalDate fin = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate debutTrim = LocalDate.of(today.getYear(), today.getMonth(), 1).minusMonths(3).minusDays(1L);

        // Itération sur les anomalies
        for (DefautQualite dq : dqsEnBase)
        {
            calculDonnee(dq, debutMois, debutTrim, fin, valeurs);

            if (dq.getDateCreation() != null)
                calculDonnee(dq, debutMois, debutTrim, fin, valeursM);

            if (dq.getDateReso() != null)
                calculDonnee(dq, debutMois, debutTrim, fin, valeursT);

            if (dq.getDateCreation() != null && dq.getDateReso() == null)
                calculEnCours(dq, today, valeursE);
        }

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "Erreur SonarQube detectées", valeurs);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "anomalies RTC créées", valeursM);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneCalcul(row, "anomalies RTC résolues", valeursT);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        valoriserCellule(row, 0, centre, "Anomalies en cours : ");

        row = sheet.createRow(sheet.getLastRowNum() + 2);
        creerLigneTotalAno(row, "Total : ", valeursE[Calcul.TOTAL.ordinal()], valeursE[Calcul.TOTALSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus d'une semaine : ", valeursE[Calcul.TOTALS.ordinal()], valeursE[Calcul.TOTALSSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus d'un mois : ", valeursE[Calcul.TOTALM.ordinal()], valeursE[Calcul.TOTALMSEC.ordinal()]);
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        creerLigneTotalAno(row, "Anomalies de plus de 3 mois : ", valeursE[Calcul.TOTALT.ordinal()], valeursE[Calcul.TOTALTSEC.ordinal()]);
        autosizeColumns(sheet);
    }
    
    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void createWb()
    {
        wb = new XSSFWorkbook();
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }

    @Override
    protected void calculIndiceColonnes()
    {
        // Pas d'implémentation
    }

    /*---------- METHODES PRIVEES ----------*/
    
    private void calculDonnee(DefautQualite dq, LocalDate debutMois, LocalDate debutTrim, LocalDate fin, int[] valeurs)
    {

        valeurs[Calcul.TOTAL.ordinal()]++;
        if (dq.isSecurite())
            valeurs[Calcul.TOTALSEC.ordinal()]++;

        if (dq.getDateDetection().isAfter(debutMois) && dq.getDateDetection().isBefore(fin))
        {
            valeurs[Calcul.TOTALM.ordinal()]++;
            if (dq.isSecurite())
                valeurs[Calcul.TOTALMSEC.ordinal()]++;
        }
        if (dq.getDateDetection().isAfter(debutTrim) && dq.getDateDetection().isBefore(fin))
        {
            valeurs[Calcul.TOTALT.ordinal()]++;
            if (dq.isSecurite())
                valeurs[Calcul.TOTALTSEC.ordinal()]++;
        }
    }

    private void calculEnCours(DefautQualite dq, LocalDate today, int[] valeursE)
    {
        valeursE[Calcul.TOTAL.ordinal()]++;

        if (dq.isSecurite())
            valeursE[Calcul.TOTALSEC.ordinal()]++;

        if (dq.getDateCreation().isBefore(today.minusMonths(3L)))
        {
            valeursE[Calcul.TOTALT.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALTSEC.ordinal()]++;
        }
        else if (dq.getDateCreation().isBefore(today.minusMonths(1L)))
        {
            valeursE[Calcul.TOTALM.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALMSEC.ordinal()]++;
        }
        else if (dq.getDateCreation().isBefore(today.minusWeeks(1L)))
        {
            valeursE[Calcul.TOTALS.ordinal()]++;
            if (dq.isSecurite())
                valeursE[Calcul.TOTALSSEC.ordinal()]++;
        }
    }
    
    /**
     * Crée la ligne de titre des statistiques
     * 
     * @param sheet
     */
    private void creerTitresStats(Sheet sheet)
    {
        // Création du style des titres
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);

        // Création des noms des colonnes
        Row titres = sheet.createRow(0);
        for (Index index : Index.values())
        {
            Cell cell = titres.createCell(index.ordinal());
            cell.setCellStyle(styleTitre);
            switch (index)
            {
                case DONNEEI:
                    cell.setCellValue(Index.DONNEEI.getValeur());
                    break;

                case MENSUELI:
                    cell.setCellValue(Index.MENSUELI.getValeur());
                    break;

                case TRIMI:
                    cell.setCellValue(Index.TRIMI.getValeur());
                    break;

                case TOTALI:
                    cell.setCellValue(Index.TOTALI.getValeur());
                    break;

                default:
                    throw new TechnicalException("Nouvel index non géré : " + index, null);
            }
        }
    }
    
    /**
     * CRée une ligne pour une anomalie dans les feuilles de version
     * 
     * @param row
     * @param totelSecu
     * @param trimSecu
     * @param menseulSecu
     * @param ano
     * @param couleur
     * @param traite
     */
    private void creerLigneCalcul(Row row, String stat, int[] valeurs)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);

        final String DONT = " dont ";
        final String SECURITE = " de sécurité";

        valoriserCellule(row, Index.DONNEEI.ordinal(), centre, stat);
        valoriserCellule(row, Index.TOTALI.ordinal(), centre, valeurs[Calcul.TOTAL.ordinal()] + DONT + valeurs[Calcul.TOTALSEC.ordinal()] + SECURITE);
        valoriserCellule(row, Index.MENSUELI.ordinal(), centre, valeurs[Calcul.TOTALM.ordinal()] + DONT + valeurs[Calcul.TOTALMSEC.ordinal()] + SECURITE);
        valoriserCellule(row, Index.TRIMI.ordinal(), centre, valeurs[Calcul.TOTALT.ordinal()] + DONT + valeurs[Calcul.TOTALTSEC.ordinal()] + SECURITE);
    }

    private void creerLigneTotalAno(Row row, String texte, int total, int secu)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);

        final String DONT = " dont ";
        final String SECURITE = " de sécurité";

        valoriserCellule(row, 0, centre, new StringBuilder(texte).append(total).append(DONT).append(secu).append(SECURITE));
    }
    
    /*---------- ACCESSEURS ----------*/
    /*---------- CLASSE PRIVEE ----------*/

    /**
     * Liste des numéros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Grégoire mathon
     * @since 1.0
     */
    // @formatter:off
    private enum Index 
    {
        DONNEEI(""), 
        MENSUELI("Mensuel"), 
        TRIMI("Trimestrielle"), 
        TOTALI("Total");

        private String valeur;

        private Index(String valeur)
        {
            this.valeur = valeur;
        }

        public String getValeur()
        {
            return valeur;
        }
    }
    
    private enum Calcul
    {
        TOTAL,
        TOTALSEC,
        TOTALM,
        TOTALMSEC,
        TOTALT,
        TOTALTSEC,
        TOTALS,
        TOTALSSEC;
    }
}
