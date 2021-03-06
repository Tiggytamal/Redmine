package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Composant;
import model.rest.sonarapi.Message;
import model.rest.sonarapi.QualityProfile;
import model.rest.sonarapi.Retour;
import model.rest.sonarapi.StatusProjet;
import model.rest.sonarapi.Vue;

public class TestRetour
{
    /*---------- ATTRIBUTS ----------*/

    private Retour modele;
    private Retour modeleNull;
    private static final Composant COMPOSANT = new Composant();
    private static final List<Vue> LISTEVUES = new ArrayList<>();
    private static final StatusProjet STATUSPROJET = new StatusProjet();
    private static final List<Vue> RESULTS = new ArrayList<>();
    private static final boolean MORE = true;
    private static final List<Message> ERRORS = new ArrayList<>();
    private static final List<QualityProfile> QGS = new ArrayList<>();
    private static final String DEFAUT = "DEFAULT";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        LISTEVUES.add(new Vue("key", "val1"));
        RESULTS.add(new Vue("key", "val2"));
        ERRORS.add(new Message("message"));
        QGS.add(new QualityProfile("id", "name"));
        modele = new Retour(COMPOSANT, LISTEVUES, STATUSPROJET, RESULTS, MORE, ERRORS, QGS, DEFAUT);
        modeleNull = new Retour();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetComponent()
    {
        assertEquals(COMPOSANT, modele.getComponent());
        assertNotNull(modeleNull.getComponent());
    }
    
    @Test
    public void testGetListeVues()
    {
        assertEquals(LISTEVUES, modele.getListeVues());
        assertNotNull(modele.getListeVues());
        assertFalse(modele.getListeVues().isEmpty());
        assertNotNull(modeleNull.getListeVues());
        assertTrue(modeleNull.getListeVues().isEmpty());
    }
    
    @Test
    public void testGetStatusProjet()
    {
        assertEquals(STATUSPROJET, modele.getStatusProjet());
        assertNotNull(modeleNull.getStatusProjet());
    }
    
    @Test
    public void testGetResults()
    {
        assertEquals(RESULTS, modele.getResults());
        assertNotNull(modele.getResults());
        assertFalse(modele.getResults().isEmpty());
        assertNotNull(modeleNull.getResults());
        assertTrue(modeleNull.getResults().isEmpty());
    }
    
    @Test
    public void testIsMore()
    {
        assertEquals(MORE, modele.isMore());
        assertFalse(modeleNull.isMore());
    }
    
    @Test
    public void testGetErrors()
    {
        assertEquals(ERRORS, modele.getErrors());
        assertNotNull(modele.getErrors());
        assertFalse(modele.getErrors().isEmpty());
        assertNotNull(modeleNull.getErrors());
        assertTrue(modeleNull.getErrors().isEmpty());
    }
    
    @Test
    public void testGetQualityGates()
    {
        assertEquals(QGS, modele.getQualityGates());
        assertNotNull(modele.getQualityGates());
        assertFalse(modele.getQualityGates().isEmpty());
        assertNotNull(modeleNull.getQualityGates());
        assertTrue(modeleNull.getQualityGates().isEmpty());
    }
    
    @Test
    public void testGetDefaut()
    {
        assertEquals(DEFAUT, modele.getDefaut());
        assertNotNull(modeleNull.getDefaut());
        assertTrue(modeleNull.getDefaut().isEmpty());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
