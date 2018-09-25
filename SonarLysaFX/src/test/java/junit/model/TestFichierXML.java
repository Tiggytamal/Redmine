package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import junit.TestXML;
import model.FichiersXML;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ProjetClarity;
import model.bdd.LotRTC;
import model.enums.TypeFichier;
import utilities.Statics;

public class TestFichierXML extends AbstractTestModel<FichiersXML> implements TestXML
{
    /*---------- ATTRIBUTS ----------*/

    private String date;

    /*---------- CONSTRUCTEURS ----------*/

    public TestFichierXML()
    {
        date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testGetFile()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getFile();
        assertNotNull(file);
        assertTrue(file.isFile());
        assertTrue(file.getPath().contains(Statics.JARPATH));
        assertEquals("fichiers.xml", file.getName());
    }

    @Test
    @Override
    public void testGetResource()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getResource();
        assertNotNull(file);
        assertTrue(file.isFile());
    }

    @Test
    public void testMajMapDonnees()
    {
        // PreTest - toutes les maps sont vides et les dates de maj aussi
        assertNotNull(handler.getMapLotsRTC());
        assertEquals(0, handler.getMapLotsRTC().size());
        assertNotNull(handler.getDateMaj());
        assertEquals(0, handler.getDateMaj().size());
        assertNotNull(handler.getMapProjetsNpc());
        assertEquals(0, handler.getMapProjetsNpc().size());

        // Initialisation des maps et mise à jour du fichier
        initMaps();

        // Test mapLotsRTC
        assertEquals(5, handler.getMapLotsRTC().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.LOTSRTC));
        
        // Test mapProjetsNpc
        assertEquals(7, handler.getMapProjetsNpc().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.NPC));
    }

    @Test
    public void testControleDonnees()
    {
        // Contrôle avec tous les fichiers vides
        String controle = handler.controleDonnees();

        // Affichage des données de chaque map
        regexControleEquals("Liste des applications", 1, controle);
        regexControleEquals("Referentiel Clarity", 1, controle);
        regexControleEquals("Responsables de services", 1, controle);
        regexControleEquals("Editions Pic", 1, controle);
        regexControleEquals("lots RTC", 1, controle);
        regexControleEquals("Composants Sonar", 1, controle);

        // les 6 fichiers ne doivent pas être chargés
        regexControleEquals(" non chargé(e).", 6, controle);

        // On doit afficher la demande de rechargement
        regexControleEquals("Merci de recharger le(s) fichier(s) de paramétrage.", 1, controle);

        // Chargement des maps
        initMaps();

        // Nouveau test du contrôle
        controle = handler.controleDonnees();

        // Test sur la maj de chaque map
        regexControleEquals("Referentiel Clarity chargé(e)s. Dernière Maj : " + date, 1, controle);
        regexControleEquals("Liste des applications chargé(e)s. Dernière Maj : " + date, 1, controle);
        regexControleEquals("Responsables de services chargé(e)s. Dernière Maj : " + date, 1, controle);
        regexControleEquals("Editions Pic chargé(e)s. Dernière Maj : " + date, 1, controle);
        regexControleEquals("lots RTC chargé(e)s. Dernière Maj : " + date, 1, controle);
        regexControleEquals("Composants Sonar chargé(e)s. Dernière Maj : " + date, 1, controle);

        // On ne doit plus afficher la demande de rechargement
        regexControleEquals("Merci de recharger le(s) fichier(s) de paramétrage.", 0, controle);

    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation des maps du fichier pour les tests
     */
    private void initMaps()
    {
        Map<String, ProjetClarity> mapClarity = new HashMap<>();
        mapClarity.put("a", ModelFactory.getModel(ProjetClarity.class));
        handler.majMapDonnees(TypeFichier.CLARITY, mapClarity);

        Map<String, Application> mapApplis = new HashMap<>();
        mapApplis.put("a", ModelFactory.getModel(Application.class));
        mapApplis.put("b", ModelFactory.getModel(Application.class));
        handler.majMapDonnees(TypeFichier.APPS, mapApplis);

        Map<String, ChefService> mapRespService = new HashMap<>();
        mapRespService.put("a", ModelFactory.getModel(ChefService.class));
        mapRespService.put("b", ModelFactory.getModel(ChefService.class));
        mapRespService.put("c", ModelFactory.getModel(ChefService.class));
        handler.majMapDonnees(TypeFichier.RESPSERVICE, mapRespService);

        Map<String, String> mapEditions = new HashMap<>();
        mapEditions.put("a", "edition");
        mapEditions.put("b", "edition");
        mapEditions.put("c", "edition");
        mapEditions.put("d", "edition");
        handler.majMapDonnees(TypeFichier.EDITION, mapEditions);

        Map<String, LotRTC> mapLotsRTC = new HashMap<>();
        mapLotsRTC.put("a", ModelFactory.getModel(LotRTC.class));
        mapLotsRTC.put("b", ModelFactory.getModel(LotRTC.class));
        mapLotsRTC.put("c", ModelFactory.getModel(LotRTC.class));
        mapLotsRTC.put("d", ModelFactory.getModel(LotRTC.class));
        mapLotsRTC.put("e", ModelFactory.getModel(LotRTC.class));
        handler.majMapDonnees(TypeFichier.LOTSRTC, mapLotsRTC);
        
        Map<String, String> mapProjetsNpc = new HashMap<>();
        mapProjetsNpc.put("a", "a");
        mapProjetsNpc.put("b", "b");
        mapProjetsNpc.put("c", "c");
        mapProjetsNpc.put("d", "d");
        mapProjetsNpc.put("e", "e");
        mapProjetsNpc.put("f", "f");
        mapProjetsNpc.put("g", "g");
        handler.majMapDonnees(TypeFichier.NPC, mapProjetsNpc);
    }

    /*---------- ACCESSEURS ----------*/
}
