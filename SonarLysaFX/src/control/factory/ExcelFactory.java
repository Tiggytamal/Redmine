package control.factory;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlApps;
import control.ControlChefService;
import control.ControlClarity;
import control.ControlEdition;
import control.ControlPic;
import control.parent.ControlExcel;
import model.enums.TypeCol;
import utilities.TechnicalException;

public class ExcelFactory
{
    private ExcelFactory() {}
    
    /**
     * Retourne une instance d'un controleur Excel en fonction du type d'�num�ration
     * @param type
     * @param file
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public  static <T extends Enum<T> & TypeCol, R extends ControlExcel<T, Y>, Y> R getControlleur(Class<T> type, File file) throws InvalidFormatException, IOException
    {
        switch (type.getName())
        {           
            case "model.enums.TypeColClarity" :
                return (R) new ControlClarity(file);
                
            case "model.enums.TypeColChefServ" :
                return (R) new ControlChefService(file);
            
            case "model.enums.TypeColPic" :
                return (R) new ControlPic(file);
                
            case "model.enums.TypeColEdition" :
                return (R) new ControlEdition(file);
                
            case "model.enums.TypeColApps" :
                return (R) new ControlApps(file);
                
            default:
                throw new TechnicalException("ExcelFactory.getControlleur - type non g�r� : " + type.toString(), null);

        }
    }
}
