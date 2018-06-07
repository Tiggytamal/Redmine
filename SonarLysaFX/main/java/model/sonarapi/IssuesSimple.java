package model.sonarapi;

import java.util.List;

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

    public IssuesSimple(int total, int p, int ps, Paging paging, List<Composant> composants, List<Issue> issues)
    {
        super(total, p, ps, paging, composants, issues);
    }

    public IssuesSimple()
    {
        super();
    }
}
