package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;

import model.enums.OptionTypeLong;

/**
 * Utilitaire de traitement des dates.
 * Permet la conversion entre les deux formats de Date {@code java.util.Date} 1.7 et {@code java.time.*} 1.8.
 */
public final class DateHelper
{
    /*---------- ATTRIBUTS ----------*/

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);

    private static final short MINPERHOUR = 60;
    private static final short SECSPERMIN = 60;
    private static final short HOURSPERDAY = 24;

    private DateHelper()
    {
        // Controleur privé empéchant les instanciations par introspection.
        throw new AssertionError();
    }

    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Appel de {@link #convert(Class, Object, ZoneId)} avec la time-zone par défaut du systeme, et l'Offset de Greenwich.
     * 
     * @param date
     *               L'objet à convertir.
     * @param classe
     *               Le choix de la classe de retour.
     * @param        <T>
     *               Classe de retour, classe fille de {@link java.time.temporal.Temporal}.
     * @return
     *         La date convertie.
     * 
     */
    public static <T extends Temporal> T convert(Class<T> classe, Object date)
    {
        return convert(classe, date, ZoneId.systemDefault());
    }

    /**
     * Permet de convertir un objet dans les nouveaux formats temporels du JDK 1.8.<br>
     * La time-zone est nécessaire pour {@code java.time.LocalDate} et {@code java.time.LocalDateTime}.<br>
     * Par défault, utilise celle du systeme. Null-safe.
     * 
     * @param classeRetour
     *                     Le choix de la classe de retour.
     * @param date
     *                     L'objet à convertir. Supporte les formats suivants :
     *                     <ul>
     *                     <li>{@link java.util.Date}
     *                     <li>{@link java.sql.Date}
     *                     <li>{@link java.sql.Timestamp}
     *                     <li>{@link java.time.LocalDate}
     *                     <li>{@link java.time.LocalDateTime}
     *                     <li>{@link java.time.ZonedDateTime}
     *                     <li>{@link java.time.Instant}
     *                     <li>{@link java.lang.Long} - Nombre de jours depuis 1970
     *                     </ul>
     * @param zone
     *                     La time_zone souhaitée. Par défault, on utlise celle du systeme.
     * 
     * @param              <T>
     *                     Classe de retour, classe fille de {@link java.time.temporal.Temporal}.
     * @return Retourne un objet implementant l'interface Temporal :
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
            throw new IllegalArgumentException("Methode DateConvert.convert : une donnée est nulle.");

        // Conversion de l'objet en Instant selon sa classe
        Instant temp = convertToInstant(date, zone);

        // Conversion de l'Instant dans la classe de sortie
        return intantToClasseRetour(classeRetour, temp, zone);
    }

    /**
     * Appel de {@link #convertLong(Class, Object, ZoneId)} avec la time-zone par défaut du systeme.
     * 
     * @param date
     *               La date au format Long à ceonvertir
     * @param classe
     *               Le choix de la classe de retour.
     * @param        <T>
     *               Classe de retour, classe fille de {@link java.time.temporal.Temporal}.
     * @return
     *         La date convertie.
     * 
     */
    public static <T extends Temporal> T convertLong(Class<T> classe, Long date, OptionTypeLong option)
    {
        return convertLong(classe, date, ZoneId.systemDefault(), option);
    }

    /**
     * Permet de convertir un {@link java.lang.Long} en date.
     * 
     * @param classeRetour
     *                     Le choix de la classe de retour.
     * @param date
     *                     La date a convertir au format Long.
     * @param zone
     *                     La time_zone souhaitée. Par défault, on utlise celle du systeme.
     * @param option
     *                     Determine le format du Long (nombre de jours, de secondes ou de milliscondes depuis 1970)
     * @param              <T>
     *                     Classe de retour, classe fille de {@link java.time.temporal.Temporal}.
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
    public static <T extends Temporal> T convertLong(Class<T> classeRetour, Long date, ZoneId zone, OptionTypeLong option)
    {
        // Conversion du long en Instant selon l'option choisie.
        Instant temp = null;
        switch (option)
        {
            case JOUR:
                temp = Instant.ofEpochSecond((Long) date * HOURSPERDAY * MINPERHOUR * SECSPERMIN);
                break;
            case SECOND:
                temp = Instant.ofEpochSecond(date);
                break;
            case MILLISECOND:
                temp = Instant.ofEpochMilli(date);
                break;
            default:
                throw new IllegalArgumentException("DateHelper.convertToInstant - option inconnue : " + option);
        }

        // Conversion de l'Instant dans la classe de sortie
        return intantToClasseRetour(classeRetour, temp, zone);
    }

    /**
     * Permet de convertir une date en un long selon l'option choisie.
     */
    public static <T extends Temporal> Long convertToLong(T date, OptionTypeLong option)
    {
        Instant instant = convertToInstant(date, ZoneId.systemDefault());
        switch (option)
        {
            case JOUR:
                return instant.toEpochMilli() / (Statics.MILLITOSECOND * HOURSPERDAY * MINPERHOUR * SECSPERMIN);
            case SECOND:
                return instant.toEpochMilli() / Statics.MILLITOSECOND;
            case MILLISECOND:
                return instant.toEpochMilli();
            default:
                throw new IllegalArgumentException("DateHelper.convertToInstant - option inconnue : " + option);
        }
    }

    /**
     * Crée un {@link LocalDate} depuis {@link java.util.Date} ou ses sous-classes avec la time-zone par défaut du systeme.
     * 
     * @param date
     *             Date à convertir.
     * @return
     *         {@link java.time.LocalDate}
     */
    public static LocalDate localDate(Date date)
    {
        return localDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDate} depuis {@link java.util.Date} ou ses sous-classes avec la time-zone indiquée.
     * 
     * @param date
     *             Date à convertir.
     * @param zone
     *             TimeZone de la date.
     * @return
     *         {@link java.time.LocalDate}
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
    
     */
    /**
     * Crée un {@link LocalDateTime} depuis {@link java.util.Date} ou ses sous-classes avec la time-zone par défaut du systeme.
     * 
     * @param date
     *             Date à convertir.
     * @return
     *         {@link java.time.LocalDateTime}
     */
    public static LocalDateTime localDateTime(Date date)
    {
        return localDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDateTime} depuis {@link java.util.Date} ou ses sous-classes avec la time-zone indiquée.
     * 
     * @param date
     *             Date à convertir.
     * @param zone
     *             TimeZone de la date.
     * @return
     *         {@link java.time.LocalDateTime}
     */
    public static LocalDateTime localDateTime(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    /**
     * Appel {@link #convertToOldDate(Object, ZoneId)} avec la time-zone par défaut du systeme.
     * 
     * @param date
     *             Date à convertir.
     * @return
     *         {@link java.util.Date}
     */
    public static Date convertToOldDate(Object date)
    {
        return convertToOldDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée une {@link java.util.Date} à partir de différents format de Date. Utilise la zone par défaut si celle-ci est nulle.<br>
     * Supporte les formats suivants:
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
     * @param date
     *             Date à convertir.
     * @param zone
     *             Time zone, utilisée seulement pour {@link java.time.LocalDate} ou {@link java.time.LocalDateTime}.
     * @return {@link java.util.Date}
     */
    public static Date convertToOldDate(Object date, ZoneId zone)
    {
        if (zone == null)
            zone = ZoneId.systemDefault();

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

        throw new IllegalArgumentException("Classe de l'objet non supportée : " + date.getClass().getName());
    }

    /**
     * Retourne le mois en français de la date en paramètre. Utilise le format {@link java.time.LocalDate}.
     * 
     * @param date
     *                Date à convertir.
     * @param pattern
     *                Pattern pour convertir la chaîne de caractères.
     * @return
     *         Date en français.
     */
    public static String dateFrancais(ChronoLocalDate date, String pattern)
    {
        if (pattern == null || date == null)
            throw new IllegalArgumentException("La date et le pattern ne peuvent être nuls");
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.FRANCE)).replace('é', 'e').replace('û', 'u');
    }

    /**
     * Retourne le mois en français de la date en paramètre. Utilise le format {@link java.time.LocalDateTime}.
     * 
     * @param date
     *                Date à convertir.
     * @param pattern
     *                Pattern pour convertir la chaîne de caractères.
     * @param         <T>
     *                Classe de retour, classe fille de {@link java.time.chrono.ChronoLocalDate}.
     * @return
     *         Mois de la date.
     */
    public static <T extends ChronoLocalDate> String dateFrancais(ChronoLocalDateTime<T> date, String pattern)
    {
        if (pattern == null || date == null)
            throw new IllegalArgumentException("La date et le pattern ne peuvent être nuls");
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.FRANCE)).replace('é', 'e').replace('û', 'u');
    }

    /**
     * Retourne la date de création d'un fichier au format {@link java.time.LocalDate}.
     * 
     * @param file
     *             Fichier à tester.
     * @return
     *         La date.
     * @throws IOException
     */
    public static LocalDate calculDateFichier(File file) throws IOException
    {
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        FileTime fileTime = attr.creationTime();
        return DateHelper.convert(LocalDate.class, fileTime.toInstant());
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Transforme un {@link java.lang.Object} en {@link java.time.Instant}
     * 
     * @param date
     *             Date à convertir.
     * @param zone
     *             Timezone de la date.
     * @return
     *         {@link java.time.Instant}
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
                retour = Instant.ofEpochMilli(((java.sql.Date) date).getTime());
                break;

            case "java.util.Date":
                retour = ((Date) date).toInstant();
                break;

            default:
                throw new IllegalArgumentException("Classe de l'objet non supportée : " + date.getClass().getName());
        }

        return retour;
    }

    /**
     * Retourne un objet de la classe demandée depuis un {@link java.time.Instant}
     * 
     * @param              <T>
     *                     Classe de retour, classe fille de {@link java.time.temporal.Temporal}.
     * @param classeRetour
     *                     Classe de retour.
     * @param temp
     *                     Date au format Instant.
     * @param zone
     *                     Timezone de la date.
     * @return
     *         La date au format choisi.
     */
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

            case "java.time.OffsetTime":
                return (T) temp.atOffset(ZoneOffset.UTC).toOffsetTime();

            case "java.time.OffsetDateTime":
                return (T) temp.atOffset(ZoneOffset.UTC);

            case "java.time.Instant":
                return (T) temp;

            case "java.time.Year":
                return (T) Year.from(temp.atZone(zone));

            case "java.time.YearMonth":
                return (T) YearMonth.from(temp.atZone(zone));

            default:
                throw new IllegalArgumentException("Classe de retour non supportée : " + classeRetour.getClass().getName());
        }
    }
    /*---------- ACCESSEURS ----------*/
}
