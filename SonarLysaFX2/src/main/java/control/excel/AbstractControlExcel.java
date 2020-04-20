package control.excel;

import java.io.File;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

import utilities.AbstractToStringImpl;
import utilities.CellHelper;

/**
 * Classe abstraite générique de contrôle des fichiers Excel
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 * 
 */
public abstract class AbstractControlExcel extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    /** Fichier Excel à modifier */
    protected File file;
    /** Workbook représentant le fichier */
    protected Workbook wb;
    /** Gestionnaire de cellules */
    protected CellHelper helper;
    /** Indice de la dernière colonne de la feuille */
    protected int maxIndice;
    /** Helper de gestion du worlkbook */
    protected CreationHelper createHelper;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlExcel(File file)
    {
        this.file = file;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES ABSTRAITES ----------*/

    /**
     * Initialise le WorkBook
     */
    protected abstract void createWb();

    /**
     * Initialise les indices des colonnes
     */
    protected abstract void calculIndiceColonnes();

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Mise à jour de l'indice max des colonnes.
     * 
     * @param i
     *          Indice de la colonne.
     */
    protected void testMax(int i)
    {
        if (maxIndice < i)
            maxIndice = i;
    }

    /*---------- ACCESSEURS ----------*/

}
