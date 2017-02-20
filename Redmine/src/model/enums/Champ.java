package model.enums;

public enum Champ
{
    APPLICATION("Application"),
    DATEOUVERTURE("Date d'ouverture"),
    DATERESOLUTION("Date de r�solution"),
    DATEPERMIEREN3("Date de 1�re affectation au N3"),
    DATEPRISENCHARGE("Date de prise en charge"),
    DA("n� de DA"),
    DATETRANSFERT("Date de transfert"),
    NUMERO("N� d'incident"),
    GRTRANSFERT("Groupe de transfert"),
    ENVIRONNEMENT("Environnement"),
    BANQUE("Banque"),
    TYPEDEMANDE("Type demande"),
    TECHNOLOGIE("Technologie"),
    DATECLOTURE("Date de cl�ture"),
    CODECLOTURE("Code cl�ture"),
    REOUVERTURE("R�-ouverture"),
    DATEREOUV("Date de r�-ouverture"),
    DATERECLO("Date de re-cl�ture"),
    STATUTSM9("Statut de l'incident"),
    FACTURE("Factur�"),
    COMMENTAIREREOUV("Commentaire de r�-ouverture"),
    DATECORRECTIONPREVUE("Date de correction pr�vue");
    
    //TODO rajouter des champs si n�cessaire. V�rifier les valeurs en table.
	
    private final String string;

    private Champ(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Champ getChamp(String champString)
    {
    	switch(champString)
    	{
    		case "Application" :
    			return APPLICATION;
    		case "Date de prise en charge" :
    			return DATEPRISENCHARGE;
    		case "n� de DA" :
    			return DA;
    		case "Date de transfert" :
    			return DATETRANSFERT;
    		default :
    			return null;
    	}
    }
}
