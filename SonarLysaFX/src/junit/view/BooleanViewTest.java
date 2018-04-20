package junit.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.TypeBool;
import view.BooleanView;

@RunWith(JfxRunner.class)
public class BooleanViewTest
{
    @Test
    public void booleanView()
    {
        BooleanView view = new BooleanView(TypeBool.VUESSUIVI, true);
        assertTrue(view.getType() == TypeBool.VUESSUIVI); 
        assertTrue(view.getField().isSelected()); 

        view = new BooleanView(TypeBool.VUESSUIVI, null);
        assertTrue(view.getType() == TypeBool.VUESSUIVI); 
        assertTrue(!view.getField().isSelected());
    }
}
