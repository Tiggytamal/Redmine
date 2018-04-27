package model.sonarapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Classe simple sans remontée des erreurs et des composants. Permet de retrouver que le nombre d'erreurs total d'un composant.
 * 
 * @author ETP8137 - Grégoire Mathon
 *
 */
@JsonIgnoreProperties({"components", "issues"})
public class IssuesSimple extends Issues
{

}
