package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Vulnerabilite;
import model.enums.TypeColExtract;
import model.enums.TypeVulnerabilite;
import utilities.CellHelper;
import utilities.TechnicalException;
import utilities.enums.Bordure;

public class ControlExtract extends ControlExcel<TypeColExtract, List<Vulnerabilite>>
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
    private Workbook wb;
    private CellHelper helper;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlExtract(File file)
    {
        super(file);
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void ajouterExtraction(List<Vulnerabilite> liste, TypeVulnerabilite type) throws IOException, InvalidFormatException
    {
        Sheet sheet = wb.getSheet(type.getNomSheet());
        Row row;       

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle gauche = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.LEFT);
        centre.setWrapText(false);
        gauche.setWrapText(false);
        
        for (Vulnerabilite vulnerabilite : liste)
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

    /*---------- METHODES PRIVEES ----------*/
    
    private void initTitres()
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
    protected void createWb()
    {
        wb = new XSSFWorkbook();
        helper = new CellHelper(wb);  
    }
    
    @Override
    protected Sheet initSheet()
    {
        // Pas d'initialisation
        return null;
    }
    
    @Override
    protected void calculIndiceColonnes(Sheet sheet)
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
    public void write()
    {
        try(FileOutputStream stream = new FileOutputStream(file))
        {
            wb.write(stream);
            wb.close();
        } catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    @Override
    public List<Vulnerabilite> recupDonneesDepuisExcel()
    {
        throw new TechnicalException("Pas d'implementation dans control.excel.ControlExtract", null);
    }

    @Override
    protected void initEnum()
    {
        enumeration = TypeColExtract.class;        
    }

    /*---------- ACCESSEURS ----------*/
}
