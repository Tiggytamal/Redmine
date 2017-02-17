package model.enums;

public enum Statut
{
    RESOLVED("Resolved"),
    NOUVEAU("Nouveau / Open"),
    WRKINPRG("Work In Progress"),
    CLOSED("Closed"),
    TRANSFERED("Transfered"),
    REFERRED("Referred"),
    PENDING("Pending"),
    NONTMA("Non trait� par TMA");
	//TODO V�rifier les valeurs des statuts en tables

    private final String string;

    private Statut(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    /**
     * Permet de retourner un Status � partir de la valeur en table.
     * 
     * @param string
     * 			La valeur � convertir
     * @return
     */
    public static Statut getStatus(String string)
    {
    	switch (string)
    	{
    		case "Resolved" :
    			return RESOLVED;
    		case "Nouveau / Open" :
    			return NOUVEAU;
    		case "Work In Progress" :
    			return WRKINPRG;
    		case "Closed" :
    			return CLOSED;
    		case "Transfered" :
    			return TRANSFERED;
    		case "Referred" :
    			return REFERRED;
    		case "Pending" :
    			return PENDING;
    		case "Non trait� par TMA" :
    			return NONTMA;
    		default :
    			return null;    			
    	}
    }
}
