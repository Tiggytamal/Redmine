package control.statistique;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import control.rest.SonarAPI;
import dao.DaoComposantBase;
import dao.DaoFactory;
import dao.DaoStatistique;
import dao.DaoStatistiqueCompo;
import model.bdd.ComposantBase;
import model.bdd.DefautQualite;
import model.bdd.Statistique;
import model.bdd.StatistiqueCompo;
import model.enums.OptionGetCompos;
import model.enums.StatistiqueCompoEnum;
import model.enums.StatistiqueEnum;
import utilities.AbstractToStringImpl;
import utilities.Statics;

/**
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 2.0
 */
public class ControlStatistique extends AbstractToStringImpl
{
    /*---------- ATTRIBUTS ----------*/

    private DaoStatistique dao;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlStatistique()
    {
        dao = DaoFactory.getMySQLDao(Statistique.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void majStatistiquesFichiers(LocalDate date)
    {
        Statistique fpj = dao.recupEltParIndexEtParType(date.toString(), StatistiqueEnum.FICHIERSPARJOUR);
        if (fpj == null)
            dao.persist(Statistique.build(date, StatistiqueEnum.FICHIERSPARJOUR).incrementerValeur());
        else
            dao.persist(fpj.incrementerValeur());
    }

    /**
     * Mise à jour du nombre de composants avec un Quality Gate à rouge dans SonarQube à ce jour.
     */
    public void majStatsComposKO()
    {
        int valeur = SonarAPI.build().getNbreComposQGKO();
        traitementGenerique(Statics.TODAY, valeur, StatistiqueEnum.COMPOSKO);
        int size = SonarAPI.build().getComposants(OptionGetCompos.MINIMALE, null).size();
        traitementGenerique(Statics.TODAY, valeur * 10000 / size, StatistiqueEnum.COMPOSKOP);
    }

    /**
     * Mise à jour du nombre de composants ne comportant ni défauts critiques ou majeurs à ce jour.
     */
    public void majStatsComposSansDefaut()
    {
        DaoComposantBase daoCompo = DaoFactory.getMySQLDao(ComposantBase.class);
        int valeur = daoCompo.composPropes();
        traitementGenerique(Statics.TODAY, daoCompo.composPropes(), StatistiqueEnum.COMPOSPROPRES);
        int size = SonarAPI.build().getComposants(OptionGetCompos.MINIMALE, null).size();
        traitementGenerique(Statics.TODAY, valeur * 10000 / size, StatistiqueEnum.COMPOSPROPRESP);
    }

    /**
     * Mise à jour du nombre d'anomalies qualimétriques en cours de traitement à ce jour.
     */
    public void majStatsAnosEnCours()
    {
        // On calcule ne nombre d'anomalie en cours en filtrant la liste de tous les défauts de qualité
        int valeur = (int) DaoFactory.getMySQLDao(DefautQualite.class).readAll().stream().filter(DefautQualite::calculDefautEnCours).count();

        // Enregistrement de la statistique
        traitementGenerique(Statics.TODAY, valeur, StatistiqueEnum.DEFAUTSENCOURS);
    }

    public void majStatsLdcTU()
    {

        int valeurToCover = traitementStatistique(StatistiqueCompoEnum.NEWLDCTOCOVER);
        int valeurNoCover = traitementStatistique(StatistiqueCompoEnum.NEWLDCNOCOVER);
        int valeur = (valeurToCover - valeurNoCover) * 10000 / valeurToCover;
        traitementGenerique(Statics.TODAY, valeur, StatistiqueEnum.NEWLDCTUP);
    }

    /*---------- METHODES PRIVEES ----------*/

    private int traitementStatistique(StatistiqueCompoEnum type)
    {
        // Map des valeurs par composant triéees par ordre chronologique descendant.
        Map<String, TreeMap<LocalDate, Integer>> map = new HashMap<>();

        // Récupération depuis la base de données des informations pour remplir la série
        List<StatistiqueCompo> liste = ((DaoStatistiqueCompo) DaoFactory.getMySQLDao(StatistiqueCompo.class)).recupAllParType(type);

        // Création d'une map avec les données. Pour chaque composant on aura une liste des lignes par date
        for (StatistiqueCompo stat : liste)
        {
            // On cherche dans la map, la treemap correspondante au composant, si on ne la trouve pas, on en crée une avec le bon comparateur intégré
            // Puis on ajoute la nouvelle valeur à celle-ci
            TreeMap<LocalDate, Integer> treeMap = map.computeIfAbsent(stat.getCompo().getNom(), k -> new TreeMap<LocalDate, Integer>((o1, o2) -> o2.compareTo(o1)));
            treeMap.put(stat.getDate(), stat.getValeur());
        }

        int retour = 0;
        for (TreeMap<LocalDate, Integer> donnees : map.values())
        {
            // Pour chaque treemap des composant, on cherche la dernière valeur la plus récente pour l'ajouter à la statistique
            Optional<Map.Entry<LocalDate, Integer>> optional = donnees.entrySet().stream().filter(e -> e.getKey().isBefore(Statics.TODAY.plusDays(1))).findFirst();
            if (optional.isPresent())
                retour += optional.get().getValue();
        }

        if (retour != 0)
            return retour;
        return 1;
    }

    /**
     * Traitement générique pour la mise à jour d'une statistique en base de données.
     * 
     * @param date
     *               Date de la statistique
     * @param valeur
     *               Valeur de la donnée
     * @param type
     *               Type de statistique
     */
    private void traitementGenerique(LocalDate date, int valeur, StatistiqueEnum type)
    {
        Statistique fpj = dao.recupEltParIndexEtParType(date.toString(), type);
        if (fpj != null)
            return;
        Statistique stat = Statistique.build(date, type);
        stat.setValeur(valeur);
        dao.persist(stat);
    }
    /*---------- ACCESSEURS ----------*/
}
