package control.task.portfolio;

import static control.rest.SonarAPI.KEY;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IAttributeHandle;
import com.ibm.team.workitem.common.model.IWorkItem;

import control.excel.ControlPic;
import control.excel.ExcelFactory;
import control.rtc.ControlRTC;
import control.task.AbstractTask;
import dao.DaoFactory;
import model.LotSuiviPic;
import model.bdd.ComposantBase;
import model.enums.ColPic;
import model.enums.EnumRTC;
import model.enums.OptionGestionErreur;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.DateHelper;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Création d'un portfolio regroupant tous les composantq mis en production sur le trimestre donné depuis le fichier extrait de la PIC.
 * 
 * @author ETP8137 - grégoire Mathon
 * @since 2.0
 *
 */
public class CreerPortfolioTrimetrielDepuisPicTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    // Constantes statiques
    private static final short ETAPES = 4;
    private static final String TITRE = "Vue MEP/TEP";
    private static final Pattern PATTERNLOTCOMPTERENDU = Pattern.compile("\\|\n\\|");

    // Attributs de traitement
    private String vueKey;
    private File file;
    private ControlRTC controlRTC;
    private LocalDate debut;

    /*---------- CONSTRUCTEURS ----------*/

    public CreerPortfolioTrimetrielDepuisPicTask(File file, LocalDate debut)
    {
        super(ETAPES, TITRE);
        this.file = file;
        controlRTC = ControlRTC.build();
        this.debut = debut;
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
        Set<ComposantBase> set = recupSetComposDepuisPic();

        etapePlus();

        creerPortfolioTEP(set);
        return true;
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Récupération des composants depuis le fichier de la PIC.
     * 
     * @return
     *         Un set avec les composants extraits.
     */
    private Set<ComposantBase> recupSetComposDepuisPic()
    {
        // Initialisation variables
        IWorkItem wi;
        String compteRendu = Statics.EMPTY;
        Set<ComposantBase> retour = new HashSet<>();

        Collection<LotSuiviPic> liste = recupDonneesDepuisExcel();

        // Récupération des composants depuis la base de données avec comme clé le nom du composant
        Map<String, ComposantBase> mapCompos = recupComposDepuisBDD();

        // Affichage
        baseMessage += "Traitement du fichier Excel...OK\nRécupération des composants en base de données...OK\nTraitement des lots dans RTC :\n";
        updateMessage(Statics.EMPTY);
        etapePlus();
        int i = 0;
        int size = liste.size();

        // Itération sur la liste des lots Pic. Pour chaque objet, on va aller dans RTC pour récupérer le lot, puis les comptes-rendus de celui-ci pour avoir
        // le nom des composants pris en compte.
        for (LotSuiviPic lot : liste)
        {
            String numero = lot.getNumero();
            updateMessage("Lot " + numero);
            i++;
            updateProgress(i, size);

            try
            {
                // Récupération du lot dans RTC, puis recherche de l'attribut compte-rendu et récupération de la valeur
                wi = controlRTC.recupWorkItemDepuisId(Integer.parseInt(numero));

                for (IAttributeHandle handle : wi.getCustomAttributes())
                {
                    IAttribute attrb = controlRTC.recupEltDepuisHandle(IAttribute.class, handle, IAttribute.FULL_PROFILE);
                    if (attrb.getIdentifier().equals(EnumRTC.COMPTERENDU.getValeur()))
                    {
                        compteRendu = controlRTC.recupValeurAttribut(attrb, wi);
                        break;
                    }
                }
            }
            catch (TeamRepositoryException e)
            {
                throw new TechnicalException("control.task.creervue.CreerVueTrimestrielleDepuisPicTask.recupListeComposDepuisPic - plantage sur " + numero, e);
            }

            // Split du compte-rendu pour ressortir les noms des composants. le nom d'un composant est toujours aprés '|\n|' et se termine avec un '|'
            String[] split = PATTERNLOTCOMPTERENDU.split(compteRendu);
            for (String string2 : split)
            {
                String key = "Composant " + string2.substring(0, string2.indexOf('|')).trim();

                // On rajoute tous les composants que l'on trouve dans la base de données
                ComposantBase compo = mapCompos.get(key);
                if (compo != null)
                    retour.add(mapCompos.get(key));
            }
        }

        return retour;
    }

    private Collection<LotSuiviPic> recupDonneesDepuisExcel()
    {
        // Affichage
        baseMessage = "Récupérations des composants :\n";
        updateMessage(baseMessage + "Traitement du fichier Excel...");

        // Récupération des données depuis le fichier Excel
        ControlPic control = ExcelFactory.getReader(ColPic.class, file);
        Collection<LotSuiviPic> retour = control.recupDonneesDepuisExcel().values();
        updateMessage(baseMessage + "Traitement du fichier Excel...OK");
        etapePlus();  
        return retour;
    }

    /**
     * Récupération des composants depuis la base de données pour comparer avec le fichier Excel.
     * 
     * @return
     *         Map des ocmposants triés par nom.
     */
    private Map<String, ComposantBase> recupComposDepuisBDD()
    {
        // Affichage
        updateMessage(baseMessage + "Traitement du fichier Excel...OK\nRécupération des composants en base de données...");
        
        List<ComposantBase> liste = DaoFactory.getMySQLDao(ComposantBase.class).readAll();
        Map<String, ComposantBase> retour = new HashMap<>((int) (liste.size() * Statics.RATIOLOAD));
        for (ComposantBase compo : liste)
        {
            retour.put(compo.getNom(), compo);
        }
        return retour;
    }

    /**
     * Création du portfolio trimestriel dans SonarQube.
     * 
     * @param set
     *            Set des composants à utiliser.
     */
    private void creerPortfolioTEP(Set<ComposantBase> set)
    {
        // Création du portfolio

        // Création de a liste des dates c'est-à-dire les 3 mois depuis la date sélectionnée
        List<LocalDate> dates = Arrays.asList(debut, debut.plusMonths(1), debut.plusMonths(2));

        // Stringbuilders pour le nom de la vue
        StringBuilder builderMois = new StringBuilder();
        StringBuilder builderAnnee = new StringBuilder();

        // Liste regroupant les dates pour créer le nom de la vue
        List<String> listAnnees = new ArrayList<>();

        // Itération sur chaque date de la liste
        for (Iterator<LocalDate> iter = dates.iterator(); iter.hasNext();)
        {
            LocalDate date = iter.next();

            // rajoute le mois au nom de la vue.
            builderMois.append(DateHelper.dateFrancais(date, "MMM").replace(".", ""));
            if (iter.hasNext())
                builderMois.append('-');

            // Rajoute l'annee au nom de la vue ci celle-ci n'a pas deje été rajoutée
            String annee = DateHelper.dateFrancais(date, "yyyy");
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
        vueKey = new StringBuilder("TEPTEP").append(annees).append(mois).append(KEY).toString();
        String nomVue = new StringBuilder("TEP ").append(Statics.SPACE).append(annees).append(Statics.SPACE).append(mois).toString();
        baseMessage = "Portfolio " + nomVue + Statics.NL;
        updateMessage(Statics.EMPTY);
        ObjetSonar parent = creerObjetSonar(vueKey, nomVue, new StringBuilder("Vue des lots mis en production pendant les mois de ").append(mois).append(Statics.SPACE).append(annees).toString(),
                TypeObjetSonar.PORTFOLIO, true);

        // Ajout des sous-vue
        int i = 0;
        int size = set.size();
        for (ComposantBase compo : set)
        {
            if (isCancelled())
                return;

            updateMessage("ajout : " + compo.getNom());
            i++;
            updateProgress(i, size);
            api.ajouterSousProjet(parent, compo.getKey());
        }

        api.calculObjetSonar(parent);
    }

    /*---------- ACCESSEURS ----------*/

}
