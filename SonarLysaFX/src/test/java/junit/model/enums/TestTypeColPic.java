package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColPic;

public class TestTypeColPic implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(13, TypeColPic.values().length);
    }
    
    @Test
    public void testGetValeur()
    {
        assertEquals("Lot", TypeColPic.LOT.getValeur());
        assertEquals("Libellé", TypeColPic.LIBELLE.getValeur());
        assertEquals("code Clarity", TypeColPic.CLARITY.getValeur());
        assertEquals("Chef de projet", TypeColPic.CPI.getValeur());
        assertEquals("Edition", TypeColPic.EDITION.getValeur());
        assertEquals("Nbre Composants", TypeColPic.NBCOMPOS.getValeur());
        assertEquals("Nbre Paquets", TypeColPic.NBPAQUETS.getValeur());
        assertEquals("Date 1er build", TypeColPic.BUILD.getValeur());
        assertEquals("Livraison DEVTU", TypeColPic.DEVTU.getValeur());
        assertEquals("Livraison TFON", TypeColPic.TFON.getValeur());
        assertEquals("Livraison VMOE", TypeColPic.VMOE.getValeur());
        assertEquals("Livraison VMOA", TypeColPic.VMOA.getValeur());
        assertEquals("Livraison édition", TypeColPic.LIV.getValeur());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("colLot", TypeColPic.LOT.getNomCol());
        assertEquals("colLibelle", TypeColPic.LIBELLE.getNomCol());
        assertEquals("colClarity", TypeColPic.CLARITY.getNomCol());
        assertEquals("colCpi", TypeColPic.CPI.getNomCol());
        assertEquals("colEdition", TypeColPic.EDITION.getNomCol());
        assertEquals("colNbCompos", TypeColPic.NBCOMPOS.getNomCol());
        assertEquals("colNbpaquets", TypeColPic.NBPAQUETS.getNomCol());
        assertEquals("colBuild", TypeColPic.BUILD.getNomCol());
        assertEquals("colDevtu", TypeColPic.DEVTU.getNomCol());
        assertEquals("colTfon", TypeColPic.TFON.getNomCol());
        assertEquals("colVmoe", TypeColPic.VMOE.getNomCol());
        assertEquals("colVmoa", TypeColPic.VMOA.getNomCol());
        assertEquals("colLiv", TypeColPic.LIV.getNomCol());
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColPic.VMOA, TypeColPic.valueOf(TypeColPic.VMOA.toString()));    
    }
}