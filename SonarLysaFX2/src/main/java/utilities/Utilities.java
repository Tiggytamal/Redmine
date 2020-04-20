package utilities;

import static utilities.Statics.EMPTY;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import application.MainScreen;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.enums.OptionDeser;
import model.enums.Severity;

/**
 * Classe de méthodes utilitaires statiques
 *
 * @author Tiggy Tamal
 * @since 1.0
 * 
 */
public final class Utilities
{
    // Pattern
    private static final Pattern PATTERNFILE = Pattern.compile("file:[A-Za-z]:.*");
    private static final Pattern PATTERNEDITION = Pattern.compile("E[0-9][0-9]");

    /** logger plantages de l'application */
    private static final String LOGPATH = "log.path";
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");
    private static final short BASEVERSION = 17;
    private static final String JAR = "jar:";
    private static final int JARLENGTH = JAR.length();

    // Cryptage
    private static final String INSTANCE = "AES/GCM/NoPadding";
    private static GCMParameterSpec parameterSpec;
    private static SecretKey secretKey;
    private static final byte[] key = new byte[16];
    private static final byte[] iv = new byte[12];
    private static final SecureRandom secureRandom;

    // Alerte
    private static final short WIDTHALERT = 300;
    private static final short HEIGHTALERT = 200;

    private Utilities()
    {
        // Protège contre l'instanciation par introspection
        throw new AssertionError();
    }

    // Méthode statique pour générer les clefs et cipehrs de cryptage
    static
    {
        try
        {
            secureRandom = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new TechnicalException("utilities.Initialisation statique - Erreur lors de la génération du SecureRandom", e);
        }
    }

    /**
     * Gets the base location of the given class.
     * <p>
     * If the class is directly on the file system (e.g., "/path/to/my/package/MyClass.class") then it will return the base directory (e.g.,
     * "file:/path/to").
     * </p>
     * <p>
     * If the class is within a JAR file (e.g., "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the path to the JAR (e.g.,
     * "file:/path/to/my-jar.jar").
     * </p>
     *
     * @param c
     *          The class whose location is desired.
     * @return
     *         URL
     */
    public static URL getLocation(final Class<?> c)
    {
        // could not load the class
        if (c == null)
            throw new TechnicalException("Utilities.getLocation - paramètre null");

        // try the easy way first
        try
        {
            final URL codeSourceLocation = c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null)
            {
                return codeSourceLocation;
            }
        }
        catch (final SecurityException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
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
        if (path.startsWith(JAR))
        {
            path = path.substring(JARLENGTH, path.length() - 2);
        }

        try
        {
            return new URL(path);
        }
        catch (final MalformedURLException e)
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
     * @return
     *         A file path suitable for use with e.g. {@link FileInputStream}
     * 
     */
    public static File urlToFile(final URL url)
    {
        if (url == null)
            throw new TechnicalException("Utilities.urlToFile - paramètre null");

        return urlToFile(url.toString());
    }

    /**
     * Converts the given URL string to its corresponding {@link File}.
     *
     * @param url
     *            The URL to convert.
     * @return
     *         A file path suitable for use with e.g. {@link FileInputStream}
     */
    public static File urlToFile(final String url)
    {
        String path = url;
        final short CUTFILE = 5;
        if (path.startsWith(JAR))
        {
            // remove "jar:" prefix and "!/" suffix
            final int index = path.indexOf("!/");
            path = path.substring(JARLENGTH, index);
        }
        try
        {
            if (osName().startsWith("Win") && PATTERNFILE.matcher(path).matches())
                path = "file:/" + path.substring(CUTFILE);
            return new File(new URL(path).toURI());
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
        }

        if (path.startsWith("file:"))
        {
            // pass through the URL as-is, minus "file:" prefix
            path = path.substring(CUTFILE);
            return new File(path);
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }

    /**
     * Gets the name of the operating system.
     * 
     * @return
     *         Le nom du système d'opération.
     */
    public static String osName()
    {
        final String osName = System.getProperty("os.name");
        return osName == null ? "Unknown" : osName;
    }

    /**
     * Transcodifition d'une version d'édition en version de composant
     * 
     * @param versionEdition
     *                       Version RTC d'un composant (E30,E31,E32,...)
     * @return
     *         La version du composant.
     */
    public static String transcoEdition(String versionEdition)
    {
        if (versionEdition != null && PATTERNEDITION.matcher(versionEdition).matches())
            return String.valueOf((Integer.parseInt(versionEdition.substring(1)) - BASEVERSION));
        throw new FunctionalException(Severity.ERROR, "Transcodification version Edition impossible : " + versionEdition);
    }

    /**
     * Permet d'enregistrer un objet pour le récupérer plus tard
     * 
     * @param adresseFichier
     *                       Adresse du fichier à enregistrer
     * @param objet
     *                       Objet à serialiser
     */
    public static void serialisation(String adresseFichier, Object objet)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(adresseFichier))))
        {
            oos.writeObject(objet);
            oos.flush();
        }
        catch (IOException e)
        {
            throw new TechnicalException("Erreur sérialisation", e);
        }
    }

    /**
     * Transofmration en objet JAVA d'une serialisation
     * 
     * @param adresseFichier
     *                       Edresse du fichier à recuperer
     * @param classeObjet
     *                       Classe de l'objet correpondante à la sauvegarde
     * @param                <T>
     *                       Classe de l'objet à désérialiser.
     * @return
     *         L'objet désérialisé.
     */
    public static <T> T deserialisation(String adresseFichier, Class<T> classeObjet)
    {
        Object objet = null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(adresseFichier))))
        {
            objet = ois.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new TechnicalException("Erreur désérialisation", e);
        }
        return classeObjet.cast(objet);
    }

    /**
     * Permet de récupérer un objet soit par désérialisation, soit par une methode en paramètre.
     * Utilisée uniquement pour le développement.
     * 
     * @param option
     *                    Choix entre sérialisation et désérialisation
     * @param classRetour
     *                    Classe de l'objet de retour
     * @param nomSer
     *                    Adresse du fichier à utiliser en cas de sérialisation / désérialisation
     * @param fonction
     *                    Fonction à utliser pour récupérer les informations necessaire à la création de l'objet de retour
     * @param             <T>
     *                    Classe de l'objet à désérialiser.
     * @return
     *         Objet désérialisé.
     */
    public static <T> T recuperation(OptionDeser option, Class<T> classRetour, String nomSer, Supplier<T> fonction)
    {
        T retour;
        switch (option)
        {
            case AUCUNE:
                retour = fonction.get();
                break;

            case DESERIALISATION:
                retour = Utilities.deserialisation(Statics.ADRESSEDESER + nomSer, classRetour);
                break;

            case SERIALISATION:
                retour = fonction.get();
                Utilities.serialisation(Statics.ADRESSEDESER + nomSer, retour);
                break;

            default:
                throw new TechnicalException("utilities.Utilities.recuperation - DeserOption " + option + " inconnue", null);
        }

        return retour;
    }

    /**
     * Initialise la fenêtre de l'explorateur de fichiers, et retourne le fichier selectionné.
     * Fenêtre pour ouvrir un fichier.
     * 
     * @param titre
     *              Titre de la fenêtre de l'explorateur
     * @return
     *         La fichier selectionné.
     */
    public static File getFileFromFileChooser(String titre)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showOpenDialog(MainScreen.ROOT.getScene().getWindow());
        if (file == null)
            throw new FunctionalException(Severity.INFO, "Impossible de récupérer le fichier.");
        return file;
    }

    /**
     * Initialise la fenêtre de l'explorateur de fichier, et retourne le fichier selectionné.
     * Fenêtre pour sauvegarder un fichier.
     * 
     * @param titre
     *              Titre de la fenêtre de l'explorateur
     * @return
     *         La fichier selectionné.
     */
    public static File saveFileFromFileChooser(String titre)
    {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fc.showSaveDialog(MainScreen.ROOT.getScene().getWindow());
        if (file == null)
            throw new FunctionalException(Severity.INFO, "Impossible de récupérer le fichier.");
        return file;
    }

    /**
     * Encode une url pour qu'elle soit bien prise en compte par les navigateurs sans erreur au niveau des caratères spéciaux.
     * 
     * @param liens
     *              Le liens à encoder.
     * @return
     *         L'url encodée.
     */
    public static String stringToUrl(String liens)
    {
        URL url;
        try
        {
            // Encoding de l'url en format comprehensible pour le navigateur.
            url = new URL(liens);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toASCIIString();
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            return Statics.EMPTY;
        }
    }

    /**
     * Cryptage du chaîne de caractères.
     * 
     * @param valeur
     *               Chaîne à crypter
     * @return
     *         La chaîne cryptée.
     */
    public static String crypterValeur(String valeur)
    {

        // Transformation en byte puis cryptage des données.
        byte[] utf8 = valeur.getBytes(StandardCharsets.UTF_8);
        byte[] enc;

        // Initialisation clef de 16 et 12 bits random.
        secureRandom.nextBytes(key);
        secureRandom.nextBytes(iv);

        // Préparation paramètres et clefs pour les ciphers.
        parameterSpec = new GCMParameterSpec(128, iv);
        secretKey = new SecretKeySpec(key, "AES");

        try
        {
            // Création cipher cryptage
            Cipher ecipher = Cipher.getInstance(INSTANCE);
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            // Cryptage
            enc = ecipher.doFinal(utf8);
        }
        catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
        {
            throw new TechnicalException("utilities.Utilities.crypterValeur - erreur lors du cryptage des données.", e);
        }

        // Encode en base 64 et retour sour forme de chaîne de caractères.
        return Base64.getEncoder().encodeToString(enc);
    }

    /**
     * Décryptage d'une chaîne de caractères.
     * 
     * @param valeurCryptee
     *                      La chaîne cryptée.
     * @return
     *         La chaîne décryptée.
     */
    public static String decrypterValeur(String valeurCryptee)
    {
        if (valeurCryptee == null || valeurCryptee.isEmpty())
            return Statics.EMPTY;

        // Decode en base 64 pour récupérer la valeur sous forme de bytes.
        byte[] dec = Base64.getDecoder().decode(valeurCryptee.getBytes(StandardCharsets.UTF_8));

        byte[] utf8;
        try
        {
            // Création cipher décryptage
            Cipher dcipher = Cipher.getInstance(INSTANCE);
            dcipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // Décryptage
            utf8 = dcipher.doFinal(dec);
        }
        catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e)
        {
            throw new TechnicalException("utilities.Utilities.decrypterValeur - erreur lors du décryptage des données.", e);
        }

        // Transformation de la série de bytes en String RTF8
        return new String(utf8, StandardCharsets.UTF_8);
    }

    /**
     * Récupération d'un fichier de propriétés depuis son nom. Le fichier doit être dans le même répertoire que le jar.
     * 
     * @param nom
     * @return
     */
    public static Properties recupFichierProperties(String nom)
    {
        // Récupération ud chemin d'accés au fichier depuis la racine du fichier jar.
        Path path = Paths.get(urlToFile(getLocation(Main.class)).getParentFile().getPath() + "\\" + nom);

        // Utilisation d'un InputStream puis d'un InputStreamReader pour gérer les accents en UTF8
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8))
        {
            // Récupération du fichier de propriétés externe
            Properties retour = new Properties();
            retour.load(inputStreamReader);
            return retour;
        }
        catch (IOException e)
        {
            throw new TechnicalException("control.dao.AbstractDao.recupFichierProperties : impossible de créer le fichier properties" + nom, e);
        }
    }

    /**
     * Retourne un Logger statique, avec vérification du paramètrage log4j2.
     * 
     * @param nomLogger
     *                  Nom du Logger à créer. Il doit être cohérent avec le paramètrage xml.
     * @return
     *         Le Logger.
     */
    public static Logger getLogger(String nomLogger)
    {
        // On vérifie si le paramètre de la destination des fichiers est déjà enregistré sinon, on utilise le fichier de paramètre pour le récupérer.
        if (System.getProperty(LOGPATH) == null)
        {
            Properties log4j2Properties = Utilities.recupFichierProperties("log4j2.properties");
            System.setProperty(LOGPATH, log4j2Properties.getProperty(LOGPATH));
        }

        return LogManager.getLogger(nomLogger);
    }

    /**
     * Ajoute un raccourci clavier à un bouton JavaFX.
     * 
     * @param bouton
     *                  Le bouton.
     * @param scene
     *                  La scène JavaFX où le bouton est placé.
     * @param raccourci
     *                  Le raccourci à ajouter.
     */
    public static void creerRaccourciClavierBouton(Button bouton, Scene scene, KeyCodeCombination raccourci)
    {
        scene.getAccelerators().put(raccourci, bouton::fire);
    }

    /**
     * Ajoute un raccourci clavier Ctrl-S à un bouton JavaFX.
     * 
     * @param bouton
     *               Le bouton.
     * @param scene
     *               La scène JavaFX où le bouton est placé.
     */
    public static void creerRaccourciClavierBoutonCtrlS(Button bouton, Scene scene)
    {
        creerRaccourciClavierBouton(bouton, scene, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    }

    /**
     * Affichage alerte d'un message d'alerte, avec le texte donné.
     *
     * @param texte
     *              Texte à afficher dans l'alerte.
     * @return
     *         L'alerte.
     */
    public static Alert createAlert(String texte)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(Statics.CSS);
        alert.initOwner(MainScreen.ROOT.getScene().getWindow());
        alert.initStyle(StageStyle.UTILITY);
        alert.initModality(Modality.NONE);
        alert.setResizable(true);
        alert.setContentText(texte);
        alert.setHeaderText(null);
        alert.show();
        alert.setWidth(WIDTHALERT);
        alert.setHeight(HEIGHTALERT);
        return alert;
    }
}
