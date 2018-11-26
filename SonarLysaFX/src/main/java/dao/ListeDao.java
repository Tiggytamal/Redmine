package dao;

import model.bdd.Application;
import model.bdd.ChefService;
import model.bdd.ComposantSonar;
import model.bdd.DateMaj;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.Produit;
import model.bdd.ProjetClarity;

public class ListeDao
{
    private ListeDao() {}
    
    public static final DaoApplication daoAppli = DaoFactory.getDao(Application.class);
    public static final DaoChefService daoChefServ = DaoFactory.getDao(ChefService.class);
    public static final DaoComposantSonar daoCompo = DaoFactory.getDao(ComposantSonar.class);
    public static final DaoDateMaj daoDateMaj = DaoFactory.getDao(DateMaj.class);
    public static final DaoDefautAppli daoDefautAppli = DaoFactory.getDao(DefautAppli.class);
    public static final DaoDefautQualite daoDefautQualite = DaoFactory.getDao(DefautQualite.class);
    public static final DaoEdition daoEdition = DaoFactory.getDao(Edition.class);
    public static final DaoLotRTC daoLotRTC = DaoFactory.getDao(LotRTC.class);
    public static final DaoProjetClarity daoProjetClarity = DaoFactory.getDao(ProjetClarity.class);
    public static final DaoProduit daoProduit = DaoFactory.getDao(Produit.class);
}
