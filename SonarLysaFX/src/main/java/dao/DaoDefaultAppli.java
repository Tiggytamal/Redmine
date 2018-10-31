package dao;

import java.io.File;
import java.io.Serializable;

import javax.persistence.EntityManager;

import model.bdd.ComposantSonar;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.enums.TypeDonnee;

public class DaoDefaultAppli extends AbstractDao<DefautAppli> implements Serializable
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
    public void persistImpl(DefautAppli defAppli)
    {
            // Gestion composantSonar
            persistSousObjet(ComposantSonar.class, defAppli.getCompo());

            // Gestion DefaultQualite
            persistSousObjet(DefautQualite.class, defAppli.getDefautQualite());
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
