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
import model.enums.Bordure;
import model.enums.ColVul;
import model.enums.TypeVulnerabilite;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de contrôle du fichier d'extraction des vulnérabilités
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class ControlExtractVul extends AbstractControlExcelWrite<ColVul, List<Vulnerabilite>>
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

    /**
     * Constructeur avec visibilité par défaut pour obliger l'utilisation de la factory
     * 
     * @param file
     *             Fichier qui sera traité par l'instance du contrôleur
     */
    ControlExtractVul(File file)
    {
        super(file);
        initTitres();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Enregistre les données dans le fichier Excel depuis une liste et un type de vulnérabilités.
     * 
     * @param liste
     *              La liste des vulnérabilités à enregistrer.
     * @param type
     *              Type des vulnérabilités à enregistrer.
     * @param task
     *              Tâche de traitement principale
     */
    public void ajouterExtraction(List<Vulnerabilite> liste, TypeVulnerabilite type, AbstractTask task)
    {
        enregistrerDonnees(liste, wb.getSheet(type.getNomSheet()), task);
    }

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void initTitres()
    {
        // création du style pour les titres.
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);

        // Création des feuilles pour chaque type de vulnérabilité
        for (TypeVulnerabilite type : TypeVulnerabilite.values())
        {
            Sheet sheet = wb.createSheet(type.getNomSheet());
            Row row = sheet.createRow(0);

            for (ColVul typeColVul : ColVul.values())
            {
                try
                {
                    valoriserCellule(row, (Integer) getClass().getDeclaredField(typeColVul.getNomCol()).get(this), centre, Statics.proprietesXML.getEnumMapColW(ColVul.class).get(typeColVul).getNom());
                }
                catch (IllegalAccessException | NoSuchFieldException e)
                {
                    throw new TechnicalException("control.excel.ControlExtractVul.initTitres - Impossible d'initialiser les colonnes du fichier.", e);
                }
            }
        }
    }

    @Override
    protected void enregistrerDonnees(List<Vulnerabilite> donnees, Sheet sheet, AbstractTask task)
    {
        // Création des styles
        CellStyle centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle gauche = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.LEFT);
        centre.setWrapText(false);
        gauche.setWrapText(false);
        Row row;

        // Données affichage
        int i = 0;
        int size = donnees.size();

        // Itération sur les données pour valoriser chaque cellule de la feuille
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
