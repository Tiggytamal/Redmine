package junit.control.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import control.mail.ControlMail;
import control.rtc.ControlRTC;
import junit.JunitBase;
import junit.TestUtils;
import model.InfoMail;
import model.enums.ParamSpec;
import model.enums.TypeInfoMail;
import model.enums.TypeMail;
import utilities.Statics;
import utilities.TechnicalException;

public class TestControlMail extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    private ControlMail handler;
    
    /*---------- CONSTRUCTEURS ----------*/

    @Before
    public void init() throws Exception
    {
        // Connexion RTC et instanciation controleur
        ControlRTC.INSTANCE.connexion();       
        handler = new ControlMail();
        
        // Mock pour éviter le départ des mails tout en testant tout le reste.
        handler = PowerMockito.spy(handler);      
        PowerMockito.doNothing().when(handler, "transportMail");
    }

    /*---------- METHODES PUBLIQUES ----------*/ 
    
    @Test
    public void testEnvoyer() throws Exception
    {
        // Appel de la méthode
        handler.envoyerMail(TypeMail.SUIVIJAVA);
        
        // Contrôle que les infos du mail sont bine remplies.
         Message message = ((Message) Whitebox.getField(ControlMail.class, "message").get(handler));
         assertTrue(message.getAllRecipients().length > 0);
         assertTrue(message.getSubject().contains(TypeMail.SUIVIJAVA.getTitre()));
         assertNotNull(message.getFrom());
    }
    
    @Test
    public void testEnvoyerMailException() throws Exception
    {
        // Initialisation des mock et préparation de l'exception
        Logger mockLogger = TestUtils.getMockLogger(ControlMail.class, "LOGGER");
        Logger mockPlantage = TestUtils.getMockLogger(ControlMail.class, "LOGPLANTAGE");
        MessagingException e = new MessagingException();
        PowerMockito.doThrow(e).when(handler, "transportMail");
        
        // Appel de la méthode
        handler.envoyerMail(TypeMail.SUIVIJAVA);
        
        // Contrôle des logs
        Mockito.verify(mockPlantage, Mockito.times(1)).error(e);
        Mockito.verify(mockLogger, Mockito.times(1)).error("Erreur lors de la création du mail. Voir log plantage");
    }
    
    @Test
    public void testInitMail() throws IllegalArgumentException, IllegalAccessException
    {
        // Ici on va tester si on trouve le même nombre de personne à qui on envoit le mail que dans les paramètres
       int nbreRec = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESMAIL).split(";").length;
       
       int nbreadr = ((String)Whitebox.getField(ControlMail.class, "adressesEnvoi").get(handler)).split(",").length;
       
       assertEquals(nbreRec + 1, nbreadr);
    }
    
    @Test
    public void testAddInfo() throws IllegalArgumentException, IllegalAccessException
    {
        // Initialisation test
        handler.addInfo(TypeInfoMail.ANOABANDON, "123456", null);
        handler.addInfo(TypeInfoMail.ANOABANDON, "654321", "000001");
        handler.addInfo(TypeInfoMail.ANONEW, "234567", "infoSupp");
        handler.addInfo(TypeInfoMail.APPLIOBSOLETE, "345678", "application");
        handler.addInfo(TypeInfoMail.LOTMAJ, "123456", null);
        
        // création de la map        
        @SuppressWarnings("unchecked")
        Map<TypeInfoMail, List<InfoMail>> mapInfos = (Map<TypeInfoMail, List<InfoMail>>) Whitebox.getField(ControlMail.class, "mapInfos").get(handler);
        
        // Contrôle
        assertFalse(mapInfos.isEmpty());
        assertFalse(mapInfos.get(TypeInfoMail.ANOABANDON).isEmpty());
        assertEquals(new InfoMail("123456", null), mapInfos.get(TypeInfoMail.ANOABANDON).get(0));
        assertEquals(new InfoMail("654321", "000001"), mapInfos.get(TypeInfoMail.ANOABANDON).get(1));
        assertFalse(mapInfos.get(TypeInfoMail.ANONEW).isEmpty());
        assertEquals(new InfoMail("234567", "infoSupp"), mapInfos.get(TypeInfoMail.ANONEW).get(0));
        assertFalse(mapInfos.get(TypeInfoMail.APPLIOBSOLETE).isEmpty());
        assertEquals(new InfoMail("345678", "application"), mapInfos.get(TypeInfoMail.APPLIOBSOLETE).get(0));
        assertFalse(mapInfos.get(TypeInfoMail.LOTMAJ).isEmpty());
        assertEquals(new InfoMail("123456", null), mapInfos.get(TypeInfoMail.LOTMAJ).get(0));
    }
    
    @Test
    public void testCreerTexte() throws Exception
    {
        // Initialisation
        handler.addInfo(TypeInfoMail.ANOABANDON, "123456", null);
        handler.addInfo(TypeInfoMail.ANOABANDON, "654321", "000001");
        handler.addInfo(TypeInfoMail.ANONEW, "234567", "infoSupp");
        handler.addInfo(TypeInfoMail.APPLIOBSOLETE, "345678", "application");
        handler.addInfo(TypeInfoMail.LOTMAJ, "123456", null);
        
        // Appel méthode
        String messageMail = Whitebox.invokeMethod(handler, "creerTexte", "debut");
        
        assertTrue(messageMail.contains(TypeInfoMail.ANOABANDON.getTitre()));
        assertTrue(messageMail.contains(TypeInfoMail.ANONEW.getTitre()));
        assertTrue(messageMail.contains(TypeInfoMail.APPLIOBSOLETE.getTitre()));
        assertTrue(messageMail.contains(TypeInfoMail.LOTMAJ.getTitre()));
        assertTrue(messageMail.contains(Statics.TIRET + "123456"));
        assertTrue(messageMail.contains(Statics.TIRET + "654321" + TypeInfoMail.ANOABANDON.getLiens() + "000001"));
        assertTrue(messageMail.contains(Statics.TIRET + "234567"));
        assertFalse(messageMail.contains(Statics.TIRET + "234567" + TypeInfoMail.ANONEW.getLiens() + "infoSupp"));
        assertTrue(messageMail.contains(Statics.TIRET + "345678" + TypeInfoMail.APPLIOBSOLETE.getLiens() + "application"));
        assertTrue(messageMail.contains(Statics.TIRET + "123456"));        
    }
    
    @Test
    public void testAddExtra()
    {
        // Initialisation
        String extra = "extratextealafindumail";
        handler.addExtra(extra);
        handler.envoyerMail(TypeMail.SUIVIJAVA);
        
        // Test que la valeur extra est bien utilisée et à la fin du mail.
        assertTrue(Pattern.compile(extra + "$").matcher(getTexteMail()).find());        
    }
    
    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Récupère le texte complet du mail à envoyer.
     * @return
     * @throws MessagingException 
     * @throws IOException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    private String getTexteMail()
    {
        try
        {
            return ((Message) Whitebox.getField(ControlMail.class, "message").get(handler)).getContent().toString();
        } catch (IllegalArgumentException | IllegalAccessException | IOException | MessagingException e)
        {
            throw new TechnicalException("Erreur récupération message mail. Test unitaire : junit.controlMail.TestControlMail.getTexteMail", e);
        }
    }
    
    /*---------- ACCESSEURS ----------*/
}