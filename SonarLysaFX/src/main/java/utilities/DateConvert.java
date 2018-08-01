package utilities;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;

/**
 * Utilitaire de conversion entre les deux formats de Date {@code java.util.Date} et {@code java.time.*}).
 * <p>
 * Toutes les méthodes sont null-safe.
 */
public class DateConvert
{
    /*---------- ATTRIBUTS ----------*/

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);
    
    private static final short MINPERHOUR = 60;
    private static final short SECSPERMIN = 60;
    private static final short HOURSPERDAY = 24;

    private DateConvert()
    {
    }

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Appel de {@link #convert(Class, Object, ZoneId)} avec la time-zone par default du système, et l'Offset de Greenwich.
     */
    public static <T extends Temporal> T convert(Class<T> classe, Object date)
    {
        return convert(classe, date, ZoneId.systemDefault());
    }

    /**
     * Permet de convertir un objet dans les nouveaux formats temporels du JDK 1.8.<br>
     * La time-zone est nécessaire pour {@code java.time.LocalDate} et {@code java.time.LocalDateTime}.<br>
     * Par défault, utilise celle du système. Null-safe.
     * 
     * @param classeRetour
     *            Le choix de la classe de retour.
     * @param date
     *            L'objet à convertir. Supporte les formats suivants :
     *            <ul>
     *            <li>{@link java.util.Date}
     *            <li>{@link java.sql.Date}
     *            <li>{@link java.sql.Timestamp}
     *            <li>{@link java.time.LocalDate}
     *            <li>{@link java.time.LocalDateTime}
     *            <li>{@link java.time.ZonedDateTime}
     *            <li>{@link java.time.Instant}
     *            <li>{@link java.lang.Long} - Nombre de jours depuis 1970
     *            </ul>
     * @param zone
     *            La time_zone souhaitée. Par default, on utlise celle du système.
     * @return Retourne un objet implémentant l'interface Temporal :
     *         <ul>
     *         <li>{@link java.time.LocalDate}
     *         <li>{@link java.time.LocalDateTime}
     *         <li>{@link java.time.ZonedDateTime}
     *         <li>{@link java.time.OffsetTime}
     *         <li>{@link java.time.OffsetDateTime}
     *         <li>{@link java.time.Instant}
     *         <li>{@link java.time.Year}
     *         <li>{@link java.time.YearMonth}
     *         </ul>
     */
    public static <T extends Temporal> T convert(Class<T> classeRetour, Object date, ZoneId zone)
    {
        if (classeRetour == null || date == null || zone == null)
            throw new IllegalArgumentException("Méthode DateConvert.convert(Class<? extends Temporal> classe , Object date, ZoneId zone) : une donnée est nulle.");

        // Conversion de l'objet en Instant selon sa classe
        Instant temp = convertToInstant(date, zone);

        // Conversion de l'Instant dans la classe de sortie
        return intantToClasseRetour(classeRetour, temp, zone);
    }

    /**
     * Appel de {@link #localDate(Date, ZoneId)} avec la time-zone par default du système.
     */
    public static LocalDate localDate(Date date)
    {
        return localDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDate} depuis {@code java.util.Date} ou ses sous-classes. Null-safe.
     */
    public static LocalDate localDate(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();

        return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }

    /**
     * Appel de {@link #localDateTime(Date, ZoneId)} avec la time-zone par default du système.
     */
    public static LocalDateTime localDateTime(Date date)
    {
        return localDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDateTime} depuis {@code java.util.Date} ou ses sous-classes. Null-safe.
     */
    public static LocalDateTime localDateTime(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof Timestamp)
            return ((Timestamp) date).toLocalDateTime();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    /**
     * Appel {@link #convertToOldDate(Object, ZoneId)} avec la time-zone par default du système.
     */
    public static Date convertToOldDate(Object date)
    {
        return convertToOldDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée une {@link java.util.Date} à partir de différents format de Date. Null-safe. Supporte les formats suivants:
     * <ul>
     * <li>{@link java.util.Date}
     * <li>{@link java.sql.Date}
     * <li>{@link java.sql.Timestamp}
     * <li>{@link java.time.LocalDate}
     * <li>{@link java.time.LocalDateTime}
     * <li>{@link java.time.ZonedDateTime}
     * <li>{@link java.time.Instant}
     * </ul>
     * 
     * @param zone
     *            Time zone, utilisée seulement pour LocalDate ou LocalDateTime.
     * @return {@link java.util.Date}
     */
    public static Date convertToOldDate(Object date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof Timestamp || date instanceof Date)
            return (Date) date;

        if (date instanceof LocalDate)
            return Date.from(((LocalDate) date).atStartOfDay(zone).toInstant());

        if (date instanceof LocalDateTime)
            return Date.from(((LocalDateTime) date).atZone(zone).toInstant());

        if (date instanceof ZonedDateTime)
            return Date.from(((ZonedDateTime) date).toInstant());

        if (date instanceof Instant)
            return Date.from((Instant) date);

        throw new UnsupportedOperationException("Classe de l'objet non supportée : " + date.getClass().getName());
    }

    /**
     * Crée un {@link Instant} depuis {@code java.util.Date} ou une de ses sous-classes. Null-safe.
     */
    public static Instant instant(Date date)
    {
        if (date == null)
            return null;

        return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * Appel {@link #zonedDateTime(Date, ZoneId)} avec la time-zone par default du système.
     */
    public static ZonedDateTime zonedDateTime(Date date)
    {
        return zonedDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link ZonedDateTime} depuis {@code java.util.Date} ou une de ses sous-classes. Null-safe.
     */
    public static ZonedDateTime zonedDateTime(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        return instant(date).atZone(zone);
    }

    /**
     * Retourne le mois en français de la date en paramètre. Utilise le format {@code java.time.LocalDate}
     * 
     * @param date
     * @return
     */
    public static String dateFrancais(LocalDate date, String pattern)
    {
        if (pattern == null || date == null)
            throw new IllegalArgumentException("La date et le pattern ne peuvent être nuls");
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.FRANCE)).replace("é", "e").replace("û", "u").replace(".", "");
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Transforme un {@code Object} en {@code Instant}
     * 
     * @param date
     * @param zone
     * @return
     */
    private static Instant convertToInstant(Object date, ZoneId zone)
    {
        Instant retour;
        switch (date.getClass().getName())
        {
            case "java.lang.Long":
                retour = Instant.ofEpochSecond((Long) date * HOURSPERDAY * MINPERHOUR * SECSPERMIN);
                break;

            case "java.time.Instant":
                retour = (Instant) date;
                break;

            case "java.time.ZonedDateTime":
                retour = ((ZonedDateTime) date).toInstant();
                break;

            case "java.time.LocalDateTime":
                retour = ((LocalDateTime) date).atZone(zone).toInstant();
                break;

            case "java.time.LocalDate":
                retour = ((LocalDate) date).atStartOfDay(zone).toInstant();
                break;

            case "java.sql.Timestamp":
                retour = ((Timestamp) date).toInstant();
                break;

            case "java.sql.Date":
                retour = ((java.sql.Date) date).toInstant();
                break;

            case "java.util.Date":
                retour = ((Date) date).toInstant();
                break;

            default:
                throw new UnsupportedOperationException("Classe de l'objet non supportée : " + date.getClass().getName());
        }

        return retour;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Temporal> T intantToClasseRetour(Class<T> classeRetour, Instant temp, ZoneId zone)
    {
        switch (classeRetour.getName())
        {
            case "java.time.LocalDate":
                return (T) temp.atZone(zone).toLocalDate();
            case "java.time.LocalDateTime":
                return (T) temp.atZone(zone).toLocalDateTime();
            case "java.time.ZonedDateTime":
                return (T) temp.atZone(zone);
            case "java.time.OffestTime":
                return (T) temp.atOffset(ZoneOffset.UTC).toOffsetTime();
            case "java.time.OffsetDateTime":
                return (T) temp.atOffset(ZoneOffset.UTC);
            case "java.time.Instant":
                return (T) temp;
            case "java.time.Year":
                return (T) Year.from(temp);
            case "java.time.YearMonth":
                return (T) YearMonth.from(temp);
            default:
                throw new UnsupportedOperationException("Classe de retour non supportée : " + classeRetour.getClass().getName());
        }
    }
    /*---------- ACCESSEURS ----------*/
}
