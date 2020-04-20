package control.mail;

import static utilities.Statics.EMPTY;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import model.bdd.DefautQualite;
import model.bdd.Utilisateur;
import model.enums.EtatCodeAppli;
import model.enums.ParamSpec;
import model.enums.QG;
import model.enums.Severity;
import utilities.AbstractToStringImpl;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de gestion d'envoi des mails.
 * Il n'est pas possible d'envoyer des mails directement depuis l'application. On prépare donc celui-ci et on l'envoie dans l'application de gestion
 * des mails par défaut du poste.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlMailOutlook extends AbstractToStringImpl implements ControlMail
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = Utilities.getLogger("plantage-log");

    // Valeurs statiques pour la création du mail */
    private static final char PV = ';';
    private static final String COPIE = "?cc=";
    private static final String SUJET = "&subject=";
    private static final String CORPS = "&body=";

    /** Charset UTF8 */
    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private StringBuilder builder;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlMailOutlook()
    {
        builder = new StringBuilder("mailto:");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Création d'un mail de relance pour l'assignation des anomalies.
     * 
     * @param destinataires
     *                      Liste des destinataires des mails. Les utilisateurs inactifs seront ignorés.
     * @return
     *         vrai si le mail a bien été créée.
     */
    public boolean creerMailAssignerDefautSonar(Set<Utilisateur> destinataires)
    {
        // Contrôle paramètres
        if (destinataires == null || destinataires.isEmpty())
            throw new FunctionalException(Severity.ERROR, "control.mail.ControlMail.creerMailAssignerDefautSonar : Liste des destinataires du mail vide");

        initMail(destinataires);

        try
        {
            // Utilisation de l'UrlEncoder pour gérer les accents français. Mais du coup, besoin de supprimer les + pour ne pas qu'ils apparaissent dans le corps
            // du mail.

            // Sujet du mail
            builder.append(SUJET);
            builder.append(replacePlus(URLEncoder.encode(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TITREASSIGNATIONANO), UTF8)));

            // Texte du mail
            builder.append(CORPS);
            builder.append(replacePlus(URLEncoder.encode(Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.TEXTEASSIGNATIONANO), UTF8)));
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
        }

        return sendMail();
    }

    /**
     * Création d'un mail après la création d'une anomalie.
     * 
     * @param destinataires
     *                      Liste des destinataires des mails. Les utilisateurs inactifs seront ignorés.
     * @param dq
     *                      Défaut Qualité servant de base pour le mail.
     * @return
     *         vrai si le mail a bien été créée.
     */
    public boolean creerMailCreerAnoRTC(Set<Utilisateur> destinataires, DefautQualite dq)
    {
        // Contrôle paramètres
        if (dq == null)
            throw new TechnicalException("control.mail.ControlMail.creerMailCreerAnoRTC : Défaut qualité null");
        if (destinataires == null || destinataires.isEmpty())
            throw new FunctionalException(Severity.ERROR, "control.mail.ControlMail.creerMailCreerAnoRTC : Liste des destinataires du mail vide" + dq.getCompo().getNom());

        initMail(destinataires);

        // Ajout du message de correction du code appli si celui est bon pour SonarQube mais quand même faux, sinon envoi d'un message normal
        if (dq.getCompo().getAppli() != null && dq.getCompo().getQualityGate() == QG.OK && dq.getEtatCodeAppli() == EtatCodeAppli.ERREUR)
            corpsAnoRTC(ParamSpec.TITRECREERANORTC, ParamSpec.TEXTECREERANORTCAPPLI, dq);
        else
            corpsAnoRTC(ParamSpec.TITRECREERANORTC, ParamSpec.TEXTECREERANORTC, dq);
        return sendMail();
    }

    /**
     * Création d'un mail après la relance d'une anomalie
     * 
     * @param destinataires
     *                      Liste des destinataires des mails. Les utilisateurs inactifs seront ignorés.
     * @param dq
     *                      Défaut Qualité servant de base pour le mail.
     * @return
     *         vrai si le mail a bien été créée.
     */
    public boolean creerMailRelanceAnoRTC(Set<Utilisateur> destinataires, DefautQualite dq)
    {
        // Contrôle paramètres
        if (dq == null)
            throw new TechnicalException("control.mail.ControlMail.creerMailRelanceAnoRTC : Défaut qualité null");
        if (destinataires == null || destinataires.isEmpty())
            throw new FunctionalException(Severity.ERROR, "control.mail.ControlMail.creerMailRelanceAnoRTC : Liste des destinataires du mail vide - " + dq.getCompo().getNom());

        initMail(destinataires);

        // Ajout du message de correction du code appli si celui est bon pour SonarQube mais quand même faux, sinon envoi d'un message normal
        if (dq.getCompo().getAppli() != null && dq.getCompo().getQualityGate() == QG.OK && dq.getEtatCodeAppli() == EtatCodeAppli.ERREUR)
            corpsAnoRTC(ParamSpec.TITRERELANCEANORTC, ParamSpec.TEXTERELANCEANORTCAPPLI, dq);
        else
            corpsAnoRTC(ParamSpec.TITRERELANCEANORTC, ParamSpec.TEXTERELANCEANORTC, dq);

        return sendMail();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation des mails avec les destinataires et personnes en copie
     * 
     * @param destinataires
     *                      Liste des destinataires des mails. Les utilisateurs inactifs seront ignorés.
     */
    private void initMail(Set<Utilisateur> destinataires)
    {
        // Destinataires
        for (Utilisateur user : destinataires)
        {
            if (!user.isActive())
                continue;
            builder.append(user.getEmail());
            builder.append(PV);
        }

        // Cc
        builder.append(COPIE);

        // Recherche des utilisateur enregistrés dans les paramètres.
        String listeNomUser = Statics.propPersoXML.getParamsSpec().get(ParamSpec.USERSMAIL);

        if (listeNomUser != null && !listeNomUser.isEmpty())
        {
            for (String nomUser : listeNomUser.split(";"))
            {
                try
                {
                    builder.append(ControlRTC.getInstance().recupContributorDepuisNom(nomUser).getEmailAddress());
                }
                catch (TeamRepositoryException e)
                {
                    throw new TechnicalException("control.mail.ControlMail.initMail - Erreur appel RTC " + nomUser, e);
                }
                builder.append(PV);
            }
        }
        else
            builder.append(PV);
    }

    /**
     * Corps du mail pour la gestion des anos RTC.
     * 
     * @param titre
     *              Sujet du mail.
     * @param texte
     *              Corps du mail.
     * @param dq
     *              Défaut Qualité servant de base pour le mail.
     */
    private void corpsAnoRTC(ParamSpec titre, ParamSpec texte, DefautQualite dq)
    {
        try
        {
            // Utilisation de l'UrlEncoder pour gérer les accents français. Mais du coup besoin de supprimer les + pour ne pas qu'ils apparaissent dans le corps
            // du mail.

            // Sujet du mail
            builder.append(SUJET);
            builder.append(replacePlus(URLEncoder.encode(Statics.proprietesXML.getMapParamsSpec().get(titre).replace("-num-", String.valueOf(dq.getNumeroAnoRTC())), UTF8)));

            // Texte du mail
            builder.append(CORPS);
            builder.append(replacePlus(URLEncoder.encode(Statics.proprietesXML.getMapParamsSpec().get(texte).replace("-lot-", dq.getLotRTC().getNumero()).replace("xxxxx", dq.getCompo().getNom())
                    .replace("key", dq.getCompo().getKey()).replace("-liensano", dq.getLiensAno()).replace("-code-", dq.getCompo().getAppli().getCode()), UTF8)));
        }
        catch (IOException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
        }
    }

    /**
     * Envoi du mail vers l'outil de gestion de mails par défaut.
     * 
     * @return
     *         vrai si le mail a été envoyé sans erreur.
     */
    private boolean sendMail()
    {
        try
        {
            // Ajout de la signature personnelle de la personne connectée depuis le fichier de paramètres
            builder.append(replacePlus(URLEncoder.encode(Statics.NL + Statics.NL + Statics.propPersoXML.getParamsSpec().get(ParamSpec.SIGNATURE), UTF8)));

            // Appel de l'application de gestion des mails par défaut
            Desktop.getDesktop().mail(new URI(builder.toString()));
        }
        catch (IOException | URISyntaxException e)
        {
            LOGPLANTAGE.error(EMPTY, e);
            return false;
        }
        return true;
    }

    /**
     * Remplacmeent des + par %20 pour le corps des mails
     * 
     * @param string
     *               Chaîne de caractères à traiter.
     * @return
     *         La chaîne de caractères modifiée.
     */
    private String replacePlus(String string)
    {
        return string.replace("+", "%20");
    }
    /*---------- ACCESSEURS ----------*/
}
