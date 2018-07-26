package junit.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.Param;
import view.ParamView;

@RunWith(JfxRunner.class)
public class TestParamView
{
    @Test
    public void testParamView()
    {
        String texte = "a";
        ParamView view = new ParamView(Param.ABSOLUTEPATH, texte);
        assertEquals(Param.ABSOLUTEPATH, view.getType());
        assertEquals(texte, view.getField().getText());   
        
        view = new ParamView(Param.NOMFICHIER, null);
        assertEquals(Param.NOMFICHIER, view.getType());
        assertEquals(0, view.getField().getText().length()); 
    }
}
