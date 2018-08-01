package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeMail;

public class TestTypeMail implements TestEnums
{

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeMail.PURGESONAR, TypeMail.valueOf(TypeMail.PURGESONAR.toString()));    
    }

    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, TypeMail.values().length);    
    }
    
    @Test
    public void testGetTitre()
    {
        assertEquals("JAVA - Rapport MAJ fichier Qualim�trie du ", TypeMail.SUIVIJAVA.getTitre());
        assertEquals("DATASTAGE - Rapport MAJ fichier Qualim�trie du ", TypeMail.SUIVIDATASTAGE.getTitre());
        assertEquals("COBOL - Rapport MAJ fichier Qualim�trie JAVA du ", TypeMail.SUIVICOBOL.getTitre());
        assertEquals("Rapport Cr�ation vues par Applications du ", TypeMail.VUEAPPS.getTitre());
        assertEquals("Rapport Purge composants SonarQube du ", TypeMail.PURGESONAR.getTitre());
    }
    
    @Test
    public void testGetDebut()
    {
        assertEquals("Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies JAVA du ", TypeMail.SUIVIJAVA.getDebut());
        assertEquals("Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies DATASTAGE du ", TypeMail.SUIVIDATASTAGE.getDebut());
        assertEquals("Bonjour,\nVoici un r�sum� du traitement journalier du fichier de suivi des anomalies COBOL du ", TypeMail.SUIVICOBOL.getDebut());
        assertEquals("Bonjour,\nVoici un r�sum� du traitement de la cr�ation des vues par application du ", TypeMail.VUEAPPS.getDebut());
        assertEquals("Bonjour,\nVoici la liste des composants purg�s du ", TypeMail.PURGESONAR.getDebut());
    }

}
