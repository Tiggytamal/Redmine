package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.enums.TypeColW;
import utilities.CellHelper;
import utilities.TechnicalException;

public abstract class ControlExcelWrite<T extends Enum<T> & TypeColW, R> extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/
    
    /** Classe de l'énumération des classes filles */
    protected Class<T> enumeration;
    
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log"); 
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected ControlExcelWrite(File file)
    {
        super(file);
        createWb();
        initEnum();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void write()
    {
        try(FileOutputStream stream = new FileOutputStream(file.getName()))
        {
            wb.write(stream);
            wb.close();
        } catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }
    
    /*---------- METHODS ABSTRAITES ----------*/
       
    /**
     * Initilisa les titres des feuilles
     */
    protected abstract void initTitres();
    
    /**
     * Enregistre les données dans la feuille donnée du fichier excel
     * 
     * @param donnees
     * @param sheet
     */
    protected abstract void enregistrerDonnees(R donnees, Sheet sheet);
    
    /*---------- METHODES PRIVEES ----------*/
    
    @Override
    protected final void createWb()
    {
        wb = new XSSFWorkbook();
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }
    
    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de récuperer la classe sous forme de type paramétré
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        
        // On récupère les paramètres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        // Instantiate the Parameter and initialize it.
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        } catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.ControlExcelRead", e);
        }
    }
    
    /*---------- ACCESSEURS ----------*/

}