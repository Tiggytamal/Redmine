package junit.control.excel;

import static org.powermock.reflect.Whitebox.getField;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import control.excel.AbstractControlExcelWrite;
import control.excel.ExcelFactory;
import junit.JunitBase;
import model.enums.TypeColW;
import utilities.Statics;

public class TestControlExcelWrite<T extends Enum<T> & TypeColW, C extends AbstractControlExcelWrite<T, Y>, Y> extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private String fichier;
    private Class<T> typeColClass;
    protected File file;
    protected C controlTest;
    protected Workbook wb;
    
    /*---------- CONSTRUCTEURS ----------*/

    /**
     * Constructeur de la classe de test
     * 
     * @param typeColClass
     *            L'énumération correspondante au fichier
     * @param fichier
     *            Nom du fichier dans les resources de test
     */
    public TestControlExcelWrite(Class<T> typeColClass, String fichier)
    {
        super();
        this.typeColClass = typeColClass;
        this.fichier = fichier;
    }
    
    /**
     * Méthode d'initialisation des tests
     * 
     * @throws InvalidFormatException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Override
    public void init() throws IOException, IllegalAccessException
    {
        file = new File(Statics.ROOT + fichier);
        controlTest = ExcelFactory.getWriter(typeColClass, file);
        wb = (Workbook) getField(controlTest.getClass(), "wb").get(controlTest);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
