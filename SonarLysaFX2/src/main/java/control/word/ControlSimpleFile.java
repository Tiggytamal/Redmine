package control.word;

import static utilities.Statics.EMPTY;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

import org.apache.logging.log4j.Logger;

import model.bdd.ComposantBase;
import model.enums.Param;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de contrôle d'édition d'un fichier texte simple
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlSimpleFile
{
    /*---------- ATTRIBUTS ----------*/

    /** logger général */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    private File file;
    private BufferedWriter writer;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlSimpleFile(File file)
    {
        this.file = file;

        try
        {
            if (file.exists() || file.createNewFile())
                writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8));
            else
                throw new TechnicalException("control.word.ControlSimpleFile - Impossible de créer le fichier : " + file.getPath());

        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("control.word.ControlSimpleFile - Impossible de créer le fichier : " + file.getPath(), e);
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static ControlSimpleFile majFichierVersion()
    {
        return new ControlSimpleFile(new File(Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATHRAPPORT) + Statics.proprietesXML.getMapParams().get(Param.FICHIERVERSION)));
    }

    /**
     * Permet de rajouter une ligne de texte dans le fichier.
     * 
     * @param texte
     *              Texte à rajouter.
     */
    public void ajouterTexte(String texte)
    {
        try
        {
            writer.write(texte);
            writer.newLine();
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("impossible d'ecrire dans fichier : " + file.getPath(), e);
        }
    }

    /**
     * Permet d'ajouter une erreur de version pour un composant donné dans le fichier.
     * 
     * @param compo
     *              Composant à traiter.
     */
    public void ajouterErreurCompo(ComposantBase compo)
    {
        StringBuilder builder = new StringBuilder(LocalDateTime.now().toString());
        builder.append(Statics.TIRET2);
        builder.append(compo.getNom()).append(Statics.TIRET2);
        builder.append(compo.getBranche()).append(Statics.TIRET2);
        builder.append(compo.getVersion()).append(Statics.TIRET2);
        builder.append(compo.getVersionMax());
        ajouterTexte(builder.toString());
    }

    public void fermeture()
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            throw new TechnicalException("impossible de fermer le fichier : " + file.getPath(), e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
