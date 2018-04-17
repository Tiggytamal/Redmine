package junit.control.rtc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.rtc.ControlRTC;
import junit.JunitBase;
import model.Anomalie;
import model.ModelFactory;

public class ControlRTCTest extends JunitBase
{
    private ControlRTC handler;

    public ControlRTCTest()
    {
        handler = ControlRTC.INSTANCE;
         if (!handler.connexion())
             System.out.println("erreur connexion");
    }

    @Before
    public void init()
    {
    }

    @Test
    public void testAttibuts() throws TeamRepositoryException
    {
        IWorkItem item = handler.recupWorkItemDepuisId(307396);
        @SuppressWarnings("unused")
        IAttribute attribut = handler.findAttribute("PRJF_BF0377_DML_Refonte Ident Auth forte", "fr.ca.cat.attribut.datedelivraison");

        System.out.println("Id : " + item.getId());
        System.out.println("Tags : " + item.getTags2());
        System.out.println("Type : " +item.getWorkItemType());
        System.out.println("Date création : " + item.getCreationDate());      
        IContributor creator = handler.recupererItemDepuisHandle(IContributor.class, item.getCreator());
        System.out.println("creator : " + creator.getName());        
        IContributor contributor = handler.recupererItemDepuisHandle(IContributor.class, item.getOwner());
        System.out.println("Owner : " + contributor.getName());
        List<IAttributeHandle> liste = item.getCustomAttributes();
        List<IAttribute> attrbs = new ArrayList<>();
        for (IAttributeHandle handle : liste)
        {
            IAttribute attrb = handler.recupererEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
            attrbs.add(attrb);
            handler.recupvalueAttribut(attrb, item);
        }
    }

    @Test
    public void creerDefect() throws TeamRepositoryException
    {
        String PROJETTEST = "PRJF_T300703";
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setCpiProjet("MATHON Grégoire");
        ano.setProjetRTC(PROJETTEST);
        
        handler.creerDefect(ano);        
    }
    
    @Test
    public void test() throws TeamRepositoryException
    {

    }
}