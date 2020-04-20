package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.TypeDonnee;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestTypeDonnee extends TestAbstractEnum<TypeDonnee>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeDonnee.values().length).isEqualTo(16);
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeDonnee.valueOf(TypeDonnee.APPS.toString())).isEqualTo(TypeDonnee.APPS);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(TypeDonnee.APPS.getValeur()).isEqualTo("Applications");
        assertThat(TypeDonnee.CLARITY.getValeur()).isEqualTo("Projets Clarity");
        assertThat(TypeDonnee.RESPSERVICE.getValeur()).isEqualTo("Chefs de Service");
        assertThat(TypeDonnee.EDITION.getValeur()).isEqualTo("Editions");
        assertThat(TypeDonnee.ISSUE.getValeur()).isEqualTo("Issue SonarQube");
        assertThat(TypeDonnee.COMPOSANT.getValeur()).isEqualTo("Composants");
        assertThat(TypeDonnee.LOTSRTC.getValeur()).isEqualTo("Lots RTC");
        assertThat(TypeDonnee.PRODUIT.getValeur()).isEqualTo("Produits");
        assertThat(TypeDonnee.SOLUTION.getValeur()).isEqualTo("Solutions");
        assertThat(TypeDonnee.DEFAULTQUALITE.getValeur()).isEqualTo("Defaults qualite");
        assertThat(TypeDonnee.DATEMAJ.getValeur()).isEqualTo("Dates de mise à jour des tables");
        assertThat(TypeDonnee.DEFAULTAPPLI.getValeur()).isEqualTo("Defaults application");
        assertThat(TypeDonnee.COMPOERR.getValeur()).isEqualTo("Composants en erreurs");
        assertThat(TypeDonnee.PROJETMC.getValeur()).isEqualTo("Projets Mobile Center");
        assertThat(TypeDonnee.USER.getValeur()).isEqualTo("Utilisateurs RTC/Sonar");
        assertThat(TypeDonnee.ANO.getValeur()).isEqualTo("Anomalies RTC");
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
