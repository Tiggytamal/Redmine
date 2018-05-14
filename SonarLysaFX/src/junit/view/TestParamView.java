package junit.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.Param;
import view.ParamView;

@RunWith(JfxRunner.class)
public class TestParamView
{
    @Test
    public void paramView()
    {
        String texte = "a";
        ParamView view = new ParamView(Param.ABSOLUTEPATH, texte);
        assertTrue(view.getType() == Param.ABSOLUTEPATH);
        assertTrue(view.getField().getText().equals(texte));   
        
        view = new ParamView(Param.NOMFICHIER, null);
        assertTrue(view.getType() == Param.NOMFICHIER);
        assertTrue(view.getField().getText().equals("")); 
    }
}
