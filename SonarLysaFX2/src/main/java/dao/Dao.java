package dao;

import java.io.File;
import java.util.List;
import java.util.Map;

import model.bdd.AbstractBDDModele;

public interface Dao<T extends AbstractBDDModele<I>, I>
{
    
    /**
     * Extrait les données depuis un fichier Excel et les persiste en base.
     * 
     * @param file
     *             Fichier à traiter.
     * @return
     *         Le nombre d'objet créés depuis le fichier.
     */
    int recupDonneesDepuisExcel(File file);
    
    /**
     * Vidage du cache de second niveau des managers.
     */
    void clearCache();
    
    /**
     * Retourne toutes les données de la table sous forme d'une liste
     * 
     * @return
     *         La liste de tous les objets récupérés en base.
     */
    List<T> readAll();
    
    /**
     * Retourne une map de toutes les données en map avec une clef dépendante de la classe
     * 
     * @return
     *         La map des objets.
     */
    Map<I, T> readAllMap();
    
    /**
     * Récupère un élément de la base de donnée selon son code.
     * 
     * @param index
     *              Index de l'objet.
     * @return
     *         L'objet ou null.
     */
    T recupEltParIndex(I index);
    
    /**
     * Methode de sauvegarde d'une collection d'éléments.<br>
     * Retourne le nombre d'éléments enregistrés.
     * 
     * @param collection
     *                   Collection des objets à enregistrer.
     * @return
     *         Le nombre d'objets persistés.
     */
    int persist(Iterable<T> collection);
    
    /**
     * Methode de sauvegarde d'un element.<br>
     * Retourne vrai si l'objet a bien été persisté, faux pour un merge.
     * 
     * @param t
     *          L'objet à persister.
     * @return
     *         Vrai si l'objet a été persisté.
     */
    boolean persist(T t);
    
    /**
     * Suppression d'une liste d'éléments de la base de données. Retourne le nombre d'éléments supprimés.
     * 
     * @param collection
     *                   Le liste des objets à supprimés.
     * @return
     *         Le nombre d'objets supprimés.
     */
    int delete(Iterable<T> collection);
    
    /**
     * Supprime un element de la base de données. retourne vrai si un élément a été supprimé.
     * 
     * @param t
     *          L'élément à supprimer.
     * @return
     *         Vrai si l'élément a été supprimé.
     */
    boolean delete(T t);
    
    /**
     * Remise à zero de la table et de l'indice de clef primaire.<br>
     * A utiliser avec précaution.
     * 
     * @return
     *         Le nombre de lignes supprimées.
     */
    int resetTable();
}
