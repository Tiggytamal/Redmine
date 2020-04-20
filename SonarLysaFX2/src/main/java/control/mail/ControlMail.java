package control.mail;

import java.util.Set;

import model.bdd.DefautQualite;
import model.bdd.Utilisateur;

/**
 * Interface gérant la création de mail depuis l'application
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 *
 */
public interface ControlMail
{
    /**
     * Création d'un mail pour demander aux personnes de se connecter à SonarQube pour créer leur compte et permettre l'assignation des anomalies.
     * 
     * @param destinataires
     *                      Déstinataires du mail.
     * @return
     *         Vrai si le mail a été envoyé.
     */
    boolean creerMailAssignerDefautSonar(Set<Utilisateur> destinataires);

    /**
     * Création d'un mail lors de la création d'une anomalie RTC.
     * 
     * @param destinataires
     *                      Déstinataires du mail.
     * @param dq
     *                      DefautQualite lié à l'anomalie RTC.
     * @return
     *         Vrai si le mail a été envoyé.
     */
    boolean creerMailCreerAnoRTC(Set<Utilisateur> destinataires, DefautQualite dq);

    /**
     * Création d'un mail lors de la relance d'une anomalie RTC.
     * 
     * @param destinataires
     *                      Déstinataires du mail.
     * @param dq
     *                      DefautQualite lié à l'anomalie RTC.
     * @return
     *         Vrai si le mail a été envoyé.
     */
    boolean creerMailRelanceAnoRTC(Set<Utilisateur> destinataires, DefautQualite dq);
}
