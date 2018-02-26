package sonarapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe simple sans remont�e des erreurs et des composants. Permet de retrouver que le nombre d'erreurs total d'un composant.
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
@JsonIgnoreProperties({"issues", "components"})
public class IssuesSimple extends Issues
{

}
