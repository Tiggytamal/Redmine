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
import model.Colonne;
import model.ProprietesXML;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypeColApps;
import model.enums.TypeColAppsW;
import model.enums.TypeColChefServ;
import model.enums.TypeColClarity;
import model.enums.TypeColCompo;
import model.enums.TypeColEdition;
import model.enums.TypeColPbApps;
import model.enums.TypeColPic;
import model.enums.TypeColProduit;
import model.enums.TypeColR;
import model.enums.TypeColSuivi;
import model.enums.TypeColSuiviApps;
import model.enums.TypeColUA;
import model.enums.TypeColVul;
import model.enums.TypeColW;
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
        // Test initialisation des map � la construction
        assertNotNull(objetTest.getMapParams());
        assertEquals(0, objetTest.getMapParams().size());
        assertNotNull(objetTest.getMapParamsBool());
        assertEquals(0, objetTest.getMapParamsBool().size());
        assertNotNull(objetTest.getMapPlans());
        assertEquals(0, objetTest.getMapPlans().size());
    }

    @Test
    public void testControleDonnees()
    {
        // ----- 1. Pr�-Test sans donn�es

        // Mise � vide d'une colonne pour �tre bien s�r qu'elle remonte en non param�tr�e
        objetTest.getEnumMapColR(TypeColSuivi.class).put(TypeColSuivi.ACTION, EMPTY);

        // Appel de la m�thode
        String retour = objetTest.controleDonnees();

        // Test de chaque type d'�num�ration - On teste la pr�sence d'au moins une fois chaque valeur, car il peut y avoir des doublons entre toutes les �numarations
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
            regexControleAtLeast(param.getNom() + NL, 1, retour);
        }

        for (ParamSpec param : ParamSpec.values())
        {
            regexControleAtLeast(param.getNom() + NL, 1, retour);
        }

        for (ParamBool param : ParamBool.values())
        {
            regexControleAtLeast(param.getNom() + NL, 1, retour);
        }

        for (TypePlan typePlan : TypePlan.values())
        {
            regexControleAtLeast(typePlan.getValeur() + NL, 1, retour);
        }

        // Contr�le des phrases de d�but et de fin
        regexControleEquals("Merci de changer les param�tres en option ou de recharger les fichiers de param�trage.", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseign�es :", 1, retour);
        regexControleEquals("Certains param�tres sont mal renseign�s :", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas param�tr�s :", 1, retour);

        // ----- 2. Test Colonnes OK

        // R�cup�ration des valeurs depuis le fichier de param�trage
        objetTest.getEnumMapColR(TypeColPic.class).putAll(proprietesXML.getEnumMapColR(TypeColPic.class));
        objetTest.getEnumMapColR(TypeColClarity.class).putAll(proprietesXML.getEnumMapColR(TypeColClarity.class));
        objetTest.getEnumMapColR(TypeColApps.class).putAll(proprietesXML.getEnumMapColR(TypeColApps.class));
        objetTest.getEnumMapColR(TypeColChefServ.class).putAll(proprietesXML.getEnumMapColR(TypeColChefServ.class));
        objetTest.getEnumMapColR(TypeColEdition.class).putAll(proprietesXML.getEnumMapColR(TypeColEdition.class));
        objetTest.getEnumMapColR(TypeColSuivi.class).putAll(proprietesXML.getEnumMapColR(TypeColSuivi.class));
        objetTest.getEnumMapColR(TypeColProduit.class).putAll(proprietesXML.getEnumMapColR(TypeColProduit.class));
        objetTest.getEnumMapColR(TypeColUA.class).putAll(proprietesXML.getEnumMapColR(TypeColUA.class));
        objetTest.getEnumMapColR(TypeColSuiviApps.class).putAll(proprietesXML.getEnumMapColR(TypeColSuiviApps.class));
        
        objetTest.getEnumMapColW(TypeColVul.class).putAll(proprietesXML.getEnumMapColW(TypeColVul.class));
        objetTest.getEnumMapColW(TypeColPbApps.class).putAll(proprietesXML.getEnumMapColW(TypeColPbApps.class));
        objetTest.getEnumMapColW(TypeColAppsW.class).putAll(proprietesXML.getEnumMapColW(TypeColAppsW.class));
        objetTest.getEnumMapColW(TypeColCompo.class).putAll(proprietesXML.getEnumMapColW(TypeColCompo.class));

        // Appel du contr�le
        retour = objetTest.controleDonnees();

        // V�rfication qu'il n'y a plus de colonnes affich�e comme non param�tr�e
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
        
        for (TypeColProduit type : TypeColProduit.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        
        for (TypeColUA type : TypeColUA.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }
        
        for (TypeColSuiviApps type : TypeColSuiviApps.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 0, retour);
        }

        // On a toujours le message final mais on r�cup�re le bon message pour les colonnes
        regexControleEquals("Merci de changer les param�tres en option ou de recharger les fichiers de param�trage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseign�es :", 0, retour);

        // ----- 2. Test Param�tres OK

        // R�cup�ration des valeurs depuis le fichier de param�trage
        objetTest.getMapParams().putAll(proprietesXML.getMapParams());
        objetTest.getMapParamsBool().putAll(proprietesXML.getMapParamsBool());
        objetTest.getMapParamsSpec().putAll(proprietesXML.getMapParamsSpec());

        // Appel du contr�le
        retour = objetTest.controleDonnees();

        // V�rfication qu'il n'y a plus de param�tre affich� comme non param�tr�
        for (Param param : Param.values())
        {
            regexControleAtLeast(param.getNom() + NL, 0, retour);
        }

        for (ParamSpec param : ParamSpec.values())
        {
            regexControleAtLeast(param.getNom() + NL, 0, retour);
        }

        for (ParamBool param : ParamBool.values())
        {
            regexControleAtLeast(param.getNom() + NL, 0, retour);
        }

        // On a toujours le message final mais on r�cup�re le bon message pour les colonnes et des param�tres
        regexControleEquals("Merci de changer les param�tres en option ou de recharger les fichiers de param�trage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseign�es :", 0, retour);
        regexControleEquals("Param�tres OK", 1, retour);
        regexControleEquals("Certains param�tres sont mal renseign�s :", 0, retour);

        // ----- 3. Test Planificateurs OK

        // R�cup�ration des valeurs depuis le fichier de param�trage
        objetTest.getMapPlans().putAll(proprietesXML.getMapPlans());

        // Appel du contr�le
        retour = objetTest.controleDonnees();

        // V�rfication qu'il n'y a plus de planificateur affich� comme non param�tr�
        for (TypePlan typePlan : TypePlan.values())
        {
            regexControleAtLeast(typePlan.getValeur() + NL, 0, retour);
        }

        // On a maintenant tous les messages OK
        regexControleEquals("Merci de changer les param�tres en option ou de recharger les fichiers de param�trage.", 0, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseign�es :", 0, retour);
        regexControleEquals("Param�tres OK", 1, retour);
        regexControleEquals("Certains param�tres sont mal renseign�s :", 0, retour);
        regexControleEquals("Planificateurs OK", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas param�tr�s :", 0, retour);

        // ----- 4. Tests avec valeurs vide dans le param�trage

        // Mise � vide de ceratiens donn�es
        objetTest.getMapParams().put(Param.ABSOLUTEPATH, EMPTY);
        objetTest.getMapParamsSpec().put(ParamSpec.MEMBRESJAVA, EMPTY);

        // Appel du contr�le
        retour = objetTest.controleDonnees();

        // V�rification de la remont�e des erreurs
        regexControleEquals(Param.ABSOLUTEPATH.getNom(), 1, retour);
        regexControleEquals(ParamSpec.MEMBRESJAVA.getNom(), 1, retour);
        regexControleEquals("Certains param�tres sont mal renseign�s :", 1, retour);
        regexControleEquals("Merci de changer les param�tres en option ou de recharger les fichiers de param�trage.", 1, retour);

    }

    @Test
    public void testGetEnumMapColR()
    {
        // Test de chaque type de colonne pour v�rifier que l'on r�cup�re bien un map non nulle.
        Map<TypeColSuivi, String> mapColsSuivi = objetTest.getEnumMapColR(TypeColSuivi.class);
        assertNotNull(mapColsSuivi);
        assertEquals(0, mapColsSuivi.size());
        Map<TypeColClarity, String> mapColsClarity = objetTest.getEnumMapColR(TypeColClarity.class);
        assertNotNull(mapColsClarity);
        assertEquals(0, mapColsClarity.size());
        Map<TypeColApps, String> mapColsApps = objetTest.getEnumMapColR(TypeColApps.class);
        assertNotNull(mapColsApps);
        assertEquals(0, mapColsApps.size());
        Map<TypeColChefServ, String> mapColsChefServ = objetTest.getEnumMapColR(TypeColChefServ.class);
        assertNotNull(mapColsChefServ);
        assertEquals(0, mapColsChefServ.size());
        Map<TypeColEdition, String> mapColsEdition = objetTest.getEnumMapColR(TypeColEdition.class);
        assertNotNull(mapColsEdition);
        assertEquals(0, mapColsEdition.size());
        Map<TypeColProduit, String> mapColsGrProjet = objetTest.getEnumMapColR(TypeColProduit.class);
        assertNotNull(mapColsGrProjet);
        assertEquals(0, mapColsGrProjet.size());
        Map<TypeColPic, String> mapColsPic = objetTest.getEnumMapColR(TypeColPic.class);
        assertNotNull(mapColsPic);
        assertEquals(0, mapColsPic.size());
        Map<TypeColUA, String> mapColsUA = objetTest.getEnumMapColR(TypeColUA.class);
        assertNotNull(mapColsUA);
        assertEquals(0, mapColsUA.size());
        Map<TypeColSuiviApps, String> mapColsSuiviApps = objetTest.getEnumMapColR(TypeColSuiviApps.class);
        assertNotNull(mapColsSuiviApps);
        assertEquals(0, mapColsSuiviApps.size());

    }

    @Test
    public void testGetEnumMapColW()
    {
        Map<TypeColVul, Colonne> mapColVul = objetTest.getEnumMapColW(TypeColVul.class);
        assertNotNull(mapColVul);
        assertEquals(0, mapColVul.size());
        Map<TypeColPbApps, Colonne> mapColPbApps = objetTest.getEnumMapColW(TypeColPbApps.class);
        assertNotNull(mapColPbApps);
        assertEquals(0, mapColPbApps.size());
        Map<TypeColAppsW, Colonne> mapColsApps = objetTest.getEnumMapColW(TypeColAppsW.class);
        assertNotNull(mapColsApps);
        assertEquals(0, mapColsApps.size());
        Map<TypeColCompo, Colonne> mapColsChefServ = objetTest.getEnumMapColW(TypeColCompo.class);
        assertNotNull(mapColsChefServ);
        assertEquals(0, mapColsChefServ.size());
    }

    @Test(expected = TechnicalException.class)
    public void testGetEnumMapRException()
    {
        objetTest.getEnumMapColR(TypeColTest.class);
    }
    
    @Test(expected = TechnicalException.class)
    public void testGetEnumMapWException()
    {
        objetTest.getEnumMapColW(TypeColTest.class);
    }

    @Test
    public void testGetMapColsInvert()
    {
        // Initialisation de la map ColSuivi
        Map<TypeColSuivi, String> mapColsSuivi = new HashMap<>();
        mapColsSuivi.put(TypeColSuivi.ANOMALIE, "key");
        objetTest.getEnumMapColR(TypeColSuivi.class).putAll(mapColsSuivi);

        // Appel m�thode
        Map<String, TypeColSuivi> retour = objetTest.getMapColsInvert(TypeColSuivi.class);

        // Contr�le des donn�es
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
        // Test si le fichier n'est pas nul et bien initialis�.
        File file = objetTest.getFile();
        assertNotNull(file);
        assertTrue(file.isFile());
        assertTrue(file.getPath().contains(Statics.JARPATH));
        assertEquals("proprietes.xml", file.getName());
    }

    @Test
    @Override
    public void testGetResource()
    {
        // Test si le fichier n'est pas nul et bien initialis�.
        File file = objetTest.getResource();
        assertNotNull(file);
        assertTrue(file.isFile());
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * permet d'appeler les getters priv�s des EnumMap
     * 
     * @param methode
     * @throws Exception
     */
    private <T> void testMapEnum(String methode) throws Exception
    {
        Map<T, String> map = Whitebox.invokeMethod(objetTest, methode);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    /*---------- ACCESSEURS ----------*/

    /*---------- CLASSES PRIVEES ----------*/

    /**
     * Enum�ration priv�e pour cr�er l'exception
     * 
     * @author ETP8137 - Gr�goire Mathon
     * @since 1.0
     */
    private enum TypeColTest implements Serializable, TypeColR, TypeColW 
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
