package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import model.enums.Bordure;

/**
 * Classe de getion des styles de cellule Excel.
 * 
 * @author ETP137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public class CellHelper
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short SIZEFONT = 12;
    private static final String ERREUR = "La couleur ou la bordure ne peuvent être nulles";

    private Workbook wb;
    private CreationHelper ch;

    /*---------- CONSTRUCTEURS ----------*/

    public CellHelper(Workbook wb)
    {
        this.wb = wb;
        ch = wb.getCreationHelper();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Retourne une map avec tous les styles possible d'une couleur. les Elements à true ont un style centre horizontalement.
     * 
     * @param couleur
     *                Couleur du texte
     * @return
     *         La map des style classés.
     */
    public Map<Boolean, List<CellStyle>> createAllStyles(IndexedColors couleur)
    {
        if (couleur == null)
            throw new IllegalArgumentException("La couleur ne peut pas être nulle");

        // Map de retour
        Map<Boolean, List<CellStyle>> retour = new HashMap<>();
        retour.put(Boolean.TRUE, new ArrayList<>());
        retour.put(Boolean.FALSE, new ArrayList<>());

        // Itération sur toutes les bordures possibles
        for (Bordure bordure : Bordure.values())
        {
            // Création du style
            CellStyle style = wb.createCellStyle();

            // Création et ajout du style non centré
            prepareStyle(style, couleur, bordure, FillPatternType.SOLID_FOREGROUND);
            retour.get(Boolean.FALSE).add(style);

            // Création et ajout du style centré
            CellStyle styleC = wb.createCellStyle();
            styleC.cloneStyleFrom(style);
            styleC.setAlignment(HorizontalAlignment.CENTER);
            retour.get(Boolean.TRUE).add(styleC);
        }

        return retour;
    }

    /**
     * Changement de la couleur du texte d'une celleule.
     * 
     * @param cell
     *              Cellule à modifier.
     * @param color
     *              Couleur à changer.
     * @return
     *         La cellule pour permettre de chaîner les traitements.
     */
    public Cell setFontColor(Cell cell, IndexedColors color)
    {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        Font font = wb.createFont();
        font.setColor(color.index);
        font.setFontName("Comic Sans MS");
        font.setFontHeightInPoints(SIZEFONT);
        style.setFont(font);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * Ajoute lre recentrage automatique pour le texte de la cellule.
     * 
     * @param cell
     *             Cellule à modifier.
     * @return
     *         La cellule pour permettre de chainer les traitements.
     */
    public Cell recentrage(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return cell;
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, la bordure desirée, l'alignement du texte et le motif.
     * 
     * @param couleur
     *                   La couleur du texte choisie.
     * @param bordure
     *                   Le type de bordure desiré.
     * @param pattern
     *                   Le motif choisi pour le fond de la cellule.
     * @param alignement
     *                   L'aignement horizontal du texte.
     * @return
     *         Le style créé.
     */
    public CellStyle getStyle(IndexedColors couleur, Bordure bordure, FillPatternType pattern, HorizontalAlignment alignement)
    {
        if (alignement == null)
            throw new IllegalArgumentException(ERREUR);

        CellStyle style = getStyle(couleur, bordure, pattern);

        // Ajout de l'alignement horizontal
        style.setAlignment(alignement);
        return style;
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, la bordure desiree et l'alignement du texte
     * 
     * @param couleur
     *                   La couleur du texte choisie.
     * @param bordure
     *                   Le type de bordure desiré.
     * @param alignement
     *                   L'aignement horizontal du texte.
     * @return
     *         Le style créé.
     */
    public CellStyle getStyle(IndexedColors couleur, Bordure bordure, HorizontalAlignment alignement)
    {
        if (alignement == null)
            throw new IllegalArgumentException(ERREUR);

        CellStyle style = getStyle(couleur, bordure, FillPatternType.SOLID_FOREGROUND);

        // Ajout de l'alignement horizontal
        style.setAlignment(alignement);
        return style;
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, sans bordure specifique, mais avec un motif particulier.
     * 
     * @param couleur
     *                Couleur de fond du style.
     * @param pattern
     *                Le motif choisi pour le fond de la cellule.
     * @return
     *         Le style créé.
     */
    public CellStyle getStyle(IndexedColors couleur, FillPatternType pattern)
    {
        return getStyle(couleur, Bordure.VIDE, pattern);
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, sans bordure specifique
     * 
     * @param couleur
     *                Couleur de fond du style.
     * @return
     *         Le style créé.
     */
    public CellStyle getStyle(IndexedColors couleur)
    {
        return getStyle(couleur, Bordure.VIDE, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, la bordure et le motif desires, et l'alignement du texte est celui par défault.
     * 
     * @param couleur
     *                couleur de fond de la cellule.
     * @param bordure
     *                designation des bordures de la cellule.
     * @param pattern
     *                Le motif choisi pour le fond de la cellule.
     * @return
     *         Le style créé.
     */
    public CellStyle getStyle(IndexedColors couleur, Bordure bordure, FillPatternType pattern)
    {
        // Renvoie un style vide sans statut d'incident
        if (couleur == null || bordure == null || pattern == null)
            throw new IllegalArgumentException(ERREUR);

        // Initialisation du style
        CellStyle style = wb.createCellStyle();

        // Création du style
        prepareStyle(style, couleur, bordure, pattern);

        return style;
    }

    /**
     * Rajoute un lien hypertexte à la cellule donnée.
     * 
     * @param adresse
     *                Liens hypertexte à ajouter à la cellule.
     * @param cell
     *                Cellule à traiter.
     */
    public void createHyperLink(String adresse, Cell cell)
    {
        // Création de l'hyperlink
        Hyperlink link = ch.createHyperlink(HyperlinkType.URL);
        link.setAddress(adresse);

        // copie du style de la cellule
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());

        // Création ed la police de caractères
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);

        // retour de la cellule
        style.setFont(font);
        cell.setHyperlink(link);
        cell.setCellStyle(style);
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de créer un stye avec les informations de couleur et de bordure.
     * 
     * @param style
     *                Le style à modifier.
     * @param couleur
     *                couleur de fond de la cellule.
     * @param bordure
     *                designation des bordures de la cellule.
     * @param pattern
     *                Le motif choisi pour le fond de la cellule.
     */
    private void prepareStyle(CellStyle style, IndexedColors couleur, Bordure bordure, FillPatternType pattern)
    {
        // Alignement vertical centre plus ligne fine en bordure
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);

        // Choix de la couleur de fond

        // Si on veut un fond unifié, on doit mettre la couleur sur le ForegroundColor
        if (pattern == FillPatternType.SOLID_FOREGROUND)
            style.setFillForegroundColor(couleur.index);

        // Avec l'utilisation d'un motif, il faut utiliser le Background en couleur de base
        else if (pattern == FillPatternType.LEAST_DOTS)
        {
            style.setFillBackgroundColor(couleur.index);
            style.setFillForegroundColor(IndexedColors.BLACK.index);
        }
        else if (pattern == FillPatternType.THICK_FORWARD_DIAG)
        {
            style.setFillBackgroundColor(IndexedColors.WHITE.index);
            style.setFillForegroundColor(couleur.index);
        }
        else
            throw new TechnicalException("utilities.CellHelper.prepareStyle - FillpatternType non prévu : " + pattern);

        style.setFillPattern(pattern);

        // Switch sur le placement de la cellule, rajout d'une bordure plus épaisse au bord du tableau
        switch (bordure)
        {
            case BAS:
                style.setBorderBottom(BorderStyle.THICK);
                break;

            case DROITE:
                style.setBorderRight(BorderStyle.THICK);
                break;

            case GAUCHE:
                style.setBorderLeft(BorderStyle.THICK);
                break;

            case HAUT:
                style.setBorderTop(BorderStyle.THICK);
                break;

            case BASDROITE:
                style.setBorderRight(BorderStyle.THICK);
                style.setBorderBottom(BorderStyle.THICK);
                break;

            case BASGAUCHE:
                style.setBorderLeft(BorderStyle.THICK);
                style.setBorderBottom(BorderStyle.THICK);
                break;

            case HAUTDROITE:
                style.setBorderTop(BorderStyle.THICK);
                style.setBorderRight(BorderStyle.THICK);
                break;

            case HAUTGAUCHE:
                style.setBorderTop(BorderStyle.THICK);
                style.setBorderLeft(BorderStyle.THICK);
                break;

            case VIDE:
                break;
        }
    }
    /*---------- ACCESSEURS ----------*/
}
