package model.fxml;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

public class FiltreurFXML<T> implements Predicate<T>
{
    /*---------- ATTRIBUTS ----------*/

    private String valeur;
    private String nomMethode;
    private BiFunction<T, String, String> getter;
    private FiltreurFXML<T> filtreurPrec;

    /*---------- CONSTRUCTEURS ----------*/

    public FiltreurFXML(String valeur, String nomMethode, BiFunction<T, String, String> getter, FiltreurFXML<T> filtreurPrec)
    {
        this.valeur = valeur;
        this.getter = getter;
        this.filtreurPrec = filtreurPrec;
        this.nomMethode = nomMethode;
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public boolean test(T t)
    {
        if (filtreurPrec != null)
            return filtreurPrec.test(t) && StringUtils.containsIgnoreCase(getter.apply(t, nomMethode), valeur);
        return StringUtils.containsIgnoreCase(getter.apply(t, nomMethode), valeur);
    }

    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
