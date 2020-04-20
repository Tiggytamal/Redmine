package junit;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.powermock.reflect.Whitebox;

import control.parsing.ControlXML;
import dao.DaoComposantBase;
import dao.DaoFactory;
import model.DataBaseXML;
import model.KeyDateMEP;
import model.ModelFactory;
import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ComposantBase;
import model.bdd.ComposantErreur;
import model.bdd.DefautQualite;
import model.bdd.IssueBase;
import model.bdd.LotRTC;
import model.bdd.Utilisateur;
import model.enums.EtatLot;
import model.enums.Param;
import model.enums.TypeEdition;
import utilities.Statics;
import utilities.TechnicalException;

/**
 * Classe permettant d'initialiser la base de données en XML pour les tsts unitaires.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class InitDataBaseXML
{
    /*---------- ATTRIBUTS ----------*/

    private DataBaseXML db;
    private DaoComposantBase daoCompos;

    /*---------- CONSTRUCTEURS ----------*/

    public InitDataBaseXML()
    {
        db = ModelFactory.build(DataBaseXML.class);
        daoCompos = DaoFactory.getMySQLDao(ComposantBase.class);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public static void main(String[] args)
    {
        new InitDataBaseXML().initDatabaseXML();
    }

    public void initDatabaseXML()
    {
        // 1. Création liste des composants
        initComposantBase();

        // 2. Création liste des Defauts Qualite
        initDefautQualite();

        // 3. Création liste des lots RTC
        initLotRTC();

        // 4. Création chefs de service
        initChefService();

        // 5. Création ComposantsErreur
        initComposErreur();

        // 6. Création de la liste des cles pour les vues TEP
        initKeyDateMEP();

        // 7. Création de la liste des utilisateurs
        initUtilisateur();

        // 8. Création de la liste des IssueBase de SonarQube
        initIssueBase();
        
        // 9. Création de la liste des applications
        initApps();

        // Sauvegarde de la base de données XML
        new ControlXML().saveXML(db);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    private void initComposantBase()
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int m = 0;
        int n = 0;
        
        for (ComposantBase compo : daoCompos.readAll())
        {
            LotRTC lot = compo.getLotRTC();

            if (lot == null)
            {
                if (j < 2)
                {
                    db.getCompos().add(compo);
                    j++;
                }
            }
            else if (k < 2 && compo.getNom().contains(Statics.proprietesXML.getMapParams().get(Param.FILTREDATASTAGE)))
            {
                db.getCompos().add(compo);
                k++;
            }
            else if (i < 4 && compo.getNom().endsWith(" 15"))
            {
                db.getCompos().add(compo);
                i++;
            }
            else if (l < 20 && !compo.getNom().matches("\\s\\d{2}$"))
            {
                db.getCompos().add(compo);
                l++;
            }
            else if (lot.getEdition() != null)
            {
                if (m < 5 && lot.getEdition().getTypeEdition() == TypeEdition.CHC)
                {
                    m++;
                    db.getCompos().add(compo);
                }
                else if (n < 5 && lot.getEdition().getTypeEdition() == TypeEdition.CDM)
                {
                    n++;
                    db.getCompos().add(compo);
                }

            }

            if (i == 20 && j == 2 && k == 2 && l == 20 && m == 10)
                break;
        }
    }

    private void initDefautQualite()
    {
        List<DefautQualite> dqs = DaoFactory.getMySQLDao(DefautQualite.class).readAll();
        db.getDqs().addAll(dqs);
    }

    private void initLotRTC()
    {
        // Récupération des lots
        List<LotRTC> lotsRTC = DaoFactory.getMySQLDao(LotRTC.class).readAll();

        // tri du plus recent au plus ancient
        Collections.sort(lotsRTC, (o1, o2) -> o2.getDateMajEtat().compareTo(o1.getDateMajEtat()));

        // Ajout des 200 premiers lots.
        int i = 0;
        for (LotRTC lotRTC : lotsRTC)
        {
            if (i < 199)
            {
                db.getLotsRTC().add(lotRTC);
                i++;
            }

            if (lotRTC.getNumero().equals("310979"))
            {
                db.getLotsRTC().add(lotRTC);
                continue;
            }

            if (i == 200)
                break;

            try
            {
                // Création lot sans edition null
                if (i % 30 == 0)
                    Whitebox.getField(LotRTC.class, "edition").set(lotRTC, null);

                // Création lot avec libellé vide
                if (i % 33 == 0)
                    Whitebox.getField(LotRTC.class, "libelle").set(lotRTC, Statics.EMPTY);

                // Création lot avec projet clairity null
                if (i % 40 == 0)
                    Whitebox.getField(LotRTC.class, "projetClarity").set(lotRTC, null);

                // Création lot hs
                if (i % 98 == 0)
                    Whitebox.getField(LotRTC.class, "rtcHS").set(lotRTC, true);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                throw new TechnicalException("Plantage mise à null edition d'un lot : " + lotRTC.getNumero());
            }
        }
    }

    private void initChefService()
    {
        // Récupération de la liste en base de donnees
        List<ChefService> liste = DaoFactory.getMySQLDao(ChefService.class).readAll();

        // Itération sur la liste pour retirer les chefs non indispensables, c'est-à-dire les inconnus sauf celui du service INCONNU.
        for (Iterator<ChefService> iter = liste.iterator(); iter.hasNext();)
        {
            ChefService chef = iter.next();
            if (!Statics.INCONNU.equals(chef.getService()) && "Chef de Service inconnu".equals(chef.getNom()))
                iter.remove();
        }

        db.getChefService().addAll(liste);
    }

    private void initComposErreur()
    {
        List<ComposantErreur> liste = DaoFactory.getMySQLDao(ComposantErreur.class).readAll();
        db.getComposErreur().addAll(liste);
    }

    private void initKeyDateMEP()
    {
        // Date initiale
        LocalDate debut = LocalDate.of(2019, 1, 1);

        for (ComposantBase compo : daoCompos.readAll())
        {
            if (compo.getLotRTC() != null && compo.getLotRTC().getDateMajEtat() != null && compo.getLotRTC().getEtatLot() == EtatLot.EDITION)
            {
                LocalDate date = compo.getLotRTC().getDateMajEtat();
                if (date.isAfter(debut.minusDays(1)) && date.isBefore(debut.plusMonths(3)))
                    db.getKeyDateMEPs().add(KeyDateMEP.build(compo.getKey(), compo.getNom(), date));
            }
        }

        // Ajout pour DataStage
        db.getKeyDateMEPs().add(KeyDateMEP.build("fr.ca.cat.ds_dwth_DataStage", "Composant DS_DataSatge", debut.plusDays(10)));

    }

    private void initUtilisateur()
    {
        // Base de données
        List<Utilisateur> liste = DaoFactory.getMySQLDao(Utilisateur.class).readAll();

        // Récupération de 10 utilisateurs + AQP
        int i = 0;
        int j = 0;
        for (Utilisateur user : liste)
        {
            if (j < 3)
            {
                if (user.getNom().equals("MATHON Gregoire") || user.getNom().equals("TRICOT Nicolas") || user.getNom().equals("PRUDENT Alain"))
                {
                    db.getUsers().add(user);
                    j++;
                }
            }

            if (i < 10)
            {
                db.getUsers().add(user);
                i++;
            }

            if (j == 3 && i == 10)
                break;
        }
    }

    private void initIssueBase()
    {
        // Base de données
        List<IssueBase> liste = DaoFactory.getMySQLDao(IssueBase.class).readAll();
        System.out.println(liste.size());
    }
    
    private void initApps()
    {
        List<Application> liste = DaoFactory.getMySQLDao(Application.class).readAll();
        db.getApps().addAll(liste);
    }
}
