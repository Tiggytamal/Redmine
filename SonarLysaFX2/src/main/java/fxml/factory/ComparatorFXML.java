package fxml.factory;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ComparatorFXML implements Comparator<Object>, Serializable
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object o1, Object o2)
    {
        if (o1 == null)
            return -1;
        
        if (o2 == null)
            return 1;
        
        if (o1 instanceof String)
            return ((String) o1).compareTo((String) o2);
        
        if (o1 instanceof Boolean)
            return ((Boolean) o1).compareTo((Boolean) o2);   
        
        if (o1 instanceof List)
            return ((List<String>)o1).get(0).compareTo(((List<String>)o2).get(0));
        
        return 0;        
    }

}
