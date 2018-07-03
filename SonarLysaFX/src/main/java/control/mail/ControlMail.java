package control.mail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import model.InfoMail;
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

    private Message message;
    private String adresseConnecte;
    private String adressesEnvoi;
    private Properties props;
    private Map<TypeInfoMail, List<InfoMail>> mapInfos;
    private final String today = DateConvert.dateFrancais(LocalDate.now(), "dd MMMM YYYY");
    private static final String TIRET = "- ";

    /*---------- CONSTRUCTEURS ----------*/

    public ControlMail()
    {
        mapInfos = new EnumMap<>(TypeInfoMail.class);
        for (TypeInfoMail type : TypeInfoMail.values())
        {
            mapInfos.put(type, new ArrayList<>());
        }

        try
        {
            initMail();
        } catch (TeamRepositoryException e)
        {
            Statics.LOGPLANTAGE.error(e);
            Statics.LOGGER.error("Plantage ua moment d'initialiser le controleur de mail. Voir log des plantages");
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
            Session session = Session.getInstance(props);

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
            Transport.send(message);

            Statics.LOGGER.info("Envoi du mail de rapport OK.");

        } catch (MessagingException e)
        {
            Statics.LOGPLANTAGE.error(e);
            Statics.LOGGER.error("Erreur lors de la création du mail. Voir log plantage");
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
            List<InfoMail> lots = mapInfos.computeIfAbsent(type, (n) -> new ArrayList<>());
            if (lots.isEmpty())
                continue;

            builder.append(type.getTitre());
            for (InfoMail info : lots)
            {
                builder.append(TIRET).append(info.getLot());
                if (info.getInfoSupp() != null && !type.getLiens().isEmpty())
                    builder.append(type.getLiens()).append(info.getInfoSupp());

                builder.append(Statics.NL);
            }
            builder.append(Statics.NL);
        }

        return builder.toString();
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

        mapInfos.get(type).add(new InfoMail(lot, infoSupp));
    }

    /*---------- CLASSES PRIVEES ----------*/
}