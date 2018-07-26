package junit.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.TypeColSuivi;
import view.ColonneView;

@RunWith(JfxRunner.class)
public class TestColonneView
{
    @Test
    public void testColonneView()
    {
        String texte = "a";
        ColonneView<TypeColSuivi> view = new ColonneView<>(TypeColSuivi.ACTION, texte);
        assertEquals(texte, view.getField().getText());
        assertEquals(TypeColSuivi.ACTION, view.getType());
        view = new ColonneView<>(TypeColSuivi.ANOMALIE, null);
        assertEquals(0, view.getField().getText().length());
        assertEquals(TypeColSuivi.ANOMALIE, view.getType());
    }
}
