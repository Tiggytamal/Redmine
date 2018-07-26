package junit.view;

import static org.junit.Assert.assertEquals;
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
        assertEquals(ParamBool.VUESSUIVI, view.getType()); 
        assertTrue(view.getField().isSelected()); 

        view = new ParamBoolView(ParamBool.VUESSUIVI, null);
        assertEquals(ParamBool.VUESSUIVI, view.getType()); 
        assertTrue(!view.getField().isSelected());
    }
}
