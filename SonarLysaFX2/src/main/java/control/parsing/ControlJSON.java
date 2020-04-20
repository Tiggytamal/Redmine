package control.parsing;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import control.statistique.ControlStatistique;
import model.parsing.ComposantJSON;
import utilities.AbstractToStringImpl;
import utilities.DateHelper;
import utilities.TechnicalException;

/**
 * Classe de contrôle de récupération des fichiers JSON
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public class ControlJSON extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    private ObjectMapper mapper;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlJSON()
    {
        mapper = new ObjectMapper();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**103
     * Lecture d'un fichier pour retourner la classe JAVA de traitement.
     * 
     * @param file
     *             Fichier à traiter
     * @return
     *         {@link ComposantJSON} créé depuis le fichier texte.
     */
    public ComposantJSON parsingCompoJSON(File file)
    {
        // Lecture du fichier JSON pour créer un ComposantJSON
        ComposantJSON retour = null;
        try
        {
            // Récupération données
            retour = mapper.readValue(file, ComposantJSON.class);

            if (!retour.isTraite())
            {
                LocalDate dateFichier = DateHelper.calculDateFichier(file);
                new ControlStatistique().majStatistiquesFichiers(dateFichier);
                
                // Mise à jour du booléen traité
                retour.setTraite(true);
                mapper.writeValue(file, retour);
            }
        }
        catch (IOException e)
        {
            throw new TechnicalException("Impossible de récupérer le fichier JSON : " + file.getPath(), e);
        }

        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
