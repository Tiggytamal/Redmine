package junit.fxml.factory;

import static com.google.common.truth.Truth.assertThat;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import dao.Dao;
import dao.DaoFactory;
import fxml.bdd.DefautQualiteBDD;
import fxml.factory.DefautQualiteFXMLTableRow;
import junit.AutoDisplayName;
import model.bdd.DefautQualite;
import model.enums.ActionDq;
import model.enums.EtatAnoRTC;
import model.enums.EtatAnoRTCProduit;
import model.enums.TypeDefautQualite;
import model.fxml.DefautQualiteFXML;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestDefautQualiteFXMLTableRow extends TestAbstractTableRow<DefautQualiteFXMLTableRow, DefautQualiteFXML, String, ActionDq, Dao<DefautQualite, String>, DefautQualite, DefautQualiteBDD>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String ORANGE = "-fx-background-color:orange";
    private static final String YELLOW = "-fx-background-color:lightyellow";
    private static final String RED = "-fx-background-color:sandybrown";
    private static final String GREEN = "-fx-background-color:lightgreen";
    private static final String BLUE = "-fx-background-color:lightblue";
    private static final String GRAY = "-fx-background-color:lightgray";

    /*---------- CONSTRUCTEURS ----------*/

    @Override
    @BeforeEach
    public void init() throws Exception
    {
        objetTest = new DefautQualiteFXMLTableRow(new DefautQualiteBDD(), DaoFactory.getMySQLDao(DefautQualite.class));
        fxml = new DefautQualiteFXML();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testUpdateItem_Vert(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // Test style vert - QG et appli OK
        fxml.setQg("OK");
        fxml.setAppliOK("OK");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(GREEN);

        // Test seulement QG OK
        fxml.setAppliOK("KO");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
        fxml.setQg("KO");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Gris(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // Test style gris - Erreur avec anomalie close
        fxml.setEtatRTC(EtatAnoRTC.CLOSE.getValeur());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(GRAY);
        fxml.setEtatRTC(EtatAnoRTCProduit.CLOSE.getValeur());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(GRAY);

        // Test style non gris
        fxml.setEtatRTC("Open");
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Orange(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // Test style Orange - Défaut pas encore traité
        fxml.setRemarque(Statics.EMPTY);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(ORANGE);

        // Test style non orange
        fxml.setRemarque(TESTSTRING);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
        fxml.setNumeroAnoRTC(Arrays.asList("123456"));
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Bleu(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // test style bleu - défaut sur les codes application
        fxml.setTypeDefaut(TypeDefautQualite.APPLI.toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(BLUE);

        // Test style non bleu
        fxml.setTypeDefaut(TypeDefautQualite.MIXTE.toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Rouge(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // Test style rouge - date de mise en production passée
        fxml.setDateMepPrev(LocalDate.of(2019, 1, 1).toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(RED);

        // Test style non rouge
        fxml.setDateMepPrev(Statics.EMPTY);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
        fxml.setDateMepPrev(LocalDate.now().plusDays(1).plusWeeks(3).toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }
    
    @Test
    public void testUpdateItem_Jaune(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        prepareUpdateItem();

        // Test style jaune - date de mise en production prévue dans moins de 3 semaines
        fxml.setDateMepPrev(LocalDate.now().plusDays(1).toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEqualTo(YELLOW);

        // Test style non jaune
        fxml.setDateMepPrev(Statics.EMPTY);
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
        fxml.setDateMepPrev(LocalDate.now().plusDays(1).plusWeeks(3).toString());
        appelUpdateItem(fxml, false);
        assertThat(objetTest.getStyle()).isEmpty();
    }

    /*---------- METHODES PRIVEES ----------*/
    
    public void prepareUpdateItem()
    {
        fxml.setNumeroAnoRTC(Arrays.asList("0"));
        fxml.setRemarque(TESTSTRING);
    }
    
    /*---------- ACCESSEURS ----------*/
}
