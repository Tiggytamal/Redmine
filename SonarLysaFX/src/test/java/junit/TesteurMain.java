package junit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import com.ibm.team.repository.common.TeamRepositoryException;

import dao.DaoDefautQualite;
import dao.DaoFactory;
import model.bdd.DefautQualite;
import model.enums.Param;
import utilities.Statics;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{

    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException, IOException, URISyntaxException
    {
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8904,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println("Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes()));

        DaoDefautQualite dao = DaoFactory.getDao(DefautQualite.class);
        List<DefautQualite> liste = dao.readAll();

        for (DefautQualite dq : liste)
        {
            String urlStr = Statics.proprietesXML.getMapParams().get(Param.LIENSANOS) + dq.getLotRTC().getProjetRTC() + Statics.FINLIENSANO + dq.getNumeroAnoRTC();
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            dq.setLiensAno(uri.toASCIIString());            
        }
        
        dao.persist(liste);
    }

}
