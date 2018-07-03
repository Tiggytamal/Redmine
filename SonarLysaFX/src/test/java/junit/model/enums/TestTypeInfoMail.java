package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.LIENANO;
import static utilities.Statics.LIENAPP;

import org.junit.Test;

import model.enums.TypeInfoMail;

public class TestTypeInfoMail
{
    @Test
    public void testSize()
    {
        assertEquals(12, TypeInfoMail.values().length);
    }
    
    @Test
    public void testGetTitre()
    {
        assertEquals("Lots avec anomalies RTC créées :\n", TypeInfoMail.ANOSRTCCREES.getTitre());
        assertEquals("Lots avec nouvelles anomalies :\n", TypeInfoMail.ANONEW.getTitre());
        assertEquals("Lotas avec anomalies abandonnées :\n", TypeInfoMail.ANOABANDON.getTitre());
        assertEquals("Lots avec anomalies non abandonnées :\n", TypeInfoMail.ANOABANDONRATE.getTitre());
        assertEquals("Lots avec anomalies à relancer :\n", TypeInfoMail.ANOARELANCER.getTitre());
        assertEquals("Anomalies RTC mises à jour :\n", TypeInfoMail.ANOMAJ.getTitre());
        assertEquals("Lots mis à jour :\n", TypeInfoMail.LOTMAJ.getTitre());
        assertEquals("Lots avec Clarity inconnu :\n", TypeInfoMail.CLARITYINCONNU.getTitre());
        assertEquals("Lots avec services sans responsable :\n", TypeInfoMail.SERVICESSANSRESP.getTitre());
        assertEquals("Liste des composants avec un code application obsolète :\n", TypeInfoMail.APPLIOBSOLETE.getTitre());
        assertEquals("Liste des composants avec une application non listée dans le référentiel :\n", TypeInfoMail.APPLINONREF.getTitre());
        assertEquals("Liste des composants sans application :\n", TypeInfoMail.COMPOSANSAPP.getTitre());
    }
    
    @Test
    public void testGetLiens()
    {
        assertEquals(LIENANO, TypeInfoMail.ANOSRTCCREES.getLiens());
        assertEquals("", TypeInfoMail.ANONEW.getLiens());
        assertEquals(LIENANO, TypeInfoMail.ANOABANDON.getLiens());
        assertEquals(LIENANO, TypeInfoMail.ANOABANDONRATE.getLiens());
        assertEquals(LIENANO, TypeInfoMail.ANOARELANCER.getLiens());
        assertEquals(" - Nouvel état : ", TypeInfoMail.ANOMAJ.getLiens());
        assertEquals(" - Nouvel état : ", TypeInfoMail.LOTMAJ.getLiens());
        assertEquals("- Clarity : ", TypeInfoMail.CLARITYINCONNU.getLiens());
        assertEquals("- Service : ", TypeInfoMail.SERVICESSANSRESP.getLiens());
        assertEquals(LIENAPP, TypeInfoMail.APPLIOBSOLETE.getLiens());
        assertEquals(LIENAPP, TypeInfoMail.APPLINONREF.getLiens());
        assertEquals("", TypeInfoMail.COMPOSANSAPP.getLiens());
    }
}
