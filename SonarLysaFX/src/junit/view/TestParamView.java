package junit.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.TypeParam;
import view.ParamView;

@RunWith(JfxRunner.class)
public class TestParamView
{
    @Test
    public void paramView()
    {
        String texte = "a";
        ParamView view = new ParamView(TypeParam.ABSOLUTEPATH, texte);
        assertTrue(view.getType() == TypeParam.ABSOLUTEPATH);
        assertTrue(view.getField().getText().equals(texte));   
        
        view = new ParamView(TypeParam.NOMFICHIER, null);
        assertTrue(view.getType() == TypeParam.NOMFICHIER);
        assertTrue(view.getField().getText().equals("")); 
    }
}
