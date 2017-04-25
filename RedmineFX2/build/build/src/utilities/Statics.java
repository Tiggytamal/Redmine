package utilities;

import java.time.LocalDate;

/**
 * Classe regroupant toutes les constantes statiques pour l'application
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Statics
{
    /** Valeur pour le s�parateur de ligne ind�pendant du syst�me */
    public static final String NL = System.getProperty("line.separator");   
    /** Date du jour */
    public static final LocalDate TODAY = LocalDate.now();
    /** Nom de la feuille d'avancement des incidents */
    public static final String sheetAvancement = "Avancement";
    /** Nom de la feuille d'avancement des incidents */
    public static final String sheetStockSM9 = "Stock SM9";
    /** valeur abandon du num�ro de DA */
    public static final String ABANDON = "abandon";
}
