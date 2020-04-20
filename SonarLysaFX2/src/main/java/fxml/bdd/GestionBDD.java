package fxml.bdd;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import application.MainScreen;
import fxml.SubView;
import fxml.dialog.LegendeDialog;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.enums.Action;
import model.enums.Bordure;
import model.enums.OptionVisibilite;
import model.enums.Severity;
import model.fxml.AbstractFXMLModele;
import model.fxml.FiltreurFXML;
import model.fxml.ListeGetters;
import utilities.AbstractToStringImpl;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Controleur générique des tables de la base de données.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class GestionBDD<T extends AbstractFXMLModele<I>, A extends Action, I> extends AbstractToStringImpl implements SubView
{
    /*---------- ATTRIBUTS ----------*/

    /** Valeur du split de l'écran lorsque la colonne de selection des colonnes n'est pas affichee */
    private static final double SPLIT13 = 0.13D;
    /** Valeur du split de l'écran lorsque la colonne de selection des colonnes est affichee */
    private static final double SPLIT21 = 0.21D;

    private static final int SIZEREGION = 5;
    private static final String BDD = "BDD/";

    private static final String DQSTRING = "dq";
    private static final String COMPOSTRING = "compo";
    private static final String COMPOPLANTESTRING = "compoPlante";
    private static final String ANOSTRING = "ano";
    private static final String USERSTRING = "user";
    private static final String SELECTALL = "Tout sélectionner";

    @FXML
    private VBox box;
    @FXML
    private Button extraire;
    @FXML
    private Button importer;
    @FXML
    private Button dq;
    @FXML
    private Button compo;
    @FXML
    private Button compoPlante;
    @FXML
    private Button ano;
    @FXML
    private Button user;
    @FXML
    private VBox tablePane;
    @FXML
    private Button visibilite;
    @FXML
    private Button legende;
    @FXML
    private VBox listeColonne;
    @FXML
    private ComboBox<ListeGetters> comboColonne;
    @FXML
    private TextField filtre;
    @FXML
    private Button ajout;
    @FXML
    private VBox listeFiltres;
    @FXML
    private SplitPane splitPane;

    /** Controleur du tableau */
    private AbstractBDD<T, A, I> controlleur;

    /** Liste des données du tableau */
    private ObservableList<Node> children;

    /** liste des checkbox gérant l'affichage des colonnes du tableau */
    private ObservableList<Node> colonnesVisibilite;

    /** Nom du tableau des données affichees */
    private String tableauActuel;

    /** Affichage des légende du tableau */
    private LegendeDialog legendeDialog;

    /*---------- CONSTRUCTEURS ----------*/

    public GestionBDD()
    {
        tableauActuel = Statics.EMPTY;
    }

    @FXML
    public void initialize()
    {
        children = splitPane.getItems();
        colonnesVisibilite = listeColonne.getChildren();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public void afficher(ActionEvent event) throws IOException
    {
        Object source = event.getSource();
        if (children.size() > 1)
            children.remove(1);

        if (!(source instanceof Button))
            throw new TechnicalException("fxml.bdd.GestionFXMLViewControl.afficher - méthode liée à une mauvaise source : " + source);

        String id = ((Button) source).getId();
        String texte = ((Button) source).getText();

        switch (id)
        {
            case DQSTRING:
                traiterAffichage(DQSTRING, "/fxml/bdd/DefaultQualiteBDD.fxml");
                MainScreen.majTitre(BDD + texte);
                break;

            case COMPOSTRING:
                traiterAffichage(COMPOSTRING, "/fxml/bdd/ComposantBDD.fxml");
                MainScreen.majTitre(BDD + texte);
                break;

            case COMPOPLANTESTRING:
                traiterAffichage(COMPOPLANTESTRING, "/fxml/bdd/ComposantPlanteBDD.fxml");
                MainScreen.majTitre(BDD + texte);
                break;

            case ANOSTRING:
                traiterAffichage(ANOSTRING, "/fxml/bdd/AnomalieRTCBDD.fxml");
                MainScreen.majTitre(BDD + texte);
                break;

            case USERSTRING:
                traiterAffichage(USERSTRING, "/fxml/bdd/UtilisateurBDD.fxml");
                MainScreen.majTitre(BDD + texte);
                break;

            default:
                throw new TechnicalException("fxml.bdd.GestionFXMLViewControl.afficher - source non traitée : " + id);
        }
    }

    /**
     * Extraction des données affichées à l'écran dans un fichier Excel.
     * 
     * @throws IOException
     *                     Exception lancée s'il un problème Excel.
     */
    @FXML
    private void extraire() throws IOException
    {
        // Récupération du fichier
        File file = recupererFichier();

        // Création wb et feuille
        Workbook wb = WorkbookFactory.create(true);
        Sheet sheet = wb.createSheet(controlleur.getTitreExtraction());

        // Appel des implémentations spécifiques
        controlleur.extractImpl();

        // Création des styles pour les cellules
        int cellIndex = 0;
        CellHelper helper = new CellHelper(wb);
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        CellStyle styleBlanc = helper.getStyle(IndexedColors.WHITE);

        TableView<T> table = controlleur.getTable();

        // Création de la ligne de titres
        Row row = sheet.createRow(0);

        // Itération sur chaque colonne puis sur chaque "sous-colonne" pour récupérer l'en-tete de celle-ci
        for (TableColumn<T, ?> col : table.getColumns())
        {
            for (TableColumn<T, ?> col2 : col.getColumns())
            {
                // On ne prend pas les colonnes invisibles ou la colonne de sélection
                if (!col2.isVisible() || col2.getText().isEmpty())
                    continue;
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(col2.getText());
                cell.setCellStyle(styleTitre);
                cellIndex++;
            }
        }

        // Ajout des données dans le tableau
        ajoutDonneesTableau(table, sheet, wb, styleBlanc);

        // Premiere ligne bloquée pour le défilement
        sheet.createFreezePane(0, 1);

        // Ajout du filtre automatique sur les colonnes
        sheet.setAutoFilter(new CellRangeAddress(0, cellIndex, 0, sheet.getLastRowNum()));

        // Autosize des colonnes
        for (int i = 0; i <= cellIndex; i++)
        {
            sheet.autoSizeColumn(i);
        }

        // Ecriture et fermeture des flux
        OutputStream fileOut = Files.newOutputStream(file.toPath());
        wb.write(fileOut);
        wb.close();
        fileOut.close();
        controlleur.refreshList();
    }

    /**
     * Gestion de l'affichage de la colonne de gestion de l'affichage des colonnes du tableau.
     */
    @FXML
    public void gestionColonne()
    {
        if (listeColonne.isVisible())
        {
            listeColonne.setVisible(false);
            listeColonne.setManaged(false);
            splitPane.setDividerPositions(SPLIT13);
        }
        else
        {
            listeColonne.setVisible(true);
            listeColonne.setManaged(true);
            splitPane.setDividerPositions(SPLIT21);
        }
    }

    /**
     * Gère l'affichage de la fenêtre de légende du tableau.
     */
    @FXML
    public void legendeColonne()
    {
        if (legendeDialog.isShowing())
            legendeDialog.close();
        else
            legendeDialog.show();
    }

    /**
     * Ajout d'un filtre d'affichage sur les données du tableau.
     */
    @FXML
    public void filtrer()
    {
        // Contrôles
        String filtreText = filtre.getText();
        if (filtreText.isEmpty() || comboColonne.getSelectionModel().getSelectedItem() == null)
            throw new FunctionalException(Severity.INFO, "Vous devez choisir un texte pour le filtre et un colonne à filtrer.");

        // Filtre sur les données
        controlleur.filtrer(filtreText);

        // Affichage du filtre selectionne à l'écran
        Region region = new Region();
        region.setPrefHeight(SIZEREGION);
        Label label = new Label(comboColonne.getSelectionModel().getSelectedItem().getAffichage() + " - " + filtreText);
        listeFiltres.getChildren().add(region);
        listeFiltres.getChildren().add(label);
        filtre.setText(Statics.EMPTY);
    }

    /**
     * Suppression de tous les filtres sur les données du tableau.
     */
    @FXML
    public void supprimerFiltre()
    {
        controlleur.supprimerFiltre();
        listeFiltres.getChildren().clear();
    }

    /**
     * Affichage du tableau des données avec ou non reset des filtres et de la visibilité des colonnes.
     * 
     * @param fxml
     *               Adresse di fichier fxml.
     * @param option
     *               Option de visibilité.
     * @throws IOException
     *                     Exception lors des traitements fxml.
     */
    public void afficherTableau(String fxml, OptionVisibilite option) throws IOException
    {
        // Récupération du controlleur dediee aux données
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        VBox bddEq = loader.load();
        children.add(bddEq);

        // Affichage avec conservation des filtres ou non
        if (option == OptionVisibilite.RESET)
            resetVisibilite(loader);
        else
            garderVisibilite(loader);

        // Gestion de la separation de l'écran selon que la colonne de sélection des colonnes est visible ou non
        if (listeColonne.isVisible())
            splitPane.setDividerPositions(SPLIT21);
        else
            splitPane.setDividerPositions(SPLIT13);

        // Initialisation de la selection des colonnes à filtrer et maj des données du tableau
        controlleur.initComboFiltre(comboColonne);

        // Chargement de la page de légendes. Initialisation ud controlleur à la main pour envoyer la valeur du fxml à celui-ci
        legendeDialog = new LegendeDialog(fxml);
    }

    public File recupererFichier()
    {
        return Utilities.saveFileFromFileChooser("Emplacement du fichier à extraire");
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Ajoute les données au fichier Excel pour l'extraction
     * 
     * @param table
     *              Représentation de la table en base de données.
     * @param sheet
     *              Feuille Excel à traiter.
     * @param wb
     *              Workbook de la feuille Excel.
     * @param style
     *              Style des cellule Excel.
     */
    private void ajoutDonneesTableau(TableView<T> table, Sheet sheet, Workbook wb, CellStyle style)
    {
        // Ajout des données dans le tableau
        for (int i = 0; i < table.getItems().size(); i++)
        {
            Row ligne = sheet.createRow(i + 1);

            ajoutLigneTableau(table, ligne, wb, i, style);
        }
    }

    /**
     * Ajoute les données d'une ligne dans le fichier Excel
     * 
     * @param table
     *              Représentation de la table en base de données.
     * @param ligne
     *              Ligne où ajouter les données.
     * @param wb
     *              Workbook
     * @param style
     *              Style des cellule Excel.
     */
    private void ajoutLigneTableau(TableView<T> table, Row ligne, Workbook wb, int i, CellStyle style)
    {
        int cellIndex = 0;

        // Récupération de la valeur de chaque colonne
        for (TableColumn<T, ?> col : table.getColumns())
        {
            for (TableColumn<T, ?> col2 : col.getColumns())
            {
                Cell cell = ligne.createCell(cellIndex);
                cell.setCellStyle(style);

                // On ne prend pas les colonnes invisibles ou la colonne de sélection
                if (!col2.isVisible() || col2.getText().isEmpty())
                    continue;

                // Controle si on a une liste (valeur + url), pour créer l'hyperliens au besoin dans le fichier excel
                if (col2.getCellData(i) instanceof ObservableList)
                {
                    @SuppressWarnings("unchecked")
                    ObservableList<String> liste = (ObservableList<String>) col2.getCellData(i);
                    if (!liste.isEmpty())
                    {
                        cell.setCellValue(liste.get(0));
                        ajouterLiens(wb, cell, liste.get(1));
                    }
                }
                else
                    cell.setCellValue(col2.getCellData(i).toString());
                cellIndex++;
            }
        }
    }

    /**
     * Reinitialisation des colonnes de selection de la visibilité des colonnes du tableau
     * 
     * @param loader
     *               Chargeur des fichier fxml.
     */
    private void garderVisibilite(FXMLLoader loader)
    {
        // Récupération ancien predicat et checkbox de selection de toutes les lignes
        FiltreurFXML<T> predicate = null;

        // S'il y avait un controlleur deje instancié, on récupère le predicat de filtre et la checkbox de selection
        if (controlleur != null)
        {
            predicate = controlleur.getPredicate();
        }

        // Chargement du nouveau controlleur et récupération de la checkbox de selection
        controlleur = loader.getController();

        // Création d'une map des colonnes avec l'en-tete de la colonne en clef. Rajout dans le tableau de la checkbox de selection
        Map<String, TableColumn<T, ?>> colonnesTableau = new HashMap<>();
        for (TableColumn<T, ?> col : controlleur.getTable().getColumns())
        {
            for (TableColumn<T, ?> col2 : col.getColumns())
            {
                if (!col2.getText().isEmpty())
                    colonnesTableau.put(col2.getText(), col2);
                else
                {
                    CheckBox checkBox = new CheckBox();
                    controlleur.setSelectionTotal(checkBox);
                    col2.setGraphic(checkBox);
                }
            }
        }

        // Itération sur chaque checkBox de selection de la visibilité des colonnes pour remettre le listener correspondant à la bonne colonne.
        for (Node node : colonnesVisibilite)
        {
            // On ne prend que les checkbox qui sont liées à une colonne. On enlève donc les autres nodes et la checkbox de sélection.
            if (node instanceof CheckBox && !SELECTALL.equals(((CheckBox) node).getText()))
            {
                CheckBox checkBox = (CheckBox) node;
                TableColumn<T, ?> col = colonnesTableau.get(checkBox.getText());

                // Remise du listener sur la colonne du tableau
                checkBox.selectedProperty().addListener((object, old, newValue) -> col.setVisible(newValue));

                // Mise à jour de la valeur selectionée précedement
                col.setVisible(checkBox.isSelected());
            }
        }

        // Rafraichissement de la liste avec le predicat
        controlleur.refreshList(predicate);
    }

    /**
     * Reinitialisation de la visibilité de la page: On rafraichit la liste des objets à afficher, et on recrée la liste des checkbox pour la visibilité
     * des colonnes.
     * 
     * @param loader
     *               Chargeur des fichier fxml.
     */
    private void resetVisibilite(FXMLLoader loader)
    {
        controlleur = loader.getController();

        // Vidage de la liste des checkbox gérant la visibilité des colonnes du tableau
        colonnesVisibilite.clear();
        ajoutCheckBoxSelection();

        // Itération sur chaque colonne du tableau pour rajouter une CheckBox de selection de la visibilité en ajoutant le listener correspondant.
        for (TableColumn<T, ?> col : controlleur.getTable().getColumns())
        {
            for (TableColumn<T, ?> col2 : col.getColumns())
            {
                // Création checkbox de selection de toutes les lignes
                if (col2.getText().isEmpty())
                {
                    CheckBox checkBox = new CheckBox();
                    controlleur.setSelectionTotal(checkBox);
                    col2.setGraphic(checkBox);
                }
                else
                {
                    // Ajout de la checkBox
                    CheckBox check = new CheckBox(col2.getText());
                    check.setSelected(col2.isVisible());
                    check.selectedProperty().addListener((object, old, newValue) -> col2.setVisible(newValue));
                    colonnesVisibilite.add(check);
                    ajoutSeparateur();
                }
            }
        }
        controlleur.refreshList();
    }

    private void ajoutCheckBoxSelection()
    {
        CheckBox selectionTotal = new CheckBox(SELECTALL);

        // Ajout Listener pour cocher et décocher toutes les autres checkbox.
        selectionTotal.selectedProperty().addListener((observable, oldValue, newValue) -> colonnesVisibilite.stream().filter(n -> n instanceof CheckBox && !SELECTALL.equals(((CheckBox) n).getText()))
                .forEach(n -> ((CheckBox) n).setSelected(newValue)));

        colonnesVisibilite.add(selectionTotal);
        ajoutSeparateur();
        ajoutSeparateur();
    }

    /**
     * Ajout d'un separateur de 5 pixels
     */
    private void ajoutSeparateur()
    {
        Region region = new Region();
        region.setPrefHeight(SIZEREGION);
        colonnesVisibilite.add(region);
    }

    /**
     * Affichage du tableau des données
     * 
     * @param bouton
     *               Représentation String du tableau affiché.
     * @param fxml
     *               Adresse du fichier fxml
     * @throws IOException
     *                     Exception lancée lors du traitement des fichiers fxml.
     */
    private void traiterAffichage(String bouton, String fxml) throws IOException
    {
        if (!tableauActuel.equals(bouton))
        {
            tableauActuel = bouton;
            afficherTableau(fxml, OptionVisibilite.RESET);
            supprimerFiltre();
        }
        else
            afficherTableau(fxml, OptionVisibilite.GARDER);
    }

    /**
     * Ajoute un liens à une cellule, soit vers Sonar soit vers RTC
     * 
     * @param wb
     *                Workbook ud fichier Excel.
     * @param cell
     *                Cellule à traiter.
     * @param adresse
     *                Adresse du lien.
     */
    private void ajouterLiens(Workbook wb, Cell cell, String adresse)
    {
        if (wb == null || cell == null || adresse == null)
            throw new IllegalArgumentException("Le worbook, la cellule ou l'adresse ne peuvent être nuls.");

        CreationHelper createHelper = wb.getCreationHelper();
        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFont(font);
        cell.setCellStyle(style);
        link.setAddress(adresse);
        cell.setHyperlink(link);
    }

    /*---------- ACCESSEURS ----------*/

}
