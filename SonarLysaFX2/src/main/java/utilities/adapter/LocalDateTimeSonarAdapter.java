package utilities.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Classe de gestion des {@code LocalDateTime} pour la persistance XML
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 * 
 */
public class LocalDateTimeSonarAdapter extends AbstractXmlAdapter<String, LocalDateTime>
{
    /*---------- ATTRIBUTS ----------*/
    
    private static final Pattern PATTERNDATE4 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}$");
    private static final Pattern PATTERNDATE2 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$");
    
    /*---------- CONSTRUCTEURS ----------*/

    public LocalDateTimeSonarAdapter()
    {
        super(v -> {
            if (PATTERNDATE4.matcher(v).matches())
                return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
            if (PATTERNDATE2.matcher(v).matches())
                return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }, LocalDateTime::toString);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
