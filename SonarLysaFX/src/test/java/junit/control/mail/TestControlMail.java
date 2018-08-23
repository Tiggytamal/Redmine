package junit.control.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import model.ModelFactory;
import model.enums.ParamSpec;
import model.enums.TypeInfo;
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
    }

    /*---------- METHODES PUBLIQUES ----------*/ 
    
    @Test
    public void testEnvoyer() throws Exception
    {
        // Appel de la méthode
        handler.envoyerMail(TypeMail.SUIVIJAVA);
        
        // Contrôle que les infos du mail sont bine remplies.
         Message message = ((Message) Whitebox.getField(ControlMail.class, "message").get(handler));
         assertNotEquals(0, message.getAllRecipients().length);
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
        handler.addInfo(TypeInfo.ANOABANDON, "123456", null);
        handler.addInfo(TypeInfo.ANOABANDON, "654321", "000001");
        handler.addInfo(TypeInfo.ANONEW, "234567", "infoSupp");
        handler.addInfo(TypeInfo.APPLIOBSOLETE, "345678", "application");
        handler.addInfo(TypeInfo.LOTMAJ, "123456", null);
        
        // création de la map        
        @SuppressWarnings("unchecked")
        Map<TypeInfo, List<InfoMail>> mapInfos = (Map<TypeInfo, List<InfoMail>>) Whitebox.getField(ControlMail.class, "mapInfos").get(handler);
        
        // Contrôle
        assertFalse(mapInfos.isEmpty());
        assertFalse(mapInfos.get(TypeInfo.ANOABANDON).isEmpty());
        assertEquals(ModelFactory.getModelWithParams(InfoMail.class, "123456", null), mapInfos.get(TypeInfo.ANOABANDON).get(0));
        assertEquals(ModelFactory.getModelWithParams(InfoMail.class, "654321", "000001"), mapInfos.get(TypeInfo.ANOABANDON).get(1));
        assertFalse(mapInfos.get(TypeInfo.ANONEW).isEmpty());
        assertEquals(ModelFactory.getModelWithParams(InfoMail.class, "234567", "infoSupp"), mapInfos.get(TypeInfo.ANONEW).get(0));
        assertFalse(mapInfos.get(TypeInfo.APPLIOBSOLETE).isEmpty());
        assertEquals(ModelFactory.getModelWithParams(InfoMail.class, "345678", "application"), mapInfos.get(TypeInfo.APPLIOBSOLETE).get(0));
        assertFalse(mapInfos.get(TypeInfo.LOTMAJ).isEmpty());
        assertEquals(ModelFactory.getModelWithParams(InfoMail.class, "123456", null), mapInfos.get(TypeInfo.LOTMAJ).get(0));
    }
    
    @Test
    public void testCreerTexte() throws Exception
    {
        // Initialisation
        handler.addInfo(TypeInfo.ANOABANDON, "123456", null);
        handler.addInfo(TypeInfo.ANOABANDON, "654321", "000001");
        handler.addInfo(TypeInfo.ANONEW, "234567", "infoSupp");
        handler.addInfo(TypeInfo.APPLIOBSOLETE, "345678", "application");
        handler.addInfo(TypeInfo.LOTMAJ, "123456", null);
        
        // Appel méthode
        String messageMail = Whitebox.invokeMethod(handler, "creerTexte", "debut");
        
        assertTrue(messageMail.contains(TypeInfo.ANOABANDON.getTitre()));
        assertTrue(messageMail.contains(TypeInfo.ANONEW.getTitre()));
        assertTrue(messageMail.contains(TypeInfo.APPLIOBSOLETE.getTitre()));
        assertTrue(messageMail.contains(TypeInfo.LOTMAJ.getTitre()));
        assertTrue(messageMail.contains(Statics.TIRET + "123456"));
        assertTrue(messageMail.contains(Statics.TIRET + "654321" + TypeInfo.ANOABANDON.getLiens() + "000001"));
        assertTrue(messageMail.contains(Statics.TIRET + "234567"));
        assertFalse(messageMail.contains(Statics.TIRET + "234567" + TypeInfo.ANONEW.getLiens() + "infoSupp"));
        assertTrue(messageMail.contains(Statics.TIRET + "345678" + TypeInfo.APPLIOBSOLETE.getLiens() + "application"));
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