package junit.control.rtc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.rtc.WorkItemInitialization;
import junit.JunitBase;
import model.Anomalie;
import model.ModelFactory;
import model.enums.Param;
import utilities.Statics;

public class TestWorkItemInitialization extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private WorkItemInitialization handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setLot("Lot 123456");
        handler = new WorkItemInitialization(null, null, null, ano);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCalculEditionRTC() throws Exception
    {
        assertEquals("E32", Whitebox.invokeMethod(handler, "calculEditionRTC", "E32"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CHC_CDM2018-S34"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CHC_2018-S34"));
        assertEquals(Statics.proprietesXML.getMapParams().get(Param.RTCLOTCHC), Whitebox.invokeMethod(handler, "calculEditionRTC", "CDM_2018-S34"));
        assertEquals("E31.0.FDL", Whitebox.invokeMethod(handler, "calculEditionRTC", "E31_Fil_De_Leau"));
        assertEquals("E30.1.FDL", Whitebox.invokeMethod(handler, "calculEditionRTC", "E30.1_Fil_De_Leau"));

    }
    
    @Test
    public void testCalculPariteEdition() throws Exception
    {
        assertTrue(Whitebox.invokeMethod(handler, "calculPariteEdition", "E32"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E31"));
        assertTrue(Whitebox.invokeMethod(handler, "calculPariteEdition", "E30"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E29"));
        assertFalse(Whitebox.invokeMethod(handler, "calculPariteEdition", "E31.0.FDL"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
