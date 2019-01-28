package junit;

import java.util.List;

import control.xml.ControlXML;
import dao.DaoFactory;
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
        List<ComposantSonar> compos = DaoFactory.getDao(ComposantSonar.class).readAll();
        List<DefautQualite> dqs = DaoFactory.getDao(DefautQualite.class).readAll();
        List<LotRTC> lotsRTC = DaoFactory.getDao(LotRTC.class).readAll();
        DataBaseXML db = ModelFactory.build(DataBaseXML.class);
        db.getCompos().addAll(compos);
        db.getDqs().addAll(dqs);
        db.getLotsRTC().addAll(lotsRTC);
        new ControlXML().saveXML(db);

    }

}
