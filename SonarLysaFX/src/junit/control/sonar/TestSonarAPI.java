package junit.control.sonar;

import static org.junit.Assert.assertTrue;

import java.util.Base64;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import control.sonar.SonarAPI;
import model.sonarapi.Composant;
import model.sonarapi.Vue;
import utilities.Statics;

public class TestSonarAPI
{
    private SonarAPI api;

    
    @Before
    public void init()
    {
        api = SonarAPI.INSTANCE;
    }
    
	@Test
	public void metrics()
	{
		@SuppressWarnings("unused")
		Composant composant = api.getMetriquesComposant("fr.ca.cat.cocl.ds:DS_COCL_RepriseOscare_Build:13", new String[] { "bugs", "vulnerabilities" });
	}

	@Test
	public void getVues()
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
        Statics.logger.info(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
	}
}