package junit.model.sonarapi;

import static junit.TestUtils.NEWVAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.rest.sonarapi.Commentaire;
import model.rest.sonarapi.Composant;
import model.rest.sonarapi.Flow;
import model.rest.sonarapi.Issue;
import model.rest.sonarapi.Rule;
import model.rest.sonarapi.TextRange;
import model.rest.sonarapi.User;

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
    public void testGetKey()
    {
        assertEquals(KEY, modele.getKey());
        assertNotNull(modeleNull.getKey());
        assertTrue(modeleNull.getKey().isEmpty());
    }
    
    @Test
    public void testSetKey()
    {
        modele.setKey(NEWVAL);
        assertEquals(NEWVAL, modele.getKey());
        modeleNull.setKey(KEY);
        assertEquals(KEY, modeleNull.getKey());
    }
    
    @Test
    public void testGetResolution()
    {
        assertEquals(RESOLUTION, modele.getResolution());
        assertNotNull(modeleNull.getResolution());
        assertTrue(modeleNull.getResolution().isEmpty());
    }
    
    @Test
    public void testSetResolution()
    {
        modele.setResolution(NEWVAL);
        assertEquals(NEWVAL, modele.getResolution());
        modeleNull.setResolution(RESOLUTION);
        assertEquals(RESOLUTION, modeleNull.getResolution());
    }
    
    @Test
    public void testGetLine()
    {
        assertEquals(LINE, modele.getLine());
        assertEquals(0, modeleNull.getLine());
    }
    
    @Test
    public void testSetLine()
    {
        modele.setLine(50);
        assertEquals(50, modele.getLine());
        modeleNull.setLine(15);
        assertEquals(15, modeleNull.getLine());
    }
    
    @Test
    public void testGetTextRange()
    {
        assertEquals(TEXTRANGE, modele.getTextRange());
        assertNotNull(modeleNull.getTextRange());
    }
    
    @Test
    public void testSetTextRange()
    {
        modele.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modele.getTextRange());
        modeleNull.setTextRange(TEXTRANGE);
        assertEquals(TEXTRANGE, modeleNull.getTextRange());
    }
    
    @Test
    public void testGetEffort()
    {
        assertEquals(EFFORT, modele.getEffort());
        assertNotNull(modeleNull.getEffort());
        assertTrue(modeleNull.getEffort().isEmpty());
    }
    
    @Test
    public void testSetEffort()
    {
        modele.setEffort(NEWVAL);
        assertEquals(NEWVAL, modele.getEffort());
        modeleNull.setEffort(EFFORT);
        assertEquals(EFFORT, modeleNull.getEffort());
    }
    
    @Test
    public void testGetDebt()
    {
        assertEquals(DEBT, modele.getDebt());
        assertNotNull(modeleNull.getDebt());
        assertTrue(modeleNull.getDebt().isEmpty());
    }
    
    @Test
    public void testSetDebt()
    {
        modele.setDebt(NEWVAL);
        assertEquals(NEWVAL, modele.getDebt());
        modeleNull.setDebt(DEBT);
        assertEquals(DEBT, modeleNull.getDebt());
    }
    
    @Test
    public void testGetCommentaires()
    {
        assertEquals(COMMENTAIRES, modele.getCommentaires());
        assertNotNull(modeleNull.getCommentaires());
        assertEquals(0, modeleNull.getCommentaires().size());
    }

    @Test
    public void testSetCommentaires()
    {
        modele.setCommentaires(null);
        assertNotNull(modele.getCommentaires());
        modeleNull.setCommentaires(COMMENTAIRES);
        assertEquals(COMMENTAIRES, modeleNull.getCommentaires());
    }
    
    @Test
    public void testGetAttr()
    {
        assertEquals(ATTR, modele.getAttr());
        assertNotNull(modeleNull.getAttr());
        assertTrue(modeleNull.getAttr().isEmpty());
    }
    
    @Test
    public void testSetAttr()
    {
        modele.setAttr(NEWVAL);
        assertEquals(NEWVAL, modele.getAttr());
        modeleNull.setAttr(ATTR);
        assertEquals(ATTR, modeleNull.getAttr());
    }
    
    @Test
    public void testGetTransitions()
    {
        assertEquals(TRANSITIONS, modele.getTransitions());
        assertNotNull(modeleNull.getTransitions());
        assertEquals(0, modeleNull.getTransitions().size());
    }

    @Test
    public void testSetTransitions()
    {
        modele.setTransitions(null);
        assertNotNull(modele.getTransitions());
        modeleNull.setTransitions(TRANSITIONS);
        assertEquals(TRANSITIONS, modeleNull.getTransitions());
    }
    
    @Test
    public void testGetActions()
    {
        assertEquals(ACTIONS, modele.getActions());
        assertNotNull(modeleNull.getActions());
        assertEquals(0, modeleNull.getActions().size());
    }

    @Test
    public void testSetActions()
    {
        modele.setActions(null);
        assertNotNull(modele.getActions());
        modeleNull.setActions(ACTIONS);
        assertEquals(ACTIONS, modeleNull.getActions());
    }
    
    @Test
    public void testGetRule()
    {
        assertEquals(RULE, modele.getRule());
        assertNotNull(modeleNull.getRule());
        assertTrue(modeleNull.getRule().isEmpty());
    }
    
    @Test
    public void testSetRule()
    {
        modele.setRule(NEWVAL);
        assertEquals(NEWVAL, modele.getRule());
        modeleNull.setRule(RULE);
        assertEquals(RULE, modeleNull.getRule());
    }
    
    @Test
    public void testGetSeverity()
    {
        assertEquals(SEVERITY, modele.getSeverity());
        assertNotNull(modeleNull.getSeverity());
        assertTrue(modeleNull.getSeverity().isEmpty());
    }
    
    @Test
    public void testSetSeverity()
    {
        modele.setSeverity(NEWVAL);
        assertEquals(NEWVAL, modele.getSeverity());
        modeleNull.setSeverity(SEVERITY);
        assertEquals(SEVERITY, modeleNull.getSeverity());
    }
    
    @Test
    public void testGetComposant()
    {
        assertEquals(COMPOSANT, modele.getComposant());
        assertNotNull(modeleNull.getComposant());
        assertTrue(modeleNull.getComposant().isEmpty());
    }
    
    @Test
    public void testSetComposant()
    {
        modele.setComposant(NEWVAL);
        assertEquals(NEWVAL, modele.getComposant());
        modeleNull.setComposant(COMPOSANT);
        assertEquals(COMPOSANT, modeleNull.getComposant());
    }
    
    @Test
    public void testGetComposantId()
    {
        assertEquals(COMPOSANTID, modele.getComposantId());
        assertNotNull(modeleNull.getComposantId());
        assertTrue(modeleNull.getComposantId().isEmpty());
    }
    
    @Test
    public void testSetComposantId()
    {
        modele.setComposantId(NEWVAL);
        assertEquals(NEWVAL, modele.getComposantId());
        modeleNull.setComposantId(COMPOSANTID);
        assertEquals(COMPOSANTID, modeleNull.getComposantId());
    }
    
    @Test
    public void testGetProjet()
    {
        assertEquals(PROJET, modele.getProjet());
        assertNotNull(modeleNull.getProjet());
        assertTrue(modeleNull.getProjet().isEmpty());
    }
    
    @Test
    public void testSetProjet()
    {
        modele.setProjet(NEWVAL);
        assertEquals(NEWVAL, modele.getProjet());
        modeleNull.setProjet(PROJET);
        assertEquals(PROJET, modeleNull.getProjet());
    }
    
    @Test
    public void testGetSubProject()
    {
        assertEquals(SUBPROJET, modele.getSubProject());
        assertNotNull(modeleNull.getSubProject());
        assertTrue(modeleNull.getSubProject().isEmpty());
    }
    
    @Test
    public void testSetSubProject()
    {
        modele.setSubProject(NEWVAL);
        assertEquals(NEWVAL, modele.getSubProject());
        modeleNull.setSubProject(SUBPROJET);
        assertEquals(SUBPROJET, modeleNull.getSubProject());
    }
    
    @Test
    public void testGetStatus()
    {
        assertEquals(STATUS, modele.getStatus());
        assertNotNull(modeleNull.getStatus());
        assertTrue(modeleNull.getStatus().isEmpty());
    }
    
    @Test
    public void testSetStatus()
    {
        modele.setStatus(NEWVAL);
        assertEquals(NEWVAL, modele.getStatus());
        modeleNull.setStatus(STATUS);
        assertEquals(STATUS, modeleNull.getStatus());
    }
    
    @Test
    public void testGetMessage()
    {
        assertEquals(MESSAGE, modele.getMessage());
        assertNotNull(modeleNull.getMessage());
        assertTrue(modeleNull.getMessage().isEmpty());
    }
    
    @Test
    public void testSetMessage()
    {
        modele.setMessage(NEWVAL);
        assertEquals(NEWVAL, modele.getMessage());
        modeleNull.setMessage(MESSAGE);
        assertEquals(MESSAGE, modeleNull.getMessage());
    }
    
    @Test
    public void testGetAutheur()
    {
        assertEquals(AUTHEUR, modele.getAutheur());
        assertNotNull(modeleNull.getAutheur());
        assertTrue(modeleNull.getAutheur().isEmpty());
    }
    
    @Test
    public void testSetAutheur()
    {
        modele.setAutheur(NEWVAL);
        assertEquals(NEWVAL, modele.getAutheur());
        modeleNull.setAutheur(AUTHEUR);
        assertEquals(AUTHEUR, modeleNull.getAutheur());
    }
    
    @Test
    public void testGetTags()
    {
        assertEquals(TAGS, modele.getTags());
        assertNotNull(modeleNull.getTags());
    }

    @Test
    public void testSetTags()
    {
        modele.setTags(null);
        assertNotNull(modele.getTags());
        modeleNull.setTags(TAGS);
        assertEquals(TAGS, modeleNull.getTags());
    }
    
    @Test
    public void testGetFlows()
    {
        assertEquals(FLOWS, modele.getFlows());
        assertNotNull(modeleNull.getFlows());
        assertEquals(0, modeleNull.getFlows().size());
    }

    @Test
    public void testSetFlows()
    {
        modele.setFlows(null);
        assertNotNull(modele.getFlows());
        modeleNull.setFlows(FLOWS);
        assertEquals(FLOWS, modeleNull.getFlows());
    }
    
    @Test
    public void testGetCreationDate()
    {
        assertEquals(CREATIONDATE, modele.getCreationDate());
        assertNotNull(modeleNull.getCreationDate());
        assertTrue(modeleNull.getCreationDate().isEmpty());
    }
    
    @Test
    public void testSetCreationDate()
    {
        modele.setCreationDate(NEWVAL);
        assertEquals(NEWVAL, modele.getCreationDate());
        modeleNull.setCreationDate(CREATIONDATE);
        assertEquals(CREATIONDATE, modeleNull.getCreationDate());
    }
    
    @Test
    public void testGetUpdateDate()
    {
        assertEquals(UPDATEDATE, modele.getUpdateDate());
        assertNotNull(modeleNull.getUpdateDate());
        assertTrue(modeleNull.getUpdateDate().isEmpty());
    }
    
    @Test
    public void testSetUpdateDate()
    {
        modele.setUpdateDate(NEWVAL);
        assertEquals(NEWVAL, modele.getUpdateDate());
        modeleNull.setUpdateDate(UPDATEDATE);
        assertEquals(UPDATEDATE, modeleNull.getUpdateDate());
    }
    
    @Test
    public void testGetCloseDate()
    {
        assertEquals(CLOSEDATE, modele.getCloseDate());
        assertNotNull(modeleNull.getCloseDate());
        assertTrue(modeleNull.getCloseDate().isEmpty());
    }
    
    @Test
    public void testSetCloseDate()
    {
        modele.setCloseDate(NEWVAL);
        assertEquals(NEWVAL, modele.getCloseDate());
        modeleNull.setCloseDate(CLOSEDATE);
        assertEquals(CLOSEDATE, modeleNull.getCloseDate());
    }
    
    @Test
    public void testGetType()
    {
        assertEquals(TYPE, modele.getType());
        assertNotNull(modeleNull.getType());
        assertTrue(modeleNull.getType().isEmpty());
    }
    
    @Test
    public void testSetType()
    {
        modele.setType(NEWVAL);
        assertEquals(NEWVAL, modele.getType());
        modeleNull.setType(TYPE);
        assertEquals(TYPE, modeleNull.getType());
    }
    
    @Test
    public void testGetComposants()
    {
        assertEquals(COMPOSANTS, modele.getComposants());
        assertNotNull(modeleNull.getComposants());
        assertEquals(0, modeleNull.getComposants().size());
    }

    @Test
    public void testSetComposants()
    {
        modele.setComposants(null);
        assertNotNull(modele.getComposants());
        modeleNull.setComposants(COMPOSANTS);
        assertEquals(COMPOSANTS, modeleNull.getComposants());
    }
    
    @Test
    public void testGetRules()
    {
        assertEquals(RULES, modele.getRules());
        assertNotNull(modeleNull.getRules());
        assertEquals(0, modeleNull.getRules().size());
    }

    @Test
    public void testSetRules()
    {
        modele.setRules(null);
        assertNotNull(modele.getRules());
        modeleNull.setRules(RULES);
        assertEquals(RULES, modeleNull.getRules());
    }
    
    @Test
    public void testGetUsers()
    {
        assertEquals(USERS, modele.getUsers());
        assertNotNull(modeleNull.getUsers());
        assertEquals(0, modeleNull.getUsers().size());
    }

    @Test
    public void testSetUsers()
    {
        modele.setUsers(null);
        assertNotNull(modele.getUsers());
        modeleNull.setUsers(USERS);
        assertEquals(USERS, modeleNull.getUsers());
    }
}