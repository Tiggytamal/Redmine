package junit.model.bdd;

import static org.junit.Assert.assertEquals;
import static utilities.Statics.DATEINCO2099;
import static utilities.Statics.DATEINCONNUE;
import static utilities.Statics.EDINCONNUE;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import junit.model.AbstractTestModel;
import model.bdd.Edition;
import model.enums.TypeEdition;
import utilities.Statics;

public class TestEdition extends AbstractTestModel<Edition>
{
    /*---------- ATTRIBUTS ----------*/

    private static final String NOM = "nom";

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testBuild()
    {
        objetTest = Edition.build(NOM, "numero");
        assertEquals(NOM, objetTest.getNom());
        assertEquals("numero", objetTest.getNumero());
    }

    @Test
    public void testGetEditionInconnue()
    {
        // Test normale
        objetTest = Edition.getEditionInconnue("edition");
        assertEquals(TypeEdition.INCONNU, objetTest.getTypeEdition());
        assertEquals(Statics.DATEINCO2099, objetTest.getDateMEP());
        assertEquals("Inconnue dans la codification des Editions", objetTest.getCommentaire());
        assertEquals(EDINCONNUE, objetTest.getNumero());
        assertEquals("edition", objetTest.getNom());

        // Test editoin nulle ou vide
        objetTest = Edition.getEditionInconnue(null);
        assertEquals(TypeEdition.INCONNU, objetTest.getTypeEdition());
        assertEquals(Statics.DATEINCO2099, objetTest.getDateMEP());
        assertEquals("Inconnue dans la codification des Editions", objetTest.getCommentaire());
        assertEquals(EDINCONNUE, objetTest.getNumero());
        assertEquals(EDINCONNUE, objetTest.getNom());

        objetTest = Edition.getEditionInconnue(Statics.EMPTY);
        assertEquals(TypeEdition.INCONNU, objetTest.getTypeEdition());
        assertEquals(Statics.DATEINCO2099, objetTest.getDateMEP());
        assertEquals("Inconnue dans la codification des Editions", objetTest.getCommentaire());
        assertEquals(EDINCONNUE, objetTest.getNumero());
        assertEquals(EDINCONNUE, objetTest.getNom());

    }

    @Test
    public void testGetMapIndex()
    {
        objetTest.setNom(NOM);
        assertEquals(objetTest.getNom(), objetTest.getMapIndex());
    }

    @Test
    public void testUpdate()
    {
        // Préparation objet
        objetTest.setNumero("00.01.02.03");
        objetTest.setNom(NOM);
        Edition update = Edition.getEditionInconnue("test");

        // Test mise à jour
        objetTest.update(update);
        assertEquals(objetTest.getNom(), update.getNom());
        assertEquals(objetTest.getCommentaire(), update.getCommentaire());
        assertEquals(objetTest.getTypeEdition(), update.getTypeEdition());
        assertEquals(objetTest.getDateMEP(), update.getDateMEP());
    }

    @Test
    public void testGetNom()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNom());

        objetTest.setNom(NOM);
        assertEquals(NOM, objetTest.getNom());
    }

    @Test
    public void testGetNumero()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getNumero());

        // Test setter et getter avec contrôle sur le set

        // string non correcte
        String string = "etat";
        objetTest.setNumero(string);
        assertEquals(EMPTY, objetTest.getNumero());

        // string null
        string = null;
        objetTest.setNumero(string);
        assertEquals(EMPTY, objetTest.getNumero());

        // string empty
        string = EMPTY;
        objetTest.setNumero(string);
        assertEquals(EMPTY, objetTest.getNumero());

        // string correct
        string = "00.02.04.06";
        objetTest.setNumero(string);
        assertEquals(string, objetTest.getNumero());
    }

    @Test
    public void testGetCommentaire()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, objetTest.getCommentaire());

        // Test setter et getter
        String string = "comm";
        objetTest.setCommentaire(string);
        assertEquals(string, objetTest.getCommentaire());
    }

    @Test
    public void testGetDateMEP()
    {
        // test sans initialisation
        assertEquals(DATEINCO2099, objetTest.getDateMEP());

        // Test setter et getter
        objetTest.setDateMEP(DATEINCONNUE);
        assertEquals(DATEINCONNUE, objetTest.getDateMEP());

        // Test protection null
        objetTest.setDateMEP(null);
        assertEquals(DATEINCONNUE, objetTest.getDateMEP());
    }

    @Test
    public void testGetTypeEdition()
    {
        // test sans initialisation
        assertEquals(TypeEdition.INCONNU, objetTest.getTypeEdition());

        // Test setter et getter
        objetTest.setTypeEdition(TypeEdition.AUTRE);
        assertEquals(TypeEdition.AUTRE, objetTest.getTypeEdition());

        // Test protection null
        objetTest.setTypeEdition(null);
        assertEquals(TypeEdition.AUTRE, objetTest.getTypeEdition());
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
