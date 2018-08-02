package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.JunitBase;
import model.Application;
import model.ComposantSonar;
import model.FichiersXML;
import model.InfoClarity;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.RespService;
import model.enums.TypeFichier;
import utilities.Statics;

public class TestFichierXML extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private FichiersXML handler;

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void init() throws Exception
    {
        handler = ModelFactory.getModel(FichiersXML.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
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
        assertEquals(0 ,handler.getMapApplis().size());
        assertNotNull(handler.getMapComposSonar());
        assertEquals(0 ,handler.getMapComposSonar().size());
        assertNotNull(handler.getMapEditions());
        assertEquals(0 ,handler.getMapEditions().size());
        assertNotNull(handler.getMapRespService());
        assertEquals(0 ,handler.getMapRespService().size());
        assertNotNull(handler.getMapLotsRTC());
        assertEquals(0 ,handler.getMapLotsRTC().size());
        assertNotNull(handler.getDateMaj());
        assertEquals(0 ,handler.getDateMaj().size());
        
        // Initialisation des maps et mise à jour du fichier
        initMaps();

        // Test mapClarity
        assertEquals(1, handler.getMapClarity().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.CLARITY));

        // Test mapApplis
        assertEquals(2, handler.getMapApplis().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.APPS));

        // Test mapRespService
        assertEquals(3, handler.getMapRespService().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.RESPSERVICE));

        // Test mapEditions
        assertEquals(4, handler.getMapEditions().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.EDITION));

        // Test mapLotsRTC
        assertEquals(5, handler.getMapLotsRTC().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.LOTSRTC));

        // Test mapClarity
        assertEquals(6, handler.getMapComposSonar().size());
        assertEquals("02/08/2018", handler.getDateMaj().get(TypeFichier.SONAR));
    }

    @Test
    public void testControleDonnees()
    {
        // Contrôle avec tous les fichiers vides
        String controle = handler.controleDonnees();

        // Affichage des données de chaque map
        regexControle("Liste des applications", 1, controle);
        regexControle("Referentiel Clarity", 1, controle);
        regexControle("Responsables de services", 1, controle);
        regexControle("Editions Pic", 1, controle);
        regexControle("lots RTC", 1, controle);
        regexControle("Composants Sonar", 1, controle);

        // les 6 fichiers ne doivent pas être chargés
        regexControle(" non chargé(e).", 6, controle);

        // On doit afficher la demande de rechargement
        regexControle("Merci de recharger le(s) fichier(s) de paramétrage.", 1, controle);

        // Chargement des maps
        initMaps();

        // Nouveau test du contrôle
        controle = handler.controleDonnees();
        
        System.out.println(controle);

        // Test sur la maj de chaque map
        regexControle("Referentiel Clarity chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        regexControle("Liste des applications chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        regexControle("Responsables de services chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        regexControle("Editions Pic chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        regexControle("lots RTC chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        regexControle("Composants Sonar chargé(e)s. Dernière Maj : 02/08/2018", 1, controle);
        
        // On ne doit plus afficher la demande de rechargement
        regexControle("Merci de recharger le(s) fichier(s) de paramétrage.", 0, controle);

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
     * Test la présence d'une chaîne dans le retour du contrôle des map
     * 
     * @param regex
     *            Chaîne de caractère à tester
     * @param nbre
     *            Nombre d'occurences attendues pour la chaîne
     * @param retourControle
     *            String de retour de la méthode de contrôle
     */
    private void regexControle(String regex, int nbre, String retourControle)
    {
        Matcher matcher = Pattern.compile(regex.replace("(", "\\(").replace(")", "\\)").replace(".", "\\.")).matcher(retourControle);
        int i = 0;
        while (matcher.find())
        {
            i++;
        }
        assertEquals(nbre, i);
    }
    
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
