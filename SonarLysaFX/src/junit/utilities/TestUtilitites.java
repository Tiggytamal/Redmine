package junit.utilities;



import org.junit.Assert;
import org.junit.Test;

import utilities.Utilities;


public class TestUtilitites
{

	@Test
	public void testTranscoEdition()
	{
		// Test envoi objet null
		Assert.assertNull(Utilities.transcoVersion(null));
		// Test envoi version de 10 à 18
		for (int i = 10; i < 18; i++)
		{
			String result = Utilities.transcoVersion(String.valueOf(i));
			Assert.assertEquals("E" + (i+17), result);
		}
	}
	
	@Test (expected = NumberFormatException.class)
	public void testTranscoEditionException()
	{
		Utilities.transcoVersion("a");
	}
}
