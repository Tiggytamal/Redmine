package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected ControlExcelWrite(File file)
    {
        super(file);
        createWb();
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
    
    protected abstract void calculIndiceColonnes();
    
    protected abstract void initTitres();
    
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
    
    /*---------- ACCESSEURS ----------*/

}