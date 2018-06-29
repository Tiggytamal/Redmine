package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFComment;
import org.junit.Before;
import org.junit.Test;

import junit.JunitBase;
import model.Anomalie;
import model.InfoClarity;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.TypeAction;

public class TestAnomalie extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private Anomalie ano;
    private XSSFComment comment;
    
    /*---------- CONSTRUCTEURS ----------*/

    public TestAnomalie()
    {
        comment = new XSSFComment(null, null, null);
    }
    
    @Before
    public void init()
    {
        ano = ModelFactory.getModel(Anomalie.class);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnomlieWithLot()
    {
        // Test création anomalie depuis LotSuiviRTC
        ano = ModelFactory.getModelWithParams(Anomalie.class, ModelFactory.getModel(LotSuiviRTC.class));
        assertNotNull(ano);
    }
    
    @Test
    public void testMajDepuisPic()
    {
        // Création lotPic
        LotSuiviRTC lotRTC = ModelFactory.getModel(LotSuiviRTC.class);
        String cpi = "cpi";
        lotRTC.setCpiProjet(cpi);
        String edition = "edition";
        lotRTC.setEdition(edition);
        String libProjet = "libProjet";
        lotRTC.setLibelle(libProjet);
        String clarity = "clarity";
        lotRTC.setProjetClarity(clarity);
        String lot = "123456";
        lotRTC.setLot(lot);
        lotRTC.setEtatLot(EtatLot.NOUVEAU);
        
        // Test
        ano.majDepuisRTC(lotRTC);
        assertEquals(cpi, ano.getCpiProjet());
        assertEquals(edition, ano.getEdition());
        assertEquals(libProjet, ano.getLibelleProjet());
        assertEquals(clarity, ano.getProjetClarity());
        assertEquals("Lot " + lot, ano.getLot());
        assertEquals(EtatLot.NOUVEAU, ano.getEtatLot());       
    }
    
    @Test
    public void testMajDepuisClarity()
    {
        // Création Clarity
        InfoClarity infoClarity = ModelFactory.getModel(InfoClarity.class);
        String direction = "direction";
        infoClarity.setDirection(direction);
        String departement = "departement";
        infoClarity.setDepartement(departement);
        String service = "service";
        infoClarity.setService(service);
        
        // Test
        ano.majDepuisClarity(infoClarity);
        assertEquals(direction, ano.getDirection());
        assertEquals(departement, ano.getDepartement());
        assertEquals(service, ano.getService());
    }
    
    @Test
    public void testCalculTraitee()
    {
        // Avec une objet juste initialisé, le booleén doit être à faux.
        ano.calculTraitee();
        assertFalse(ano.isTraitee());
        
        // Test avec remarque non nulle.
        ano.setRemarque("");
        ano.calculTraitee();
        assertFalse(ano.isTraitee());
        
        // Test avec remarque non vide.
        ano.setRemarque("remarque");
        ano.calculTraitee();
        assertTrue(ano.isTraitee());
        
        // test avec numéro d'anomalie non 0.
        ano.setRemarque(null);
        ano.setNumeroAnomalie(10);
        ano.calculTraitee();
        assertTrue(ano.isTraitee());
        
        // Test avec les deux bons
        ano.setRemarque("rem");
        ano.calculTraitee();
        assertTrue(ano.isTraitee());
    }
    
    @Test
    public void testGetMatieresString()
    {
        // Initialisation
        Set<Matiere> matieres = new HashSet<>();
        matieres.add(Matiere.JAVA);
        matieres.add(Matiere.DATASTAGE);
        ano.setMatieres(matieres);
        
        // Test
        assertTrue(ano.getMatieresString().contains(Matiere.JAVA.toString()));
        assertTrue(ano.getMatieresString().contains(Matiere.DATASTAGE.toString()));
        assertTrue(ano.getMatieresString().contains(" - "));
    }
    
    @Test
    public void testSetMatieresString()
    {
        // Test avec string vide ou null
        ano.setMatieresString(null);
        assertTrue(ano.getMatieres().isEmpty());
        ano.setMatieresString("");
        assertTrue(ano.getMatieres().isEmpty());
        
        
        // Test avec une matière
        String matiere = "JAVA";
        ano.setMatieresString(matiere);
        assertEquals(Matiere.JAVA, ano.getMatieres().iterator().next());
        
        // Test avec deux matières
        String matieres = "JAVA - DATASTAGE";
        ano.setMatieresString(matieres);
        assertTrue(ano.getMatieres().contains(Matiere.JAVA));
        assertTrue(ano.getMatieres().contains(Matiere.DATASTAGE));
    }
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getDirection());
        
        // Test setter et getter
        String direction = "Direction";
        ano.setDirection(direction);
        assertEquals(direction, ano.getDirection());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getDepartement());
        
        // Test setter et getter
        String string = "Departement";
        ano.setDepartement(string);
        assertEquals(string, ano.getDepartement());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getService());
        
        // Test setter et getter
        String string = "Service";
        ano.setService(string);
        assertEquals(string, ano.getService());       
    }
    
    @Test
    public void testGetResponsableService()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getResponsableService());
        
        // Test setter et getter
        String string = "RespService";
        ano.setResponsableService(string);
        assertEquals(string, ano.getResponsableService());       
    }
    
    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getProjetClarity());
        
        // Test setter et getter
        String string = "projetClarity";
        ano.setProjetClarity(string);
        assertEquals(string, ano.getProjetClarity());       
    }
    
    @Test
    public void testGetLibelleProjet()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getLibelleProjet());
        
        // Test setter et getter
        String string = "libelleProjet";
        ano.setLibelleProjet(string);
        assertEquals(string, ano.getLibelleProjet());       
    }
    
    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getCpiProjet());
        
        // Test setter et getter
        String string = "cpiProjet";
        ano.setCpiProjet(string);
        assertEquals(string, ano.getCpiProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getEdition());
        
        // Test setter et getter
        String string = "edition";
        ano.setEdition(string);
        assertEquals(string, ano.getEdition());       
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getLot());
        
        // Test setter et getter
        String string = "lot";
        ano.setLot(string);
        assertEquals(string, ano.getLot());       
    }
    
    @Test
    public void testGetNumeroAnomalie()
    {
        // test valeur vide ou nulle
        assertEquals(0, ano.getNumeroAnomalie());
        
        // Test setter et getter
        int integer = 12;
        ano.setNumeroAnomalie(integer);
        assertEquals(integer, ano.getNumeroAnomalie());       
    }
    
    @Test
    public void testGetEtat()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getEtat());
        
        // Test setter et getter
        String string = "etat";
        ano.setEtat(string);
        assertEquals(string, ano.getEtat());       
    }
    
    @Test
    public void testGetSecurite()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getSecurite());
        
        // Test setter et getter
        String string = "securite";
        ano.setSecurite(string);
        assertEquals(string, ano.getSecurite());       
    }
    
    @Test
    public void testGetRemarque()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getRemarque());
        
        // Test setter et getter
        String string = "remarque";
        ano.setRemarque(string);
        assertEquals(string, ano.getRemarque());       
    }
    
    @Test
    public void testGetLiensLot()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getLiensLot());
        
        // Test setter et getter
        String string = "liensLot";
        ano.setLiensLot(string);
        assertEquals(string, ano.getLiensLot());       
    }
    
    @Test
    public void testGetLiensAno()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getLiensAno());
        
        // Test setter et getter
        String string = "liensAno";
        ano.setLiensAno(string);
        assertEquals(string, ano.getLiensAno());       
    }
    
    @Test
    public void testGetTypeAssemblage()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getTypeAssemblage());
        
        // Test setter et getter
        String string = "typeAssemblage";
        ano.setTypeAssemblage(string);
        assertEquals(string, ano.getTypeAssemblage());       
    }
    
    @Test
    public void testGetVersion()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getVersion());
        
        // Test setter et getter
        String string = "version";
        ano.setVersion(string);
        assertEquals(string, ano.getVersion());       
    }
    
    @Test
    public void testGetProjetRTC()
    {
        // test valeur vide ou nulle
        assertEquals("", ano.getProjetRTC());
        
        // Test setter et getter
        String string = "projetRTC";
        ano.setProjetRTC(string);
        assertEquals(string, ano.getProjetRTC());       
    }
    
    @Test
    public void testGetEnvironnement()
    {
        // test valeur vide ou nulle
        assertEquals(EtatLot.INCONNU, ano.getEtatLot());
        
        // Test setter et getter
        EtatLot env = EtatLot.DEVTU;
        ano.setEtatLot(env);
        assertEquals(env, ano.getEtatLot());       
    }
    
    @Test
    public void testGetAction()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getAction());
        
        // Test setter et getter
        TypeAction action = TypeAction.CREER;
        ano.setAction(action);
        assertEquals(action, ano.getAction());       
    }
    
    @Test
    public void testGetDateCreation()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateCreation());
        
        // Test setter et getter
        ano.setDateCreation(today);
        assertEquals(today, ano.getDateCreation());       
    }
    
    @Test
    public void testGetDateDetection()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateDetection());
        
        // Test setter et getter
        ano.setDateDetection(today);
        assertEquals(today, ano.getDateDetection());       
    }
    
    @Test
    public void testGetDateRelance()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateRelance());
        
        // Test setter et getter
        ano.setDateRelance(today);
        assertEquals(today, ano.getDateRelance());       
    }
    
    /* TEST COMMENTAIRES */
    
    @Test
    public void testGetDirectionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDirectionComment());
        
        // Test setter et getter
        ano.setDirectionComment(comment);
        assertEquals(comment, ano.getDirectionComment());   
    }
    
    @Test
    public void testGetDepartementComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDepartementComment());
        
        // Test setter et getter
        ano.setDepartementComment(comment);
        assertEquals(comment, ano.getDepartementComment());   
    }
    
    @Test
    public void testGetServiceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getServiceComment());
        
        // Test setter et getter
        ano.setServiceComment(comment);
        assertEquals(comment, ano.getServiceComment());   
    }
    
    @Test
    public void testGetResponsableServiceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getResponsableServiceComment());
        
        // Test setter et getter
        ano.setResponsableServiceComment(comment);
        assertEquals(comment, ano.getResponsableServiceComment());   
    }
    
    @Test
    public void testGetProjetClarityComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getProjetClarityComment());
        
        // Test setter et getter
        ano.setProjetClarityComment(comment);
        assertEquals(comment, ano.getProjetClarityComment());   
    }
    
    @Test
    public void testGetLibelleProjetComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getLibelleProjetComment());
        
        // Test setter et getter
        ano.setLibelleProjetComment(comment);
        assertEquals(comment, ano.getLibelleProjetComment());   
    }
    
    @Test
    public void testGetCpiProjetComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getCpiProjetComment());
        
        // Test setter et getter
        ano.setCpiProjetComment(comment);
        assertEquals(comment, ano.getCpiProjetComment());   
    }
    
    @Test
    public void testGetEditionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getEditionComment());
        
        // Test setter et getter
        ano.setEditionComment(comment);
        assertEquals(comment, ano.getEditionComment());   
    }
    
    @Test
    public void testGetLotComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getLotComment());
        
        // Test setter et getter
        ano.setLotComment(comment);
        assertEquals(comment, ano.getLotComment());   
    }
    
    @Test
    public void testGetLiensLotComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getLiensLotComment());
        
        // Test setter et getter
        ano.setLiensLotComment(comment);
        assertEquals(comment, ano.getLiensLotComment());   
    }
    
    @Test
    public void testGetEnvironnementComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getEtatLotComment());
        
        // Test setter et getter
        ano.setEtatLotComment(comment);
        assertEquals(comment, ano.getEtatLotComment());   
    }
    
    @Test
    public void testGetNumeroAnomalieComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getNumeroAnomalieComment());
        
        // Test setter et getter
        ano.setNumeroAnomalieComment(comment);
        assertEquals(comment, ano.getNumeroAnomalieComment());   
    }
    
    @Test
    public void testGetLiensAnoComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getLiensAnoComment());
        
        // Test setter et getter
        ano.setLiensAnoComment(comment);
        assertEquals(comment, ano.getLiensAnoComment());   
    }
    
    @Test
    public void testGetEtatComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getEtatComment());
        
        // Test setter et getter
        ano.setEtatComment(comment);
        assertEquals(comment, ano.getEtatComment());   
    }
    
    @Test
    public void testGetTypeAssemblageComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getTypeAssemblageComment());
        
        // Test setter et getter
        ano.setTypeAssemblageComment(comment);
        assertEquals(comment, ano.getTypeAssemblageComment());   
    }
    
    @Test
    public void testGetSecuriteComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getSecuriteComment());
        
        // Test setter et getter
        ano.setSecuriteComment(comment);
        assertEquals(comment, ano.getSecuriteComment());   
    }
    
    @Test
    public void testGetRemarqueComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getRemarqueComment());
        
        // Test setter et getter
        ano.setRemarqueComment(comment);
        assertEquals(comment, ano.getRemarqueComment());   
    }
    
    @Test
    public void testGetVersionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getVersionComment());
        
        // Test setter et getter
        ano.setVersionComment(comment);
        assertEquals(comment, ano.getVersionComment());   
    }
    
    @Test
    public void testGetDateCreationComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateCreationComment());
        
        // Test setter et getter
        ano.setDateCreationComment(comment);
        assertEquals(comment, ano.getDateCreationComment());   
    }
    
    @Test
    public void testGetDateDetectionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateDetectionComment());
        
        // Test setter et getter
        ano.setDateDetectionComment(comment);
        assertEquals(comment, ano.getDateDetectionComment());   
    }
    
    @Test
    public void testGetDateRelanceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getDateRelanceComment());
        
        // Test setter et getter
        ano.setDateRelanceComment(comment);
        assertEquals(comment, ano.getDateRelanceComment());   
    }
    
    @Test
    public void testGetMatieresComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getMatieresComment());
        
        // Test setter et getter
        ano.setMatieresComment(comment);
        assertEquals(comment, ano.getMatieresComment());   
    }
    
    @Test
    public void testGetProjetRTCComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getProjetRTCComment());
        
        // Test setter et getter
        ano.setProjetRTCComment(comment);
        assertEquals(comment, ano.getProjetRTCComment());   
    }
    
    @Test
    public void testGetActionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, ano.getActionComment());
        
        // Test setter et getter
        ano.setActionComment(comment);
        assertEquals(comment, ano.getActionComment());   
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
