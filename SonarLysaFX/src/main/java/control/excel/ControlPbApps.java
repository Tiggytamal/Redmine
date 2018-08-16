package control.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.Application;
import model.enums.TypeColPbApps;
import utilities.enums.Bordure;

/**
 * Classe de controle des applications en écriture
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class ControlPbApps extends AbstractControlExcelWrite<TypeColPbApps, Collection<Application>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String PBAPPLI = "Composants avec pb. appli";
    
    private int colCode;
    private int colAppli;
    private int colCpiLot;
    private int colDep;
    private int colService;

    /*---------- CONSTRUCTEURS ----------*/

    ControlPbApps(File sortie)
    {
        super(sortie);
        
        // Création des deux feuilles du fichier Excel
        wb.createSheet(PBAPPLI);
        calculIndiceColonnes();
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void creerfeuille(Set<Application> applisOpenSonar)
    {
        Sheet sheet = wb.getSheet(PBAPPLI);
        enregistrerDonnees(applisOpenSonar, sheet);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void calculIndiceColonnes()
    {
        colCode = 0;
        colAppli = 1;
        colCpiLot = 2;
        colDep = 3;
        colService = 4;
    }

    @Override
    protected final void initTitres()
    {
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        centre.setWrapText(false);

        // Création de la ligne de titre
        Sheet sheet = wb.getSheet(PBAPPLI);

        Row row = sheet.createRow(0);
        valoriserCellule(row, colCode, centre, "Composant");
        valoriserCellule(row, colAppli, centre, "Code application");
        valoriserCellule(row, colCpiLot, centre, "Cpi Lot");
        valoriserCellule(row, colDep, centre, "Departement");
        valoriserCellule(row, colService, centre, "Service");
        autosizeColumns(sheet);
    }

    @Override
    protected void enregistrerDonnees(Collection<Application> donnees, Sheet sheet)
    {
        List<String> codeApps = new ArrayList<>();
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        centre.setWrapText(false);
        Row row;

        for (Application app : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colCode, centre, app.getCode());

            codeApps.add(app.getCode());
        }
        autosizeColumns(sheet);
    }

    /*---------- ACCESSEURS ----------*/
}
