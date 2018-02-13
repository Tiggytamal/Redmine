package control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import model.enums.Environnement;
import model.excel.Anomalie;
import model.excel.LotSuiviPic;
import model.xml.Application;
import sonarapi.SonarAPI;
import sonarapi.model.Composant;
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
	private static final String SANSVERSION = "-";

	/*---------- CONSTRUCTEURS ----------*/
	
	/**
	 * Constructeur de base pour l'api Sonar, avec le mot de passe et le l'identifiant
	 * 
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
	 * Constructeur sans param�tre qui permet de r�cuperer le contr�leur Sonar de test.
	 * 
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
		ControlPic excel = new ControlPic(file);
	    Map<LocalDate, List<Vue>> mapLot = excel.recupLotExcel(recupererLotsSonarQube());
	    excel.close();
	    if(mapLot.size() == 1)
	    {
	        creerVueMensuelle(mapLot);
	    }
	    else if (mapLot.size() == 3)
	    {
	        creerVueTrimestrielle(mapLot);
	    }
	}
	
	public void creerVuesQGErreur() throws InvalidFormatException, IOException
	{
		// 1. R�cup�ration des donn�es depuis les fichiers Excel.
		
		// Fichier des anomalies en route
		ControlAno controlAno = new ControlAno(new File("d:\\qualityGate anomalies.xlsx"));
		List<String> listeLotenAno = controlAno.listAnomaliesSurLotsCrees();
		controlAno.close();
		System.out.println("nombre ano cr��es : " + listeLotenAno.size());
		
		// Fichier des lots �dition E30
		ControlPic controlE30 = new ControlPic(new File("d:\\E30.xlsx"));
		Map<String, LotSuiviPic> anomaliesE30 = controlE30.recupLotAnomalieExcel();
		controlE30.close();
		System.out.println("nombre lots E30 : " + anomaliesE30.size());
		
		// Fichier des lots Edition E31
		ControlPic controlE31 = new ControlPic(new File("d:\\E31.xlsx"));
		Map<String, LotSuiviPic> anomaliesE31 = controlE31.recupLotAnomalieExcel();
		controlE31.close();
		System.out.println("nombre lots E31 : " + anomaliesE31.size());
		
		// 2. R�cup�ration des lots Sonar en erreur.
		Map<String,List<String>> mapLots = lotSonarQGError(new String[] {"13", "14"});
		
		System.out.println("total erreurs E30 : " + mapLots.get("13").size());
		System.out.println("total erreurs E31 : " + mapLots.get("14").size());
		
		// 3. Supression des lots d�j� cr��s
		
		Iterator<String> iter = mapLots.get("13").iterator();
		while (iter.hasNext())
		{
			String numeroLot = iter.next();
			if(listeLotenAno.contains(numeroLot))
			{
				iter.remove();
			}
			else
			{
				LotSuiviPic lot = anomaliesE30.get(numeroLot);
				Anomalie ano = new Anomalie();
				ano.setCpiProjet(lot.getCpiProjet());
				ano.setEdition(lot.getEdition());
				ano.setLibelleProjet(lot.getLibelle());
				ano.setProjetClarity(lot.getProjetClarity());
				ano.setLot("Lot " + lot.getLot());
				ano.setEnvironnement(calculerEnvironnement(lot));			
			}
		}
		System.out.println("total erreurs E30 apr�s : " + mapLots.get("13").size());
		
		
		iter = mapLots.get("14").iterator();
		while (iter.hasNext())
		{
			String numeroLot = iter.next();
			if(listeLotenAno.contains(numeroLot))
			{
				iter.remove();
			}			
		}
		System.out.println("total erreurs E31 apr�s : " + mapLots.get("14").size());
		
		

		
		// 4. Cr�ation des vues
		for (Map.Entry<String,List<String>> entry : mapLots.entrySet())
		{
	    	// Cr�ation de la vue et envoie vers SonarQube
	    	Vue vueParent = creerVue("LotsErreurKey" + entry.getKey(), "Lots en erreur - Edition " + entry.getKey(), "Vue regroupant tous les lots avec des composants en erreur", true);
			
			for (String lot : entry.getValue())
			{
				Vue vue = new Vue();
				vue.setKey("view_lot_" + lot);
				vue.setName("Lot " + lot);
				// Ajout des sous-vue
				api.ajouterSousVue(vue, vueParent);
			}
		}
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
	
	/**
	 * Permet de r�cup�rer les composants de Sonar tri�s par version
	 * 
	 * @return
	 */
	private Map<String, List<Projet>> recupererComposantsSonarVersion(String[] versions)
	{		
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets = api.getComposants();
		
		// Cr�ation de la map de retour en utilisant les versions donn�es
		Map<String, List<Projet>> retour = new HashMap<>();		
		
		for (String version : versions)
		{
			retour.put(version, new ArrayList<>());
		}
		retour.put(SANSVERSION, new ArrayList<>());
		
		boolean ajoute = false;
		
		for (Projet projet : projets)
		{			
			for (String version : versions)
			{
				if (projet.getNom().endsWith(version))
				{				
					retour.get(version).add(projet);
					ajoute = true;
				}
			}
			if (!ajoute)
			{
				retour.get(SANSVERSION).add(projet);
			}
		}
		return retour;		
	}
	
	private Map<String,List<String>> lotSonarQGError(String[] versions)
	{	    
		// R�cup�ration des composants Sonar selon les version demand�es
		Map<String, List<Projet>> mapProjets = recupererComposantsSonarVersion(versions);
	    
		// Cr�ation de la map de retour
		Map<String,List<String>> retour = new HashMap<>();

		// It�ration sur les composants pour remplir la map de retour avec les lot en erreur par version
	    for (Map.Entry<String, List<Projet>> entry : mapProjets.entrySet())
		{
	    	retour.put(entry.getKey(), new ArrayList<>());
	    	// Iteration sur la liste des projets
			for (Projet projet : entry.getValue())
			{
				//R�cup�ration du composant
				Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] {"lot","alert_status"});
				
				// R�cup�ration depuis la map des m�triques su num�ro de lot et su status de la Quality Gate
				Map<String, String> metriques = composant.getMapMetriques();			
				String lot = metriques.get("lot");
				String alert = metriques.get("alert_status");
				
				if (alert != null && Status.getStatus(alert) == Status.ERROR && lot != null && !lot.isEmpty())
				{
					retour.get(entry.getKey()).add(lot);
				}
			}
		}
	    return retour;	    
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
			if (!composant.getMetriques().isEmpty())
			{					
				String application = composant.getMetriques().get(0).getValue().trim().toUpperCase();		

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
    	Vue vue = creerVue(new StringBuilder("MEPMEP").append(nom).append(date).toString().replace("�", "e").replace("�", "u"), 
    			new StringBuilder("TEP ").append(nom).append(Statics.SPACE).append(date).toString(), new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString(), true);
    	
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
		Vue vue = creerVue(new StringBuilder("MEP").append(DateConvert.dateFrancais(entry.getKey(),"MMMyyyy")).append("Key").toString(), nomVue, 
				new StringBuilder("Vue des lots mis en production pendant le mois de ").append(entry.getKey()).toString(), true);
		
		// Ajout des sous-vue
		api.ajouterSousVues(entry.getValue(), vue);
    }
    
    /**
     * Cr�e une vue dans Sonar avec suppression ou non de la vue pr�c�dente.
     * @param key
     * @param name
     * @param description
     * @param suppression
     * @return
     */
    private Vue creerVue(String key, String name, String description, boolean suppression)
    {
    	//Contr�le
    	if (key == null || key.isEmpty() || name == null || name.isEmpty())
    	{
    		throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
    	}
    	
    	// Cr�ation de la vue
    	Vue vue = new Vue();
    	vue.setKey(key);
    	vue.setName(name);
    	
    	// Ajout de la description si elle est valoris�e
    	if(description != null)
    	{
    		vue.setDescription(description);
    	}
    	
    	// Suppresison de la vue pr�cedente
    	if (suppression)
    	{
    		api.supprimerVue(vue);
    	}
    	
    	// Appel de l'API Sonar
    	api.creerVue(vue);
    	
    	return vue;
    }
    
    /**
     * Permet de valoriser l'environnement par rapport aux dates de publication
     * @param lot
     * @return
     */
	private Environnement calculerEnvironnement(LotSuiviPic lot)
	{
		if (lot.getLivraison() != null)
			return Environnement.EDITION;
		if (lot.getVmoa() != null)
			return Environnement.VMOA;
		if (lot.getVmoe() != null)
			return Environnement.VMOE;
		if (lot.getTfon() != null)
			return Environnement.TFON;
		if (lot.getDevtu() != null)
			return Environnement.DEVTU;
		return Environnement.NOUVEAU;
	}
	
	/*---------- ACCESSEURS ----------*/
}