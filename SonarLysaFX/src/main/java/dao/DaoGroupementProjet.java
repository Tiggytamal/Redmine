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
    private static final String TABLE = "projets_groupe";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DaoGroupementProjet()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    DaoGroupementProjet(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

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
    protected void persistImpl(GroupementProjet t)
    {
        // Pas d'implémentation nécessaire           
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
