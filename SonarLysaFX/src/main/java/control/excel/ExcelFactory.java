package control.excel;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import model.enums.TypeColR;
import model.enums.TypeColW;
import utilities.TechnicalException;

public interface ExcelFactory
{   
    /**
     * Retourne une instance d'un controleur Excel en fonction du type d'énumération
     * @param type
     * @param file
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T> & TypeColR, R extends ControlExcelRead<T, Y>, Y> R getReader(Class<T> type, File file)
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
                
            case "model.enums.TypeColSuivi" :
                return (R) new ControlSuivi(file);
                
            default:
                throw new TechnicalException("ExcelFactory.getReader - type non géré : " + type.toString(), null);
        }
    }
    
    /**
     * Retourne une instance d'un controleur Excel en fonction du type d'énumération
     * @param type
     * @param file
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T> & TypeColW, R extends ControlExcelWrite<T, Y>, Y> R getWriter(Class<T> type, File file)
    {
        switch (type.getName())
        {           
            case "model.enums.TypeColVul" :                
                return (R) new ControlExtractVul(file);
                
            case "model.enums.TypeColApps" :
                return (R) new ControlAppsW(file);
                
            default:
                throw new TechnicalException("ExcelFactory.getXriter - type non géré : " + type.toString(), null);
        }
    }
}
