package junit.control;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import control.ControlXML;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import junit.TestUtils;
import model.Application;
import model.InfoClarity;
import model.LotSuiviPic;
import model.ProprietesXML;
import model.FichiersXML;
import model.enums.TypeFichier;
import utilities.Statics;

@RunWith(JfxRunner.class)
public class ControlXMLTest
{
	private ControlXML handler;
	
	@Before
	public void init()
	{
		handler = new ControlXML();
	}

	@Test
	public void calculerListeApplisDepuisExcel() throws InvalidFormatException, IOException, JAXBException
	{
		handler.recupListeAppsDepuisExcel(new File("d:\\liste applis.xlsx"));
		handler.recupInfosClarityDepuisExcel(new File("d:\\Referentiel_Projets.xlsm"));
	}
	
	@Test
	@TestInJfxThread
	public void recuprerParamXML() throws InvalidFormatException, JAXBException, IOException
	{
	    handler.recupererXML(FichiersXML.class);
	    handler.recupererXML(ProprietesXML.class);
	}
	
	@Test
	public void controleDonneesParam() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		FichiersXML param = new FichiersXML();
		
		// 1. Test parametre initialisé
		StringBuilder builder = new StringBuilder();
		builder.append("Données des lots Pic manquantes.").append(Statics.NL);
		builder.append("Liste des apllications manquante.").append(Statics.NL);
		builder.append("Informations referentiel Clarity manquantes.").append(Statics.NL);
		builder.append("Merci de recharger le(s) fichier(s) de paramétrage");
	    Assert.assertEquals(builder.toString(), TestUtils.callPrivate("controleDonneesParam", handler, String.class, param));	
	    
	    // 2. Test maps remplies
	    param = new FichiersXML();
	    param.getListeApplications().add(new Application());
	    param.getMapClarity().put("key", new InfoClarity());
	    param.getLotsPic().put("key", new LotSuiviPic());	    
	    param.setDateFichier(TypeFichier.APPS);
	    param.setDateFichier(TypeFichier.CLARITY);
	    param.setDateFichier(TypeFichier.LOTSPICS);
	    builder = new StringBuilder();
	    builder.append("Lots Pics chargés. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.LOTSPICS)).append(Statics.NL);
	    builder.append("Liste des apllications chargée. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.APPS)).append(Statics.NL);
	    builder.append("Referentiel Clarity chargé. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.CLARITY)).append(Statics.NL);
	    Assert.assertEquals(builder.toString(), TestUtils.callPrivate("controleDonneesParam", handler, String.class, param));	
	}
	
	@Test
	public void recupEditionDepuisExcel() throws InvalidFormatException, IOException, JAXBException
	{
	    handler.recupEditionDepuisExcel(new File("d:\\Codification des Editions.xls"));	
	}
}