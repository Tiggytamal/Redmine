package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import model.bdd.DateMaj;
import model.enums.TypeDonnee;

public class DaoDateMaj extends AbstractDao<DateMaj> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;

    /*---------- CONSTRUCTEURS ----------*/

    DaoDateMaj()
    {
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
    public DateMaj recupEltParCode(String typeDonnee)
    {        
        List<DateMaj> liste = em.createNamedQuery("DateMaj.findByIndex", DateMaj.class).setParameter("index", TypeDonnee.valueOf(typeDonnee)).getResultList();
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
        retour = em.createNamedQuery("DateMaj.resetTable").executeUpdate();
        em.createNativeQuery("ALTER TABLE dates_maj AUTO_INCREMENT = 0").executeUpdate();
        em.getTransaction().commit();
        return retour;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
