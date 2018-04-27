package model.sonarapi;

public enum Status 
{
	/*---------- ATTRIBUTS ----------*/

	OK(Valeur.OK),
	WARN(Valeur.WARN),
	ERROR(Valeur.ERROR),
	NONE(Valeur.NONE);
	
	private String string;

	/*---------- CONSTRUCTEURS ----------*/
	
	private Status(String string)
	{
		this.string = string;
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Status getStatus (String status)
    {
    	switch(status)
    	{
    		case Valeur.OK :
    			return OK;
    		case Valeur.WARN :
    			return WARN;
    		case Valeur.ERROR :
    			return ERROR;
    		case Valeur.NONE :
    			return NONE;
    		default :
    			return null;
    	}
    }
	
	/*---------- CLASSE INTERNE ----------*/
    
    private static class Valeur
    {
    	private Valeur() {}
    	
    	private static final String OK = "OK";
    	private static final String NONE = "NONE";
    	private static final String ERROR = "ERROR";
    	private static final String WARN = "WARN";   	
    }   
}