package junit;

import java.io.IOException;
import java.util.Base64;

import org.powermock.reflect.Whitebox;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.xml.ControlXML;
import model.Info;
import utilities.Statics;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{

    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException, IOException
    {
        Info info = new ControlXML().recupererXMLResources(Info.class);
        Whitebox.setInternalState(Statics.class, info);
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8904,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println("Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        
        System.out.println("4.0".substring(0,1));
        System.out.println("CHC2017-S24".matches("^CHC(_CDM){0,1}20[12][0-9]\\-S[0-5][0-9]$"));
    }

}
