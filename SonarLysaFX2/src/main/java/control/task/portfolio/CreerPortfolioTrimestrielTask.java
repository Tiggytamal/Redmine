package control.task.portfolio;

import static control.rest.SonarAPI.KEY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mchange.util.AssertException;

import control.task.AbstractTask;
import model.KeyDateMEP;
import model.enums.OptionGestionErreur;
import model.enums.OptionPortfolioTrimestriel;
import model.enums.Param;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.DateHelper;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Permet de créer les porfolios trimestriels des composants mis en production.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class CreerPortfolioTrimestrielTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short ETAPES = 3;
    private static final int TRIMESTRE = 3;
    private static final String TITRE = "Vue MEP/TEP";

    private String vueKey;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private OptionPortfolioTrimestriel option;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioTrimestrielTask(LocalDate dateDebut, OptionPortfolioTrimestriel option)
    {
        super(ETAPES, TITRE);
        this.dateDebut = dateDebut;
        dateFin = dateDebut.plusMonths(TRIMESTRE).minusDays(1);
        annulable = true;
        this.option = option;
    }

    /**
     * Constructeur sans paramètre pour les tests. Ne pas utiliser, lance une exception
     */
    public CreerPortfolioTrimestrielTask()
    {
        super(ETAPES, TITRE);
        throw new AssertException();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        if (vueKey != null && !vueKey.isEmpty())
            api.supprimerObjetSonar(vueKey, TypeObjetSonar.PORTFOLIO, OptionGestionErreur.OUI);
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected Boolean call() throws Exception
    {
        // Variables
        List<KeyDateMEP> compos;
        baseMessage = "Récupérations des lots dans Sonar";
        updateMessage(baseMessage + "...");

        // Récupération des données
        switch (option)
        {
            case DATASTAGE:
                compos = recupKeyComposDataStage();
                break;

            case ALL:
                compos = recupKeyCompos();
                break;

            default:
                throw new TechnicalException("control.task.CreerVueTrimestrielleTask.creerVueTrimestrielle : option inconnue - " + option);
        }

        updateMessage(" OK");
        etapePlus();

        // Tri des composants par mois de mise en production
        TreeMap<LocalDate, List<KeyDateMEP>> mapLot = trierComposPourTEP(compos);
        etapePlus();

        // Création des vues trimestrielles
        creerPortfolioTrimestriel(mapLot);

        return Boolean.TRUE;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    /**
     * Récupération et tri des composants par date pour la création de la vue TEP.
     * 
     * @param compos
     *               Listes des objets représentants les composants à trier (cle, nom et date).
     * @return
     *         Une TreeMap triées des clefs des composants.
     */
    private TreeMap<LocalDate, List<KeyDateMEP>> trierComposPourTEP(List<KeyDateMEP> compos)
    {
        TreeMap<LocalDate, List<KeyDateMEP>> retour = new TreeMap<>();

        // Affichage et variables
        baseMessage = "Traitement RTC :\n";
        int size = compos.size();
        int i = 0;

        // Itération sur les lots Sonar
        for (KeyDateMEP compo : compos)
        {
            // Création d'une nouvelle date au 1er du mois qui servira du clef à la map.
            LocalDate clef = LocalDate.of(compo.getDate().getYear(), compo.getDate().getMonth(), 1);
            retour.computeIfAbsent(clef, k -> new ArrayList<>()).add(compo);

            i++;
            updateProgress(i, size);
        }
        return retour;
    }

    /**
     * Récupère toutes les clefs des composants.
     *
     * @return
     *         Liste des valeur récupérées en base.
     */
    private List<KeyDateMEP> recupKeyCompos()
    {
        return daoCompo.recupKeyComposTEP(dateDebut, dateFin);
    }

    /**
     * Récupère toutes les clefs des composants DataStage.
     *
     * @return
     *         Liste des valeur récupérées en base.
     */
    private List<KeyDateMEP> recupKeyComposDataStage()
    {
        List<KeyDateMEP> retour = recupKeyCompos();

        for (Iterator<KeyDateMEP> iter = retour.iterator(); iter.hasNext();)
        {
            if (!iter.next().getNom().startsWith(Statics.proprietesXML.getMapParams().get(Param.FILTREDATASTAGE)))
                iter.remove();
        }
        return retour;
    }

    /**
     * Crée la vue Sonar pour une recherche trimetrielle des composants mis en production.
     *
     * @param mapCle
     *               Map des compoasnts à ajouter à la vue.
     */
    private void creerPortfolioTrimestriel(TreeMap<LocalDate, List<KeyDateMEP>> mapCle)
    {
        if (isCancelled())
            return;

        // Liste des composants à ajouter à la vue
        List<KeyDateMEP> composTEP = new ArrayList<>();

        // Stringbuilders pour le nom de la vue
        StringBuilder builderMois = new StringBuilder();
        StringBuilder builderAnnee = new StringBuilder();

        // Liste regroupant les dates pour créer le nom de la vue
        List<String> listAnnees = new ArrayList<>();

        // Itération sur chaque entry de la map
        for (Iterator<Entry<LocalDate, List<KeyDateMEP>>> iter = mapCle.entrySet().iterator(); iter.hasNext();)
        {
            Entry<LocalDate, List<KeyDateMEP>> entry = iter.next();

            // Regroupe tous les composants dans la même liste.
            composTEP.addAll(entry.getValue());

            // Récupération de la date
            LocalDate clef = entry.getKey();

            // rajoute le mois au nom de la vue.
            builderMois.append(DateHelper.dateFrancais(clef, "MMM").replace(".", ""));
            if (iter.hasNext())
                builderMois.append('-');

            // Rajoute l'annee au nom de la vue ci celle-ci n'a pas deje été rajoutee
            String annee = DateHelper.dateFrancais(clef, "yyyy");
            if (!listAnnees.contains(annee))
            {
                listAnnees.add(annee);
                builderAnnee.append(annee);
                if (iter.hasNext())
                    builderAnnee.append('-');
            }
        }

        // Suppression - au besoin à la fin du texte
        if (builderAnnee.charAt(builderAnnee.length() - 1) == '-')
            builderAnnee.deleteCharAt(builderAnnee.length() - 1);

        String mois = builderMois.toString();
        String annees = builderAnnee.toString();

        // Création de la vue dans SonarQube
        vueKey = new StringBuilder("TEPTEP").append(option.getTitre()).append(annees).append(mois).append(KEY).toString();
        String nomVue = new StringBuilder("TEP ").append(option.getTitre()).append(Statics.SPACE).append(annees).append(Statics.SPACE).append(mois).toString();
        etapePlus();
        String base = "Vue " + nomVue + Statics.NL;
        updateMessage(base);
        ObjetSonar parent = creerObjetSonar(vueKey, nomVue, new StringBuilder("Vue des lots mis en production pendant les mois de ").append(mois).append(Statics.SPACE).append(annees).toString(),
                TypeObjetSonar.PORTFOLIO, true);

        // Ajout des sous-vue
        int i = 0;
        int size = composTEP.size();
        for (KeyDateMEP cle : composTEP)
        {
            if (isCancelled())
                return;

            updateMessage(base + "ajout : " + cle.getNom());
            i++;
            updateProgress(i, size);
            api.ajouterSousProjet(parent, cle.getCle());
        }

        api.calculObjetSonar(parent);
    }
}
