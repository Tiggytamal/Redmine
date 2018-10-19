package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import model.bdd.DateMaj;
import model.enums.TypeDonnee;

public class DaoDateMaj extends AbstractDao<DateMaj> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "dates_maj";

    /*---------- CONSTRUCTEURS ----------*/

    DaoDateMaj()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.DATEMAJ;
    }

    DaoDateMaj(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.DATEMAJ;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        // Pas de récupération depuis un fichier Excel
        return 0;
    }
    
    @Override
    public DateMaj recupEltParIndex(String typeDonnee)
    {        
        List<DateMaj> liste = em.createNamedQuery(modele.getSimpleName() + FINDINDEX, modele).setParameter(INDEX, TypeDonnee.valueOf(typeDonnee)).getResultList();
        if (liste.isEmpty())
            return null;
        else
            return liste.get(0);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
