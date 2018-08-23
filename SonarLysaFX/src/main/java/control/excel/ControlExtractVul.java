package control.excel;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.Vulnerabilite;
import model.enums.TypeColVul;
import model.enums.TypeVulnerabilite;
import utilities.enums.Bordure;

/**
 * Classe de contrôle du fichier d'extraction des vulnérabilitès
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlExtractVul extends AbstractControlExcelWrite<TypeColVul, List<Vulnerabilite>>
{
    /*---------- ATTRIBUTS ----------*/

    private int colSeverity;
    private int colStatus;
    private int colMess;
    private int colDateCrea;
    private int colLot;
    private int colClarity;
    private int colAppli;
    private int colComp;
    private int colLib;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlExtractVul(File file)
    {
        super(file);
        calculIndiceColonnes();
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void ajouterExtraction(List<Vulnerabilite> liste, TypeVulnerabilite type)
    {
        Sheet sheet = wb.getSheet(type.getNomSheet());
        enregistrerDonnees(liste, sheet);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);

        // Création des feuilles pour chaque type de vulnérabilité
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            Sheet sheet = wb.createSheet(type.getNomSheet());
            Row row = sheet.createRow(0);
            valoriserCellule(row, colSeverity, centre, "Criticité", null);
            valoriserCellule(row, colStatus, centre, "Statut", null);
            valoriserCellule(row, colMess, centre, "Message", null);
            valoriserCellule(row, colDateCrea, centre, "Date création", null);
            valoriserCellule(row, colLot, centre, "Lot", null);
            valoriserCellule(row, colClarity, centre, "Code Clarity", null);
            valoriserCellule(row, colAppli, centre, "Code application", null);
            valoriserCellule(row, colComp, centre, "Nom composant", null);
            valoriserCellule(row, colLib, centre, "Bibliothèque", null);
        }
    }

    @Override
    protected final void calculIndiceColonnes()
    {
        colSeverity = 7;
        colStatus = 5;
        colMess = 8;
        colDateCrea = 6;
        colLot = 2;
        colClarity = 4;
        colAppli = 3;
        colComp = 0;
        colLib = 1;
        maxIndice = 7;
    }

    @Override
    protected void enregistrerDonnees(List<Vulnerabilite> donnes, Sheet sheet)
    {

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle gauche = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.LEFT);
        centre.setWrapText(false);
        gauche.setWrapText(false);
        Row row;

        for (Vulnerabilite vulnerabilite : donnes)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colSeverity, centre, vulnerabilite.getSeverite(), null);
            valoriserCellule(row, colStatus, centre, vulnerabilite.getStatus(), null);
            valoriserCellule(row, colMess, gauche, vulnerabilite.getMessage(), null);
            valoriserCellule(row, colDateCrea, centre, vulnerabilite.getDateCreation(), null);
            valoriserCellule(row, colLot, centre, vulnerabilite.getLot(), null);
            valoriserCellule(row, colClarity, centre, vulnerabilite.getClarity(), null);
            valoriserCellule(row, colAppli, centre, vulnerabilite.getAppli(), null);
            valoriserCellule(row, colComp, gauche, vulnerabilite.getComposant(), null);
            valoriserCellule(row, colLib, gauche, vulnerabilite.getLib(), null);
        }

        autosizeColumns(sheet);
    }

    /*---------- ACCESSEURS ----------*/
}
