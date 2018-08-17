package control.excel;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.CompoPbApps;
import model.enums.TypeColPbApps;
import utilities.enums.Bordure;

/**
 * Classe de controle des applications en écriture
 * 
 * @author ETP8137 - Grégoier Mathon
 * @since 1.0
 *
 */
public class ControlPbApps extends AbstractControlExcelWrite<TypeColPbApps, List<CompoPbApps>>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String PBAPPLI = "Composants avec pb. appli";
    
    private int colCode;
    private int colAppli;
    private int colLot;
    private int colCpiLot;
    private int colDep;
    private int colService;
    private int colChefServ;

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

    public void creerfeuille(List<CompoPbApps> composPbApps)
    {
        Sheet sheet = wb.getSheet(PBAPPLI);
        enregistrerDonnees(composPbApps, sheet);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void calculIndiceColonnes()
    {
        colCode = 0;
        colAppli = 1;
        colLot = 2;
        colCpiLot = 3;
        colDep = 4;
        colService = 5;
        colChefServ = 6;
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
        valoriserCellule(row, colLot, centre, "Lot RTC");
        valoriserCellule(row, colCpiLot, centre, "Cpi Lot");
        valoriserCellule(row, colDep, centre, "Departement");
        valoriserCellule(row, colService, centre, "Service");
        valoriserCellule(row, colChefServ, centre, "Chef de Service");
        autosizeColumns(sheet);
    }

    @Override
    protected void enregistrerDonnees(List<CompoPbApps> donnees, Sheet sheet)
    {
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        centre.setWrapText(false);
        Row row;

        for (CompoPbApps compo : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colCode, centre, compo.getCodeComposant());
            valoriserCellule(row, colAppli, centre, compo.getCodeAppli());
            valoriserCellule(row, colLot, centre, compo.getLotRTC());
            valoriserCellule(row, colCpiLot, centre, compo.getCpiLot());
            valoriserCellule(row, colDep, centre, compo.getDepart());
            valoriserCellule(row, colService, centre, compo.getService());
            valoriserCellule(row, colChefServ, centre, compo.getChefService());
        }
        autosizeColumns(sheet);
    }

    /*---------- ACCESSEURS ----------*/
}
