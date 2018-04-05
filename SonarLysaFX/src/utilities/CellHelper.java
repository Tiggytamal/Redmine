package utilities;

import java.util.HashMap;
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

import utilities.enums.Bordure;

/**
 * Classe de getion des styles de cellule Excel.
 * 
 * @author ETP137 - Grégoire Mathon
 * @since 1.0
 */
public class CellHelper
{
    /* ---------- ATTIBUTES ---------- */
    private Workbook wb;
    private CreationHelper ch;

    /* ---------- CONSTUCTORS ---------- */

    public CellHelper(Workbook wb)
    {
        this.wb = wb;
        ch = wb.getCreationHelper();
    }

    /* ---------- METHODS ---------- */

    /**
     * Retourne une map avec tous les styles possible d'une couleur. les Elements à true ont un style centré horizontalement.
     * 
     * @param couleur
     * @return
     */
    public Map<Boolean, CellStyle> createAllStyles(IndexedColors couleur)
    {
        if (couleur == null)
            throw new IllegalArgumentException("La couleur ne peut pas être nulle");

        // Map de retour
        Map<Boolean, CellStyle> retour = new HashMap<>();

        // Itération sur toutes les bordures possibles
        for (Bordure bordure : Bordure.values())
        {
            // Création du style
            CellStyle style = wb.createCellStyle();

            // Alignement vertical centré plus lignes fines en bordure
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setWrapText(true);

            // Choix de la couleur de fond
            style.setFillForegroundColor(couleur.index);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

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
            retour.put(false, style);
            CellStyle styleC = wb.createCellStyle();
            styleC.cloneStyleFrom(style);
            styleC.setAlignment(HorizontalAlignment.CENTER);
            retour.put(true, styleC);
        }
        return retour;
    }

    public Cell setFontColor(Cell cell, IndexedColors color)
    {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        Font font = wb.createFont();
        font.setColor(color.index);
        font.setFontName("Comic Sans MS");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        cell.setCellStyle(style);
        return cell;
    }

    public Cell recentrage(Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return cell;
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, la bordure désirée et l'alignement du texte
     * 
     * @param IndexedColors
     *            {@link org.apache.poi.ss.usermodel.IndexedColors}
     * @param bordure
     *            {@link utilities.enums.Bordure}
     * @param alignement
     *            {@link org.apache.poi.ss.usermodel.HorizontalAlignment}
     * @return
     */
    public CellStyle getStyle(IndexedColors couleur, Bordure bordure, HorizontalAlignment alignement)
    {
        if (couleur == null || bordure == null || alignement == null)
            throw new IllegalArgumentException("La couleur ou la bordure ne peuvent être nulles");

        CellStyle style = getStyle(couleur, bordure);
        // Ajout de l'alignement horizontal
        style.setAlignment(alignement);
        return style;
    }
    
    /**
     * Retourne le style de cellule voulu selon la couleur, la bordure désirée et l'alignement du texte est celui par défault.
     * 
     * @param couleur
     * @param bordure
     * @return
     */
    public CellStyle getStyle(IndexedColors couleur, Bordure bordure)
    {
        // Initialisation du style
        CellStyle style = wb.createCellStyle();

        // Renvoie un style vide sans statut d'incident
        if (couleur == null || bordure == null)
            throw new IllegalArgumentException("La couleur ou la bordure ne peuvent être nulles");

        // Alignement vertical centré plus ligne fine en bordure
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);

        // Choix de la couleur de fond
        style.setFillForegroundColor(couleur.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

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
        return style;
    }

    /**
     * Retourne le style de cellule voulu selon la couleur, sans bordure spécifique
     * 
     * @param couleur
     * @return
     */
    public CellStyle getStyle(IndexedColors couleur)
    {
        return getStyle(couleur, Bordure.VIDE);
    }

    /**
     * Rajoute un lien hypertexte à la cellule donnée
     * @param adresse
     * @param cell
     * @return
     */
    public Cell createHyperLink(String adresse, Cell cell)
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
        return cell;
    }

    /* ---------- PRIVATE METHODS ---------- */
    /* ---------- ACCESS ---------- */

}
