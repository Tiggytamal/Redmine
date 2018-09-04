package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;
import static utilities.Statics.NL;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import junit.TestXML;
import model.ProprietesXML;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypeColApps;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColEdition;
import model.enums.TypeColNPC;
import model.enums.TypeColPic;
import model.enums.TypeColR;
import model.enums.TypeColSuivi;
import model.enums.TypePlan;
import utilities.Statics;
import utilities.TechnicalException;

public class TestProprieteXML extends AbstractTestModel<ProprietesXML> implements TestXML
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testProprieteXML()
    {
        // Test initialisation des map à la construction       
        assertNotNull(handler.getMapParams());
        assertEquals(0, handler.getMapParams().size());
        assertNotNull(handler.getMapParamsBool());
        assertEquals(0, handler.getMapParamsBool().size());
        assertNotNull(handler.getMapPlans());
        assertEquals(0, handler.getMapPlans().size());     
    }
    
    
    @Test
    public void testControleDonnees()
    {
        // ----- 1. Pré-Test sans données
        
        // Mise à vide d'une colonne pour être bien sûr qu'elle remonte en non paramétrée
        handler.getEnumMapColR(TypeColSuivi.class).put(TypeColSuivi.ACTION, EMPTY);
        
        // Appel de la méthode
        String retour = handler.controleDonnees();
        
        // Test de chaque type dénumération - On teste la présence d'au moisn une fois chaque valeur, car il peut y avoir des doublons entre toutes les énumarations
        for (TypeColClarity type : TypeColClarity.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
        for (TypeColApps type : TypeColApps.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
        for (TypeColChefServ type : TypeColChefServ.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
        for (TypeColEdition type : TypeColEdition.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
        for (TypeColPic type : TypeColPic.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
        for (TypeColSuivi type : TypeColSuivi.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }
               
        for (Param param : Param.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 1, retour);
        }
        
        for (ParamSpec param : ParamSpec.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 1, retour);
        }
        
        for (ParamBool param : ParamBool.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 1, retour);
        }
        
        for (TypePlan typePlan : TypePlan.values())
        {
            regexControleAtLeast(typePlan.getValeur() + NL, 1, retour);
        }
        
        // Contrôle des phrases de début et de fin
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignées :", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignés :", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas paramétrés :", 1, retour);
                
        // ----- 2. Test Colonnes OK
        
        // Récupération des valeurs depuis le fichier de paramétrage
        handler.getEnumMapColR(TypeColPic.class).putAll(proprietesXML.getEnumMapColR(TypeColPic.class));
        handler.getEnumMapColR(TypeColClarity.class).putAll(proprietesXML.getEnumMapColR(TypeColClarity.class));
        handler.getEnumMapColR(TypeColApps.class).putAll(proprietesXML.getEnumMapColR(TypeColApps.class));
        handler.getEnumMapColR(TypeColChefServ.class).putAll(proprietesXML.getEnumMapColR(TypeColChefServ.class));
        handler.getEnumMapColR(TypeColEdition.class).putAll(proprietesXML.getEnumMapColR(TypeColEdition.class));
        handler.getEnumMapColR(TypeColSuivi.class).putAll(proprietesXML.getEnumMapColR(TypeColSuivi.class));
        handler.getEnumMapColR(TypeColNPC.class).putAll(proprietesXML.getEnumMapColR(TypeColNPC.class));
        
        // Appel du contrôle
        retour = handler.controleDonnees();
        
        // Vérfication qu'il n'y a plus de colonnes affichée comme non paramétrée
        for (TypeColClarity type : TypeColClarity.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColApps type : TypeColApps.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColChefServ type : TypeColChefServ.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColEdition type : TypeColEdition.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColPic type : TypeColPic.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColSuivi type : TypeColSuivi.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        for (TypeColNPC type : TypeColNPC.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        
        // On a toujours le message final mais on récupère le bon message pour les colonnes
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignées :", 0, retour);
        
        
        // ----- 2. Test Paramètres OK
        
        // Récupération des valeurs depuis le fichier de paramétrage
        handler.getMapParams().putAll(proprietesXML.getMapParams());
        handler.getMapParamsBool().putAll(proprietesXML.getMapParamsBool());
        handler.getMapParamsSpec().putAll(proprietesXML.getMapParamsSpec());        
        
        // Appel du contrôle
        retour = handler.controleDonnees();
        
        // Vérfication qu'il n'y a plus de paramètre affiché comme non paramétré
        for (Param param : Param.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 0, retour);
        }
        
        for (ParamSpec param : ParamSpec.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 0, retour);
        }
        
        for (ParamBool param : ParamBool.values())
        {
            regexControleAtLeast(param.getNom()+ NL, 0, retour);
        }
        
        // On a toujours le message final mais on récupère le bon message pour les colonnes et des paramètres
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignées :", 0, retour);
        regexControleEquals("Paramètres OK", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignés :", 0, retour);
        
        
        // ----- 3. Test Planaificateurs OK
        
        // Récupération des valeurs depuis le fichier de paramétrage
        handler.getMapPlans().putAll(proprietesXML.getMapPlans());
        
        // Appel du contrôle
        retour = handler.controleDonnees();
        
        // Vérfication qu'il n'y a plus de planificateur affiché comme non paramétré
        for (TypePlan typePlan : TypePlan.values())
        {
            regexControleAtLeast(typePlan.getValeur() + NL, 0, retour);
        }
        
        // On a maintenant tous les messages OK
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.", 0, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignées :", 0, retour);
        regexControleEquals("Paramètres OK", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignés :", 0, retour);
        regexControleEquals("Planificateurs OK", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas paramétrés :", 0, retour);
        
        // ----- 4. Tests avec valeurs vide dans le paramétrage
        
        // Mise à vide de ceratiens données
        handler.getMapParams().put(Param.ABSOLUTEPATH, EMPTY);
        handler.getMapParamsSpec().put(ParamSpec.MEMBRESJAVA, EMPTY);
        
        // Appel du contrôle
        retour = handler.controleDonnees();
        
        // Vérification de la remontée des erreurs
        regexControleEquals(Param.ABSOLUTEPATH.getNom(), 1, retour);
        regexControleEquals(ParamSpec.MEMBRESJAVA.getNom(), 1, retour);        
        regexControleEquals("Certains paramètres sont mal renseignés :", 1, retour);
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de paramétrage.", 1, retour);
        
        
    }
    
    @Test
    public void testGetEnumMap()
    {
        Map<TypeColSuivi, String> mapColsSuivi = handler.getEnumMapColR(TypeColSuivi.class); 
        assertNotNull(mapColsSuivi);
        assertEquals(0, mapColsSuivi.size());
        Map<TypeColClarity, String> mapColsClarity = handler.getEnumMapColR(TypeColClarity.class);
        assertNotNull(mapColsClarity);
        assertEquals(0, mapColsClarity.size());
        Map<TypeColApps, String> mapColsApps = handler.getEnumMapColR(TypeColApps.class);
        assertNotNull(mapColsApps);
        assertEquals(0, mapColsApps.size());
        Map<TypeColChefServ, String> mapColsChefServ = handler.getEnumMapColR(TypeColChefServ.class);
        assertNotNull(mapColsChefServ);
        assertEquals(0, mapColsChefServ.size());
        Map<TypeColEdition, String> mapColsEdition = handler.getEnumMapColR(TypeColEdition.class);
        assertNotNull(mapColsEdition);
        assertEquals(0, mapColsEdition.size());
        Map<TypeColPic, String> mapColsPic = handler.getEnumMapColR(TypeColPic.class);
        assertNotNull(mapColsPic);
        assertEquals(0, mapColsPic.size());
    }
    
    @Test (expected = TechnicalException.class)
    public void testGetEnumMapException()
    {
        handler.getEnumMapColR(TypeColTest.class);
    }
    
    @Test
    public void testGetMapColsInvert()
    {
        // Initialisation de la map ColSuivi
        Map<TypeColSuivi, String> mapColsSuivi = new HashMap<>();
        mapColsSuivi.put(TypeColSuivi.ANOMALIE, "key");
        handler.getEnumMapColR(TypeColSuivi.class).putAll(mapColsSuivi);
        
        // Appel méthode
        Map<String, TypeColSuivi> retour = handler.getMapColsInvert(TypeColSuivi.class);
        
        // Contrôle des données
        assertNotNull(retour);
        assertEquals(1, retour.size());
        assertTrue(retour.containsKey("key"));
        assertTrue(retour.containsValue(TypeColSuivi.ANOMALIE));        
    }
    
    @Test
    public void testGetterPrive() throws Exception
    {
        testMapEnum("getMapColsSuivi");
        testMapEnum("getMapColsClarity");
        testMapEnum("getMapColsChefServ");
        testMapEnum("getMapColsPic");
        testMapEnum("getMapColsEdition");
        testMapEnum("getMapColsApps"); 
    }
    
    @Test
    @Override
    public void testGetFile()
    {
        // Test si le fichier n'est pas nul et bien initialisé.
        File file = handler.getFile();
        assertNotNull(file);
        assertTrue(file.isFile());
        assertTrue(file.getPath().contains(Statics.JARPATH));
        assertEquals("proprietes.xml", file.getName());
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
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * permet d'appeler les getters privés des EnumMap
     * 
     * @param methode
     * @throws Exception
     */
    private <T> void testMapEnum(String methode) throws Exception
    {
        Map<T, String> map = Whitebox.invokeMethod(handler, methode);
        assertNotNull(map);
        assertEquals(0, map.size());
    }
    
    /*---------- ACCESSEURS ----------*/
    
    /*---------- CLASSES PRIVEES ----------*/
    
    /**
     * Enumération privée pour créer l'exception
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private enum TypeColTest implements Serializable, TypeColR
    {
        VIDE;

        @Override
        public String getValeur()
        {
            return null;
        }

        @Override
        public String getNomCol()
        {
            return null;
        }        
    }
}
