package junit;

import java.util.List;

import control.xml.ControlXML;
import dao.ListeDao;
import model.DataBaseXML;
import model.ModelFactory;
import model.bdd.ComposantSonar;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;

/**
 * Classe permettant d'initialiser la base de données en XML pour les tsts unitaires.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class InitDataBaseXML
{

    public static void main(String[] args)
    {
        List<ComposantSonar> compos = ListeDao.daoCompo.readAll();
        List<DefautQualite> dqs = ListeDao.daoDefautQualite.readAll();
        List<LotRTC> lotsRTC = ListeDao.daoLotRTC.readAll();
        DataBaseXML db = ModelFactory.build(DataBaseXML.class);
        db.getCompos().addAll(compos);
        db.getDqs().addAll(dqs);
        db.getLotsRTC().addAll(lotsRTC);
        new ControlXML().saveXML(db);
    }

}
