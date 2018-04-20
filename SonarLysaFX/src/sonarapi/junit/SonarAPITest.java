package sonarapi.junit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Vue;

public class SonarAPITest
{
    private SonarAPI api;

    
    @Before
    public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
    {
        api = SonarAPI.INSTANCE;
    }
    
	@Test
	public void metrics() throws InvalidFormatException, IOException
	{
		@SuppressWarnings("unused")
		Composant composant = api.getMetriquesComposant("fr.ca.cat.cocl.ds:DS_COCL_RepriseOscare_Build:13", new String[] { "bugs", "vulnerabilities" });
	}

	@Test
	public void getVues() throws InvalidFormatException, IOException
	{
		List<Vue> vues = api.getVues();
		assertTrue(vues != null && !vues.isEmpty());
	}
	
	@Test
	public void creerVue()
	{
		Vue vue = new Vue();
		vue.setKey("APPLI_Master_5MPR");
		vue.setName("APPLI_Master_5MPR");
		vue.setDescription("vue description");
		api.creerVue(vue);
	}
	
	@Test
	public void creerVueAsync()
	{
		Vue vue = new Vue();
		vue.setKey("bueKey");
		vue.setName("Vue Name sdfs df");
		vue.setDescription("vue description");
		api.creerVueAsync(vue);
	}
	
	@Test
	public void test()
	{
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8901,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
	}
}