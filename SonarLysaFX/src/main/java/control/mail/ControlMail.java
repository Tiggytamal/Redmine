package control.mail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
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
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.ParamSpec;
import model.enums.TypeInfoMail;
import utilities.DateConvert;
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

    private static final String SERVEUR = Statics.proprietesXML.getMapParams().get(Param.IPMAIL);
    private static final String PORT = Statics.proprietesXML.getMapParams().get(Param.PORTMAIL);

    private Message message;
    private String adresseConnecte;
    private String adressesEnvoi;
    private Properties props;
    private Map<String, EtatLot> lotsMaJ;
    private Map<String, String> anoMaJ;
    private Map<TypeInfoMail, List<String>> mapInfos;
    private final String today = DateConvert.dateFrancais(LocalDate.now(), "dd MMMM YYYY");

    /*---------- CONSTRUCTEURS ----------*/

    public ControlMail()
    {
        lotsMaJ = new HashMap<>();
        anoMaJ = new HashMap<>();
        mapInfos = new EnumMap<>(TypeInfoMail.class);
        for (TypeInfoMail type : TypeInfoMail.values())
        {
            mapInfos.put(type, new ArrayList<>());
        }
        try
        {
            initInfosMail();
        } catch (TeamRepositoryException e)
        {
            Statics.LOGPLANTAGE.error(e);
            Statics.LOGGER.error("Plantage ua moment d'initialiser le controleur de mail. Voir log des plantages");
        }
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void envoyerMail()
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

            // ---- 5. Sujet du mail. -----
            message.setSubject("Rapport MAJ fichier Qualimétrie du " + today);

            // ----- 6. Corps du mail. -----
            message.setText(creerTexte());

            // ----- 7. Envoi du mail. -----
            Transport.send(message);

            Statics.LOGGER.info("Envoi du mail du rapport OK.");

        } catch (MessagingException e)
        {
            throw new TechnicalException("Erreur Mail", e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/
    
    /**
     * Initialisation des informations de base pour envoyer le mail
     * 
     * @throws TeamRepositoryException
     */
    private void initInfosMail() throws TeamRepositoryException
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
    
    /**
     * Créee le corps du mail
     * 
     * @return
     */
    private String creerTexte()
    {
        String retour = "";
        
        // Début
        String debut = "Bonjour,\nVoici un résumé du traitement journalier du fichier de suivi des anomalies du " + today + Statics.NL;
        
        // Nouvelles anomalie
        
        return retour + debut;
    }
    
    /*---------- ACCESSEURS ----------*/

    /**
     * Enregistre une information dans le mail de rapport
     * 
     * @param type
     * @param info
     */
    public void addInfo(TypeInfoMail type, String info)
    {
        mapInfos.get(type).add(info);
    }
    
    /**
     * Enregistre la mise à jour d'un lot dans le mail de rapport
     * 
     * @param lot
     * @param EtatLot
     */
    public void addMajLot(String lot, EtatLot EtatLot)
    {
        lotsMaJ.put(lot, EtatLot);
    }
    
    /**
     * Enregistre la mise à jour d'un lot dans le mail de rapport
     * 
     * @param lot
     * @param EtatLot
     */
    public void addMajAno(String lot, String EtatAno)
    {
        anoMaJ.put(lot, EtatAno);
    }

    /*---------- CLASSES PRIVEES ----------*/
}