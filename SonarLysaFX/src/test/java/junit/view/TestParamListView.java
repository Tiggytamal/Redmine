package junit.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import junit.JunitBase;
import model.enums.ParamSpec;
import utilities.Statics;
import view.ParamListView;

@RunWith(JfxRunner.class)
public class TestParamListView extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private ParamListView handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        handler = new ParamListView(ParamSpec.MEMBRESJAVA);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testSauverValeurs()
    {
        String saveParam = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESJAVA);
                
        handler.sauverValeurs();
        
        Statics.proprietesXML.getMapParamsSpec().put(ParamSpec.MEMBRESJAVA, saveParam);
        
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
