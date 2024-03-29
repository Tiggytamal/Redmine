package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import control.task.AbstractTask;
import model.Colonne;
import model.enums.TypeColW;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe m�re des contr�leur pour les fihciers Excel en �criture
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 *
 * @param <T>
 *            Enum�ration repr�sentant le type de colonnes du fichier
 * @param <R>
 *            Liste des donn�es � utiliser
 * 
 */
public abstract class AbstractControlExcelWrite<T extends Enum<T> & TypeColW, R> extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    /** Classe de l'�num�ration des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlExcelWrite(File file)
    {
        super(file);
        createWb();
        initEnum();
    }

    /*---------- METHODS ABSTRAITES ----------*/

    /**
     * Initilisa les titres des feuilles
     */
    protected abstract void initTitres();

    /**
     * Enregistre les donn�es dans la feuille donn�e du fichier excel
     * 
     * @param donnees
     * @param sheet
     */
    protected abstract void enregistrerDonnees(R donnees, Sheet sheet, AbstractTask task);

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Ecris le fichier sur le disque. Retourne vrai si celui-ci a bine �t� modifi�.
     * 
     * @return
     */
    public boolean write()
    {
        try (FileOutputStream stream = new FileOutputStream(file.getPath()))
        {
            long time = file.lastModified();
            wb.write(stream);
            wb.close();
            return time < file.lastModified();
        }
        catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    /*---------- METHODES PROTECTED ----------*/

    @Override
    protected final void calculIndiceColonnes()
    {
        Map<T, Colonne> map = Statics.proprietesXML.getEnumMapColW(enumeration);

        int nbreCol = 0;

        for (Map.Entry<T, Colonne> entry : map.entrySet())
        {
            T typeCol = entry.getKey();
            try
            {
                Field field = getFieldAccessible(typeCol.getNomCol());
                field.set(this, Integer.parseInt(entry.getValue().getIndice()));
                testMax((int) field.get(this));
                nbreCol++;
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur � l'affectation d'une variable lors de l'initialisation d'une colonne : " + typeCol.getNomCol(), e);
            }
        }

        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        int enumLength = enumeration.getEnumConstants().length;
        if (nbreCol != enumLength)
            throw new FunctionalException(Severity.ERROR, "Le fichier excel est mal configur�, v�rifi� les colonnes de celui-ci : Diff�rence = " + (enumLength - nbreCol));
    }

    @Override
    protected final void createWb()
    {
        wb = new XSSFWorkbook();
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }

    protected final int getNumCol(TypeColW type)
    {
        try
        {
            Field field = getFieldAccessible(type.getNomCol());
            return (int) field.get(this);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            throw new TechnicalException("Mauvaise d�claration des noms de colonnes : " + type.getNomCol(), e);
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de r�cuperer la classe sous forme de type param�tr�
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On r�cup�re les param�tres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // Instantiate the Parameter and initialize it.
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'�num�ration - control.excel.ControlExcelRead", e);
        }
    }
    
    private Field getFieldAccessible(String nom) throws NoSuchFieldException
    {
        Field field = getClass().getDeclaredField(nom);
        AccessController.doPrivileged((PrivilegedAction<T>) () -> 
        { 
            field.setAccessible(true);
            return null;
        });
        return field;
    }

    /*---------- ACCESSEURS ----------*/

}
