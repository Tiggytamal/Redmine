package control.task;

import java.time.LocalDate;
import java.util.List;

import control.rest.ControlRepack;
import dao.DaoEdition;
import dao.DaoFactory;
import model.bdd.ComposantSonar;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.rest.repack.RepackREST;
import utilities.Statics;

public class MajComposRepackTask extends AbstractTask
{
    /*---------- ATTRIBUTS ----------*/

    private static final String TITRE = "Mise à jour des Lots RTC";
    private static final int ETAPES = 1;


    /*---------- CONSTRUCTEURS ----------*/

    public MajComposRepackTask()
    {
        super(ETAPES, TITRE);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void annulerImpl()
    {
        // Pas d'implémentation pour le moment
    }

    @Override
    public Boolean call() throws Exception
    {
        majComposRepack();
        return DaoFactory.getDao(ComposantSonar.class).persist(mapCompos.values()) > 0;
    }

    /*---------- METHODES PRIVEES ----------*/

    private boolean majComposRepack()
    {
        // Itération sur tous les composants Sonar
        for (ComposantSonar compo : mapCompos.values())
        {
            traitementCompo(compo);
            if (compo.getDateRepack() != null)
                traitementLot(compo);
        }
        return true;
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * 
     * @param compo
     */
    private void traitementCompo(ComposantSonar compo)
    {
        // Récupération de tous les repacks du composants
        List<RepackREST> repacks = ControlRepack.INSTANCE.getRepacksComposant(compo);

        // Récupératoin de la date de chaque repack pour garder la plus recente de toute
        LocalDate date = Statics.DATEINCONNUE;
        for (RepackREST repackREST : repacks)
        {
            if (repackREST.getNomGc() != null && repackREST.getNomGc() != "BOREAL_Packaging")
            {
                LocalDate temp = calculDateRepack(repackREST.getNomGc());
                if (temp.isAfter(date))
                    date = temp;
            }
        }

        if (date != Statics.DATEINCONNUE)
            compo.setDateRepack(date);
    }

    /**
     * 
     * @param compo
     */
    private void traitementLot(ComposantSonar compo)
    {

        LotRTC lot = compo.getLotRTC();
        LocalDate repackCompo = compo.getDateRepack();

        if (Statics.DATEINCO2099.equals(lot.getEdition().getDateMEP()) && (lot.getDateRepack() == null || repackCompo.isAfter(lot.getDateRepack())))
                lot.setDateRepack(repackCompo);
    }

    /**
     * 
     * @param nomGc
     * @return
     */
    private LocalDate calculDateRepack(String nomGc)
    {
        if (nomGc == null)
            return Statics.DATEINCONNUE;

        DaoEdition dao = DaoFactory.getDao(Edition.class);
        if (nomGc.matches("^E[2-9]\\d_GC\\d\\d[A-Z]?$"))
            return dao.readAllMap().get(nomGc.substring(0, 3)).getDateMEP();

        if (nomGc.matches("^E[2-9]\\d_CDMS\\d\\d$"))
            return dao.recupDateEditionDepuisRepack(nomGc);

        if (nomGc.matches("^E[2.9]\\d_.*$"))
            return dao.readAllMap().get(nomGc.substring(0, 3)).getDateMEP();

        return Statics.DATEINCONNUE;
    }

}
