package junit.model.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.enums.TypeRapport;

public class TestTypeRapport implements TestEnums
{
    @Test
    @Override
    public void testSize()
    {
        assertEquals(5, TypeRapport.values().length);
    }

    @Test
    @Override
    public void testConstructeur()
    {
        assertEquals(TypeRapport.SUIVIJAVA, TypeRapport.valueOf(TypeRapport.SUIVIJAVA.toString()));
    }
    
    @Test
    public void testGetDebut()
    {
        assertEquals("R�sum� du traitement de suivi des anomalies JAVA", TypeRapport.SUIVIJAVA.getDebut());
        assertEquals("R�sum� du traitement de suivi des anomalies DATASTAGE", TypeRapport.SUIVIDATASTAGE.getDebut());
        assertEquals("R�sum� du traitement de suivi des anomalies COBOL", TypeRapport.SUIVICOBOL.getDebut());
        assertEquals("R�sum� du traitement de la cr�ation des vues par application", TypeRapport.VUEAPPS.getDebut());
        assertEquals("Liste des composants purg�s", TypeRapport.PURGESONAR.getDebut());
    }
    
    @Test
    public void testGetNomCol()
    {
        assertEquals("rapport JAVA - ", TypeRapport.SUIVIJAVA.getNomFichier());
        assertEquals("rapport DATASTAGE - ", TypeRapport.SUIVIDATASTAGE.getNomFichier());
        assertEquals("rapport COBOL - ", TypeRapport.SUIVICOBOL.getNomFichier());
        assertEquals("rapport Vues Applications - ", TypeRapport.VUEAPPS.getNomFichier());
        assertEquals("rapport Purge Sonar - ", TypeRapport.PURGESONAR.getNomFichier());
    }
}
