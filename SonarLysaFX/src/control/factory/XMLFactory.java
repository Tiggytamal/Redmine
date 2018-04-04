package control.factory;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlChefService;
import control.ControlClarity;
import control.ControlEdition;
import control.ControlPic;
import control.parent.ControlExcel;
import model.enums.TypeCol;

public class XMLFactory
{
    private XMLFactory() {}
    
    public  static <T extends Enum<T> & TypeCol, R extends ControlExcel<T>> ControlExcel<T> getControlleur(Class<T> type, File file) throws InvalidFormatException, IOException
    {
        switch (type.getName())
        {           
            case "model.enums.TypeColClarity" :
                return (ControlExcel<T>) new ControlClarity(file);

                
            case "model.enums.TypeColChefServ" :
                return (ControlExcel<T>) new ControlChefService(file);

            
            case "model.enums.TypeColPic" :
                return (ControlExcel<T>) new ControlPic(file);
                
            case "model.enums.TypeColEdition" :
                return (ControlExcel<T>) new ControlEdition(file);
                
            default:
                return null;

        }
    }
}
