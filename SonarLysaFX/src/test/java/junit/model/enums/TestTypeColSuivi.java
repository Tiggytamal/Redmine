package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeColSuivi;

/**
 * Classe de test de l'énumération TypeColSuivi
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class TestTypeColSuivi implements TestEnums
{
    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeColSuivi.ACTION, TypeColSuivi.valueOf(TypeColSuivi.ACTION.toString()));
    }
    
    @Test
    @Override
    public void testSize()
    {
        assertEquals(24, TypeColSuivi.values().length);
    }
    
    @Test
    public void testGetValeur()
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
        assertEquals("Projet NPC", TypeColSuivi.NPC.getValeur());
    }
    
    @Test
    public void testGetNomCol()
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
        assertEquals("colEnv", TypeColSuivi.ENV.getNomCol());
        assertEquals("colAno", TypeColSuivi.ANOMALIE.getNomCol());
        assertEquals("colEtat", TypeColSuivi.ETAT.getNomCol());
        assertEquals("colSec", TypeColSuivi.SECURITE.getNomCol());
        assertEquals("colRemarque", TypeColSuivi.REMARQUE.getNomCol());
        assertEquals("colVer", TypeColSuivi.VERSION.getNomCol());        
        assertEquals("colDateCrea", TypeColSuivi.DATECREATION.getNomCol());
        assertEquals("colDateDetec", TypeColSuivi.DATEDETECTION.getNomCol());
        assertEquals("colDateRel", TypeColSuivi.DATERELANCE.getNomCol());
        assertEquals("colMatiere", TypeColSuivi.MATIERE.getNomCol());
        assertEquals("colProjetRTC", TypeColSuivi.PROJETRTC.getNomCol());
        assertEquals("colAction", TypeColSuivi.ACTION.getNomCol());
        assertEquals("colNpc", TypeColSuivi.NPC.getNomCol());
    }
}
