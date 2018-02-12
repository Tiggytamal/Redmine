package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.xml.Application;
import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Metrique;
import sonarapi.model.Projet;
import sonarapi.model.Status;
import sonarapi.model.Vue;
import utilities.DateConvert;
import utilities.Statics;

public class ControlSonar
{

	/*---------- ATTRIBUTS ----------*/

	private final SonarAPI api;
	private final ControlXML controlXML;
	private static final Logger logSansApp = LogManager.getLogger("sansapp-log");
	private static final Logger loginconnue = LogManager.getLogger("inconnue-log");
	private static final Logger lognonlistee = LogManager.getLogger("nonlistee-log");

	/*---------- CONSTRUCTEURS ----------*/
	
	/**
	 * Constructeur de base pour l'api Sonar, avec le mot de passe et le l'identifiant de 
	 * @param name
	 * @param password
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws InvalidFormatException 
	 */
	public ControlSonar(String name, String password) throws InvalidFormatException, JAXBException, IOException
	{
		api = new SonarAPI(Statics.URI, name, password);
		controlXML = new ControlXML();
		controlXML.recuprerParamXML();
	}
	
	/**
	 * Instanciation sans param�tre qui permet de r�cuperer le contr�leur Sonar de test.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws InvalidFormatException 
	 */
	public ControlSonar() throws InvalidFormatException, JAXBException, IOException
	{
		api = SonarAPI.getInstanceTest();
		controlXML = new ControlXML();
		controlXML.recuprerParamXML();
	}

	/*---------- METHODES PUBLIQUES ----------*/
	
	
	
	public void creerVueParApplication()
	{	
		// Cr�ation de la liste des composants par application
		Map<String, List<Projet>> mapApplication = controlerSonarQube();
				
		// Parcours de la liste pour cr�er chaque vue applicative avec ses composants
		for (Map.Entry<String, List<Projet>> entry : mapApplication.entrySet())
		{
			// Cr�ation de la vue principale
			Vue vue = new Vue();
			vue.setName("APPLI MASTER " + entry.getKey());
			vue.setKey("APPMASTERAPP" + entry.getKey());
			vue.setDescription("Liste des composants de l'application " + entry.getKey());
			api.supprimerVue(vue);
			api.creerVue(vue);
			api.ajouterSousProjets(entry.getValue(), vue);		
		}		
	}
	
	public Map<String, List<Projet>> controlerSonarQube()
	{
		// R�cup�ration des composants Sonar
		Map<String, Projet> mapProjets = recupererComposantsSonar();
				
		// Cr�ation de la liste des composants par application
		return creerMapApplication(mapProjets);
	}

	public void creerVueProduction(File file) throws InvalidFormatException, IOException
	{
	    Map<LocalDate, List<Vue>> mapLot = recupMapLotExcel(file);	
	    if(mapLot.size() == 1)
	    {
	        creerVueMensuelle(mapLot);
	    }
	    else if (mapLot.size() == 3)
	    {
	        creerVueTrimestrielle(mapLot);
	    }
	}
	
	public void creerVueQGErreur()
	{
		// R�cup�ration des lots Sonar en erreur.
		Set<String> setLots = lotSonarQGError();
		
		System.out.println("ok");
    	// Cr�ation de la lvue et envoie vers SonarQube
    	Vue vue = new Vue();
		vue.setName("Lots en erreur");
		vue.setKey("LotsErreurKey");
		vue.setDescription("Vue regroupant toutes les vues avec des composants en erreur");    	
//		api.creerVue(vue);
		
		List<Vue> listeVues = new ArrayList<>();
		
		for (String key : setLots)
		{
			Vue vueAdd = new Vue();
			vueAdd.setKey("view_lot_" + key);
			vueAdd.setName("Lot " + key);
			System.out.println(key);
		}
    	
		// Ajout des sous-vue
//		api.ajouterSousVues(listeVues, vue);
	}

	/**
	 * Lance la mise � jour des vues dans SonarQube. Indispenssable apr�s la cr�ation d'une nouvelle vue.
	 */
	public void majVues()
	{
		api.majVues();
	}

	/*---------- METHODES PRIVEES ----------*/

	/**
	 * R�cup�re tous les lots cr�er dans Sonar.
	 * 
	 * @return
	 */
	private Map<String, Vue> recupererLotsSonarQube()
	{
		Map<String, Vue> map = new HashMap<>();
		List<Vue> views = api.getVues();
		for (Vue view : views)
		{
			if (view.getName().startsWith("Lot "))
			{
				map.put(view.getName().substring(4), view);
			}
		}
		return map;
	}
	
	/**
	 * Permet de classer tous les lots Sonar du fichier dans une map. On enl�ve d'abord tout ceux qui ne sont pas pr�sents dans SonarQube.<br>
	 * Puis on les classes dans des listes, la clef de chaque liste correspond au mois et � l'ann�e de mise en production du lot.<br>
	 * Le fichier excel doit avoir un formattage sp�cifique, avec une colonne <b>Lot</b> (num�rique) et un colonne <b>livraison �dition</b> (date).<br>
	 * 
	 * @param file
	 * 			Le fichier excel envoy� par l'interface
	 * @return
	 * @throws EncryptedDocumentException 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private Map<LocalDate, List<Vue>> recupMapLotExcel(File file) throws InvalidFormatException, IOException
	{
	    // Cr�ation du workbook depuis le fichier excel
		Workbook wb = WorkbookFactory.create(file);

		// R�cup�ration de la premi�re feuille
		Sheet sheet = wb.getSheetAt(0);
		
		//Traitement Excel et contr�le avec SonarQube
		Map<LocalDate, List<Vue>> retour = traitementExcel(sheet, wb);
		
		// Ecriture du fichier Excel
		wb.write(new FileOutputStream(file.getName()));

		wb.close();

		return retour;
	}
	
	private Map<LocalDate, List<Vue>> traitementExcel(Sheet sheet, Workbook wb)
	{
		// Initialisation de la map de retour		
		Map<LocalDate, List<Vue>> retour = new HashMap<>();
		
		// R�cup�ration depuis Sonar de tous les lots existants
	    Map<String, Vue> mapQube = recupererLotsSonarQube();
				
		// Index des colonnes des lots et des dates
		int colLot = 0;
		int colDate = 0;
		
		//R�cup�ration des indices de colonnes pour le num�rod e lot et la date de mise en production
		for (Cell cell : sheet.getRow(0)) 
		{
			if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Lot"))
			{
				colLot = cell.getColumnIndex();
			}
			else if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Livraison �dition"))
			{
			    colDate = cell.getColumnIndex();
			}	
		}
		
		// parcours de la feuille Excel pour r�cup�rer tous les lots et leurs dates de mise en production avec mise � jour du fichier Excel
		for (int i = 1; i < sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
		    traitementLigneExcel(row, colLot, colDate, retour, mapQube, wb);
		}
		return retour;		
	}

	
	/**
	 * Effectue le traitement de chaque ligne du fichier Excel
	 * 
	 * @param row
	 * 			Ligne du fichier Excel � traiter
	 * @param colLot
	 * 			Indice de la colonne des num�ros de lot
	 * @param colDate
	 * 			Indice de la colonne des dates de livraison en production
	 * @param retour
	 * 			Map retournant tous les lots � rajouter dans la vue
	 * @param mapQube
	 * 			Map des vues retourn�es par SonarQube
	 * @param wb
	 * 			Workbook 
	 */
	private void traitementLigneExcel(Row row, int colLot, int colDate, Map<LocalDate, List<Vue>> retour, Map<String, Vue> mapQube, Workbook wb)
	{
	    Cell cellLot = row.getCell(colLot);
	    Cell cellDate = row.getCell(colDate);
	    
	    if (cellLot.getCellTypeEnum() != CellType.NUMERIC && cellDate.getCellTypeEnum() != CellType.NUMERIC)
	        return;
	    
	    String lot = String.valueOf((int)cellLot.getNumericCellValue());
	    
	    // ON teste si le num�ro de lot est bien pr�sent dans Sonar.
	    if (mapQube.keySet().contains(lot))
	    {
	        // R�cup�ration de la date depuis le fichier Excel en format JDK 1.8.
	        LocalDate date = DateConvert.localDate(cellDate.getDateCellValue());
	        // Cr�ation d'une nouvelle date au 1er du mois qui servira du clef � la map.
            LocalDate clef = LocalDate.of(date.getYear(), date.getMonth(), 1);
            
            // It�ration sur toutes les cellules de la ligne pour mettre � jour et changer la couleur de celles-ci.
            for (int j = 0; j < row.getLastCellNum(); j++)
			{               
            	Cell cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				CellStyle style = wb.createCellStyle();
				style.cloneStyleFrom(cell.getCellStyle());					
				style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cell.setCellStyle(style);
			}
            	                    
            if (retour.keySet().contains(clef))
            {
                retour.get(clef).add(mapQube.get(lot));
            }
            else
            {
                List<Vue> liste = new ArrayList<>();
                liste.add(mapQube.get(lot));
                retour.put(clef, liste);
            }           	            
	    }   		
	}

	/**
	 * Permet de r�cup�rer la derni�re version de chaque composants cr��s dans Sonar
	 * @return
	 */
	private Map<String, Projet> recupererComposantsSonar()
	{
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets = api.getComposants();
		
		// Triage ascendant de la liste par nom de projet 
		projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));
		
		// Cr�ation de la regex pour retirer les num�ros de version des composants
		Pattern pattern = Pattern.compile("^\\D*");
		
		// Cr�ation de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine de caract�res cr��es par la regex comme clef dans la map.
		// Les compossant �tant tri�s par ordre alphab�tique, on va �craser tous les composants qui ont un num�ro de version obsol�te.
		Map<String, Projet> retour = new HashMap<>();
		
		for (Projet projet : projets)
		{
			Matcher matcher = pattern.matcher(projet.getNom());
			if (matcher.find())
			{				
				retour.put(matcher.group(0), projet);
			}
		}
		return retour;		
	}
	
	private Set<String> lotSonarQGError()
	{	    
		// R�cup�ration des composants Sonar
		Collection<Projet> listProjets = recupererComposantsSonar().values();
	    
	    Set<String> setLotErreur = new TreeSet<>();

	    // Iteration sur la liste des projets
		for (Projet projet : listProjets)
		{
			//R�cup�ration du composant
			Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] {"lot","alert_status"});
			
			if (!composant.getListeMeriques().isEmpty())
			{					
				String lot = "";
				String alert = "";
				for (Metrique metrique : composant.getListeMeriques())
				{
					if (metrique.getMetric().equals("lot"))
					{
						lot = metrique.getValue();
					}
					else if (metrique.getMetric().equals("alert_status"))
					{
						alert = metrique.getValue();
					}
				}
				
				if (Status.getStatus(alert) == Status.ERROR && !lot.isEmpty())
				{
					setLotErreur.add(lot);
				}
			}	
		}

	    return setLotErreur;	    
	}
	
	/**
	 * Cr�e une map de toutes les apllications dans Sonar avec pour chacunes la liste des composants li�s.
	 * 
	 * @param mapProjets
	 * @return
	 */
	private HashMap<String, List<Projet>> creerMapApplication(Map<String, Projet> mapProjets)
	{
		// Initialisation de la map
		HashMap<String, List<Projet>> mapApplications = new HashMap<>();
		
		// It�ration sur la liste des projets
		for (Projet projet : mapProjets.values())
		{
			// R�cup�ration du code application
			Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] {"application"});
			
			// Test si la liste est vide, cela veut dire que le projet n'a pas de code application.
			if (!composant.getListeMeriques().isEmpty())
			{					
				String application = composant.getListeMeriques().get(0).getValue().trim().toUpperCase();		

				// Si l'application n'est pas dans la PIC, on continue au projet suivant.
				if(!testAppli(application, composant.getNom()))
				{
					continue;
				}
				
				// Mise � jour de la map de retour avec en clef, le code application et en valeur : la liste des projets li�s.
				if(mapApplications.keySet().contains(application))
				{
					mapApplications.get(application).add(projet);
				}
				else
				{
					List<Projet> liste = new ArrayList<>();
					liste.add(projet);
					mapApplications.put(application, liste);
				}			
			}
			else
			{
				logSansApp.warn(composant.getNom() + " - " + composant.getKey());
			}
		}
		return mapApplications;
	}
	
	/**
	 * V�rifie qu'une application d'un composant Sonar est pr�sente dans la liste des applications de la PIC.
	 * 
	 * @param application
	 *         Application enregistr�e pour le composant dans Sonar.
	 * @param nom
	 *         Nom du composant Sonar.
	 */
	private boolean testAppli(String application, String nom)
	{
		if(application.equals(Statics.INCONNUE))
		{
			loginconnue.warn(nom);
			return false;
		}
		
		Map<String, Application> vraiesApplis = controlXML.getMapApplis();
		
		if(vraiesApplis.keySet().contains(application))
		{
			if(vraiesApplis.get(application).isActif())
			{
				return true;
			}
			lognonlistee.warn("Application obsol�te : " + application + " - composant : " + nom);
			return false;
		}
		lognonlistee.warn("Application n'existant pas dans le r�f�renciel : " + application + " - composant : " + nom);				
		return false;
	}
	
	/**
	 * Cr�e la vue Sonar pour une recherche trimetrielle des composants mis en production .
	 * 
	 * @param mapLot
	 */
    private void creerVueTrimestrielle(Map<LocalDate, List<Vue>> mapLot)
    {
        //Cr�ation des variables. Transfert de la HashMap dans une TreeMap pour trier les dates.
        List<Vue> lotsTotal = new ArrayList<>();
        Map<LocalDate, List<Vue>> treeLot = new TreeMap<>(mapLot);
    	Iterator<Entry<LocalDate, List<Vue>>> iter = treeLot.entrySet().iterator();
    	StringBuilder builderNom = new StringBuilder();
    	StringBuilder builderDate = new StringBuilder();
    	List<String> dates = new ArrayList<>();
    	
    	// It�ration sur la map pour regrouper tous les lots dans la m�me liste.
    	// Cr�e le nom du fichier sous la forme TEMP MMM-MMM-MMM yyyy(-yyyy)
    	while (iter.hasNext())
    	{
    		Entry<LocalDate, List<Vue>> entry =  iter.next();
    		// Regroupe tous les lots dans la m�me liste.
    		lotsTotal.addAll(entry.getValue());
    		LocalDate clef = entry.getKey();
    		
 
    		builderNom.append(DateConvert.dateFrancais(clef, "MMM"));
            if (iter.hasNext())
            {
                builderNom.append("-");
            }
    		
    		String date = DateConvert.dateFrancais(clef, "yyyy");
    		if (!dates.contains(date))
    		{
    			dates.add(date);
    			builderDate.append(date);
    			if (iter.hasNext())
    			{
    			    builderDate.append("-");
    			}
    		}
    	}
    	
    	if (builderDate.charAt(builderDate.length()-1) == '-')	
    	{
    		builderDate.deleteCharAt(builderDate.length()-1);
    	}
    	
    	String nom = builderNom.toString();
    	String date = builderDate.toString();
    	
    	// Cr�ation de la vue et envoie vers SonarQube
    	Vue vue = new Vue();
		vue.setName(new StringBuilder("TEP ").append(nom).append(Statics.SPACE).append(date).toString());
		vue.setKey(new StringBuilder("MEPMEP").append(nom).append(date).toString().replace("�", "e").replace("�", "u"));
		vue.setDescription(new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString());    	
		api.creerVue(vue);
    	
		// Ajout des sous-vue
		api.ajouterSousVues(lotsTotal, vue);
    }

    /**
     * Cr�e la vue Sonar pour une recherche mensuelle des composants mis en production .
     * @param mapLot
     */
    private void creerVueMensuelle(final Map<LocalDate, List<Vue>> mapLot)
    {
    	Iterator<Entry<LocalDate, List<Vue>>> iter = mapLot.entrySet().iterator();
    	Entry<LocalDate, List<Vue>> entry =  iter.next();
    	String nomVue =  new StringBuilder("MEP ").append(DateConvert.dateFrancais(entry.getKey(),"MMM yyyy")).toString();
     
		// Cr�ation de la vue principale
		Vue vue = new Vue();
		vue.setName(nomVue);
		vue.setKey(new StringBuilder("MEP").append(DateConvert.dateFrancais(entry.getKey(),"MMMyyyy")).append("Key").toString());
		vue.setDescription(new StringBuilder("Vue des lots mis en production pendant le mois de ").append(entry.getKey()).toString());
		api.creerVue(vue);
		
		// Ajout des sous-vue
		api.ajouterSousVues(entry.getValue(), vue);
    }
	
	/*---------- ACCESSEURS ----------*/
}