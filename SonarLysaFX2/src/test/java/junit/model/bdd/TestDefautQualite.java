package junit.model.bdd;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.powermock.reflect.Whitebox;

import junit.AutoDisplayName;
import model.ModelFactory;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EtatCodeAppli;
import model.enums.EtatDefaut;
import model.enums.Param;
import model.enums.TypeDefautQualite;
import utilities.Statics;
import utilities.TechnicalException;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDefautQualite extends TestAbstractBDDModele<DefautQualite, String>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    
    @Override
    protected void initImpl()
    {
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        objetTest.setCompo(ComposantBase.build(KEY, NOM));
        objetTest.getLotRTC().setProjetRTC("ProjetRTC");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test création anomalie depuis LotSuiviRTC
        assertThat(objetTest).isNotNull();
    }

    @Test
    public void testToString(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        String string = objetTest.toString();
        assertThat(string).contains("numeroAnoRTC=0");
        assertThat(string).contains("etatAnoRTC=<null>");
        assertThat(string).contains("remarque=");
        assertThat(string).contains("dateCreation=<null>");
        assertThat(string).contains("dateDetection=");
        assertThat(string).contains("dateRelance=<null>");
        assertThat(string).contains("dateReso=<null>");
        assertThat(string).contains("dateReouv=<null>");
        assertThat(string).contains("dateMepPrev=<null>");
        assertThat(string).contains("etatDefaut=NOUVEAU");
        assertThat(string).contains("idBase=0");
        assertThat(string).contains("timeStamp=");
    }

    @Test
    public void testCalculTraitee(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Avec une objet juste initialise, le booleen doit être à faux.
        assertThat(objetTest.calculTraitee()).isFalse();

        // Test avec remarque non nulle.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque(EMPTY);
        assertThat(objetTest.calculTraitee()).isFalse();

        // Test avec remarque non vide.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque("remarque");
        assertThat(objetTest.calculTraitee()).isTrue();

        // test avec numero d'anomalie non 0.
        objetTest.setEtatDefaut(EtatDefaut.NOUVEAU);
        objetTest.setRemarque(null);
        objetTest.setLotRTC(LotRTC.getLotRTCInconnu(LOT123456));
        objetTest.setNumeroAnoRTC(10);
        assertThat(objetTest.calculTraitee()).isTrue();

        // Avec un etat non nouveau
        objetTest.setEtatDefaut(EtatDefaut.ABANDONNE);
        assertThat(objetTest.calculTraitee()).isTrue();

    }

    @Test
    public void testGetMapIndex(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec objet initial avec compo et lot
        assertThat(objetTest.getMapIndex()).isNotNull();
        assertThat(objetTest.getMapIndex()).isEqualTo(objetTest.getCompo().getMapIndex() + objetTest.getLotRTC().getNumero());

        // Test avec composant, pas de changement sur l'index.
        String index = objetTest.getMapIndex();
        Whitebox.getField(objetTest.getClass(), "compo").set(objetTest, null);
        objetTest.initMapIndex();
        assertThat(objetTest.getMapIndex()).isEqualTo(index);

        // Test avec lot null, pas de changement sur l'index

        // Reinitialisation et mise à null du lot.
        initImpl();
        Whitebox.getField(objetTest.getClass(), "lotRTC").set(objetTest, null);
        objetTest.initMapIndex();
        assertThat(objetTest.getMapIndex()).isEqualTo(index);
    }

    @Test
    public void testGetLiensAno(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test numéro anomalie ou lotRTC vide

        // Initialisation
        setField("numeroAnoRTC", 0);
        setField("lotRTC", null);

        assertThat(objetTest.getLiensAno()).isEmpty();

        // Test numéro non 0
        objetTest.setNumeroAnoRTC(123456);
        assertThat(objetTest.getLiensAno()).isEmpty();

        // Test lot RTC non null
        LotRTC lotRTC = LotRTC.getLotRTCInconnu(LOT123456);
        objetTest.setLotRTC(lotRTC);
        assertThat(objetTest.getLiensAno()).isEmpty();

        // Test lot RTC non nullet projetRTC valorisé
        lotRTC.setProjetRTC(NOM);
        assertThat(objetTest.getLiensAno()).isEqualTo(Statics.proprietesXML.getMapParams().get(Param.LIENSRTC) + lotRTC.getProjetRTC() + Statics.FINLIENSRTC + String.valueOf(123456));
    }

    @Test
    public void testAjouterCreateurIssue(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        // Apel méthode
        Utilisateur user = ModelFactory.build(Utilisateur.class);
        objetTest.ajouterCreateurIssue(user);

        // Contrôle
        assertThat(objetTest.getCreateurIssues()).contains(user);
    }

    @Test
    public void testEquals_hashCode(TestInfo testInfo) throws JAXBException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());
        
        initDataBase();
        DefautQualite dq2 = databaseXML.getDqs().get(1);

        testSimpleEquals(dq2);
        
        // Init params objetTest
        objetTest.setAnalyseId("id");
        objetTest.setDateCreation(today);
        objetTest.setDateDetection(today);
        objetTest.setDateRelance(today);
        objetTest.setDateMepPrev(today);
        objetTest.setDateReouv(today);
        objetTest.setDateReso(today);
        objetTest.setEtatAnoRTC("Closed");
        objetTest.setEtatDefaut(EtatDefaut.OBSOLETE);
        objetTest.setNumeroAnoRTC(123456);
        objetTest.setRemarque("remarque");
        Set<Utilisateur> set = new HashSet<>();
        set.add(ModelFactory.build(Utilisateur.class));
        objetTest.setCreateurIssues(set);
        
        // Test paramètres 1 apr 1
        dq2.setAnalyseId(objetTest.getAnalyseId());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setCompo(objetTest.getCompo());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setCreateurIssues(objetTest.getCreateurIssues());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setDateCreation(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setDateDetection(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());
        
        dq2.setDateMepPrev(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setDateRelance(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setDateReouv(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setDateReso(today);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setEtatCodeAppli(objetTest.getEtatCodeAppli());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setEtatDefaut(objetTest.getEtatDefaut());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setEtatAnoRTC(objetTest.getEtatAnoRTC());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setLotRTC(objetTest.getLotRTC());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setNumeroAnoRTC(123456);
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setRemarque(objetTest.getRemarque());
        assertThat(objetTest.equals(dq2)).isFalse();
        assertThat(objetTest.hashCode()).isNotEqualTo(dq2.hashCode());

        dq2.setTypeDefaut(objetTest.getTypeDefaut());
        assertThat(objetTest.equals(dq2)).isTrue();
        assertThat(objetTest.hashCode()).isEqualTo(dq2.hashCode());
    }

    @Test
    public void testGetCompo(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec initialisation
        assertThat(objetTest.getCompo()).isNotNull();

        // Protection null
        objetTest.setCompo(null);
        assertThat(objetTest.getCompo()).isNotNull();

        // Test getter et setter
        ComposantBase compo = ComposantBase.build(KEY, NOM);
        objetTest.setCompo(compo);
        assertThat(objetTest.getCompo()).isEqualTo(compo);
    }

    @Test
    public void testGetLotRTC(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test avec initialisation
        assertThat(objetTest.getLotRTC()).isNotNull();

        // Protection null
        objetTest.setLotRTC(null);
        assertThat(objetTest.getLotRTC()).isNotNull();

        // Test getter et setter
        LotRTC lotRTC = LotRTC.getLotRTCInconnu(LOT123456);
        objetTest.setLotRTC(lotRTC);
        assertThat(objetTest.getLotRTC()).isEqualTo(lotRTC);
    }

    @Test
    public void testGetNumeroAnoRTC(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getNumeroAnoRTC()).isEqualTo(0);

        // Test setter et getter
        int integer = 12;
        objetTest.setNumeroAnoRTC(integer);
        assertThat(objetTest.getNumeroAnoRTC()).isEqualTo(integer);

        // Protection remise à zero
        objetTest.setNumeroAnoRTC(0);
        assertThat(objetTest.getNumeroAnoRTC()).isEqualTo(integer);
    }

    @Test
    public void testCreateurIssues(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation non null
        assertThat(objetTest.getCreateurIssues()).isNotNull();
        assertThat(objetTest.getCreateurIssues()).isEmpty();

        // Test getter et setter
        Set<Utilisateur> set = new HashSet<>();
        set.add(ModelFactory.build(Utilisateur.class));
        objetTest.setCreateurIssues(set);
        assertThat(objetTest.getCreateurIssues()).isEqualTo(set);

        // Protection setter null et vide
        objetTest.setCreateurIssues(null);
        assertThat(objetTest.getCreateurIssues()).isEqualTo(set);
        objetTest.setCreateurIssues(new HashSet<>());
        assertThat(objetTest.getCreateurIssues()).isEqualTo(set);

        // Protection null avec introspection
        setField("createurIssues", null);
        assertThat(objetTest.getCreateurIssues()).isNotNull();
        assertThat(objetTest.getCreateurIssues()).isEmpty();
    }

    @Test
    public void testGetAnalyseId(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getAnalyseId()).isNotNull();
        assertThat(objetTest.getAnalyseId()).isEmpty();

        // Test setter et getter
        String analyse = "analyse";
        objetTest.setAnalyseId(analyse);
        assertThat(objetTest.getAnalyseId()).isEqualTo(analyse);

        // Protection remise à null ou vide
        objetTest.setAnalyseId(null);
        assertThat(objetTest.getAnalyseId()).isEqualTo(analyse);
        objetTest.setAnalyseId(Statics.EMPTY);
        assertThat(objetTest.getAnalyseId()).isEqualTo(analyse);
    }

    @Test
    public void testGetEtatRTC(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getEtatAnoRTC(), s -> objetTest.setEtatAnoRTC(s));
    }

    @Test
    public void testGetRemarque(TestInfo testInfo)
    {
        testSimpleString(testInfo, () -> objetTest.getRemarque(), s -> objetTest.setRemarque(s));
    }

    @Test
    public void testGetDateCreation(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateCreation(), d -> objetTest.setDateCreation(d));
    }

    @Test
    public void testGetDateDetection(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur vide ou nulle
        assertThat(objetTest.getDateDetection()).isEqualTo(Statics.TODAY);

        // Test setter et getter
        objetTest.setDateDetection(today);
        assertThat(objetTest.getDateDetection()).isEqualTo(today);

        // Protection null
        objetTest.setDateDetection(null);
        assertThat(objetTest.getDateDetection()).isEqualTo(today);

        // Protection getter sur date deje nulle
        setField("dateDetection", null);
        assertThat(objetTest.getDateDetection()).isEqualTo(LocalDate.now());
    }

    @Test
    public void testGetDateRelance(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation avec valeur à null
        assertThat(objetTest.getDateRelance()).isNull();

        // Test getter et setter
        objetTest.setDateRelance(today);
        assertThat(objetTest.getDateRelance()).isEqualTo(today);

        // Test protection setter null
        objetTest.setDateRelance(null);
        assertThat(objetTest.getDateRelance()).isNull();
    }

    @Test
    public void testGetDateReso(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateReso(), d -> objetTest.setDateReso(d));
    }

    @Test
    public void testEtatDefaut(TestInfo testInfo) throws IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur nouveau
        assertThat(objetTest.getEtatDefaut()).isEqualTo(EtatDefaut.NOUVEAU);

        // Test setter et getter
        EtatDefaut etatDefaut = EtatDefaut.ABANDONNE;
        objetTest.setEtatDefaut(etatDefaut);
        assertThat(objetTest.getEtatDefaut()).isEqualTo(etatDefaut);

        // Test protection getter sur objet etat null
        Whitebox.getField(objetTest.getClass(), "etatDefaut").set(objetTest, null);
        assertThat(objetTest.getEtatDefaut()).isEqualTo(EtatDefaut.NOUVEAU);

        // Test remontée erreur technique
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.setEtatDefaut(null));
        assertThat(e.getMessage()).isEqualTo("Tentative de mise à null de DefautQualite.etatDefaut");
    }

    @Test
    public void testGetEtatCodeAppli(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test initialisation
        assertThat(objetTest.getEtatCodeAppli()).isEqualTo(EtatCodeAppli.NA);

        // Test setter et getter
        objetTest.setEtatCodeAppli(EtatCodeAppli.OK);
        assertThat(objetTest.getEtatCodeAppli()).isEqualTo(EtatCodeAppli.OK);

        // Test protectedion setter null
        assertThrows(TechnicalException.class, () -> objetTest.setEtatCodeAppli(null));
    }

    @Test
    public void testGetTypeDefaut(TestInfo testInfo) throws IllegalArgumentException, IllegalAccessException
    {
        LOGGER.debug(testInfo.getDisplayName());

        // test valeur nouveau
        assertThat(objetTest.getTypeDefaut()).isEqualTo(TypeDefautQualite.INCONNU);

        // Test setter et getter
        TypeDefautQualite etatDefaut = TypeDefautQualite.APPLI;
        objetTest.setTypeDefaut(etatDefaut);
        assertThat(objetTest.getTypeDefaut()).isEqualTo(etatDefaut);

        // Test protection getter sur objet type null
        Whitebox.getField(objetTest.getClass(), "typeDefaut").set(objetTest, null);
        assertThat(objetTest.getTypeDefaut()).isEqualTo(TypeDefautQualite.INCONNU);

        // Test remontée erreur technique
        TechnicalException e = assertThrows(TechnicalException.class, () -> objetTest.setTypeDefaut(null));
        assertThat(e.getMessage()).isEqualTo("Tentative de mise à null de DefautQualite.typeDefaut");

    }

    @Test
    public void testGetDateReouv(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateReouv(), d -> objetTest.setDateReouv(d));
    }

    @Test
    public void testGetDateMepPrev(TestInfo testInfo)
    {
        testSimpleLocalDate(testInfo, () -> objetTest.getDateMepPrev(), d -> objetTest.setDateMepPrev(d));
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
