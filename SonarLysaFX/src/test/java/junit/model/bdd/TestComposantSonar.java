package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.EtatAppli;
import model.enums.InstanceSonar;
import model.enums.Matiere;
import model.enums.QG;
import utilities.Statics;

public class TestComposantSonar extends AbstractTestModel<ComposantSonar>
{
    /*---------- ATTRIBUTS ----------*/

    private static final int NOMBRE = 1234;

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testConstructeur()
    {
        objetTest = ComposantSonar.build("id", "key", "nom");
        assertEquals("nom", objetTest.getNom());
        assertEquals("key", objetTest.getKey());
        assertEquals("id", objetTest.getId());
    }

    @Test
    public void testGetMapIndex()
    {
        assertEquals(objetTest.getKey(), objetTest.getMapIndex());
    }

    @Test
    public void testSetSecurityRatingDepuisSonar()
    {
        // Test données classiques
        final String F = "F";
        objetTest.setSecurityRatingDepuisSonar(null);
        assertEquals(F, objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar(Statics.EMPTY);
        assertEquals(F, objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("0");
        assertEquals("A", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("1");
        assertEquals("A", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("2");
        assertEquals("B", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("3");
        assertEquals("C", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("4");
        assertEquals("D", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("5");
        assertEquals("E", objetTest.getSecurityRating());
        objetTest.setSecurityRatingDepuisSonar("6");
        assertEquals(F, objetTest.getSecurityRating());
    }

    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNom());

        // Test setter et getter
        String nom = "Nom";
        objetTest.setNom(nom);
        assertEquals(nom, objetTest.getNom());
    }

    @Test
    public void testGetLotRTC()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getLotRTC());

        // Test setter et getter
        LotRTC lotRTC = LotRTC.getLotRTCInconnu("lot");
        objetTest.setLotRTC(lotRTC);
        assertEquals(lotRTC, objetTest.getLotRTC());

        // Test remise à null sans effet
        objetTest.setLotRTC(null);
        assertEquals(lotRTC, objetTest.getLotRTC());
    }

    @Test
    public void testGetKey()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getKey());

        // Test setter et getter
        String key = "Key";
        objetTest.setKey(key);
        assertEquals(key, objetTest.getKey());
    }

    @Test
    public void testGetId()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getId());

        // Test setter et getter
        String id = "Id";
        objetTest.setId(id);
        assertEquals(id, objetTest.getId());
    }

    @Test
    public void testGetAppli()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getAppli());

        // Test setter et getter
        Application appli = ModelFactory.build(Application.class);
        appli.setCode("Appli");
        objetTest.setAppli(appli);
        assertEquals(appli, objetTest.getAppli());

        // Test remise à null sans effet
        objetTest.setAppli(null);
        assertEquals(appli, objetTest.getAppli());
    }

    @Test
    public void testGetLdc()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getLdc());

        // Test setter et getter
        int ldc = 123456;
        objetTest.setLdc(ldc);
        assertEquals(ldc, objetTest.getLdc());

        // Test setter String
        objetTest.setLdc("123");
        assertEquals(123, objetTest.getLdc());
    }

    @Test
    public void testGetSecurityRating()
    {
        // test valeur vide ou nulle
        assertEquals(Statics.EMPTY, objetTest.getSecurityRating());

        // Test setter et getter
        String securiteRat = "B";
        objetTest.setSecurityRating(securiteRat);
        assertEquals(securiteRat, objetTest.getSecurityRating());
    }

    @Test
    public void testGetVulnerabilites()
    {
        // test valeur vide ou nulle
        assertEquals(0, objetTest.getVulnerabilites());

        // Test setter et getter
        objetTest.setVulnerabilites(NOMBRE);
        assertEquals(NOMBRE, objetTest.getVulnerabilites());
    }

    @Test
    public void testSetVulnerabilites()
    {
        objetTest.setVulnerabilites(NOMBRE);

        // Test String null
        String nombre = null;
        objetTest.setVulnerabilites(nombre);
        assertEquals(NOMBRE, objetTest.getVulnerabilites());

        // Test String vide
        nombre = Statics.EMPTY;
        objetTest.setVulnerabilites(nombre);
        assertEquals(NOMBRE, objetTest.getVulnerabilites());

        // Test nombre correct
        nombre = "123";
        objetTest.setVulnerabilites(nombre);
        assertEquals(123, objetTest.getVulnerabilites());

    }

    @Test
    public void testIsSecurite()
    {
        // Test setter et getter
        assertFalse(objetTest.isSecurite());
        objetTest.setSecurite(true);
        assertTrue(objetTest.isSecurite());
    }

    @Test
    public void testGetQualityGate()
    {
        // Test sans initialisation
        assertEquals(QG.NONE, objetTest.getQualityGate());

        // Test valeure nulle
        QG qg = null;
        objetTest.setQualityGate(qg);
        assertEquals(QG.NONE, objetTest.getQualityGate());

        // Test setter et getter
        qg = QG.OK;
        objetTest.setQualityGate(qg);
        assertEquals(QG.OK, objetTest.getQualityGate());
        
        // Test verison String
        objetTest.setQualityGate("ERROR");
        assertEquals(QG.ERROR, objetTest.getQualityGate());
    }

    @Test
    public void testGetBloquants()
    {
        // Test setter et getter
        assertEquals(0.0F, objetTest.getBloquants(), 0.1F);
        
        objetTest.setBloquants(1.0F);
        assertEquals(1.0F, objetTest.getBloquants(), 0.1F);       
    }

    @Test
    public void testGetCritiques()
    {
        // Test setter et getter
        assertEquals(0.0F, objetTest.getCritiques(), 0.1F);
        
        objetTest.setCritiques(1.0F);
        assertEquals(1.0F, objetTest.getCritiques(), 0.1F); 
    }

    @Test
    public void testGetDuplication()
    {
        // Test setter et getter
        assertEquals(0.0F, objetTest.getDuplication(), 0.1F);
        
        objetTest.setDuplication(1.0F);
        assertEquals(1.0F, objetTest.getDuplication(), 0.1F); 
    }

    @Test
    public void testIsVersionRelease()
    {
        // Test setter et getter
        assertFalse(objetTest.isVersionRelease());
        objetTest.setVersionRelease(true);
        assertTrue(objetTest.isVersionRelease());
    }

    @Test
    public void testGetMatiere()
    {
        // Test sans initialisation
        assertNull(objetTest.getMatiere());
        
        // Test setter et getter
        Matiere qg = Matiere.ANDROID;
        objetTest.setMatiere(qg);
        assertEquals(Matiere.ANDROID, objetTest.getMatiere());

        // Test valeure nulle
        qg = null;
        objetTest.setMatiere(qg);
        assertEquals(Matiere.ANDROID, objetTest.getMatiere());
    }

    @Test
    public void testGetEtatAppli()
    {
        // Test sans initialisation
        assertEquals(EtatAppli.OK, objetTest.getEtatAppli());

        // Test valeure nulle
        EtatAppli ea = null;
        objetTest.setEtatAppli(ea);
        assertEquals(EtatAppli.OK, objetTest.getEtatAppli());

        // Test setter et getter
        ea = EtatAppli.OBS;
        objetTest.setEtatAppli(ea);
        assertEquals(EtatAppli.OBS, objetTest.getEtatAppli());
    }

    @Test
    public void testGetInstance()
    {
        // Test sans initialisation
        assertNull(objetTest.getInstance());

        // Test setter et getter
        InstanceSonar ea = InstanceSonar.LEGACY;
        objetTest.setInstance(ea);
        assertEquals(InstanceSonar.LEGACY, objetTest.getInstance());
        
        // Test valeure nulle
        ea = null;
        objetTest.setInstance(ea);
        assertEquals(InstanceSonar.LEGACY, objetTest.getInstance());
    }

    @Test
    public void testGetVersion()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getVersion());

        // Test setter et getter
        String string = "Vs";
        objetTest.setVersion(string);
        assertEquals(string, objetTest.getVersion());
    }

    @Test
    public void testGetDateRepack()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getDateRepack());
        
        
        // Test setter et getter
        objetTest.setDateRepack(today);
        assertEquals(today, objetTest.getDateRepack());
        
        // Protection set null
        objetTest.setDateRepack(null);
        assertEquals(today, objetTest.getDateRepack());
        
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
