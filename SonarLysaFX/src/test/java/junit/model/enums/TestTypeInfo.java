package junit.model.enums;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.EMPTY;
import static utilities.Statics.LIENANO;
import static utilities.Statics.LIENAPP;

import org.junit.Test;

import model.enums.TypeInfo;

public class TestTypeInfo implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(16, TypeInfo.values().length);
    }
    
    @Test
    public void testGetTitre()
    {
        assertEquals("Lots avec anomalies RTC cr��es :\n", TypeInfo.ANOSRTCCREES.getTitre());
        assertEquals("Lots avec nouvelles anomalies :\n", TypeInfo.ANONEW.getTitre());
        assertEquals("Lots avec anomalies ferm�es (abandonn�es ou cl�tur�es) :\n", TypeInfo.ANOABANDON.getTitre());
        assertEquals("Lots avec anomalies non ferm�es :\n", TypeInfo.ANOABANDONRATE.getTitre());
        assertEquals("Lots avec anomalies � relancer :\n", TypeInfo.ANOARELANCER.getTitre());
        assertEquals("Anomalies RTC mises � jour :\n", TypeInfo.ANOMAJ.getTitre());
        assertEquals("Lots mis � jour :\n", TypeInfo.LOTMAJ.getTitre());
        assertEquals("Lots inconnus dans l'extraction RTC:\n", TypeInfo.LOTNONRTC.getTitre());
        assertEquals("Lots avec Clarity inconnu :\n", TypeInfo.CLARITYINCONNU.getTitre());
        assertEquals("Lots avec services sans responsable :\n", TypeInfo.SERVICESSANSRESP.getTitre());
        assertEquals("Liste des composants avec un code application obsol�te :\n", TypeInfo.APPLIOBSOLETE.getTitre());
        assertEquals("Liste des composants avec code application OK sur une verison pr�cedente :\n", TypeInfo.APPLICOMPOPRECOK.getTitre());
        assertEquals("Liste des composants avec une application non list�e dans le r�f�rentiel :\n", TypeInfo.APPLINONREF.getTitre());
        assertEquals("Liste des composants sans application :\n", TypeInfo.COMPOSANSAPP.getTitre());
        assertEquals("Liste des composants purg�s :\n", TypeInfo.COMPOPURGE.getTitre());
        assertEquals("Liste des ua trouv�es dans le catalogue des UAs :\n", TypeInfo.COMPOUAEXCEL.getTitre());
    }
    
    @Test
    public void testGetLiens()
    {
        assertEquals(LIENANO, TypeInfo.ANOSRTCCREES.getLiens());
        assertEquals(EMPTY, TypeInfo.ANONEW.getLiens());
        assertEquals(LIENANO, TypeInfo.ANOABANDON.getLiens());
        assertEquals(LIENANO, TypeInfo.ANOABANDONRATE.getLiens());
        assertEquals(LIENANO, TypeInfo.ANOARELANCER.getLiens());
        assertEquals(" - Nouvel �tat : ", TypeInfo.ANOMAJ.getLiens());
        assertEquals(" - Nouvel �tat : ", TypeInfo.LOTMAJ.getLiens());
        assertEquals(EMPTY, TypeInfo.LOTNONRTC.getLiens());
        assertEquals("- Clarity : ", TypeInfo.CLARITYINCONNU.getLiens());
        assertEquals("- Service : ", TypeInfo.SERVICESSANSRESP.getLiens());
        assertEquals(LIENAPP, TypeInfo.APPLIOBSOLETE.getLiens());
        assertEquals(LIENAPP, TypeInfo.APPLICOMPOPRECOK.getLiens());
        assertEquals(LIENAPP, TypeInfo.APPLINONREF.getLiens());
        assertEquals(EMPTY, TypeInfo.COMPOSANSAPP.getLiens());
        assertEquals(EMPTY, TypeInfo.COMPOPURGE.getLiens());
        assertEquals(LIENAPP, TypeInfo.COMPOUAEXCEL.getLiens());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeInfo.COMPOSANSAPP, TypeInfo.valueOf(TypeInfo.COMPOSANSAPP.toString()));    
    }
}
