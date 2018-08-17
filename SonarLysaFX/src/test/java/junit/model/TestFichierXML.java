package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.TestXML;
import model.Application;
import model.ComposantSonar;
import model.FichiersXML;
import model.InfoClarity;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.RespService;
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
        assertNotNull(handler.getMapClarity());
        assertEquals(0, handler.getMapClarity().size());
        assertNotNull(handler.getMapApplis());
        assertEquals(0, handler.getMapApplis().size());
        assertNotNull(handler.getMapComposSonar());
        assertEquals(0, handler.getMapComposSonar().size());
        assertNotNull(handler.getMapEditions());
        assertEquals(0, handler.getMapEditions().size());
        assertNotNull(handler.getMapRespService());
        assertEquals(0, handler.getMapRespService().size());
        assertNotNull(handler.getMapLotsRTC());
        assertEquals(0, handler.getMapLotsRTC().size());
        assertNotNull(handler.getDateMaj());
        assertEquals(0, handler.getDateMaj().size());

        // Initialisation des maps et mise à jour du fichier
        initMaps();

        // Test mapClarity
        assertEquals(1, handler.getMapClarity().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.CLARITY));

        // Test mapApplis
        assertEquals(2, handler.getMapApplis().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.APPS));

        // Test mapRespService
        assertEquals(3, handler.getMapRespService().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.RESPSERVICE));

        // Test mapEditions
        assertEquals(4, handler.getMapEditions().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.EDITION));

        // Test mapLotsRTC
        assertEquals(5, handler.getMapLotsRTC().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.LOTSRTC));

        // Test mapClarity
        assertEquals(6, handler.getMapComposSonar().size());
        assertEquals(date, handler.getDateMaj().get(TypeFichier.SONAR));

        // Test map null
        assertEquals(handler, handler.majMapDonnees(TypeFichier.SONAR, null));
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

    @Test
    public void testGetListComposants()
    {
        // Test avec liste vide
        List<ComposantSonar> liste = handler.getListComposants();
        assertNotNull(liste);
        assertEquals(0, liste.size());

        // Après intialisation la liste doit contenir les objets de la map
        initMaps();
        liste = handler.getListComposants();
        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        assertEquals(6, liste.size());
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation des maps du fichier pour les tests
     */
    private void initMaps()
    {
        Map<String, InfoClarity> mapClarity = new HashMap<>();
        mapClarity.put("a", ModelFactory.getModel(InfoClarity.class));
        handler.majMapDonnees(TypeFichier.CLARITY, mapClarity);

        Map<String, Application> mapApplis = new HashMap<>();
        mapApplis.put("a", ModelFactory.getModel(Application.class));
        mapApplis.put("b", ModelFactory.getModel(Application.class));
        handler.majMapDonnees(TypeFichier.APPS, mapApplis);

        Map<String, RespService> mapRespService = new HashMap<>();
        mapRespService.put("a", ModelFactory.getModel(RespService.class));
        mapRespService.put("b", ModelFactory.getModel(RespService.class));
        mapRespService.put("c", ModelFactory.getModel(RespService.class));
        handler.majMapDonnees(TypeFichier.RESPSERVICE, mapRespService);

        Map<String, String> mapEditions = new HashMap<>();
        mapEditions.put("a", "edition");
        mapEditions.put("b", "edition");
        mapEditions.put("c", "edition");
        mapEditions.put("d", "edition");
        handler.majMapDonnees(TypeFichier.EDITION, mapEditions);

        Map<String, LotSuiviRTC> mapLotsRTC = new HashMap<>();
        mapLotsRTC.put("a", ModelFactory.getModel(LotSuiviRTC.class));
        mapLotsRTC.put("b", ModelFactory.getModel(LotSuiviRTC.class));
        mapLotsRTC.put("c", ModelFactory.getModel(LotSuiviRTC.class));
        mapLotsRTC.put("d", ModelFactory.getModel(LotSuiviRTC.class));
        mapLotsRTC.put("e", ModelFactory.getModel(LotSuiviRTC.class));
        handler.majMapDonnees(TypeFichier.LOTSRTC, mapLotsRTC);

        Map<String, ComposantSonar> mapComposSonar = new HashMap<>();
        mapComposSonar.put("a", ModelFactory.getModel(ComposantSonar.class));
        mapComposSonar.put("b", ModelFactory.getModel(ComposantSonar.class));
        mapComposSonar.put("c", ModelFactory.getModel(ComposantSonar.class));
        mapComposSonar.put("d", ModelFactory.getModel(ComposantSonar.class));
        mapComposSonar.put("e", ModelFactory.getModel(ComposantSonar.class));
        mapComposSonar.put("f", ModelFactory.getModel(ComposantSonar.class));
        handler.majMapDonnees(TypeFichier.SONAR, mapComposSonar);
    }

    /*---------- ACCESSEURS ----------*/
}
