package control.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import control.task.AbstractTask;
import model.Colonne;
import model.enums.ColW;
import model.enums.EtatLot;
import model.enums.Severity;
import utilities.CellHelper;
import utilities.DateHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe mère des contrôleurs pour les fichiers Excel en écriture
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 *
 * @param <T>
 *        Enumération représentant le type de colonne du fichier
 * @param <R>
 *        Liste des données à utiliser
 * 
 */
public abstract class AbstractControlExcelWrite<T extends Enum<T> & ColW, R> extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** Classe de l'énumération des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlExcelWrite(File file)
    {
        super(file);
        createWb();
        initEnum();
        calculIndiceColonnes();
    }

    /*---------- METHODS ABSTRAITES ----------*/

    /**
     * Initilise les feuilles et crée la ligne de titres
     */
    protected abstract void initTitres();

    /**
     * Enregistre les {@code donnees} dans la feuille {@code sheet} du fichier excel, depuis une liste R.
     * 
     * @param donnees
     *                Liste des données à enregistrer.
     * @param sheet
     *                Feuille Excel pour écrire les données.
     * @param task
     *                Tâche parente. Peut-être null.
     */
    protected abstract void enregistrerDonnees(R donnees, Sheet sheet, AbstractTask task);

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Ecris le fichier sur le disque. Retourne vrai si celui-ci a bien été modifié.
     * 
     * @return
     *         Retourne vrai si le fichier a été écris sans erreur.
     */
    public boolean write()
    {
        // Initialisation des filtres ainsi que de la taille des colonnes, et blocage de la ligne des titres
        for (Sheet sheet : wb)
        {
            sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(0).getLastCellNum(), 0, sheet.getLastRowNum()));
            sheet.createFreezePane(0, 1);
            autosizeColumns(sheet);
        }

        try (OutputStream stream = Files.newOutputStream(file.toPath()))
        {
            long time = file.lastModified();
            wb.write(stream);
            wb.close();
            return time <= file.lastModified();
        }
        catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    /*---------- METHODES PROTECTED ----------*/

    /**
     * recalcule la largeur de chaque colonne de la feuille.
     * 
     * @param sheet
     *              Feuille à traiter
     */
    protected void autosizeColumns(Sheet sheet)
    {
        for (int i = 0; i <= maxIndice; i++)
        {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Permet de créer et de valoriser une cellule. Le texte peut-etre de type {@link String}, {@link EtatLot}, {@link LocalDate}.
     * 
     * @param row
     *                 Ligne dans laquelle on veut créer la cellule
     * @param indexCol
     *                 Index de colonne pour créer la cellule
     * @param style
     *                 Style utilisé pour la cellule
     * @param texte
     *                 Texte de la cellule
     * @return
     *         La cellule valorisée pour enchaîner les traitements.
     */
    protected Cell valoriserCellule(Row row, Integer indexCol, CellStyle style, Object texte)
    {
        // Contrôle
        if (row == null || indexCol == null)
            throw new IllegalArgumentException("La ligne ou l'indice de la cellule ne peuvent être nulles.");

        // Création cellule
        Cell cell = row.createCell(indexCol);

        // Style
        if (style != null)
            cell.setCellStyle(style);

        // Ajout du texte non null dans le bon format
        if (texte == null)
            return cell;

        if (texte instanceof String)
            cell.setCellValue((String) texte);
        else if (texte instanceof Number)
            cell.setCellValue((double) (int) texte);
        else if (texte instanceof EtatLot)
            cell.setCellValue(((EtatLot) texte).getValeur());
        else if (texte instanceof LocalDate || texte instanceof LocalDateTime)
            cell.setCellValue(DateHelper.convertToOldDate(texte));
        else
            cell.setCellValue(texte.toString());

        return cell;
    }

    /**
     * Permet d'ajouter une formule Excel à une cellule.
     * 
     * @param row
     *                 Ligne dans laquelle on veut créer la cellule
     * @param indexCol
     *                 Index de colonne pour créer la cellule
     * @param style
     *                 Style utilise pour la cellule
     * @param formule
     *                 Formule de la cellule
     * @return
     *      La cellule modifiée pour le chaînage des traitements.
     */
    protected Cell valoriserFormuleCellule(Row row, Integer indexCol, CellStyle style, String formule)
    {
        // Contrôle
        if (row == null || indexCol == null)
            throw new IllegalArgumentException("La ligne ou l'indice de la cellule ne peuvent être nulles.");

        // Création cellule
        Cell cell = row.createCell(indexCol);

        // Style
        if (style != null)
            cell.setCellStyle(style);

        // pas d'action sur une formule nulle ou vide
        if (formule == null || formule.isEmpty())
            return cell;

        cell.setCellFormula(formule);
        return cell;
    }

    @Override
    protected final void calculIndiceColonnes()
    {
        // Récupération des paramètres depuis les propriétés
        Map<T, Colonne> map = Statics.proprietesXML.getEnumMapColW(enumeration);

        int nbreCol = 0;

        // Initilisation de chaque valeur de la classe en utilisant celles du fichier de propriété
        // On récupère le champ par introspection depuis le nom de l'énumération puis on valorise avec la valeur enregistrée
        for (Map.Entry<T, Colonne> entry : map.entrySet())
        {
            T typeCol = entry.getKey();
            try
            {
                Field field = getFieldAccessible(typeCol.getNomCol());
                field.set(this, Integer.valueOf(entry.getValue().getIndice()));
                testMax((int) field.get(this));
                nbreCol++;
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur à l'affectation d'une variable lors de l'initialisation d'une colonne : " + typeCol.getNomCol(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        int enumLength = enumeration.getEnumConstants().length;
        if (nbreCol != enumLength)
            throw new FunctionalException(Severity.ERROR, "Le fichier excel est mal configuré, verifiez les colonnes de celui-ci : Difference = " + (enumLength - nbreCol));
    }

    @Override
    protected final void createWb()
    {
        try
        {
            wb = WorkbookFactory.create(true);
        }
        catch (IOException e)
        {
            throw new TechnicalException("Impossible de créer le workbook.", e);
        }
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
    }

    /**
     * Récupération du numéro de la colonne depuis le champ de la classe et l'énumération.
     * 
     * @param type
     *             énumération
     * @return
     *         Le numéro du champ désiré
     */
    protected final int getNumCol(ColW type)
    {
        try
        {
            Field field = getFieldAccessible(type.getNomCol());
            return (int) field.get(this);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            throw new TechnicalException("control.excel.AbstractControlExcelWrite.getNumCol - Mauvaise déclaration des noms de colonnes : " + type.getNomCol(), e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation de l'énumération pour le contrôleur
     */
    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de récupérer la classe sous forme de ParameterizedType
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = Statics.PATTERNSPACE.split(pt.getActualTypeArguments()[0].toString())[1];

        // Instanciation de l'énumération
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new TechnicalException("control.excel.AbstractControlExcelWrite.initEnum - Impossible d'instancier l'énumération", e);
        }
    }

    /**
     * Permet d'accéder à un champ non public en utilisant le contrôleur de privilèges.
     * 
     * @param nom
     *            Le champ du champ à accéder
     * @return
     *         Le champ accessible
     * @throws NoSuchFieldException
     *                              Exception remontée si le champ n'existe pas.
     */
    private Field getFieldAccessible(String nom) throws NoSuchFieldException
    {
        Field field = getClass().getDeclaredField(nom);
        AccessController.doPrivileged((PrivilegedAction<T>) () -> { field.setAccessible(true); return null; });
        return field;
    }

    /*---------- ACCESSEURS ----------*/

}
