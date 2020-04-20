package junit.control.task.extraction;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.excel.ControlExtractVul;
import control.task.extraction.ExtractionVulnerabiliteTask;
import junit.AutoDisplayName;
import junit.control.task.TestAbstractTask;
import model.ModelFactory;
import model.Vulnerabilite;
import model.bdd.Application;
import model.bdd.ComposantBase;
import model.bdd.LotRTC;
import model.enums.TypeVulnerabilite;
import model.rest.sonarapi.Issue;
import utilities.Statics;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestCreerExtractVulnerabiliteTask extends TestAbstractTask<ExtractionVulnerabiliteTask>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    @Override
    public void initImpl()
    {
        objetTest = new ExtractionVulnerabiliteTask(new File(Statics.RESSTEST + "testExtract.xlsx"));
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testCall(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Appel de la methode call qui ne sert qu'e appeler la methode privee.
        // Controle du bon retour à true. Mock du contrôleur pour éviter écriture du fichier.
        ControlExtractVul control = Mockito.mock(ControlExtractVul.class);
        Mockito.when(control.write()).thenReturn(true);
        setField("control", control);
        assertThat((boolean) Whitebox.invokeMethod(objetTest, "call")).isTrue();
    }

    @Test
    public void testConvertIssueToVul(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Initialisation
        Issue issue = new Issue();
        issue.setStatut("status");
        issue.setDateCreation(LocalDateTime.now());
        issue.setSeverite("severity");
        issue.setMessage("javajaf.jar");
        ComposantBase composant = ModelFactory.build(ComposantBase.class);
        composant.setNom(NOM);
        composant.setAppli(ModelFactory.build(Application.class));
        LotRTC lotRTC = ModelFactory.build(LotRTC.class);
        lotRTC.setNumero(LOT123456);
        composant.setLotRTC(lotRTC);
        Vulnerabilite retour = Whitebox.invokeMethod(objetTest, "convertIssueToVul", issue, composant);

        // Controleur que les valeurs sont bonnes après conversion
        assertThat(retour.getAppli()).isEmpty();
        assertThat(retour.getLot()).isEqualTo(LOT123456);
        assertThat(retour.getComposant()).isEqualTo(NOM);
        assertThat(retour.getStatus()).isEqualTo("status");
        assertThat(retour.getSeverite()).isEqualTo("severity");
        assertThat(retour.getMessage()).isEqualTo("javajaf.jar");
    }

    @Test
    public void testRecupVulnerabilitesSonar(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        // Test que la liste des vulnerabilités ouvertes ne contient pas de résolue ou close.
        List<Vulnerabilite> result = Whitebox.invokeMethod(objetTest, "recupVulnerabilitesSonar", TypeVulnerabilite.OUVERTE);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertThat(vulnerabilite.getStatus()).isNoneOf("RESOLVED", "CLOSED");
        }

        // Test que la liste des vulnerabilités résolues ne contient que des résolues ou closes.
        result = Whitebox.invokeMethod(objetTest, "recupVulnerabilitesSonar", TypeVulnerabilite.RESOLUE);
        for (Vulnerabilite vulnerabilite : result)
        {
            assertThat(vulnerabilite.getStatus()).isAnyOf("RESOLVED", "CLOSED");
        }
    }

    @Test
    public void testExtractLib(TestInfo testInfo) throws Exception
    {
        LOGGER.debug(testInfo.getDisplayName());

        String methode = "extractLib";
        String jar = "boudu.jar";
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "Filename: boudu.jar")).isEqualTo(jar);
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "Filename: boudu.jar | autre texte")).isEqualTo(jar);
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "Filename: boudu.jar/autretexte")).isEqualTo(jar);
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "boudu.jar")).isEqualTo(jar);
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "boudu.jar/autretexte")).isEqualTo(jar);
        assertThat((String) Whitebox.invokeMethod(objetTest, methode, "boudu.jar | autre texte")).isEqualTo(jar);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
