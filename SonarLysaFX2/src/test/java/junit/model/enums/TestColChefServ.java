package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ActionDq;
import model.enums.ColChefServ;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColChefServ extends TestAbstractEnum<ActionDq>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    @Override
    public void testLength(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColChefServ.values().length).isEqualTo(5);
    }

    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColChefServ.DIRECTION.getValeur()).isEqualTo("Direction");
        assertThat(ColChefServ.DEPARTEMENT.getValeur()).isEqualTo("Departement");
        assertThat(ColChefServ.SERVICE.getValeur()).isEqualTo("Service");
        assertThat(ColChefServ.FILIERE.getValeur()).isEqualTo("Filiere");
        assertThat(ColChefServ.MANAGER.getValeur()).isEqualTo("Manager");
    }

    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColChefServ.DIRECTION.getNomCol()).isEqualTo("colDir");
        assertThat(ColChefServ.DEPARTEMENT.getNomCol()).isEqualTo("colDepart");
        assertThat(ColChefServ.SERVICE.getNomCol()).isEqualTo("colService");
        assertThat(ColChefServ.FILIERE.getNomCol()).isEqualTo("colFil");
        assertThat(ColChefServ.MANAGER.getNomCol()).isEqualTo("colManager");
    }

    @Test
    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColChefServ.valueOf(ColChefServ.DEPARTEMENT.toString())).isEqualTo(ColChefServ.DEPARTEMENT);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
