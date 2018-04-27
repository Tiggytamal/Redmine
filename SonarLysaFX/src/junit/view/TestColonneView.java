package junit.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import model.enums.TypeColSuivi;
import view.ColonneView;

@RunWith(JfxRunner.class)
public class TestColonneView
{
    @Test
    public void colonneView()
    {
        String texte = "a";
        ColonneView<TypeColSuivi> view = new ColonneView<>(TypeColSuivi.ACTION, texte);
        assertTrue(view.getField().getText().equals(texte));
        assertTrue(view.getType() == TypeColSuivi.ACTION);
        view = new ColonneView<>(TypeColSuivi.ANOMALIE, null);
        assertTrue(view.getField().getText().equals(""));
        assertTrue(view.getType() == TypeColSuivi.ANOMALIE);
    }
}
