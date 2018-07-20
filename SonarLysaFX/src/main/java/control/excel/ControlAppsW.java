package control.excel;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.Application;
import model.enums.TypeColApps;
import utilities.enums.Bordure;

public class ControlAppsW extends ControlExcelWrite<TypeColApps, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/
    
    private int colCode;
    private int colActif;
    private int colLib;
    private int colOpen;
    private int colMainFrame;
    
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected ControlAppsW(File file)
    {
        super(file);
        calculIndiceColonnes();
        initTitres();
        initEnum();
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    public void creerfeuilleSonar(Set<Application> applisOpenSonar)
    {
        Sheet sheet = wb.getSheet("Gérées dans Sonar");
        enregistrerDonnees(applisOpenSonar, sheet);                
    }

    public void creerfeuilleNonSonar(List<Application> applisOpenNonSonar)
    {
        Sheet sheet = wb.getSheet("Non gérées dans Sonar");
        enregistrerDonnees(applisOpenNonSonar, sheet);        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    @Override
    protected void calculIndiceColonnes()
    {
        colCode = 0;
        colActif = 1;
        colLib = 2;
        colOpen = 3;
        colMainFrame = 4;
    }

    @Override
    protected void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        
        wb.createSheet("Gérées dans Sonar");
        wb.createSheet("Non gérées dans Sonar");

        // Création des feuilles pour chaque type de vulnérabilité
        for (Iterator<Sheet> iter = wb.sheetIterator(); iter.hasNext();)
        {
            Sheet sheet = iter.next();
            Row row = sheet.createRow(0);
            valoriserCellule(row, colCode, centre, "Code Application", null);
            valoriserCellule(row, colActif, centre, "Actif", null);
            valoriserCellule(row, colLib, centre, "Libellé", null);
            valoriserCellule(row, colOpen, centre, "Open", null);
            valoriserCellule(row, colMainFrame, centre, "MainFrame", null);
        }
    }

    @Override
    protected void initEnum()
    {
        enumeration = TypeColApps.class;

    }

    @Override
    protected void enregistrerDonnees(Collection<Application> donnees, Sheet sheet)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        centre.setWrapText(false);
        Row row;

        for (Application apps : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colCode, centre, apps.getCode(), null);
            valoriserCellule(row, colActif, centre, String.valueOf(apps.isActif()), null);
            valoriserCellule(row, colLib, centre, apps.getLibelle(), null);
            valoriserCellule(row, colOpen, centre, String.valueOf(apps.isOpen()), null);
            valoriserCellule(row, colMainFrame, centre, String.valueOf(apps.isMainFrame()), null);
        }
        autosizeColumns(sheet);
    }



    /*---------- ACCESSEURS ----------*/
}