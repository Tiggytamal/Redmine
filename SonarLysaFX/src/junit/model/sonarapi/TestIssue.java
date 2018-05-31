package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static junit.TestUtils.NOTNULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.sonarapi.Commentaire;
import model.sonarapi.Composant;
import model.sonarapi.Flow;
import model.sonarapi.Issue;
import model.sonarapi.Rule;
import model.sonarapi.TextRange;
import model.sonarapi.User;

public class TestIssue
{
    /*---------- ATTRIBUTS ----------*/

    private Issue modele;
    private Issue modeleNull;

    private static final String KEY = "key";
    private static final String RESOLUTION = "resolution";
    private static final int LINE = 10;
    private static final TextRange TEXTRANGE = new TextRange();
    private static final String EFFORT = "efort";
    private static final String DEBT = "debt";
    private static final List<Commentaire> COMMENTAIRES = new ArrayList<>();
    private static final String ATTR = "attr";
    private static final List<String> TRANSITIONS = new ArrayList<>();
    private static final List<String> ACTIONS = new ArrayList<>();
    private static final String RULE = "rule";
    private static final String SEVERITY = "severity";
    private static final String COMPOSANT = "composant";
    private static final String COMPOSANTID = "composantId";
    private static final String PROJET = "projet";
    private static final String SUBPROJET = "subprojet";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String AUTHEUR = "autheur";
    private static final List<String> TAGS = new ArrayList<>();
    private static final List<Flow> FLOWS = new ArrayList<>();
    private static final String CREATIONDATE = "creationDate";
    private static final String UPDATEDATE = "updateDate";
    private static final String CLOSEDATE = "closeDate";
    private static final String TYPE = "type";
    private static final List<Composant> COMPOSANTS = new ArrayList<>();
    private static final List<Rule> RULES = new ArrayList<>();
    private static final List<User> USERS = new ArrayList<>();

    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init()
    {
        // Initialisation Lists
        COMMENTAIRES.add(new Commentaire());
        TRANSITIONS.add(KEY);
        ACTIONS.add(RESOLUTION);
        TAGS.add(EFFORT);
        FLOWS.add(new Flow());
        COMPOSANTS.add(new Composant());
        RULES.add(new Rule());
        USERS.add(new User());
        
        // création modele
        modele = new Issue(KEY, RESOLUTION, LINE, TEXTRANGE, EFFORT, DEBT, COMMENTAIRES, ATTR, TRANSITIONS, ACTIONS, RULE, SEVERITY, COMPOSANT, COMPOSANTID, PROJET, SUBPROJET, STATUS, MESSAGE,
                AUTHEUR, TAGS, FLOWS, CREATIONDATE, UPDATEDATE, CLOSEDATE, TYPE, COMPOSANTS, RULES, USERS);
        modeleNull = new Issue();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void getKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNull(modeleNull.getKey());
    }
    
    @Test
    public void setKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getKey());
    }
    
    @Test
    public void getResolution()
    {
        assertEquals(RESOLUTION, modele.getResolution());
        assertNull(modeleNull.getResolution());
    }
    
    @Test
    public void setResolution()
    {
        modele.setResolution(NEWVAL);
        assertEquals(NEWVAL, modele.getResolution());
        modeleNull.setResolution(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getResolution());
    }
    
    @Test
    public void getLine()
    {
        assertEquals(LINE, modele.getLine());
        assertEquals(0, modeleNull.getLine());
    }
    
    @Test
    public void setLine()
    {
        modele.setLine(50);
        assertEquals(50, modele.getLine());
        modeleNull.setLine(15);
        assertEquals(15, modeleNull.getLine());
    }
    
    @Test
    public void getTextRange()
    {
        assertEquals(TEXTRANGE, modele.getTextRange());
        assertNull(modeleNull.getTextRange());
    }
    
    @Test
    public void setTextRange()
    {
        modele.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modele.getTextRange());
        modeleNull.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modeleNull.getTextRange());
    }
    
    @Test
    public void getEffort()
    {
        assertEquals(EFFORT, modele.getEffort());
        assertNull(modeleNull.getEffort());
    }
    
    @Test
    public void setEffort()
    {
        modele.setEffort(NEWVAL);
        assertEquals(NEWVAL, modele.getEffort());
        modeleNull.setEffort(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getEffort());
    }
    
    @Test
    public void getDebt()
    {
        assertEquals(DEBT, modele.getDebt());
        assertNull(modeleNull.getDebt());
    }
    
    @Test
    public void setDebt()
    {
        modele.setDebt(NEWVAL);
        assertEquals(NEWVAL, modele.getDebt());
        modeleNull.setDebt(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getDebt());
    }
    
    @Test
    public void getCommentaires()
    {
        assertEquals(COMMENTAIRES, modele.getCommentaires());
        assertNotNull(modeleNull.getCommentaires());
        assertTrue(modeleNull.getCommentaires().isEmpty());
    }

    @Test
    public void setCommentaires()
    {
        modele.setCommentaires(null);
        assertNotNull(modele.getCommentaires());
        modeleNull.setCommentaires(COMMENTAIRES);
        assertEquals(COMMENTAIRES, modeleNull.getCommentaires());
    }
    
    @Test
    public void getAttr()
    {
        assertEquals(ATTR, modele.getAttr());
        assertNull(modeleNull.getAttr());
    }
    
    @Test
    public void setAttr()
    {
        modele.setAttr(NEWVAL);
        assertEquals(NEWVAL, modele.getAttr());
        modeleNull.setAttr(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getAttr());
    }
    
    @Test
    public void getTransitions()
    {
        assertEquals(TRANSITIONS, modele.getTransitions());
        assertNotNull(modeleNull.getTransitions());
        assertTrue(modeleNull.getTransitions().isEmpty());
    }

    @Test
    public void setTransitions()
    {
        modele.setTransitions(null);
        assertNotNull(modele.getTransitions());
        modeleNull.setTransitions(TRANSITIONS);
        assertEquals(TRANSITIONS, modeleNull.getTransitions());
    }
    
    @Test
    public void getActions()
    {
        assertEquals(ACTIONS, modele.getActions());
        assertNotNull(modeleNull.getActions());
        assertTrue(modeleNull.getActions().isEmpty());
    }

    @Test
    public void setActions()
    {
        modele.setActions(null);
        assertNotNull(modele.getActions());
        modeleNull.setActions(ACTIONS);
        assertEquals(ACTIONS, modeleNull.getActions());
    }
    
    @Test
    public void getRule()
    {
        assertEquals(RULE, modele.getRule());
        assertNull(modeleNull.getRule());
    }
    
    @Test
    public void setRule()
    {
        modele.setRule(NEWVAL);
        assertEquals(NEWVAL, modele.getRule());
        modeleNull.setRule(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getRule());
    }
    
    @Test
    public void getSeverity()
    {
        assertEquals(SEVERITY, modele.getSeverity());
        assertNull(modeleNull.getSeverity());
    }
    
    @Test
    public void setSeverity()
    {
        modele.setSeverity(NEWVAL);
        assertEquals(NEWVAL, modele.getSeverity());
        modeleNull.setSeverity(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getSeverity());
    }
    
    @Test
    public void getComposant()
    {
        assertEquals(COMPOSANT, modele.getComposant());
        assertNull(modeleNull.getRule());
    }
    
    @Test
    public void setComposant()
    {
        modele.setComposant(NEWVAL);
        assertEquals(NEWVAL, modele.getComposant());
        modeleNull.setComposant(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getComposant());
    }
    
    @Test
    public void getComposantId()
    {
        assertEquals(COMPOSANTID, modele.getComposantId());
        assertNull(modeleNull.getComposantId());
    }
    
    @Test
    public void setComposantId()
    {
        modele.setComposantId(NEWVAL);
        assertEquals(NEWVAL, modele.getComposantId());
        modeleNull.setComposantId(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getComposantId());
    }
    
    @Test
    public void getProjet()
    {
        assertEquals(PROJET, modele.getProjet());
        assertNull(modeleNull.getProjet());
    }
    
    @Test
    public void setProjet()
    {
        modele.setProjet(NEWVAL);
        assertEquals(NEWVAL, modele.getProjet());
        modeleNull.setProjet(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getProjet());
    }
    
    @Test
    public void getSubProject()
    {
        assertEquals(SUBPROJET, modele.getSubProject());
        assertNull(modeleNull.getSubProject());
    }
    
    @Test
    public void setSubProject()
    {
        modele.setSubProject(NEWVAL);
        assertEquals(NEWVAL, modele.getSubProject());
        modeleNull.setSubProject(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getSubProject());
    }
    
    @Test
    public void getStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNull(modeleNull.getStatus());
    }
    
    @Test
    public void setStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getStatus());
    }
    
    @Test
    public void getMessage()
    {
        assertEquals(MESSAGE, modele.getMessage());
        assertNull(modeleNull.getMessage());
    }
    
    @Test
    public void setMessage()
    {
        modele.setMessage(NEWVAL);
        assertEquals(NEWVAL, modele.getMessage());
        modeleNull.setMessage(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getMessage());
    }
    
    @Test
    public void getAutheur()
    {
        assertEquals(AUTHEUR, modele.getAutheur());
        assertNull(modeleNull.getAutheur());
    }
    
    @Test
    public void setAutheur()
    {
        modele.setAutheur(NEWVAL);
        assertEquals(NEWVAL, modele.getAutheur());
        modeleNull.setAutheur(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getAutheur());
    }
    
    @Test
    public void getTags()
    {
        assertEquals(TAGS, modele.getTags());
        assertNotNull(modeleNull.getTags());
    }

    @Test
    public void setTags()
    {
        modele.setTags(null);
        assertNotNull(modele.getTags());
        modeleNull.setTags(TAGS);
        assertEquals(TAGS, modeleNull.getTags());
    }
    
    @Test
    public void getFlows()
    {
        assertEquals(FLOWS, modele.getFlows());
        assertNotNull(modeleNull.getFlows());
        assertTrue(modeleNull.getFlows().isEmpty());
    }

    @Test
    public void setFlows()
    {
        modele.setFlows(null);
        assertNotNull(modele.getFlows());
        modeleNull.setFlows(FLOWS);
        assertEquals(FLOWS, modeleNull.getFlows());
    }
    
    @Test
    public void getCreationDate()
    {
        assertEquals(CREATIONDATE, modele.getCreationDate());
        assertNull(modeleNull.getCreationDate());
    }
    
    @Test
    public void setCreationDate()
    {
        modele.setCreationDate(NEWVAL);
        assertEquals(NEWVAL, modele.getCreationDate());
        modeleNull.setCreationDate(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getCreationDate());
    }
    
    @Test
    public void getUpdateDate()
    {
        assertEquals(UPDATEDATE, modele.getUpdateDate());
        assertNull(modeleNull.getUpdateDate());
    }
    
    @Test
    public void setUpdateDate()
    {
        modele.setUpdateDate(NEWVAL);
        assertEquals(NEWVAL, modele.getUpdateDate());
        modeleNull.setUpdateDate(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getUpdateDate());
    }
    
    @Test
    public void getCloseDate()
    {
        assertEquals(CLOSEDATE, modele.getCloseDate());
        assertNull(modeleNull.getCloseDate());
    }
    
    @Test
    public void setCloseDate()
    {
        modele.setCloseDate(NEWVAL);
        assertEquals(NEWVAL, modele.getCloseDate());
        modeleNull.setCloseDate(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getCloseDate());
    }
    
    @Test
    public void getType()
    {
        assertEquals(TYPE, modele.getType());
        assertNull(modeleNull.getType());
    }
    
    @Test
    public void setType()
    {
        modele.setType(NEWVAL);
        assertEquals(NEWVAL, modele.getType());
        modeleNull.setType(NOTNULL);
        assertEquals(NOTNULL, modeleNull.getType());
    }
    
    @Test
    public void getComposants()
    {
        assertEquals(COMPOSANTS, modele.getComposants());
        assertNotNull(modeleNull.getComposants());
        assertTrue(modeleNull.getComposants().isEmpty());
    }

    @Test
    public void setComposants()
    {
        modele.setComposants(null);
        assertNotNull(modele.getComposants());
        modeleNull.setComposants(COMPOSANTS);
        assertEquals(COMPOSANTS, modeleNull.getComposants());
    }
    
    @Test
    public void getRules()
    {
        assertEquals(RULES, modele.getRules());
        assertNotNull(modeleNull.getRules());
        assertTrue(modeleNull.getRules().isEmpty());
    }

    @Test
    public void setRules()
    {
        modele.setRules(null);
        assertNotNull(modele.getRules());
        modeleNull.setRules(RULES);
        assertEquals(RULES, modeleNull.getRules());
    }
    
    @Test
    public void getUsers()
    {
        assertEquals(USERS, modele.getUsers());
        assertNotNull(modeleNull.getUsers());
        assertTrue(modeleNull.getUsers().isEmpty());
    }

    @Test
    public void setUsers()
    {
        modele.setUsers(null);
        assertNotNull(modele.getUsers());
        modeleNull.setUsers(USERS);
        assertEquals(USERS, modeleNull.getUsers());
    }
}