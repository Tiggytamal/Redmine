package junit.control.word;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.word.ControlRapport;
import junit.JunitBase;
import junit.TestUtils;
import model.InfoMail;
import model.ModelFactory;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import utilities.Statics;

public class TestControlRapport extends JunitBase
{
    /*---------- ATTRIBUTS ----------*/
    
    private ControlRapport handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @Override
    public void init() throws Exception
    {
        handler = new ControlRapport(TypeRapport.SUIVIJAVA);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Test
    public void testCreerRapport()
    {       
        // Création logger
        Logger logger = TestUtils.getMockLogger(ControlRapport.class, "LOGGER");
        
        // Appel méthode
        handler.creerFichier();
        
        // Contrôle bon appel du logger
        Mockito.verify(logger, Mockito.times(1)).info("Création du rapport OK.");
        
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
        Map<TypeInfo, List<InfoMail>> mapInfos = (Map<TypeInfo, List<InfoMail>>) Whitebox.getField(ControlRapport.class, "mapInfos").get(handler);
        
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
        Whitebox.invokeMethod(handler, "creerTexte");
        
        // Récupération document pour itération sur les paragraphs
        XWPFDocument document = (XWPFDocument) Whitebox.getField(ControlRapport.class, "document").get(handler);
        Iterator<XWPFParagraph> iter = document.getParagraphsIterator();
        StringBuilder texteBuilder = new StringBuilder();
        if (iter.hasNext())
        {
            XWPFParagraph paragraph = iter.next();
            texteBuilder.append(paragraph.getParagraphText());
        }
        String texte = texteBuilder.toString();
        
        // Test des données
        assertTrue(texte.contains(TypeInfo.ANOABANDON.getTitre()));
        assertTrue(texte.contains(TypeInfo.ANONEW.getTitre()));
        assertTrue(texte.contains(TypeInfo.APPLIOBSOLETE.getTitre()));
        assertTrue(texte.contains(TypeInfo.LOTMAJ.getTitre()));
        assertTrue(texte.contains(Statics.TIRET + "123456"));
        assertTrue(texte.contains(Statics.TIRET + "654321" + TypeInfo.ANOABANDON.getLiens() + "000001"));
        assertTrue(texte.contains(Statics.TIRET + "234567"));
        assertFalse(texte.contains(Statics.TIRET + "234567" + TypeInfo.ANONEW.getLiens() + "infoSupp"));
        assertTrue(texte.contains(Statics.TIRET + "345678" + TypeInfo.APPLIOBSOLETE.getLiens() + "application"));
        assertTrue(texte.contains(Statics.TIRET + "123456"));        
    }
    
    @Test
    public void testAddExtra() throws IllegalAccessException
    {
        //Test avec extra vide
        String extra = (String) Whitebox.getField(ControlRapport.class, "extra").get(handler);
        assertNotNull(extra);
        assertTrue(extra.isEmpty());
        
        // Initialisation
        String extraTest = "extra a la fin du rapport";
        handler.addExtra(extraTest); 
        extra = (String) Whitebox.getField(ControlRapport.class, "extra").get(handler);
        assertNotNull(extra);
        assertFalse(extra.isEmpty());
        assertEquals(extraTest, extra);
        
    }
    
    @Test
    public void testWriteException() throws Exception
    {
        // Mock logger et document pour créer l'exception à l'écriture du fichier
        Logger logger = TestUtils.getMockLogger(ControlRapport.class, "LOGPLANTAGE");
        XWPFDocument document = Mockito.mock(XWPFDocument.class);
        Whitebox.getField(ControlRapport.class, "document").set(handler, document);

        Whitebox.invokeMethod(handler, "write");
        Mockito.verify(logger, Mockito.times(1)).error(Mockito.any(IOException.class));
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
