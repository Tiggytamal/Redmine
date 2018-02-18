package junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestUtils
{
	private TestUtils() {}
	
	/**
	 * Permet d'appeler une m�thode priv�e d'une classe.<br>
	 * le {@code nomMethode} est le nom de la m�thode que l'on veut appeler. {@code instance} correspond � l'instance de la classe que l'on veut utiliser. {@code params} est un array de tous les param�tres de la m�thode
	 * @param nomMethode
	 * @param instance
	 * @param params
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void callPrivate (String nomMethode, Object instance, Class<?>... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Class<?>[] classPrams = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++)
		{
			classPrams[i] = params[i].getClass();
		}
		Method call = instance.getClass().getDeclaredMethod(nomMethode, classPrams);
		call.setAccessible(true);
		call.invoke(instance, (Object[]) params);
	}
}