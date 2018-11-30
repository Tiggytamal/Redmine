package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.getField;
import static utilities.Statics.EMPTY;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.model.AbstractTestModel;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.EtatDefaut;
import model.enums.TypeAction;
import model.enums.TypeDefaut;
import model.enums.TypeVersion;
import utilities.Statics;
import utilities.TechnicalException;

public class TestDefautQualite extends AbstractTestModel<DefautQualite>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void initImpl()
    {
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu("123456"));
    }

    @Test
    public void testAnomlieWithLot()
    {
        // Test création anomalie depuis LotSuiviRTC
        objetTest = DefautQualite.build(ModelFactory.build(LotRTC.class));
        assertNotNull(objetTest);
    }

    @Test
    public void testToString()
    {
        objetTest = ModelFactory.build(DefautQualite.class);
        String string = objetTest.toString();
        System.out.println(string);
        assertTrue(string.contains("liensLot=<null>"));
        assertTrue(string.contains("numeroAnoRTC=0"));
        assertTrue(string.contains("liensAno=<null>"));
        assertTrue(string.contains("etatRTC=<null>"));
        assertTrue(string.contains("typeVersion=SNAPSHOT"));
        assertTrue(string.contains("securite=false"));
        assertTrue(string.contains("remarque="));
        assertTrue(string.contains("dateCreation=<null>"));
        assertTrue(string.contains("dateDetection="));
        assertTrue(string.contains("dateRelance=<null>"));
        assertTrue(string.contains("dateReso=<null>"));
        assertTrue(string.contains("dateReouv=<null>"));
        assertTrue(string.contains("dateMepPrev=<null>"));
        assertTrue(string.contains("etatDefaut=NOUVEAU"));
        assertTrue(string.contains("typeDefaut=SONAR"));
        assertTrue(string.contains("action=VIDE"));
        assertTrue(string.contains("idBase=0"));
        assertTrue(string.contains("timeStamp="));
    }

    @Test
    public void testGetMapIndex()
    {
        assertEquals("123456", objetTest.getMapIndex());
    }

    @Test
    public void testCalculTraitee() throws IllegalArgumentException, IllegalAccessException
    {
        // Avec une objet juste initialisé, le booleén doit être à faux.
        assertFalse(objetTest.calculTraitee());

        // Test avec remarque non nulle.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque(EMPTY);
        assertFalse(objetTest.calculTraitee());

        // Test avec remarque non vide.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque("remarque");
        assertTrue(objetTest.calculTraitee());

        // test avec numéro d'anomalie non 0.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque(null);
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu("123456"));
        objetTest.setNumeroAnoRTC(10);
        assertTrue(objetTest.calculTraitee());

        // Avec une action non nulle
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        getField(objetTest.getClass(), "numeroAnoRTC").set(objetTest, 0);
        objetTest.setAction(TypeAction.ASSEMBLER);
        assertTrue(objetTest.calculTraitee());

        // Avec une action vide
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setAction(TypeAction.VIDE);
        objetTest.setRemarque(EMPTY);
        assertFalse(objetTest.calculTraitee());

        // Avec un etat non nouveau
        objetTest.setAction(TypeAction.ASSEMBLER);
        objetTest.setEtatDefaut(EtatDefaut.ABANDONNE);
        assertTrue(objetTest.calculTraitee());

    }

    @Test
    public void testControlLiens() throws IllegalArgumentException, IllegalAccessException
    {
        final String LIENSLOT = "http://ttp10-snar.ca-technologies.fr/governance?id=view_lot_123456";
        final String LIENSANO = "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/PROJET#action=com.ibm.team.workitem.viewWorkItem&id=12456";

        // Test sans numéro
        objetTest.controleLiens();
        assertEquals(EMPTY, objetTest.getLiensAno());
        assertEquals(LIENSLOT, objetTest.getLiensLot());

        // Test liens lot null
        objetTest.setLiensLot(null);
        assertEquals(LIENSLOT, objetTest.getLiensLot());

        // Test liens lot vide
        objetTest.setLiensLot(EMPTY);
        assertEquals(LIENSLOT, objetTest.getLiensLot());

        // Test liens ano sans numero ano
        objetTest.setLiensAno(EMPTY);
        getField(objetTest.getClass(), "numeroAnoRTC").set(objetTest, 0);
        assertEquals(EMPTY, objetTest.getLiensAno());
        objetTest.setLiensAno(null);
        assertEquals(EMPTY, objetTest.getLiensAno());

        // Test avec liens ano null et numero ano
        objetTest.setNumeroAnoRTC(12456);
        objetTest.setLiensAno(null);
        objetTest.getLotRTC().setProjetRTC("PROJET");
        assertEquals(LIENSANO, objetTest.getLiensAno());

        // Test avec liens ano vide et numero ano
        objetTest.setLiensAno(EMPTY);
        assertEquals(LIENSANO, objetTest.getLiensAno());
        objetTest.setLiensAno("a");
        assertEquals("a", objetTest.getLiensAno());

    }

    @Test
    public void testCreerLiensAnoRTC() throws Exception
    {
        assertEquals(EMPTY, objetTest.getLiensAno());
        final String LIENSANO = "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/PROJET#action=com.ibm.team.workitem.viewWorkItem&id=0";
        objetTest.getLotRTC().setProjetRTC("PROJET");
        Whitebox.invokeMethod(objetTest, "creerLiensAnoRTC");
        assertEquals(LIENSANO, objetTest.getLiensAno());
    }

    @Test
    public void testCreerLiensLotRTC() throws Exception
    {
        final String LIENSLOT = "http://ttp10-snar.ca-technologies.fr/governance?id=view_lot_123456";
        getField(objetTest.getClass(), "lotRTC").set(objetTest, null);
        Whitebox.invokeMethod(objetTest, "creerLiensLotRTC");
        assertEquals(LIENSLOT, objetTest.getLiensLot());
    }

    @Test
    public void testGetLotRTC()
    {
        // Test avec initialisation
        assertNotNull(objetTest.getLotRTC());

        // Protection null
        objetTest.setLotRTC(null);
        assertNotNull(objetTest.getLotRTC());
    }

    @Test
    public void testControleLiensAno() throws Exception
    {
        objetTest.setNumeroAnoRTC(123456);
        getField(objetTest.getClass(), "liensAno").set(objetTest, null);
        Whitebox.invokeMethod(objetTest, "controleLiensAno");
        assertEquals(EMPTY, objetTest.getLiensAno());

    }

    @Test
    public void testGetNumeroAnomalie()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getNumeroAnoRTC());

        // Test setter et getter
        int integer = 12;
        objetTest.setNumeroAnoRTC(integer);
        assertEquals(integer, objetTest.getNumeroAnoRTC());
    }

    @Test
    public void testGetEtat()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getEtatRTC());

        // Test setter et getter
        String string = "etat";
        objetTest.setEtatRTC(string);
        assertEquals(string, objetTest.getEtatRTC());
    }

    @Test
    public void testGetSecurite()
    {
        // test valeur vide ou nulle
        assertEquals(false, objetTest.isSecurite());

        // Test setter et getter
        objetTest.setSecurite(true);
        assertTrue(objetTest.isSecurite());
    }

    @Test
    public void testGetRemarque()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getRemarque());

        // Test setter et getter
        String string = "remarque";
        objetTest.setRemarque(string);
        assertEquals(string, objetTest.getRemarque());
    }

    @Test
    public void testGetLiensLot()
    {
        // test valeur vide ou nulle
        assertEquals("http://ttp10-snar.ca-technologies.fr/governance?id=view_lot_123456", objetTest.getLiensLot());

        // Test setter et getter
        String string = "liensLot";
        objetTest.setLiensLot(string);
        assertEquals(string, objetTest.getLiensLot());
    }

    @Test
    public void testGetLiensAno()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLiensAno());

        // Test setter et getter
        String string = "liensAno";
        objetTest.setLiensAno(string);
        assertEquals(string, objetTest.getLiensAno());
    }

    @Test
    public void testGetVersion() throws IllegalArgumentException, IllegalAccessException
    {
        // test valeur vide ou nulle
        assertEquals(TypeVersion.SNAPSHOT, objetTest.getTypeVersion());

        // Test setter et getter
        TypeVersion typeVersion = TypeVersion.RELEASE;
        objetTest.setTypeVersion(typeVersion);
        assertEquals(typeVersion, objetTest.getTypeVersion());

        // Protection null
        objetTest.setTypeVersion(null);
        assertEquals(typeVersion, objetTest.getTypeVersion());

        // Test remise à null remonte SNAPSHOT
        getField(objetTest.getClass(), "typeVersion").set(objetTest, null);
        assertEquals(TypeVersion.SNAPSHOT, objetTest.getTypeVersion());
    }

    @Test
    public void testGetAction() throws IllegalArgumentException, IllegalAccessException
    {
        // test valeur vide ou nulle
        assertEquals(TypeAction.VIDE, objetTest.getAction());

        // Test setter et getter
        objetTest.setAction(TypeAction.CREER);
        assertEquals(TypeAction.CREER, objetTest.getAction());

        // protection null
        objetTest.setAction(null);
        assertEquals(TypeAction.CREER, objetTest.getAction());

        objetTest.setAction(TypeAction.VIDE);
        assertEquals(TypeAction.VIDE, objetTest.getAction());

        getField(objetTest.getClass(), "action").set(objetTest, null);
        assertEquals(TypeAction.VIDE, objetTest.getAction());
    }

    @Test
    public void testGetDateCreation()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getDateCreation());

        // Test setter et getter
        objetTest.setDateCreation(today);
        assertEquals(today, objetTest.getDateCreation());

        // Proection null
        objetTest.setDateCreation(null);
        assertEquals(today, objetTest.getDateCreation());
    }

    @Test
    public void testGetDateDetection()
    {
        // test valeur vide ou nulle
        assertEquals(Statics.TODAY, objetTest.getDateDetection());

        // Test setter et getter
        objetTest.setDateDetection(today);
        assertEquals(today, objetTest.getDateDetection());

        // Protection null
        objetTest.setDateDetection(null);
        assertEquals(today, objetTest.getDateDetection());
    }

    @Test
    public void testGetDateRelance()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getDateRelance());

        // Test setter et getter
        objetTest.setDateRelance(today);
        assertEquals(today, objetTest.getDateRelance());
    }

    @Test
    public void testGetDateReso()
    {
        // test valeur vide ou nulle
        assertEquals(null, objetTest.getDateReso());

        // Test setter et getter
        objetTest.setDateReso(today);
        assertEquals(today, objetTest.getDateReso());
    }

    @Test
    public void testEtatDefaut()
    {
        // test valeur nouveau
        assertEquals(EtatDefaut.NOUVEAU, objetTest.getEtatDefaut());

        // Test setter et getter
        EtatDefaut etatDefaut = EtatDefaut.ABANDONNE;
        objetTest.setEtatDefaut(etatDefaut);
        assertEquals(etatDefaut, objetTest.getEtatDefaut());

        // Test remontée erreur technique
        boolean erreur = false;
        try
        {
            objetTest.setEtatDefaut(null);
        }
        catch (TechnicalException e)
        {
            assertEquals("Tentative de mise à null de DefautQualite.etatDefaut", e.getMessage());
            erreur = true;
        }
        assertTrue("Pas d'exception renvoyée : DefautQualite.setEtatDefaut à null", erreur);
    }

    @Test
    public void testTypeDefaut()
    {
        // test valeur nouveau
        assertEquals(TypeDefaut.SONAR, objetTest.getTypeDefaut());

        // Test setter et getter
        TypeDefaut etatDefaut = TypeDefaut.APPLI;
        objetTest.setTypeDefaut(etatDefaut);
        assertEquals(etatDefaut, objetTest.getTypeDefaut());

        // Test remontée erreur technique
        boolean erreur = false;
        try
        {
            objetTest.setTypeDefaut(null);
        }
        catch (TechnicalException e)
        {
            assertEquals("Tentative de mise à null de DefautQualite.typeDefaut", e.getMessage());
            erreur = true;
        }
        assertTrue("Pas d'exception renvoyée : DefautQualite.setTypeDefaut à null", erreur);
    }

    @Test
    public void testGetNewCodeAppli()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNewCodeAppli());

        // Test setter et getter
        String string = "code";
        objetTest.setNewCodeAppli(string);
        assertEquals(string, objetTest.getNewCodeAppli());
    }

    @Test
    public void testGetDateReouv()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getDateReouv());

        // Test setter et getter
        objetTest.setDateReouv(today);
        assertEquals(today, objetTest.getDateReouv());
    }

    @Test
    public void testGetDateMepPrev()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getDateMepPrev());

        // Test setter et getter
        objetTest.setDateMepPrev(today);
        assertEquals(today, objetTest.getDateMepPrev());

        // Protection null
        objetTest.setDateMepPrev(null);
        assertEquals(today, objetTest.getDateMepPrev());
    }

    @Test
    public void testGetTimeStamp() throws IllegalArgumentException, IllegalAccessException
    {
        assertNotNull(objetTest.getTimeStamp());

        getField(objetTest.getClass(), "timeStamp").set(objetTest, null);
        assertNotNull(objetTest.getTimeStamp());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
