package junit;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.xml.ControlXML;
import dao.DaoFactory;
import model.DataBaseXML;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautQualite;

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
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8904,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        
        Map<String, ComposantSonar> mapCompos = DaoFactory.getDao(ComposantSonar.class).readAllMap();
        Map<String, DefautQualite> mapDQ = DaoFactory.getDao(DefautQualite.class).readAllMap();
        DataBaseXML db = ModelFactory.build(DataBaseXML.class);
        db.getMapCompos().putAll(mapCompos);
        db.getMapDefautQualite().putAll(mapDQ);
        new ControlXML().saveXML(db);
    }

}
