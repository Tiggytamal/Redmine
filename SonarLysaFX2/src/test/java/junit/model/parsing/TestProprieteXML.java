package junit.model.parsing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utilities.Statics.NL;
import static utilities.Statics.proprietesXML;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import junit.TestXML;
import junit.model.TestAbstractModele;
import model.Colonne;
import model.enums.ColAppliDir;
import model.enums.ColChefServ;
import model.enums.ColClarity;
import model.enums.ColCompo;
import model.enums.ColEdition;
import model.enums.ColPic;
import model.enums.ColR;
import model.enums.ColVul;
import model.enums.ColW;
import model.enums.Param;
import model.enums.ParamBool;
import model.enums.ParamSpec;
import model.enums.TypePlan;
import model.parsing.ProprietesXML;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestProprieteXML extends TestAbstractModele<ProprietesXML> implements TestXML
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testinitialisation(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation des map à la construction
        assertThat(objetTest.getMapParams()).isNotNull();
        assertThat(objetTest.getMapParams()).hasSize(0);
        assertThat(objetTest.getMapParamsBool()).isNotNull();
        assertThat(objetTest.getMapParamsBool()).hasSize(0);
        assertThat(objetTest.getMapPlans()).isNotNull();
        assertThat(objetTest.getMapPlans()).hasSize(0);
    }

    @Test
    public void testControleDonnees(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // ----- 1. Pre-Test sans donnees

        // Mise à vide d'une colonne pour être bien sûr qu'elle remonte en non parametrée
        objetTest.getEnumMapColR(ColEdition.class).put(ColEdition.COMMENTAIRE, EMPTY);

        // Appel de la methode
        String retour = objetTest.controleDonnees();

        // Test de chaque type d'énumération - On teste la presence d'au moins une fois chaque valeur, car il peut y avoir des doublons entre toutes les
        // enumarations
        for (ColClarity type : ColClarity.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }

        for (ColChefServ type : ColChefServ.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }

        for (ColEdition type : ColEdition.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }

        for (ColAppliDir type : ColAppliDir.values())
        {
            regexControleAtLeast(type.getValeur() + NL, 1, retour);
        }

        for (Param param : Param.values())
        {
            if (!param.isPerso())
                regexControleAtLeast(param.getNom() + NL, 1, retour);
        }

        for (ParamSpec param : ParamSpec.values())
        {
            if (!param.isPerso())
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

        // Contrôle des phrases de debut et de fin
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignees :", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignes :", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas parametres :", 1, retour);

        // ----- 2. Test Colonnes OK

        // Récupération des valeurs depuis le fichier de parametrage
        objetTest.getEnumMapColR(ColClarity.class).putAll(proprietesXML.getEnumMapColR(ColClarity.class));
        objetTest.getEnumMapColR(ColChefServ.class).putAll(proprietesXML.getEnumMapColR(ColChefServ.class));
        objetTest.getEnumMapColR(ColEdition.class).putAll(proprietesXML.getEnumMapColR(ColEdition.class));
        objetTest.getEnumMapColR(ColAppliDir.class).putAll(proprietesXML.getEnumMapColR(ColAppliDir.class));
        objetTest.getEnumMapColR(ColPic.class).putAll(proprietesXML.getEnumMapColR(ColPic.class));

        objetTest.getEnumMapColW(ColVul.class).putAll(proprietesXML.getEnumMapColW(ColVul.class));
        objetTest.getEnumMapColW(ColCompo.class).putAll(proprietesXML.getEnumMapColW(ColCompo.class));

        // Appel du contrôle
        retour = objetTest.controleDonnees();

        // Verfication qu'il n'y a plus de colonnes affichée comme non parametrée
        for (ColClarity type : ColClarity.values())
        {
            regexControleEquals(type.getValeur() + NL, 0, retour);
        }

        for (ColChefServ type : ColChefServ.values())
        {
            regexControleEquals(type.getValeur() + NL, 0, retour);
        }

        for (ColEdition type : ColEdition.values())
        {
            regexControleEquals(type.getValeur() + NL, 0, retour);
        }

        for (ColAppliDir type : ColAppliDir.values())
        {
            regexControleEquals(type.getValeur() + NL, 0, retour);
        }

        // On a toujours le message final mais on récupère le bon message pour les colonnes
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignees :", 0, retour);

        // ----- 2. Test Paramètres OK

        // Récupération des valeurs depuis le fichier de parametrage
        objetTest.getMapParams().putAll(proprietesXML.getMapParams());
        objetTest.getMapParamsBool().putAll(proprietesXML.getMapParamsBool());
        objetTest.getMapParamsSpec().putAll(proprietesXML.getMapParamsSpec());

        // Appel du contrôle
        retour = objetTest.controleDonnees();

        // Verfication qu'il n'y a plus de paramètre affiché comme non parametré
        for (Param param : Param.values())
        {
            regexControleEquals(param.getNom() + NL, 0, retour);
        }

        for (ParamSpec param : ParamSpec.values())
        {
            if (!param.isPerso())
                regexControleEquals(param.getNom() + NL, 0, retour);
        }

        for (ParamBool param : ParamBool.values())
        {
            regexControleEquals(param.getNom() + NL, 0, retour);
        }

        // On a toujours le message final mais on récupère le bon message pour les colonnes et des paramètres
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.", 1, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignees :", 0, retour);
        regexControleEquals("Paramètres OK", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignes :", 0, retour);

        // ----- 3. Test Planificateurs OK

        // Récupération des valeurs depuis le fichier de parametrage
        objetTest.getMapPlans().putAll(proprietesXML.getMapPlans());

        // Appel du contrôle
        retour = objetTest.controleDonnees();

        // Verfication qu'il n'y a plus de planificateur affiche comme non parametre
        for (TypePlan typePlan : TypePlan.values())
        {
            regexControleEquals(typePlan.getValeur() + NL, 0, retour);
        }

        // On a maintenant tous les messages OK
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.", 0, retour);
        regexControleEquals("Nom colonnes OK", 1, retour);
        regexControleEquals("Certaines colonnes sont mal renseignees :", 0, retour);
        regexControleEquals("Paramètres OK", 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignes :", 0, retour);
        regexControleEquals("Planificateurs OK", 1, retour);
        regexControleEquals("Certains planificateurs ne sont pas parametres :", 0, retour);

        // ----- 4. Tests avec valeurs vide dans le parametrage

        // Mise à vide de ceratiens donnees
        objetTest.getMapParamsSpec().put(ParamSpec.MEMBRESAQP, EMPTY);

        // Appel du contrôle
        retour = objetTest.controleDonnees();

        // Verification de la remontée des erreurs
        regexControleEquals(ParamSpec.MEMBRESAQP.getNom(), 1, retour);
        regexControleEquals("Certains paramètres sont mal renseignes :", 1, retour);
        regexControleEquals("Merci de changer les paramètres en option ou de recharger les fichiers de parametrage.", 1, retour);

    }

    @ParameterizedTest
    @ValueSource(classes =
    { ColClarity.class, ColChefServ.class, ColEdition.class })
    public <T extends ColR> void testGetEnumMapColR(Class<T> clazz, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test de chaque type de colonne pour vérifier que l'on récupère bien un map non nulle.
        Map<T, String> map = objetTest.getEnumMapColR(clazz);
        assertThat(map).isNotNull();
        assertThat(map).isNotEmpty();
    }

    @Test
    public void testGetEnumMapColR_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThrows(TechnicalException.class, () -> objetTest.getEnumMapColR(TypeColTest.class));
    }

    @ParameterizedTest
    @ValueSource(classes = { ColVul.class, ColCompo.class })
    public <T extends ColW> void testGetEnumMapColW(Class<T> clazz, TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test map retour énumération n'est pas vide et est protégée
        Map<T, Colonne> map = objetTest.getEnumMapColW(clazz);
        assertThat(map).isNotNull();
        assertThat(map).isNotEmpty();
    }

    @Test
    public void testGetEnumMapColW_Exception(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThrows(TechnicalException.class, () -> objetTest.getEnumMapColR(TypeColTest.class));
    }

    @Test
    public void testGetMapColsInvert(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation de la map ColSuivi
        Map<ColEdition, String> mapColsSuivi = new HashMap<>();
        mapColsSuivi.put(ColEdition.COMMENTAIRE, KEY);
        objetTest.getEnumMapColR(ColEdition.class).putAll(mapColsSuivi);

        // Appel methode
        Map<String, ColEdition> retour = objetTest.getMapColsInvert(ColEdition.class);

        // Contrôle des donnees
        assertThat(retour).isNotNull();
        assertThat(retour).hasSize(2);
        assertThat(retour).containsKey(KEY);
        assertThat(retour.values()).containsExactly(ColEdition.COMMENTAIRE, ColEdition.EDITION);
    }

    @ParameterizedTest
    @ValueSource(strings =
    { "getMapColsClarity", "getMapColsChefServ", "getMapColsEdition" })
    public void testGetterPrive(String valeur, TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        testMapEnum(valeur);
    }

    @Test
    @Override
    public void testGetFile(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test si le fichier n'est pas nul et bien initialise.
        File file = objetTest.getFile();
        assertThat(file).isNotNull();
        assertThat(file.isFile()).isTrue();
        assertThat(file.getPath()).contains(Statics.JARPATH);
        assertThat(file.getName()).isEqualTo("proprietes.xml");
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * permet d'appeler les getters prives des EnumMap
     * 
     * @param methode
     *                Nom de la méthode à tester.
     * @throws Exception
     *                   Exception remontée par la Whitebox.
     */
    private void testMapEnum(String methode) throws Exception
    {
        Map<?, String> map = Whitebox.invokeMethod(objetTest, methode);
        assertThat(map).isNotNull();
        assertThat(map).isNotEmpty();
    }

    /*---------- ACCESSEURS ----------*/

    /*---------- CLASSES PRIVEES ----------*/

    /**
     * Enumeration privee pour créer l'exception
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private enum TypeColTest implements Serializable, ColR, ColW
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
