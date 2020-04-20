package dao;

import java.time.LocalDate;
import java.util.List;

import model.bdd.DefautQualite;
import model.enums.InstanceSonar;

public class DaoDefautQualite extends AbstractMySQLDao<DefautQualite, String>
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "defaults_qualite";
    private static final String DEBUT = "debut";
    private static final String FIN = "fin";

    /*---------- CONSTRUCTEURS ----------*/

    DaoDefautQualite()
    {
        super(TABLE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    protected void persistImpl(DefautQualite t)
    {
        // Pas d'implementation necessaire
    }

    /**
     * Remonte tous les défauts de qualité de l'instance SonarQube donnée
     * 
     * @param instance
     *                 Instance sonar.
     * @return
     *         Liste des DefautQualite.
     */
    public List<DefautQualite> findAllByInstance(InstanceSonar instance)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".findAllByInstance", DefautQualite.class).setParameter("instance", instance).getResultList();
    }

    /**
     * Calcul le nombre d'anomalies créées dans la période données.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long crees(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".crees", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies abandonnées créées dans la période données.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long abandonnees(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".abandonnees", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies abandonnées créées dans la période données avec un défaut de sécurité.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long abandonneesSecu(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".abandonneesSecu", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies obsolètes créées dans la période données.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long obsoletes(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".obsoletes", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies obsolètes créées dans la période données avec un défaut de sécurité.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long obsoletesSecu(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".obsoletesSecu", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies closes créées dans la période données.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long closes(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".closes", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies closes créées dans la période données avec un défaut de sécurité.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long closesSecu(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".closesSecu", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies en cours créées dans la période données.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long enCours(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".enCours", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies en cours créées dans la période données avec un défaut de sécurité.
     * 
     * @param dateDebut
     *                  Date de debut de la période.
     * @param dateFin
     *                  Date de fin de la période.
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long enCoursSecu(LocalDate dateDebut, LocalDate dateFin)
    {
        return em.createNamedQuery(modele.getSimpleName() + ".enCoursSecu", Long.class).setParameter(DEBUT, dateDebut).setParameter(FIN, dateFin).getSingleResult();
    }

    /**
     * Calcul le nombre d'anomalies en cours.
     * 
     * @return
     *         Le nombre d'anomalies trouvées.
     */
    public long enCoursTotal()
    {
        return em.createNamedQuery(modele.getSimpleName() + ".enCoursTotal", Long.class).getSingleResult();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
