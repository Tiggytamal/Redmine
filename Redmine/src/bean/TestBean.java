package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import dao.DaoChamp;
import dao.DaoIncident;
import dao.DaoProjet;
import model.Incident;

@ManagedBean (name = "test")
@RequestScoped
public class TestBean implements Serializable
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    /** Doa de la classe Incident */
    @EJB
    private DaoIncident daoi;

    /** Dao de la classe Projet */
    @EJB
    private DaoProjet daop;

    /** Dao de la classe Champ */
    @EJB
    private DaoChamp daoc;

    /** Liste des noms P�les � afficher */
    private List<String> listNomsProjets;

    /** nom du p�le s�lectionn� */
    private String nom;

    /** Liste des incidents */
    private List<Incident> listIncidents;
    
    /* ---------- CONSTUCTORS ---------- */

    /* ---------- METHODS ---------- */

    @PostConstruct
    private void postConstruct()
    {
        listNomsProjets = daop.findAllPoleNames();
    }

    public String chargerIncidents()
    {
        if (nom == null)
            return "";
        
        if (nom.equals("all"))
            listIncidents = daoi.readAll();
        else
            listIncidents = daoi.findByProject(nom);
        
        listBean.setListIncidents(listIncidents);
        
        System.out.println(listIncidents.size());
        triageIncident();

        return "";
    }

    /**
     * Permet de trier la liste des incidents en ne gardant que ceux du mois en cours
     */
    private void triageIncident()
    {
        // R�cup�ration de la date du jour
        LocalDate date = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Iterator<Incident> iter = listIncidents.iterator(); iter.hasNext();)
        {
            Incident incident = iter.next();
            String dateString = incident.getMapValeurs().get("Date de prise en charge");
            if (dateString == null)
            {
                iter.remove();
                continue;
            }
            LocalDate dateIncident = null;
            try
            {
                dateIncident = LocalDate.parse(dateString.substring(0, 10), f);
            }
            catch (DateTimeParseException e)
            {
                iter.remove();
                continue;
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println(dateString);
                iter.remove();
                continue;
            }
            if (dateIncident == null || dateIncident.getYear() != date.getYear() || dateIncident.getMonth() != date.getMonth())
                iter.remove();
        }
        System.out.println(listIncidents.size());
    }

    /* ---------- ACCESS ---------- */

    /**
     * @return the listProjets
     */
    public List<String> getListNomsProjets()
    {
        return listNomsProjets;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @param listNomsProjets
     *            the listProjets to set
     */
    public void setListNomsProjets(List<String> listNomsProjets)
    {
        this.listNomsProjets = listNomsProjets;
    }
    
    /**
     * @return the listBean
     */
    public ListBean getListBean()
    {
        return listBean;
    }

    /**
     * @param listBean the listBean to set
     */
    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }
}