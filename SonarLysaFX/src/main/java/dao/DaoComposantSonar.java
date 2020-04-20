package dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import model.bdd.Application;
import model.bdd.ComposantSonar;
import model.bdd.LotRTC;
import model.enums.TypeDonnee;

/**
 * Classe de DOA pour la sauvegarde des composants Sonar en base de donn�es
 * 
 * @author ETP8137 - Gr�goire Mathon
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
        // Presistance application li�e
        persistSousObjet(Application.class, em, compo.getAppli());

        // Persistance lot li�
        persistSousObjet(LotRTC.class, em, compo.getLotRTC());
    }
    
    public void updateVraisDefauts(int val, String nom)
    {
        em.getTransaction().begin();
        em.createNativeQuery("update composants c set c.vrais_defauts = ?1 where c.nom = ?2").setParameter(1, val).setParameter(2, nom).executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * R�cup�ration de la liste des num�ros de lots avec un composant Sonar
     * 
     * @return
     */
    public List<String> recupLotsAvecComposants()
    {
        return em.createNamedQuery("ComposantSonar.recupLotsRTC", String.class).getResultList();
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
