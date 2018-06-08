package junit;

import java.util.Set;
import java.util.TreeSet;

import model.Anomalie;
import model.ModelFactory;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{
    public static void main(String[] args)
    {
        Set<Anomalie> set = new TreeSet<>((o1, o2) -> Integer.valueOf(o1.getNumeroAnomalie()).compareTo(Integer.valueOf(o2.getNumeroAnomalie())));
        Anomalie ano = ModelFactory.getModel(Anomalie.class);
        Anomalie ano2 = ModelFactory.getModel(Anomalie.class);
        ano.setNumeroAnomalie(20);
        ano2.setNumeroAnomalie(10);
        set.add(ano);
        set.add(ano2);
        System.out.println(set);
    }
}