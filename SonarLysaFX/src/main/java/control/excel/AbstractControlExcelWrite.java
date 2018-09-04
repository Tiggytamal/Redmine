package control.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Colonne;
import model.enums.TypeColW;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.enums.Severity;

/**
 * Classe mère des contrôleur pour les fihciers Excel en écriture
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 *
 * @param <T>
 *            Enumération représentant le type de colonnes du fichier
 * @param <R>
 *            Liste des donnèes à utiliser
 * 
 */
public abstract class AbstractControlExcelWrite<T extends Enum<T> & TypeColW, R> extends AbstractControlExcel
{
    /*---------- ATTRIBUTS ----------*/

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    /** Classe de l'énumération des classes filles */
    protected Class<T> enumeration;

    /*---------- CONSTRUCTEURS ----------*/

    protected AbstractControlExcelWrite(File file)
    {
        super(file);
        createWb();
        initEnum();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public void write()
    {
        try (FileOutputStream stream = new FileOutputStream(file.getPath()))
        {
            wb.write(stream);
            wb.close();
        }
        catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    /*---------- METHODS ABSTRAITES ----------*/

    /**
     * Initilisa les titres des feuilles
     */
    protected abstract void initTitres();

    /**
     * Enregistre les données dans la feuille donnée du fichier excel
     * 
     * @param donnees
     * @param sheet
     */
    protected abstract void enregistrerDonnees(R donnees, Sheet sheet);

    /*---------- METHODES PRIVEES ----------*/

    @Override
    protected final void calculIndiceColonnes()
    {
        Map<T, Colonne> map = Statics.proprietesXML.getEnumMapColW(enumeration);
        
        int nbreCol = 0;
        
        Field field;
        
        for (Map.Entry<T, Colonne> entry : map.entrySet())
        {
            T typeCol = entry.getKey();
            try
            {
                field = getClass().getDeclaredField(typeCol.getNomCol());
                field.setAccessible(true);                
                field.set(this, Integer.parseInt(entry.getValue().getIndice()));
                testMax((int) field.get(this));
                nbreCol++;
            } 
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new TechnicalException("Erreur à l'affectation d'une variable lors de l'initialisation d'une colonne : " + typeCol.getNomCol(), e);
            }
        }
        
        // Gestion des erreurs si on ne trouve pas le bon nombre de colonnes
        int enumLength = enumeration.getEnumConstants().length;
        if (nbreCol != enumLength)
            throw new FunctionalException(Severity.ERROR, "Le fichier excel est mal configuré, vérifié les colonnes de celui-ci : Différence = " + (enumLength - nbreCol));
    }
    
    @Override
    protected final void createWb()
    {
        wb = new XSSFWorkbook();
        helper = new CellHelper(wb);
        createHelper = wb.getCreationHelper();
        ca = createHelper.createClientAnchor();
    }

    @SuppressWarnings("unchecked")
    private void initEnum()
    {
        // Permet de récuperer la classe sous forme de type paramétré
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();

        // On récupère les paramètres de classe (ici T et R), on prend le premier (T), et le split permet d'enlever le "classe" devant le nom
        String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

        // Instantiate the Parameter and initialize it.
        try
        {
            enumeration = (Class<T>) Class.forName(parameterClassName);
        }
        catch (ClassNotFoundException e)
        {
            LOGPLANTAGE.error(e);
            throw new TechnicalException("Impossible d'instancier l'énumération - control.excel.ControlExcelRead", e);
        }
    }

    /*---------- ACCESSEURS ----------*/

}
