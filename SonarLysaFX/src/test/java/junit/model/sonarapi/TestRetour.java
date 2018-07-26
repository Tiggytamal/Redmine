package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Composant;
import model.sonarapi.Message;
import model.sonarapi.QualityGate;
import model.sonarapi.Retour;
import model.sonarapi.StatusProjet;
import model.sonarapi.Vue;

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
    private static final List<QualityGate> QGS = new ArrayList<>();
    private static final String DEFAUT = "DEFAULT";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Before
    public void init()
    {
        LISTEVUES.add(new Vue("key", "val1"));
        RESULTS.add(new Vue("key", "val2"));
        ERRORS.add(new Message("message"));
        QGS.add(new QualityGate("id", "name"));
        modele = new Retour(COMPOSANT, LISTEVUES, STATUSPROJET, RESULTS, MORE, ERRORS, QGS, DEFAUT);
        modeleNull = new Retour();
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testGetComponent()
    {
        assertEquals(COMPOSANT, modele.getComponent());
        assertNull(modeleNull.getComponent());
    }
    
    @Test
    public void testGetListeVues()
    {
        assertEquals(LISTEVUES, modele.getListeVues());
        assertNotNull(modele.getListeVues());
        assertFalse(modele.getListeVues().isEmpty());
        assertNull(modeleNull.getListeVues());
    }
    
    @Test
    public void testGetStatusProjet()
    {
        assertEquals(STATUSPROJET, modele.getStatusProjet());
        assertNull(modeleNull.getStatusProjet());
    }
    
    @Test
    public void testGetResults()
    {
        assertEquals(RESULTS, modele.getResults());
        assertNotNull(modele.getResults());
        assertFalse(modele.getResults().isEmpty());
        assertNull(modeleNull.getResults());
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
        assertNull(modeleNull.getErrors());
    }
    
    @Test
    public void testGetQualityGates()
    {
        assertEquals(QGS, modele.getQualityGates());
        assertNotNull(modele.getQualityGates());
        assertFalse(modele.getQualityGates().isEmpty());
        assertNull(modeleNull.getQualityGates());
    }
    
    @Test
    public void testGetDefaut()
    {
        assertEquals(DEFAUT, modele.getDefaut());
        assertNull(modeleNull.getDefaut());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
