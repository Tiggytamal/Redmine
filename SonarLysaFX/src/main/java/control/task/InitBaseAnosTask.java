package control.task;

import static utilities.Statics.proprietesXML;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import control.excel.ControlSuivi;
import control.excel.ExcelFactory;
import dao.DaoAnomalie;
import dao.DaoFactory;
import model.bdd.DefaultQualite;
import model.bdd.LotRTC;
import model.enums.Param;
import model.enums.TypeColSuivi;

public class InitBaseAnosTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Initialisation table des anomalies";
    private static final short ETAPES = 4;

    /*---------- CONSTRUCTEURS ----------*/

    public InitBaseAnosTask()
    {
        super(ETAPES, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'annulation pour le momment
    }

    @Override
    public Boolean call() throws Exception
    {
        updateMessage("Remise à zéro de la table des anomalies");
        DaoFactory.getDao(DefaultQualite.class).resetTable();
        
        etapePlus();
        String base = "Traitement du fichier JAVA :\n";
        updateMessage(base);
        boolean java = sauvegarde(initDefaults(proprietesXML.getMapParams().get(Param.NOMFICHIERJAVA), base));
        
        etapePlus();
        base = "Traitement du fichier DATASTAGE :\n";
        updateMessage(base);
        boolean dataStage = sauvegarde(initDefaults(proprietesXML.getMapParams().get(Param.NOMFICHIERDATASTAGE), base));
        
        etapePlus();
        base = "Traitement du fichier COBOL :\n";
        updateMessage(base);
        boolean cobol =  sauvegarde(initDefaults(proprietesXML.getMapParams().get(Param.NOMFICHIERCOBOL), base));
        
        return java && dataStage && cobol;
    }

    /*---------- METHODES PRIVEES ----------*/

    private Collection<DefaultQualite> initDefaults(String fichier, String base)
    {
        String name = proprietesXML.getMapParams().get(Param.ABSOLUTEPATH) + fichier;
        ControlSuivi controlAno = ExcelFactory.getReader(TypeColSuivi.class, new File(name));
        
        updateMessage(base + "Récupération des lots en base...");
        Map<String, LotRTC> lotsRTC = DaoFactory.getDao(LotRTC.class).readAllMap();
        
        updateMessage(base + "Traitement des défaults en cours");
        List<DefaultQualite> liste = controlAno.recupAnoEnCoursDepuisExcel(lotsRTC, this);
        
        updateMessage(base + "Traitement des défaults clos");
        liste.addAll(controlAno.recupAnoClosesDepuisExcel(lotsRTC, this));
        
        updateMessage(base + "Traitement des défaults abandonnés");
        return controlAno.controlAnoAbandon(liste, lotsRTC);
    }
    
    private boolean sauvegarde(Collection<DefaultQualite> liste)
    {
        DaoAnomalie dao = DaoFactory.getDao(DefaultQualite.class);
        int taille = dao.persist(liste);
        dao.majDateDonnee();
        return taille > 0;
    }

    /*---------- ACCESSEURS ----------*/

}
