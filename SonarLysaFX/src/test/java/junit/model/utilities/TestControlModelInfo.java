package junit.model.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.reflect.Whitebox.invokeMethod;
import static utilities.Statics.EMPTY;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import control.word.ControlRapport;
import junit.JunitBase;
import model.Anomalie;
import model.CompoPbApps;
import model.ModelFactory;
import model.enums.TypeRapport;
import model.utilities.ControlModelInfo;
import utilities.Statics;

public class TestControlModelInfo extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private ControlModelInfo handler;
    private ControlRapport controlRapport;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new ControlModelInfo(); 
        controlRapport = new ControlRapport(TypeRapport.SUIVIJAVA);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testControleClarity() throws Exception
    {
        // Intialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        ano.setProjetClarity("a");

        // Test 1 - aucune correspondance
        handler.controleClarity(ano, controlRapport);
        assertEquals(Statics.INCONNU, ano.getDepartement());
        assertEquals(Statics.INCONNU, ano.getService());
        assertEquals(Statics.INCONNUE, ano.getDirection());

        // Test 2 - correspondance parfaite - données tirées du fichier excel
        ano.setProjetClarity("BDGREF047");
        handler.controleClarity(ano, controlRapport);
        assertEquals("Controle de gestion et pilotage", ano.getDepartement());
        assertEquals("Controle de gestion des filieres", ano.getService());
        assertEquals("FINANCE ACHATS LOGISTIQUE", ano.getDirection());

        // test 3 - correspondance trouvé avec algo de recherche du projet T le plus récent
        // On doit trouver les informations du projet T3004730E
        ano.setProjetClarity("T3004730");
        handler.controleClarity(ano, controlRapport);
        assertEquals("Distribution Ouest et Marches specialises", ano.getDepartement());
        assertEquals("Distribution Ouest", ano.getService());
        assertEquals("DOMAINE DISTRIBUTION ET OUTILS SOCLES", ano.getDirection());

        // test 4 - Correspondance avec les deux derniers caratères manquants
        ano.setProjetClarity("P00839");
        handler.controleClarity(ano, controlRapport);
        assertEquals("Risques Financier RH et CIS", ano.getDepartement());
        assertEquals("Financier", ano.getService());
        assertEquals("DOMAINES REGALIENS", ano.getDirection());
        
        // Test 5 - projet T mais trop long
        ano.setProjetClarity("T300473000");
        handler.controleClarity(ano, controlRapport);
        assertEquals(Statics.INCONNU, ano.getDepartement());
        assertEquals(Statics.INCONNU, ano.getService());
        assertEquals(Statics.INCONNUE, ano.getDirection());       
    }
    
    @Test
    public void testControleChefDeService() throws Exception
    {
        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);

        // Test 1 nulle
        handler.controleChefDeService(ano, controlRapport);
        assertEquals(EMPTY, ano.getSecurite());

        // Test 2 empty
        ano.setService(EMPTY);
        assertEquals(EMPTY, ano.getSecurite());

        // Test 3 ok
        ano.setService("Projets Credits");
        handler.controleChefDeService(ano, controlRapport);
        assertEquals("METROP-TAINTURIER, NATHALIE", ano.getResponsableService());

        // Test 4 loggin
        ano.setService("abc");
        ano.setResponsableService("abc");
        handler.controleChefDeService(ano, controlRapport);
        assertEquals("abc", ano.getResponsableService());
    }
    
    @Test
    public void testControleNPC()
    {
        // Initialisation
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        
        // Test NPC
        ano.setProjetRTC(Statics.fichiersXML.getMapProjetsNpc().values().iterator().next());
        handler.controleNPC(ano);
        assertEquals(Statics.X, ano.getNpc());
        
        // Test non NPC
        ano.setProjetRTC("pas NPC");
        handler.controleNPC(ano);
        assertEquals(Statics.EMPTY, ano.getNpc());
    }
    
    @Test
    public void testInitChefService() throws Exception
    {
        // Initialisation
        CompoPbApps compo = ModelFactory.getModel(CompoPbApps.class);
        
        String methode = "initChefService";
        String service = "ASSURANCE QUALITE";
        String serviceNonExistant = "serv";
        
        // Test avec bon service
        Whitebox.invokeMethod(handler, methode, compo, service);
        assertEquals(Statics.fichiersXML.getMapRespService().get(service).getNom(), compo.getChefService());
        
        // Test avec service non existant
        Whitebox.invokeMethod(handler, methode, compo, serviceNonExistant);
        assertEquals(Statics.INCONNU, compo.getChefService());
    }
    
    @Test
    public void testControleKey() throws Exception
    {
        String methode = "controleKey";
        assertTrue(invokeMethod(handler, methode, "a", "a"));
        assertTrue(invokeMethod(handler, methode, "A", "a"));
        assertFalse(invokeMethod(handler, methode, "A", "b"));
        assertTrue(invokeMethod(handler, methode, "BEF000", "BEF0009"));
        assertTrue(invokeMethod(handler, methode, "T7004360", "T7004360E"));
        assertTrue(invokeMethod(handler, methode, "BF046502", "BF046500"));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
