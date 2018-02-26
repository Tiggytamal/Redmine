package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.Anomalie;
import model.enums.Environnement;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

/**
 * Classe de getion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Gr�goire Mathon
 *
 */
public class ControlAno extends ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	// Liste des indices des colonnes
	private int colDir;
	private int colDepart;
	private int colService;
	private int colResp;
	private int colClarity;
	private int colLib;
	private int colCpi;
	private int colEdition;
	private int colLot;
	private int colEnv;
	private int colAno;
	private int colEtat;
	private int colSec;
	private int colRemarque;

	// Liste des noms de colonnes
	private static final String DIRECTION = "Direction";
	private static final String DEPARTEMENT = "D�partement";
	private static final String SERVICE = "Service";
	private static final String RESPSERVICE = "Responsable Service";
	private static final String CLARITY = "Projet Clarity";
	private static final String LIBELLE = "Libelle projet";
	private static final String CPI = "CPI du lot";
	private static final String EDITION = "Edition";
	private static final String LOT = "Lot projet RTC";
	private static final String ENV = "Etat du lot";
	private static final String ANOMALIE = "Anomalie";
	private static final String ETAT = "Etat de l'anomalie";
	private static final String SECURITE = "Securit�";
	private static final String REMARQUE = "Remarque";
	private static final String TRAITE = "Trait�";

	// Nom de la feuillle avec les naomalies en cours
	private static final String SQ = "SUIVI Qualit�";
	private static final String AC = "Anomalies closes";
	private static final String CLOSE = "Close";
	private static final String ABANDONNEE = "Abandonn�e";
	private static final String LIENSLOTS = "http://ttp10-snar.ca-technologies.fr/governance?id=";
	private static final String LIENSANO = "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/Support%20NICE#action=com.ibm.team.workitem.viewWorkItem&id=";
	private static final String SECURITEKO = "X";

	/*---------- CONSTRUCTEURS ----------*/

	protected ControlAno(File file) throws InvalidFormatException, IOException
	{
		super(file);
	}

	/*---------- METHODES PUBLIQUES ----------*/

	protected List<Anomalie> listAnomaliesSurLotsCrees()
	{
		// R�cup�ration de la premi�re feuille
		Sheet sheet = wb.getSheet(SQ);

		// Liste de retour
		List<Anomalie> retour = new ArrayList<>();

		// It�ration sur chaque ligne pour cr�er les anomalies
		for (int i = 1; i <= sheet.getLastRowNum(); i++)
		{
			Row row = sheet.getRow(i);

			// Cr�ation de l'anomalie
			Anomalie ano = new Anomalie();
			ano.setDirection(row.getCell(colDir).getStringCellValue());
			ano.setDepartement(row.getCell(colDepart).getStringCellValue());
			ano.setService(row.getCell(colService).getStringCellValue());
			ano.setResponsableService(row.getCell(colResp).getStringCellValue());
			ano.setProjetClarity(row.getCell(colClarity).getStringCellValue());
			ano.setLibelleProjet(row.getCell(colLib).getStringCellValue());
			ano.setCpiProjet(row.getCell(colCpi).getStringCellValue());
			ano.setEdition(row.getCell(colEdition).getStringCellValue());
			ano.setLot(row.getCell(colLot).getStringCellValue());
			ano.setEnvironnement(Environnement.getEnvironnement(row.getCell(colEnv).getStringCellValue()));
			
			// Num�ro anomalie
			Cell cellAno = row.getCell(colAno);
			ano.setNumeroAnomalie((int) cellAno.getNumericCellValue());
			// Si le liens n'est pas nul on le sauvegarde
			if (cellAno.getHyperlink() != null)
				ano.setLiensAno(cellAno.getHyperlink().getAddress());
			
			ano.setEtat(row.getCell(colEtat).getStringCellValue());
			ano.setSecurite(row.getCell(colSec).getStringCellValue());
			ano.setRemarque(row.getCell(colRemarque).getStringCellValue());
			retour.add(ano);
		}
		return retour;
	}

	protected List<Anomalie> createSheetError(String nomSheet, List<Anomalie> anoAcreer) throws IOException
	{
		// Cr�ation de la feuille de calcul
		Sheet sheet = wb.getSheet(nomSheet);

		List<Anomalie> retour = new ArrayList<>();

		// Liste des lots existants. On it�re si la feuille existe d�j�, pour r�cup�rer tous les num�ros de lot d�j� enregistr�s.
		List<String> lotsTraites = new ArrayList<>();
		if (sheet != null)
		{
			Iterator<Row> iter = sheet.rowIterator();
			while (iter.hasNext())
			{
				Row row = iter.next();
				Cell cellLot = row.getCell(Index.LOTI.ordinal());
				Cell cellT = row.getCell(Index.TRAITEI.ordinal());
				if (cellLot.getCellTypeEnum() == CellType.STRING && cellT.getCellTypeEnum() == CellType.STRING && cellT.getStringCellValue().equals("O"))
					lotsTraites.add(row.getCell(Index.LOTI.ordinal()).getStringCellValue());
			}
			wb.removeSheetAt(wb.getSheetIndex(sheet));
		}

		// Recr�ation de la feuille
		sheet = wb.createSheet(nomSheet);
		Cell cell;

		// Cr�ation du style des titres
		CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);
		
		// Cr�ation des noms des colonnes
		Row titres = sheet.createRow(0);
		for (Index index : Index.values())
		{
			cell = titres.createCell(index.ordinal());
			cell.setCellStyle(styleTitre);
			switch (index)
			{
				case LOTI:
					cell.setCellValue(LOT);
					break;
				case EDITIONI:
					cell.setCellValue(EDITION);
					break;
				case ENVI:
					cell.setCellValue(ENV);
					break;
				case TRAITEI:
					cell.setCellValue(TRAITE);
					break;
			}
		}

		// It�ration sur les anomalies � cr�er. Si elles sont d�j� dans les anomalies trait�es, on cr��e une ligne � l'�tat trait�e sinon on cr�e une ligne � l'�tat non trait� et on ajoute celle-ci aux anomalies � cr�er.
		for (Anomalie anomalie : anoAcreer)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			if (lotsTraites.contains(anomalie.getLot()))
			{
				creerLigneVersion(row, anomalie, IndexedColors.WHITE, "O");
			}
			else
			{
				creerLigneVersion(row, anomalie, IndexedColors.LIGHT_YELLOW, "N");
				retour.add(anomalie);
			}
		}

		sheet.autoSizeColumn(Index.LOTI.ordinal());
		sheet.autoSizeColumn(Index.EDITIONI.ordinal());
		sheet.autoSizeColumn(Index.ENVI.ordinal());
		// Ecriture du fichier Excel
		write();
		return retour;
	}

	/**
	 * Permet de mettre � jour les anomalies avec une quality Gate bonne
	 * 
	 * @param lotsEnErreur
	 *            Liste de tous les lots avec un Quality Gate � "ERROR".
	 * @throws IOException
	 */
	protected void majAnoOK(Set<String> lotsEnErreur) throws IOException
	{
		// R�cup�ration de la feuille avec les anomalies
		Sheet sheet = wb.getSheet(SQ);

		// It�ration sur chaque ligne
		for (int i = 1; i <= sheet.getLastRowNum(); i++)
		{
			Row row = sheet.getRow(i);

			// Si le num�ro n'est pas pr�sent dans la liste, c'est que le Quality Gate est bon
			String string = row.getCell(colLot).getStringCellValue().substring(4);
			if (!lotsEnErreur.contains(string))
			{
				majCouleurLigne(row, IndexedColors.LIGHT_GREEN);
			}
			else
			{
				majCouleurLigne(row, IndexedColors.WHITE);
			}
		}

		// Ecriture du fichier
		write();
	}

	/**
	 * Rajoute les nouvelles anomalies � la premi�re page du fichier Excel
	 * 
	 * @param anoAajouter
	 * @param lotsSecurite 
	 * @param anoAajouter2
	 * @param lotsEnErreur
	 * @throws IOException
	 */
	protected void majNouvellesAno(List<Anomalie> lotsEnAno, List<Anomalie> anoAajouter, Set<String> lotsEnErreurSonar, List<String> lotsSecurite) throws IOException
	{
		// Cr�ation d'une nouvelle feuille d'anomalies
		Sheet sheet = wb.getSheet(SQ);
		if (sheet != null)
			wb.removeSheetAt(wb.getSheetIndex(sheet));
		sheet = wb.createSheet(SQ);
		creerLigneTitres(sheet);

		// R�cup�ration feuille des anomalies closes
		Sheet sheetClose = wb.getSheet(AC);
		if (sheetClose == null)
		{
			sheetClose = wb.createSheet(AC);
			creerLigneTitres(sheetClose);
		}
				
		for (Anomalie ano : lotsEnAno)
		{
			Row row;
			String lot  = ano.getLot().substring(4);
			
			// Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
			if (lotsSecurite.contains(lot))
				ano.setSecurite(SECURITEKO);
			
			// Si une anomalie est close dans RTC, on la transfert sur l'autre feuille.
			if (CLOSE.equals(ano.getEtat()) || ABANDONNEE.equals(ano.getEtat()))
			{
				row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
				creerLigneSQ(row, ano, CouleurLigne.BLANC);
				continue;
			}

			// Mise en vert des anomalies avec un Quality Gate bon
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			if (!lotsEnErreurSonar.contains(lot))
			{
				creerLigneSQ(row, ano, CouleurLigne.VERT);
			}
			else
			{
				creerLigneSQ(row, ano, CouleurLigne.BLANC);
			}

		}

		// Ajout des nouvelles anomalies
		for (Anomalie ano : anoAajouter)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			// Contr�le si le lot a une erreur de s�curit� pour mettre � jour la donn�e.
			if (lotsSecurite.contains(ano.getLot().substring(4)))
				ano.setSecurite(SECURITEKO);
			creerLigneSQ(row, ano, CouleurLigne.ROUGE);
		}
		autosizeColumns(sheet);
		autosizeColumns(sheetClose);
		write();
	}

	@Override
	protected void calculIndiceColonnes()
	{
		// R�cup�ration de la premi�re feuille
		Sheet sheet = wb.getSheet(SQ);
		if (sheet == null)
			throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier n'a pas de page Suivi Qualit�");
				
		titres = sheet.getRow(0);
		
		// R�cup�ration des indices de colonnes
		for (Cell cell : titres)
		{
			if (cell.getCellTypeEnum() == CellType.STRING)
			{
				switch (cell.getStringCellValue())
				{
					case DIRECTION:
						colDir = cell.getColumnIndex();
						testMax(colDir);
						break;
					case DEPARTEMENT:
						colDepart = cell.getColumnIndex();
						testMax(colDepart);
						break;
					case SERVICE:
						colService = cell.getColumnIndex();
						testMax(colDir);
						break;
					case RESPSERVICE:
						colResp = cell.getColumnIndex();
						testMax(colService);
						break;
					case CLARITY:
						colClarity = cell.getColumnIndex();
						testMax(colClarity);
						break;
					case LIBELLE:
						colLib = cell.getColumnIndex();
						testMax(colLib);
						break;
					case CPI:
						colCpi = cell.getColumnIndex();
						testMax(colCpi);
						break;
					case EDITION:
						colEdition = cell.getColumnIndex();
						testMax(colEdition);
						break;
					case LOT:
						colLot = cell.getColumnIndex();
						testMax(colLot);
						break;
					case ENV:
						colEnv = cell.getColumnIndex();
						testMax(colEnv);
						break;
					case ANOMALIE:
						colAno = cell.getColumnIndex();
						testMax(colAno);
						break;
					case ETAT:
						colEtat = cell.getColumnIndex();
						testMax(colEtat);
						break;
					case SECURITE:
						colSec = cell.getColumnIndex();
						testMax(colEtat);
						break;
					case REMARQUE:
						colRemarque = cell.getColumnIndex();
						testMax(colRemarque);
						break;
					default:
						// Colonne sans nom reconnu
						break;
				}
			}
		}
	}

	/*---------- METHODES PRIVEES ----------*/

	/*---------- METHODES PRIVEES ----------*/

	private void creerLigneSQ(Row row, Anomalie ano, CouleurLigne couleur)
	{
		// Contr�les
		if (couleur == null || row == null || ano == null)
			throw new IllegalArgumentException("Les arguments ne peuvent aps �tre nuls");

		// Variables
		CellHelper helper = new CellHelper(wb);
		CellStyle normal = null;
		CellStyle centre = null;

		// Switch sur la couleur pour cr�er les styles
		switch (couleur)
		{
			case BLANC:
				normal = helper.getStyle(IndexedColors.WHITE);
				centre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
				break;
			case ROUGE:
				normal = helper.getStyle(IndexedColors.LIGHT_ORANGE);
				centre = helper.getStyle(IndexedColors.LIGHT_ORANGE, Bordure.VIDE, HorizontalAlignment.CENTER);
				break;
			case VERT:
				normal = helper.getStyle(IndexedColors.LIGHT_GREEN);
				centre = helper.getStyle(IndexedColors.LIGHT_GREEN, Bordure.VIDE, HorizontalAlignment.CENTER);
				break;
		}

		// Alimentation avec les donn�es de l'anomalie
		Cell cell = row.createCell(colDir);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getDirection());
		cell = row.createCell(colDepart);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getDepartement());
		cell = row.createCell(colService);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getService());
		cell = row.createCell(colResp);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getResponsableService());
		cell = row.createCell(colClarity);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getProjetClarity());
		cell = row.createCell(colLib);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getLibelleProjet());
		cell = row.createCell(colCpi);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getCpiProjet());
		cell = row.createCell(colEdition);
		cell.setCellStyle(centre);
		cell.setCellValue(ano.getEdition());
		cell = row.createCell(colLot);
		cell.setCellStyle(centre);
		
		// Gestion des num�ros de lots
		cell.setCellValue(ano.getLot());
		ajouterLiens(cell, LIENSLOTS, ano.getLot().substring(4));
		
		// Gestion de la variable d'environnement
		cell = row.createCell(colEnv);
		cell.setCellStyle(centre);
		if (ano.getEnvironnement() != null)
			cell.setCellValue(ano.getEnvironnement().toString());
		
		// Gestion des num�ros d'anomalies
		cell = row.createCell(colAno);
		cell.setCellStyle(centre);
		int numeroAno = ano.getNumeroAnomalie();
		if (numeroAno != 0)
		{
			cell.setCellValue(numeroAno);
			if (ano.getLiensAno() != null)
				ajouterLiens(cell, ano.getLiensAno());
			else 
				ajouterLiens(cell, LIENSANO, String.valueOf(numeroAno));
		}
		cell = row.createCell(colEtat);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getEtat());
		cell = row.createCell(colSec);
		cell.setCellStyle(centre);
		cell.setCellValue(ano.getSecurite());
		cell = row.createCell(colRemarque);
		cell.setCellStyle(normal);
		cell.setCellValue(ano.getRemarque());
	}

	private void creerLigneVersion(Row row, Anomalie ano, IndexedColors couleur, String traite)
	{
		CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

		Cell cell = row.createCell(Index.LOTI.ordinal());
		cell.setCellValue(ano.getLot());
		cell.setCellStyle(centre);
		cell = row.createCell(Index.EDITIONI.ordinal());
		cell.setCellValue(ano.getEdition());
		cell.setCellStyle(helper.getStyle(couleur));
		cell = row.createCell(Index.ENVI.ordinal());
		cell.setCellValue(ano.getEnvironnement().toString());
		cell.setCellStyle(centre);
		cell = row.createCell(Index.TRAITEI.ordinal());
		cell.setCellValue(traite);
		cell.setCellStyle(centre);
	}
	
	private void creerLigneTitres(Sheet sheet)
	{
		// Cr�ation de la ligne de titres
		Row titresNew = sheet.createRow(0);
		
		// On it�re sur la ligne de titres
		for (int i = 0; i < titres.getLastCellNum(); i++)
		{
			Cell newCell = titresNew.createCell(i);		
			Cell oldCell = titres.getCell(i);
			
	        // On continue si la cellule du titre est nulle
	        if (oldCell == null) 
	        {
	            continue;
	        }

	        // Copie du style des cellules
	        CellStyle newCellStyle = wb.createCellStyle();
	        newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
	        newCell.setCellStyle(newCellStyle);

	        // Copie des titres
	        newCell.setCellValue(oldCell.getStringCellValue());
	        switch (oldCell.getCellTypeEnum()) 
	        {
	            case BLANK:
	                newCell.setCellValue(oldCell.getStringCellValue());
	                break;
	            case BOOLEAN:
	                newCell.setCellValue(oldCell.getBooleanCellValue());
	                break;
	            case ERROR:
	                newCell.setCellErrorValue(oldCell.getErrorCellValue());
	                break;
	            case FORMULA:
	                newCell.setCellFormula(oldCell.getCellFormula());
	                break;
	            case NUMERIC:
	                newCell.setCellValue(oldCell.getNumericCellValue());
	                break;
	            case STRING:
	                newCell.setCellValue(oldCell.getRichStringCellValue());
	                break;
				case _NONE:
					break;
	        }
		}
	}
		
	private void ajouterLiens(Cell cell, String baseAdresse, String variable)
	{
		if (cell == null || baseAdresse == null || baseAdresse.isEmpty())
			throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent �tre nulles");
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
		Font font = wb.createFont();
		font.setUnderline(Font.U_SINGLE);
		font.setColor(IndexedColors.BLUE.index);
		cell.getCellStyle().setFont(font);
		link.setAddress(baseAdresse + variable);
		cell.setHyperlink(link);
	}
	
	private void ajouterLiens(Cell cell, String baseAdresse)
	{
		ajouterLiens(cell, baseAdresse, null);
	}
	
	/*---------- ACCESSEURS ----------*/

	/**
	 * Liste des num�ros de colonnes des feuilles d'environnement
	 * 
	 * @author ETP8137 - Gr�goire mathon
	 *
	 */
	private enum Index {
		LOTI, EDITIONI, ENVI, TRAITEI;
	}

	private enum CouleurLigne {
		BLANC, VERT, ROUGE;
	}
}