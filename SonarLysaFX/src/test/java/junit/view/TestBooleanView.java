package junit.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.ParamBool;
import view.ParamBoolView;

@RunWith(JfxRunner.class)
public class TestBooleanView
{
    @Test
    public void testBooleanView()
    {
        ParamBoolView view = new ParamBoolView(ParamBool.VUESSUIVI, true);
        assertTrue(view.getType() == ParamBool.VUESSUIVI); 
        assertTrue(view.getField().isSelected()); 

        view = new ParamBoolView(ParamBool.VUESSUIVI, null);
        assertTrue(view.getType() == ParamBool.VUESSUIVI); 
        assertTrue(!view.getField().isSelected());
    }
}
