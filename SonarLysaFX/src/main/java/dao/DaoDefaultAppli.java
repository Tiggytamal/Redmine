package dao;

import java.io.File;
import java.io.Serializable;

import javax.persistence.EntityManager;

import model.bdd.ComposantSonar;
import model.bdd.DefaultAppli;
import model.bdd.DefaultQualite;
import model.enums.TypeDonnee;

public class DaoDefaultAppli extends AbstractDao<DefaultAppli> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "defaults_application";

    /*---------- CONSTRUCTEURS ----------*/

    DaoDefaultAppli()
    {
        super(TABLE);

        typeDonnee = TypeDonnee.DEFAULTQUALITE;
    }

    DaoDefaultAppli(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.DEFAULTQUALITE;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public boolean persistImpl(DefaultAppli defAppli)
    {
        if (defAppli.getIdBase() == 0)
        {
            defAppli.initTimeStamp();

            // Gestion composantSonar
            persistSousObjet(ComposantSonar.class, defAppli.getCompo());

            // Gestion DefaultQualite
            persistSousObjet(DefaultQualite.class, defAppli.getDefaultQualite());

            // Persistance
            em.persist(defAppli);
            return true;
        }
        else
            em.merge(defAppli);
        return false;
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
