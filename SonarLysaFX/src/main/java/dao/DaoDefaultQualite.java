package dao;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import model.bdd.DefautQualite;
import model.enums.Matiere;
import model.enums.TypeDonnee;

public class DaoDefaultQualite extends AbstractDao<DefautQualite> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "defaults_qualite";

    /*---------- CONSTRUCTEURS ----------*/

    DaoDefaultQualite()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.DEFAULTQUALITE;
    }

    DaoDefaultQualite(EntityManager em)
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

    public Map<String, DefautQualite> readAllMapMatiere(Matiere matiere)
    {
        Map<String, DefautQualite> retour = new HashMap<>();

        // Itération pour ne prendre que les anomalies de la matière désirée
        for (DefautQualite dq : readAll())
        {
            if (dq.getLotRTC().getMatieres().contains(matiere))
                retour.put(dq.getMapIndex(), dq);
        }

        return retour;
    }

    @Override
    protected void persistImpl(DefautQualite t)
    {
        // Pas d'implémentation nécessaire       
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
