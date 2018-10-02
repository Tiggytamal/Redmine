package control.excel;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.task.AbstractTask;
import model.Vulnerabilite;
import model.enums.TypeColVul;
import model.enums.TypeVulnerabilite;
import utilities.Statics;
import utilities.TechnicalException;
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

    public void ajouterExtraction(List<Vulnerabilite> liste, TypeVulnerabilite type, AbstractTask task)
    {
        Sheet sheet = wb.getSheet(type.getNomSheet());
        enregistrerDonnees(liste, sheet, task);
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

            for (TypeColVul typeColVul : TypeColVul.values())
            {
                try
                {
                    valoriserCellule(row, (Integer) getClass().getDeclaredField(typeColVul.getNomCol()).get(this), centre,
                            Statics.proprietesXML.getEnumMapColW(TypeColVul.class).get(typeColVul).getNom());
                }
                catch (IllegalAccessException | NoSuchFieldException | SecurityException e)
                {
                    throw new TechnicalException("", e);
                }
            }
        }
    }

    @Override
    protected void enregistrerDonnees(List<Vulnerabilite> donnees, Sheet sheet, AbstractTask task)
    {

        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle gauche = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.LEFT);
        centre.setWrapText(false);
        gauche.setWrapText(false);
        Row row;
        
        // Données affichage
        int i = 0;
        int size = donnees.size();

        for (Vulnerabilite vulnerabilite : donnees)
        {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            valoriserCellule(row, colSeverity, centre, vulnerabilite.getSeverite());
            valoriserCellule(row, colStatus, centre, vulnerabilite.getStatus());
            valoriserCellule(row, colMess, gauche, vulnerabilite.getMessage());
            valoriserCellule(row, colDateCrea, centre, vulnerabilite.getDateCreation());
            valoriserCellule(row, colLot, centre, vulnerabilite.getLot());
            valoriserCellule(row, colClarity, centre, vulnerabilite.getClarity());
            valoriserCellule(row, colAppli, centre, vulnerabilite.getAppli());
            valoriserCellule(row, colComp, gauche, vulnerabilite.getComposant());
            valoriserCellule(row, colLib, gauche, vulnerabilite.getLib());

            // Affichage
            i++;
            task.updateProgress(i, size);
        }

        autosizeColumns(sheet);
    }

    /*---------- ACCESSEURS ----------*/
}
