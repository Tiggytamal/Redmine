package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Vulnerabilite;
import model.enums.TypeVulnerabilite;
import utilities.TechnicalException;

public class ControlExtract
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
    private File file;
    private Workbook wb;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlExtract(File file)
    {
        this.file = file;
        colSeverity = 7;
        colStatus = 5;
        colMess = 4;
        colDateCrea = 6;
        colLot = 1;
        colClarity = 3;
        colAppli = 2;
        colComp = 0;
        
        initWb();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void ajouterExtraction(List<Vulnerabilite> liste, TypeVulnerabilite type) throws IOException, InvalidFormatException
    {
        Sheet sheet = wb.getSheet(type.getNomSheet());
        Row row;

        for (Vulnerabilite vulnerabilite : liste)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell cell = row.createCell(colSeverity);
            cell.setCellValue(vulnerabilite.getSeverite());
            cell = row.createCell(colStatus);
            cell.setCellValue(vulnerabilite.getStatus());
            cell = row.createCell(colMess);
            cell.setCellValue(vulnerabilite.getMessage());
            cell = row.createCell(colDateCrea);
            cell.setCellValue(vulnerabilite.getDateCreation());
            cell = row.createCell(colLot);
            cell.setCellValue(vulnerabilite.getLot());
            cell = row.createCell(colClarity);
            cell.setCellValue(vulnerabilite.getClarity());
            cell = row.createCell(colAppli);
            cell.setCellValue(vulnerabilite.getAppli());
            cell = row.createCell(colComp);
            cell.setCellValue(vulnerabilite.getComposant());
        }
    }
    
    /**
     * Ecriture du fichier et fermeture du workbook
     * 
     * @throws IOException
     */
    public void write() throws IOException
    {
        try (FileOutputStream stream = new FileOutputStream(file))
        {
            wb.write(stream);

        } catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        } finally
        {
            wb.close();
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    

    private void initWb()
    {
        wb = new XSSFWorkbook();
        
        // Création des feuilles pour chaque type de vulnérabilité
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            Sheet sheet = wb.createSheet(type.getNomSheet());
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(colSeverity);
            cell.setCellValue("Criticité");
            cell = row.createCell(colStatus);
            cell.setCellValue("Statut");
            cell = row.createCell(colMess);
            cell.setCellValue("Message");
            cell = row.createCell(colDateCrea);
            cell.setCellValue("date création");
            cell = row.createCell(colLot);
            cell.setCellValue("Lot");
            cell = row.createCell(colClarity);
            cell.setCellValue("Code Clarity");
            cell = row.createCell(colAppli);
            cell.setCellValue("Code application");
            cell = row.createCell(colComp);
            cell.setCellValue("nom composant");           
        }        
    }

    /*---------- ACCESSEURS ----------*/
}
