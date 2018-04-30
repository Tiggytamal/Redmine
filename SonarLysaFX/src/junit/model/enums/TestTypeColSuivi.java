package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColSuivi;

public class TestTypeColSuivi
{
    @Test
    public void testSize()
    {
        assertEquals(21, TypeColSuivi.values().length);
    }
    
    @Test
    public void getValeur()
    {
        assertEquals("Numéro du lot", TypeColSuivi.LOT.getValeur());
        assertEquals("Libellé projet", TypeColSuivi.LIBELLE.getValeur());
        assertEquals("Code Clarity", TypeColSuivi.CLARITY.getValeur());
        assertEquals("Chef de projet du lot", TypeColSuivi.CPI.getValeur());
        assertEquals("Edition", TypeColSuivi.EDITION.getValeur());
        assertEquals("Direction", TypeColSuivi.DIRECTION.getValeur());
        assertEquals("Département", TypeColSuivi.DEPARTEMENT.getValeur());
        assertEquals("Service", TypeColSuivi.SERVICE.getValeur());
        assertEquals("Responsable Service", TypeColSuivi.RESPSERVICE.getValeur());
        assertEquals("Date de création", TypeColSuivi.DATECREATION.getValeur());
        assertEquals("Matière", TypeColSuivi.MATIERE.getValeur());
        assertEquals("Projet RTC", TypeColSuivi.PROJETRTC.getValeur());
        assertEquals("Action", TypeColSuivi.ACTION.getValeur());
    }
    
    @Test
    public void getNomCol()
    {
        assertEquals("colLot", TypeColSuivi.LOT.getNomCol());
        assertEquals("colLib", TypeColSuivi.LIBELLE.getNomCol());
        assertEquals("colClarity", TypeColSuivi.CLARITY.getNomCol());
        assertEquals("colCpi", TypeColSuivi.CPI.getNomCol());
        assertEquals("colEdition", TypeColSuivi.EDITION.getNomCol());
        assertEquals("colDir", TypeColSuivi.DIRECTION.getNomCol());
        assertEquals("colDepart", TypeColSuivi.DEPARTEMENT.getNomCol());
        assertEquals("colService", TypeColSuivi.SERVICE.getNomCol());
        assertEquals("colResp", TypeColSuivi.RESPSERVICE.getNomCol());
        assertEquals("colDateCrea", TypeColSuivi.DATECREATION.getNomCol());
        assertEquals("colMatiere", TypeColSuivi.MATIERE.getNomCol());
        assertEquals("colProjetRTC", TypeColSuivi.PROJETRTC.getNomCol());
        assertEquals("colAction", TypeColSuivi.ACTION.getNomCol());
    }
}
