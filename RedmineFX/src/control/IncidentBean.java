package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dao.DaoIncident;
import model.Incident;
import utilities.GrowlException;
import utilities.enums.Severity;
import utilities.interfaces.Instance;

public class IncidentBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    //Daos

    /** Doa de la classe Incident */
    private DaoIncident daoi;
    
    // Attributs du Bean
    
    /* ---------- CONSTUCTORS ---------- */

    public IncidentBean()
    {
        instanciation();
    }
    
    @Override
    public void instanciation()
    {
    }

    /* ---------- METHODS ---------- */

    /**
     * Charge les incidents depuis la base Redmine en fonction du p�le choisi
     * @return
     * @throws GrowlException 
     */
    public List<Incident> chargerIncidents(String nomPole) throws GrowlException
    {
        if (nomPole == null || nomPole.isEmpty())
            throw new GrowlException(Severity.SEVERITY_ERROR,"Vous devez choisir un p�le", null);

        List<Incident> listIncidents = new ArrayList<>();
        listIncidents = daoi.findByProject(nomPole);
        System.out.println("nombre d'incident du p�le : " + listIncidents.size());       
        return listIncidents;
    }

    /* ---------- ACCESS ---------- */

}