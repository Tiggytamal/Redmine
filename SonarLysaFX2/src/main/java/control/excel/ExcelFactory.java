package control.excel;

import java.io.File;

import model.enums.ColR;
import model.enums.ColW;
import utilities.TechnicalException;

/**
 * Factory de création des contrôleurs Excel. Retourne le controlleur associé à la classe de modèle associée.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 * 
 */
public interface ExcelFactory
{
    /**
     * Retourne une instance d'un contrôleur Excel en lecture en fonction du type d'énumeration.
     *
     * @param      <T>
     *             Classe de l'numération.
     * @param type
     *             L'énumération correspondante au controlleur Excel.
     * @param      <Y>
     *             Classe de l'objet de retour du controleur.
     * @param file
     *             Le fichier à traiter
     * @param      <R>
     *             Classe du controleur Excel à retourner.
     * @return
     *         Le controlleur Excel
     */
    @SuppressWarnings("unchecked")
    static <T extends Enum<T> & ColR, R extends AbstractControlExcelRead<T, Y>, Y> R getReader(Class<T> type, File file)
    {
        switch (type.getName())
        {
            case "model.enums.ColClarity":
                return (R) new ControlClarity(file);

            case "model.enums.ColChefServ":
                return (R) new ControlChefService(file);

            case "model.enums.ColEdition":
                return (R) new ControlEdition(file);

            case "model.enums.ColAppliDir":
                return (R) new ControlAppliDir(file);

            case "model.enums.ColPic":
                return (R) new ControlPic(file);

            default:
                throw new TechnicalException("control.excel.ExcelFactory.getReader - type non géré : " + type.toString(), null);
        }
    }

    /**
     * Retourne une instance d'un contrôleur Excel en écriture en fonction du type d'énumeration
     * 
     * @param      <T>
     *             Classe de l'numération.
     * @param type
     *             L'énumération correspondante au controlleur Excel.
     * @param      <Y>
     *             Classe de l'objet de retour du controleur.
     * @param file
     *             Le fichier à traiter
     * @param      <R>
     *             Classe du controleur Excel à retourner.
     * @return
     *         Le controlleur Excel
     */
    @SuppressWarnings("unchecked")
    static <T extends Enum<T> & ColW, R extends AbstractControlExcelWrite<T, Y>, Y> R getWriter(Class<T> type, File file)
    {
        switch (type.getName())
        {
            case "model.enums.ColVul":
                return (R) new ControlExtractVul(file);

            case "model.enums.ColCompo":
                return (R) new ControlExtractCompo(file);

            case "model.enums.ColRegle":
                return (R) new ControlExtractRegles(file);

            default:
                throw new TechnicalException("ExcelFactory.getWriter - type non géré : " + type.toString(), null);
        }
    }
}
