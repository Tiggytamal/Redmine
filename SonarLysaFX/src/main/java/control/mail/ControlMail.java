package control.mail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import model.InfoMail;
import model.ModelFactory;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeInfoMail;
import model.enums.TypeMail;
import utilities.DateConvert;
import utilities.Statics;

/**
 * Classe de contrôle pour l'envoi des mails
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public class ControlMail
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SERVEUR = Statics.proprietesXML.getMapParams().get(Param.IPMAIL);
    private static final String PORT = Statics.proprietesXML.getMapParams().get(Param.PORTMAIL);
    
    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log"); 

    private Message message;
    private String adresseConnecte;
    private String adressesEnvoi;
    private Properties props;
    private String extra;
    private Map<TypeInfoMail, List<InfoMail>> mapInfos;
    private final String today = DateConvert.dateFrancais(LocalDate.now(), "dd MMMM YYYY");

    /*---------- CONSTRUCTEURS ----------*/

    public ControlMail()
    {
        // Initialisation variables
        mapInfos = new EnumMap<>(TypeInfoMail.class);
        extra = "";
        for (TypeInfoMail type : TypeInfoMail.values())
        {
            mapInfos.put(type, new ArrayList<>());
        }

        // Données génériques du mail
        try
        {
            initMail();
        } 
        catch (TeamRepositoryException e)
        {
            LOGPLANTAGE.error(e);
            LOGGER.error("Plantage au moment d'initialiser le controleur de mail. Voir log des plantages");
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Permet d'envoyer un mail, selon son type
     * 
     * @param typeMail
     *            Enumération du type de mail
     */
    public void envoyerMail(TypeMail typeMail)
    {
        try
        {
            Session session = Session.getInstance(props, new MailAuthenticator());

            // ----- 2. Création du message depuis la session -----
            message = new MimeMessage(session);

            // ----- 3. Enregistrement envoyeur du mail -----
            message.setFrom(new InternetAddress(adresseConnecte));

            // ----- 4. Récupération des adresses pour envoyer les mails de rapport. -----
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adressesEnvoi));

            // ----- 5. Sitre du mail. -----
            message.setSubject(typeMail.getTitre() + today);

            // ----- 6. Corps du mail. -----
            message.setText(creerTexte(typeMail.getDebut()));

            // ----- 7. Envoi du mail. -----
            transportMail();

            LOGGER.info("Envoi du mail de rapport OK.");
        } 
        catch (MessagingException e)
        {
            LOGPLANTAGE.error(e);
            LOGGER.error("Erreur lors de la création du mail. Voir log plantage");
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Initialisation des informations de base pour envoyer le mail
     * 
     * @throws TeamRepositoryException
     */
    private void initMail() throws TeamRepositoryException
    {
        props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.host", SERVEUR);
        props.put("mail.smtp.port", PORT);

        adresseConnecte = ControlRTC.INSTANCE.recupContributorDepuisId(Statics.info.getPseudo()).getEmailAddress();

        // Création de la liste des adresses
        StringBuilder adressesBuilder = new StringBuilder();
        String[] noms = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESMAIL).split(";");

        // Itération sur la liste des membres, puis création de la liste des adresses séparées par des virgules.
        for (int i = 0; i < noms.length; i++)
        {
            adressesBuilder.append(ControlRTC.INSTANCE.recupContributorDepuisNom(noms[i]).getEmailAddress());
            adressesBuilder.append(",");
        }

        // Ajout de l'adresse AQP
        adressesBuilder.append(Statics.proprietesXML.getMapParams().get(Param.AQPMAIL));

        adressesEnvoi = adressesBuilder.toString();
    }

    private String creerTexte(String debut)
    {
        // Début
        StringBuilder builder = new StringBuilder(debut).append(today).append(Statics.NL).append(Statics.NL);

        // Gestion des infos
        for (TypeInfoMail type : TypeInfoMail.values())
        {
            List<InfoMail> lots = mapInfos.computeIfAbsent(type, n -> new ArrayList<>());
            if (lots.isEmpty())
                continue;

            builder.append(type.getTitre());
            for (InfoMail info : lots)
            {
                builder.append(Statics.TIRET).append(info.getLot());
                if (!info.getInfoSupp().isEmpty() && !type.getLiens().isEmpty())
                    builder.append(type.getLiens()).append(info.getInfoSupp());

                builder.append(Statics.NL);
            }
            builder.append(Statics.NL);
        }

        // Ajout de données extra
        builder.append(extra);
        
        return builder.toString();
    }
    
    protected void transportMail() throws MessagingException
    {
        Transport.send(message);
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Enregistre une information dans le mail de rapport
     * 
     * @param type
     * @param info
     */
    public void addInfo(TypeInfoMail type, String lot, String infoSupp)
    {
        mapInfos.get(type).add(ModelFactory.getModelWithParams(InfoMail.class, lot, infoSupp));
    }
    
    /**
     * Ajoute des données sous forme d'une chaîne de caratères au mail.
     * 
     * @param extra
     */
    public void addExtra(String extra)
    {
        this.extra += extra;
    }

    /*---------- CLASSES PRIVEES ----------*/

    /**
     * CLasse pour l'authentification du serveur mail.
     * 
     * @author ETP8137 - Grégoire Mathon
     * @since 1.0
     */
    private static class MailAuthenticator extends Authenticator
    {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(Statics.info.getPseudo(), Statics.info.getMotDePasse());
        }
    }
}
