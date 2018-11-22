package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import control.excel.ControlGroupeProjets;
import control.excel.ExcelFactory;
import model.bdd.Produit;
import model.bdd.LotRTC;
import model.enums.GroupeProduit;
import model.enums.TypeColProduit;
import model.enums.TypeDonnee;

public class DaoProduit extends AbstractDao<Produit> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "projets_groupe";
    
    /*---------- CONSTRUCTEURS ----------*/
    
    DaoProduit()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    DaoProduit(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.GROUPE;
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Récupération des données du fichier Excel
        ControlGroupeProjets control = ExcelFactory.getReader(TypeColProduit.class, file);
        List<Produit> listeExcel = control.recupDonneesDepuisExcel();

        // Reset de la table
        resetTable();

        // Persistance en base
        int retour = persist(listeExcel);
        
        // Mise à des lots en focntion des groupes
        Map<String, Produit> map = readAllMap();
        DaoLotRTC daoLotRTC = DaoFactory.getDao(LotRTC.class);
        List<LotRTC> lotsRTC = daoLotRTC.readAll();
        
        for (LotRTC lotRTC : lotsRTC)
        {
            if (map.containsKey(lotRTC.getProjetRTC()))
                lotRTC.setGroupeProduit(map.get(lotRTC.getProjetRTC()).getGroupe());
            else
                lotRTC.setGroupeProduit(GroupeProduit.AUCUN);
        }
        
        daoLotRTC.persist(lotsRTC);
        majDateDonnee();
        return retour;
    }

    @Override
    protected void persistImpl(Produit t)
    {
        // Pas d'implémentation nécessaire           
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
