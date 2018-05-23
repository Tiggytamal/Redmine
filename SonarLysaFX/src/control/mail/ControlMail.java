package control.mail;

import java.util.EnumMap;
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

import com.ibm.team.repository.common.TeamRepositoryException;

import control.rtc.ControlRTC;
import model.enums.ParamSpec;
import model.enums.TypeInfoMail;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe de contrôle pour l'envoi des mails
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public class ControlMail
{
    /*---------- ATTRIBUTS ----------*/

    private static final String SERVEUR = "muz10-e1smtp-IN-DC-INT.zres.ztech";

    private Message message;
    private String adresseConnecte;
    private String adressesEnvoi;
    private Properties props;
    private Map<TypeInfoMail, String> mapInformations; 

    /*---------- CONSTRUCTEURS ----------*/

    public ControlMail()
    {
        mapInformations = new EnumMap<>(TypeInfoMail.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void envoyerMail() throws MessagingException
    {
        Transport.send(message);
    }

    public void initInfosMail() throws TeamRepositoryException
    {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SERVEUR);
        props.put("mail.smtp.port", "25");
        
        adresseConnecte = ControlRTC.INSTANCE.recupContributorDepuisId(Statics.info.getPseudo()).getEmailAddress();
        // Création de la liste des adresses
        StringBuilder adressesBuilder = new StringBuilder();
        String[] noms = Statics.proprietesXML.getMapParamsSpec().get(ParamSpec.MEMBRESMAIL).split(";");

        // Itération sur la liste des membres, puis création de la liste des adresses séparées par des virgules.
        for (int i = 0; i < noms.length; i++)
        {
            adressesBuilder.append(ControlRTC.INSTANCE.recupContributorDepuisNom(noms[i]).getEmailAddress());
            if (i != noms.length - 1)
                adressesBuilder.append(",");
        }
        adressesEnvoi = adressesBuilder.toString();
    }
    
    public void test()
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

            // ---- 5. Sujet du mail. -----
            message.setSubject("Rapport MAJ fichier Qualimétrie" + Statics.TODAY);

            // ----- 6. Corps du mail. -----
            message.setText("Mail test ");

            // ----- 7. Envoi du mail. -----
            Transport.send(message);

            Statics.logger.info("Envoi du mail du rapport OK.");

        } catch (MessagingException e)
        {
            throw new TechnicalException("Erreur Mail", e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public void addValeur(TypeInfoMail type)
    {
        
    }
    
    /*---------- CLASSES PRIVEES ----------*/

    /**
     * 
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