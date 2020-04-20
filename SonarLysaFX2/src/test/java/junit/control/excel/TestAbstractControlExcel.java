package junit.control.excel;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;

import control.excel.AbstractControlExcel;
import junit.JunitBase;

public abstract class TestAbstractControlExcel<T, C extends AbstractControlExcel> extends JunitBase<C>
{
    /*---------- ATTRIBUTS ----------*/

    /** L'énumération du type de colonne */
    protected Class<T> colClass;
    /** Le chemin d'acces au fichier (ressources ou repertoire) - doit être initialise dans les classes concretes */
    protected String fichier;
    /** Le workbook gérant le fichier Excel - doit être initialise dans les classes concretes */
    protected Workbook wb;
    /** Le fichier Excel utilise - doit être initialise dans les classes concretes */
    protected File file;

    /*---------- CONSTRUCTEURS ----------*/

    protected TestAbstractControlExcel(Class<T> colClass, String fichier)
    {
        this.colClass = colClass;
        this.fichier = fichier;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
