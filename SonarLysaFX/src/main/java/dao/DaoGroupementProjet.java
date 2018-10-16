package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlGroupeProjets;
import control.excel.ExcelFactory;
import model.bdd.GroupementProjet;
import model.bdd.LotRTC;
import model.enums.GroupeProjet;
import model.enums.TypeColGrProjet;
import model.enums.TypeDonnee;

public class DaoGroupementProjet extends AbstractDao<GroupementProjet> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DaoGroupementProjet()
    {
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    DaoGroupementProjet(EntityManager em)
    {
        super(em);
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlGroupeProjets control = ExcelFactory.getReader(TypeColGrProjet.class, file);
        List<GroupementProjet> listeExcel = control.recupDonneesDepuisExcel();

        // Reset de la table
        resetTable();

        // Persistance en base
        int retour = persist(listeExcel);
        
        // Mise à des lots en focntion des groupes
        Map<String, GroupementProjet> map = readAllMap();
        DaoLotRTC daoLotRTC = DaoFactory.getDao(LotRTC.class);
        List<LotRTC> lotsRTC = daoLotRTC.readAll();
        
        for (LotRTC lotRTC : lotsRTC)
        {
            if (map.containsKey(lotRTC.getProjetRTC()))
                lotRTC.setGroupe(map.get(lotRTC.getProjetRTC()).getGroupe());
            else
                lotRTC.setGroupe(GroupeProjet.AUCUN);
        }
        
        daoLotRTC.persist(lotsRTC);
        majDateDonnee();
        return retour;
    }

    @Override
    public GroupementProjet recupEltParCode(String nomProjet)
    {
        List<GroupementProjet> liste = em.createNamedQuery("GroupementProjet.findByIndex", GroupementProjet.class).setParameter("index", nomProjet).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    @Override
    public int resetTable()
    {
        int retour = 0;
        em.getTransaction().begin();
        retour = em.createNamedQuery("GroupementProjet.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE projets_groupe AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

}
