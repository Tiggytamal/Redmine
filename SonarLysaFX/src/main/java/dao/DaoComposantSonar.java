package dao;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.enums.TypeDonnee;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de données
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class DaoComposantSonar extends AbstractDao<ComposantSonar> implements Serializable
{
    /*---------- ATTRIBUTS ----------*/

    private static final long serialVersionUID = 1L;
    private static final String TABLE = "composants";

    /*---------- CONSTRUCTEURS ----------*/

    DaoComposantSonar()
    {
        super(TABLE);
        typeDonnee = TypeDonnee.COMPOSANT;
    }

    DaoComposantSonar(EntityManager em)
    {
        super(TABLE, em);
        typeDonnee = TypeDonnee.COMPOSANT;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public int recupDonneesDepuisExcel(File file)
    {
        return 0;
    }

    @Override
    public void persistImpl(ComposantSonar compo)
    {
            // Presistance application liée
            persistSousObjet(Application.class, compo.getAppli());

            // Persistance lot lié
            persistSousObjet(LotRTC.class, compo.getLotRTC());
            
            // Persistance edition liée
            persistSousObjet(Edition.class, compo.getEdition());
    }

    public void controleDoublons()
    {
        List<ComposantSonar> aSupprimer = new ArrayList<>();
        Map<String, ComposantSonar> map = new HashMap<>();

        for (ComposantSonar compo : readAll())
        {
            ComposantSonar compoMap = map.get(compo.getMapIndex());
            if (compoMap == null)
                map.put(compo.getMapIndex(), compo);
            else
            {
                if (compoMap.getIdBase() > compo.getIdBase())
                    aSupprimer.add(compoMap);
                else
                    aSupprimer.add(compo);
            }
        }

        delete(aSupprimer);

    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
