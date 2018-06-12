package junit.model.sonarapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void getComponent()
    {
        assertEquals(COMPOSANT, modele.getComponent());
        assertNull(modeleNull.getComponent());
    }
    
    @Test
    public void getListeVues()
    {
        assertEquals(LISTEVUES, modele.getListeVues());
        assertTrue(modele.getListeVues() != null);
        assertFalse(modele.getListeVues().isEmpty());
        assertNull(modeleNull.getListeVues());
    }
    
    @Test
    public void getStatusProjet()
    {
        assertEquals(STATUSPROJET, modele.getStatusProjet());
        assertNull(modeleNull.getStatusProjet());
    }
    
    @Test
    public void getResults()
    {
        assertEquals(RESULTS, modele.getResults());
        assertTrue(modele.getResults() != null);
        assertFalse(modele.getResults().isEmpty());
        assertNull(modeleNull.getResults());
    }
    
    @Test
    public void isMore()
    {
        assertEquals(MORE, modele.isMore());
        assertFalse(modeleNull.isMore());
    }
    
    @Test
    public void getErrors()
    {
        assertEquals(ERRORS, modele.getErrors());
        assertTrue(modele.getErrors() != null);
        assertFalse(modele.getErrors().isEmpty());
        assertNull(modeleNull.getErrors());
    }
    
    @Test
    public void getQualityGates()
    {
        assertEquals(QGS, modele.getQualityGates());
        assertTrue(modele.getQualityGates() != null);
        assertFalse(modele.getQualityGates().isEmpty());
        assertNull(modeleNull.getQualityGates());
    }
    
    @Test
    public void getDefaut()
    {
        assertEquals(DEFAUT, modele.getDefaut());
        assertNull(modeleNull.getDefaut());
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
