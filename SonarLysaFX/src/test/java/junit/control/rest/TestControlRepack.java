package junit.control.rest;

import java.util.List;

import org.junit.Test;

import control.rest.ControlRepack;
import dao.ListeDao;
import junit.JunitBase;
import model.rest.repack.RepackREST;

public class TestControlRepack extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlRepack handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = ControlRepack.INSTANCE;
    }

    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetRepacksComposant()
    {        
        List<RepackREST> liste = handler.getRepacksComposant(ListeDao.daoCompo.recupEltParIndex("fr.ca.cat.controlermifentretienjoint:SRVT_ControlerMIFEntretienJoint_Build:14"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
