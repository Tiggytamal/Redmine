package junit;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.powermock.reflect.Whitebox;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.xml.ControlXML;
import dao.ListeDao;
import model.Info;
import model.bdd.ComposantSonar;
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
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));

        List<ComposantSonar> compos = ListeDao.daoCompo.readAll();
        for (ComposantSonar compo : compos)
        {
            if (compo.getAppli() == null)
                System.out.println(compo.getNom());
        }
    }

}
