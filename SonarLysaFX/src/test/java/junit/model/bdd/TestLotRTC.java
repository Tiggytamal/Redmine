package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.enums.EtatLot;
import model.enums.GroupeProduit;
import model.enums.Matiere;
import model.enums.QG;
import utilities.Statics;

public class TestLotRTC extends AbstractTestModel<LotRTC>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String LOT = "123456";

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testGetLotRTCInconnu()
    {
        objetTest = LotRTC.getLotRTCInconnu(LOT);
        assertEquals(LOT, objetTest.getLot());
        assertEquals(EMPTY, objetTest.getLibelle());
        assertEquals(EMPTY, objetTest.getProjetClarityString());
        assertEquals(EMPTY, objetTest.getEditionString());
        assertEquals(EMPTY, objetTest.getCpiProjet());
        assertEquals(EtatLot.NOUVEAU, objetTest.getEtatLot());
        assertEquals(EMPTY, objetTest.getProjetRTC());
        assertEquals(LOT, objetTest.getLot());
        assertNotNull(objetTest.getEdition());
        assertEquals(Statics.EDINCONNUE, objetTest.getEdition().getNumero());
    }

    @Test
    public void testGetMapIndex()
    {
        objetTest.setLot("123");
        assertEquals(objetTest.getLot(), objetTest.getMapIndex());
    }

    @Test
    public void update()
    {
        // Préparation update
        LotRTC lot = LotRTC.getLotRTCInconnu(LOT);
        lot.setLibelle("A");
        lot.setProjetClarity(ProjetClarity.getProjetClarityInconnu("a"));
        lot.setCpiProjet("C");
        lot.setEtatLot(EtatLot.ABANDONNE);
        lot.setProjetRTC("D");
        lot.setDateMajEtat(Statics.DATEINCONNUE);

        // Update
        objetTest.update(lot);
        assertEquals(lot.getLibelle(), objetTest.getLibelle());
        assertEquals(lot.getProjetClarity(), objetTest.getProjetClarity());
        assertEquals(lot.getCpiProjet(), objetTest.getCpiProjet());
        assertEquals(lot.getEtatLot(), objetTest.getEtatLot());
        assertEquals(lot.getProjetRTC(), objetTest.getProjetRTC());
        assertEquals(lot.getDateMajEtat(), objetTest.getDateMajEtat());
        assertNotEquals(lot.getLot(), objetTest.getLot());
        assertEquals(lot.getDateMajEtat(), objetTest.getDateMajEtat());
    }

    @Test
    public void testGetMatieresString()
    {
        // Test null
        objetTest.setMatieres(null);
        assertEquals(EMPTY, objetTest.getMatieresString());
        
        // Test avec liste remplie
        objetTest.addMatiere(Matiere.COBOL);
        objetTest.addMatiere(Matiere.DATASTAGE);
        assertTrue(objetTest.getMatieresString().contains(" - "));
        assertTrue(objetTest.getMatieresString().contains("DATASTAGE"));
        assertTrue(objetTest.getMatieresString().contains("COBOL"));
    }

    @Test
    public void testAddMatiere()
    {
        // Contrôle ajout matières
        objetTest.addMatiere(Matiere.COBOL);
        assertTrue(objetTest.getMatieres().contains(Matiere.COBOL));
        
        // Test protection null
        objetTest.addMatiere(null);
        assertFalse(objetTest.getMatieres().contains(null));
        
        // Protection set null
        objetTest.setMatieres(null);
        objetTest.addMatiere(Matiere.JAVA);
        assertTrue(objetTest.getMatieres().contains(Matiere.JAVA));
    }

    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLot());

        // Test setter et getter
        String direction = "123456";
        objetTest.setLot(direction);
        assertEquals(direction, objetTest.getLot());
    }

    @Test
    public void testGetLibelle()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getLibelle());

        // Test setter et getter
        String string = "Libelle";
        objetTest.setLibelle(string);
        assertEquals(string, objetTest.getLibelle());
    }

    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getProjetClarity());

        // Test setter et getter
        ProjetClarity pc = ProjetClarity.getProjetClarityInconnu("123456");
        objetTest.setProjetClarity(pc);
        assertEquals(pc, objetTest.getProjetClarity());
    }

    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCpiProjet());

        // Test setter et getter
        String string = "CPI";
        objetTest.setCpiProjet(string);
        assertEquals(string, objetTest.getCpiProjet());
    }

    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getEdition());

        // Test setter et getter
        Edition edition = Edition.getEditionInconnue(null);
        objetTest.setEdition(edition);
        assertEquals(edition, objetTest.getEdition());
    }

    @Test
    public void testGetEtatLot()
    {
        // test valeur vide ou nulle
        assertEquals(EtatLot.INCONNU, objetTest.getEtatLot());

        // Test setter et getter
        objetTest.setEtatLot(EtatLot.DEVTU);;
        assertEquals(EtatLot.DEVTU, objetTest.getEtatLot());
    }

    @Test
    public void testGetProjetRTC()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getProjetRTC());

        // Test setter et getter
        String string = "Projet";
        objetTest.setProjetRTC(string);
        assertEquals(string, objetTest.getProjetRTC());
    }

    @Test
    public void testGetDateMajEtat()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getDateMajEtat());

        // Test setter et getter
        LocalDate date = LocalDate.of(2018, 10, 10);
        objetTest.setDateMajEtat(date);
        assertEquals(date, objetTest.getDateMajEtat());
    }

    @Test
    public void testGetProjetClarityString()
    {
        // test valeur vide ou nulle
        assertNotNull(objetTest.getProjetClarityString());
        assertTrue(objetTest.getProjetClarityString().isEmpty());

        // Test setter et getter
        String code = "Code";
        objetTest.setProjetClarityString(code);
        assertEquals(code, objetTest.getProjetClarityString());
    }

    @Test
    public void testGetQualityGate()
    {
        // Test sans initialisation
        assertEquals(QG.NONE, objetTest.getQualityGate());

        // Test valeure nulle
        QG qg = null;
        objetTest.setQualityGate(qg);
        assertEquals(QG.NONE, objetTest.getQualityGate());

        // Test setter et getter
        qg = QG.OK;
        objetTest.setQualityGate(qg);
        assertEquals(QG.OK, objetTest.getQualityGate());
    }

    @Test
    public void testGetMatieres()
    {
        // Test sans initialisation
        assertNotNull(objetTest.getMatieres());
        assertTrue(objetTest.getMatieres().isEmpty());

        // Test setter et getter
        Set<Matiere> matieres = new HashSet<>();
        matieres.add(Matiere.ANDROID);

        objetTest.setMatieres(matieres);
        assertEquals(matieres, objetTest.getMatieres());
        
        // Test matieres null
        objetTest.setMatieres(null);
        assertNotNull(objetTest.getMatieres());
        assertTrue(objetTest.getMatieres().isEmpty());
    }

    @Test
    public void testGetGroupeProduit()
    {
        // Test sans initialisation
        assertNotNull(objetTest.getGroupeProduit());
        assertEquals(GroupeProduit.AUCUN, objetTest.getGroupeProduit());

        // Test setter et getter
        objetTest.setGroupeProduit(GroupeProduit.NPC);
        assertEquals(GroupeProduit.NPC, objetTest.getGroupeProduit());
    }

    @Test
    public void testGetDefautQualite()
    {
        // Test sans initialisation
        assertNull(objetTest.getDefaultQualite());

        // Test getter et setter
        DefautQualite dq = ModelFactory.build(DefautQualite.class);
        objetTest.setDefaultQualite(dq);
        assertEquals(dq, objetTest.getDefaultQualite());
    }

    @Test
    public void testGetEditionString()
    {
        // test valeur vide ou nulle
        assertNull(objetTest.getEditionString());

        // Test setter et getter
        String code = "Code";
        objetTest.setEditionString(code);
        assertEquals(code, objetTest.getEditionString());
    }

    @Test
    public void testGetDateRepack()
    {
        // Test sans initialisation
        assertNull(objetTest.getDateRepack());

        // Test setter et getter
        LocalDate date = LocalDate.of(2018, 10, 10);
        objetTest.setDateRepack(date);
        assertEquals(date, objetTest.getDateRepack());
    }
    
    @Test
    public void testIsRtcHS()
    {
        // Test getter et setter
        assertFalse(objetTest.isRtcHS());
        objetTest.setRtcHS(true);
        assertTrue(objetTest.isRtcHS());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
