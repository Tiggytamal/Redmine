package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * Classe de m�thode utilitaires statiques
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Utilities
{
    private Utilities() {}

    /**
     * Gets the base location of the given class.
     * <p>
     * If the class is directly on the file system (e.g.,
     * "/path/to/my/package/MyClass.class") then it will return the base directory
     * (e.g., "file:/path/to").
     * </p>
     * <p>
     * If the class is within a JAR file (e.g.,
     * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
     * path to the JAR (e.g., "file:/path/to/my-jar.jar").
     * </p>
     *
     * @param c
     *            The class whose location is desired.
     * @see FileUtils#urlToFile(URL) to convert the result to a {@link File}.
     */
    public static URL getLocation(final Class<?> c)
    {
        if (c == null)
        {
        	return null; // could not load the class
        }

        // try the easy way first
        try
        {
            final URL codeSourceLocation = c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null)
            {
            	return codeSourceLocation;
            }
        }
        catch (SecurityException | NullPointerException e)
        {
            // NB: Cannot access protection domain.
        }

        // NB: The easy way failed, so we try the hard way. We ask for the class
        // itself as a resource, then strip the class's path from the URL string,
        // leaving the base path.

        // get the class's raw resource path
        final URL classResource = c.getResource(c.getSimpleName() + ".class");
        if (classResource == null)
            return null;

        final String url = classResource.toString();
        final String suffix = c.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix))
            return null;

        // strip the class's path from the URL string
        final String base = url.substring(0, url.length() - suffix.length());

        String path = base;

        // remove the "jar:" prefix and "!/" suffix, if present
        if (path.startsWith("jar:"))
        {
        	path = path.substring(4, path.length() - 2);
        }

        try
        {
            return new URL(path);
        }
        catch (final MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the given {@link URL} to its corresponding {@link File}.
     * <p>
     * This method is similar to calling {@code new File(url.toURI())} except that
     * it also handles "jar:file:" URLs, returning the path to the JAR file.
     * </p>
     * 
     * @param url
     *            The URL to convert.
     * @return A file path suitable for use with e.g. {@link FileInputStream}
     * @throws IllegalArgumentException
     *             if the URL does not correspond to a file.
     */
    public static File urlToFile(final URL url)
    {
        return url == null ? null : urlToFile(url.toString());
    }

    /**
     * Converts the given URL string to its corresponding {@link File}.
     * 
     * @param url
     *            The URL to convert.
     * @return A file path suitable for use with e.g. {@link FileInputStream}
     * @throws IllegalArgumentException
     *             if the URL does not correspond to a file.
     */
    public static File urlToFile(final String url)
    {
        String path = url;
        if (path.startsWith("jar:"))
        {
            // remove "jar:" prefix and "!/" suffix
            int index = path.indexOf("!/");
            path = path.substring(4, index);
        }
        try
        {
            if (osName().startsWith("Win") && path.matches("file:[A-Za-z]:.*"))
            {
                path = "file:/" + path.substring(5);
            }
            return new File(new URL(path).toURI());
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            // NB: URL is not completely well-formed.
        }

        if (path.startsWith("file:"))
        {
            // pass through the URL as-is, minus "file:" prefix
            path = path.substring(5);
            return new File(path);
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }

    /** Gets the name of the operating system. */
    public static String osName()
    {
        String osName = System.getProperty("os.name");
        return osName == null ? "Unknown" : osName;
    }
}
