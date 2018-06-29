package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Supplier;

import utilities.enums.Severity;

/**
 * Classe de méthode utilitaires statiques
 *
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Utilities
{   
    private Utilities()
    {
    }

    /**
     * Gets the base location of the given class.
     * <p>
     * If the class is directly on the file system (e.g., "/path/to/my/package/MyClass.class") then it will return the base directory (e.g., "file:/path/to").
     * </p>
     * <p>
     * If the class is within a JAR file (e.g., "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the path to the JAR (e.g.,
     * "file:/path/to/my-jar.jar").
     * </p>
     *
     * @param c
     *            The class whose location is desired.
     */
    public static URL getLocation(final Class<?> c)
    {
        // could not load the class
        if (c == null)
            return null;

        // try the easy way first
        try
        {
            final URL codeSourceLocation = c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null)
            {
                return codeSourceLocation;
            }
        } catch (final SecurityException e)
        {
            Statics.LOGPLANTAGE.error(e);
        }

        // NB: The easy way failed, so we try the hard way. We ask for the class
        // itself as a resource, then strip the class's path from the URL string,
        // leaving the base path.

        // get the class's raw resource path
        final URL classResource = c.getResource(c.getSimpleName() + ".class");

        // cannot find class resource
        if (classResource == null)
        return null;

        final String url = classResource.toString();
        final String suffix = c.getCanonicalName().replace('.', '/') + ".class";

        // weird URL
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
        } catch (final MalformedURLException e)
        {
            return null;
        }
    }

    /**
     * Converts the given {@link URL} to its corresponding {@link File}.
     * <p>
     * This method is similar to calling {@code new File(url.toURI())} except that it also handles "jar:file:" URLs, returning the path to the JAR file.
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
            final int index = path.indexOf("!/");
            path = path.substring(4, index);
        }
        try
        {
            if (osName().startsWith("Win") && path.matches("file:[A-Za-z]:.*"))
                path = "file:/" + path.substring(5);
            return new File(new URL(path).toURI());
        } catch (MalformedURLException | URISyntaxException e)
        {
            Statics.LOGPLANTAGE.error(e);
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
        final String osName = System.getProperty("os.name");
        return osName == null ? "Unknown" : osName;
    }

    /**
     * Transcodification d'une version de composant en version d'édition
     * 
     * @param versionComposant
     *            Version Sonar d'un composant (14,15,16,...)
     * @return
     */
    public static String transcoVersion(String versionComposant)
    {
        if (versionComposant == null)
            return null;
        return "E" + (Integer.parseInt(versionComposant) + 17);
    }

    /**
     * Transcodifition dune version d'édition en version de composant
     * 
     * @param versionEdition
     *            Version RTC d'un composant (E30,E31,E32,...)
     * @return
     */
    public static String transcoEdition(String versionEdition)
    {
        if (versionEdition != null && versionEdition.matches("E[0-9][0-9]"))
            return String.valueOf((Integer.parseInt(versionEdition.substring(1)) - 17));
        throw new FunctionalException(Severity.ERROR, "Transcodification version Edition impossible - " + versionEdition);
    }

    /**
     * Permet d'enregistrer un objet pour le récupérer plus tard
     * 
     * @param adresseFichier
     *            Adresse du fichier à enregistrer
     * @param objet
     *            Objet à sérialiser
     */
    public static void serialisation(String adresseFichier, Object objet)
    {
        try (FileOutputStream fichier = new FileOutputStream(adresseFichier);)
        {
            ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(objet);
            oos.flush();
        } catch (IOException e)
        {
            throw new TechnicalException("Erreur sérialisation", e);
        }
    }

    /**
     * Transofmration en objet JAVA d'une sérialisation
     * 
     * @param adresseFichier
     *            Edresse du fichier à récupérer
     * @param classeObjet
     *            Classe de l'objet correpondante à la sauvegarde
     * @return
     */
    public static <T> T deserialisation(String adresseFichier, Class<T> classeObjet)
    {
        Object objet = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(adresseFichier));)
        {
            objet = ois.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            throw new TechnicalException("Erreur desérialisation", e);
        }
        return classeObjet.cast(objet);
    }

    /**
     * Permet de récupérer un objet soit par désiralisation, soit par une méthode en paramètre
     * Le paramètrage de déserialisation est dans la classe Main. utilisée uniquement pour le dévelloppement.
     * 
     * @param deserialisation
     *            Choix entre sérialisation et désérialisation
     * @param classRetour
     *            Classe de l'objet de retour
     * @param nomSer
     *            Adresse du fichier à utiliser en cas de sérialisation / désérialisation
     * @param fonction
     *            Fonction à utliser pour récupérer les informations necessaire à la créationde l'objet de retour
     * @return
     */
    public static <T> T recuperation(boolean deserialisation, Class<T> classRetour, String nomSer, Supplier<T> fonction)
    {
        T retour;
        if (deserialisation)
        {
            retour = Utilities.deserialisation(nomSer, classRetour);
        }
        else
        {
            retour = fonction.get();
            Utilities.serialisation(nomSer, retour);
        }
        return retour;
    }
}