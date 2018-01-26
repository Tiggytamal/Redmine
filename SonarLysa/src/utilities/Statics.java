package utilities;

import java.time.LocalDate;

public class Statics
{
	private Statics() {}
	/** Adresse du serveur SonarQube */
	public static final String URI = "http://ttp10-snar.ca-technologies.fr";
    /** Valeur pour le s�parateur de ligne ind�pendant du syst�me */
    public static final String NL = System.getProperty("line.separator");   
    /** Date du jour */
    public static final LocalDate TODAY = LocalDate.now();
}
