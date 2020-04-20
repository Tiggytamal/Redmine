package junit.model.enums;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import junit.AutoDisplayName;
import model.enums.ColPic;

@DisplayNameGeneration(AutoDisplayName.class)
public class TestColPic extends TestAbstractEnum<ColPic>
{
    /*---------- ATTRIBUTS ----------*/
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void testConstructeur(TestInfo testInfo)
    {
        assertThat(ColPic.valueOf(ColPic.LIBELLE.toString())).isEqualTo(ColPic.LIBELLE);
    }

    @Override
    public void testLength(TestInfo testInfo)
    {
        assertThat(ColPic.values().length).isEqualTo(6);
    }
    
    @Test
    public void testGetValeur(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColPic.LOT.getValeur()).isEqualTo("Lot");
        assertThat(ColPic.LIBELLE.getValeur()).isEqualTo("Libellé");
        assertThat(ColPic.CLARITY.getValeur()).isEqualTo("code Clarity");
        assertThat(ColPic.CPI.getValeur()).isEqualTo("Chef de projet");
        assertThat(ColPic.EDITION.getValeur()).isEqualTo("Edition");
        assertThat(ColPic.NBCOMPOS.getValeur()).isEqualTo("Nbre Composants");
        assertThat(ColPic.NBPAQUETS.getValeur()).isEqualTo("Nbre Paquets");
        assertThat(ColPic.BUILD.getValeur()).isEqualTo("Date 1er build");
        assertThat(ColPic.DEVTU.getValeur()).isEqualTo("Livraison DEVTU");
        assertThat(ColPic.TFON.getValeur()).isEqualTo("Livraison TFON");
        assertThat(ColPic.VMOE.getValeur()).isEqualTo("Livraison VMOE");
        assertThat(ColPic.VMOA.getValeur()).isEqualTo("Livraison VMOA");
        assertThat(ColPic.LIV.getValeur()).isEqualTo("Livraison édition");
    }
    
    @Test
    public void testGetNomCol(TestInfo testInfo)
    {
        LOGGER.debug(testInfo.getDisplayName());

        assertThat(ColPic.LOT.getNomCol()).isEqualTo("colLot");
        assertThat(ColPic.LIBELLE.getNomCol()).isEqualTo("colLibelle");
        assertThat(ColPic.CLARITY.getNomCol()).isEqualTo("colClarity");
        assertThat(ColPic.CPI.getNomCol()).isEqualTo("colCpi");
        assertThat(ColPic.EDITION.getNomCol()).isEqualTo("colEdition");
        assertThat(ColPic.NBCOMPOS.getNomCol()).isEqualTo("colNbCompos");
        assertThat(ColPic.NBPAQUETS.getNomCol()).isEqualTo("colNbpaquets");
        assertThat(ColPic.BUILD.getNomCol()).isEqualTo("colBuild");
        assertThat(ColPic.DEVTU.getNomCol()).isEqualTo("colDevtu");
        assertThat(ColPic.TFON.getNomCol()).isEqualTo("colTfon");
        assertThat(ColPic.VMOE.getNomCol()).isEqualTo("colVmoe");
        assertThat(ColPic.VMOA.getNomCol()).isEqualTo("colVmoa");
        assertThat(ColPic.LIV.getNomCol()).isEqualTo("colLiv");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
