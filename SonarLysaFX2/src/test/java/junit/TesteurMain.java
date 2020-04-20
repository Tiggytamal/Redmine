package junit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import control.rest.SonarAPI;
import dao.Dao;
import dao.DaoFactory;
import dao.DaoStatistiqueCompo;
import model.bdd.ComposantBase;
import model.bdd.StatistiqueCompo;
import model.enums.Bordure;
import model.enums.OptionGetCompos;
import model.enums.StatistiqueCompoEnum;
import model.enums.TypeObjetSonar;
import model.rest.sonarapi.ComposantSonar;
import model.rest.sonarapi.ObjetSonar;
import utilities.CellHelper;
import utilities.Statics;
import utilities.TechnicalException;

public class TesteurMain<C> extends JunitBase<C>
{
    private static Dao<ComposantBase, String> daoCompo = DaoFactory.getMySQLDao(ComposantBase.class);
    // private static DaoDefautQualite daoDq = DaoFactory.getDao(DefautQualite.class);
    // private static DaoUtilisateur daoUser = DaoFactory.getDao(Utilisateur.class);
    // private static DaoChefService daoChef = DaoFactory.getDao(ChefService.class);
    // private static DaoChefService daoChef = DaoFactory.getDao(ChefService.class);
    // private static final Pattern PATTERNSPLIT = Pattern.compile(" ");

    public static void main(String[] args) throws Exception
    {
        // Création encoding MDP
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m894,;:!");
        System.out.println("Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        // Création encoding MDP admin/admin
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println("Basic " + Base64.getEncoder().encodeToString(builder.toString().getBytes()));
    }

    @BeforeEach
    @Override
    public void init() throws Exception
    {

    }

    @Test
    // @Disabled(TESTMANUEL)
    public void testCompoBaseVersusCompoSonar()
    {
        List<ComposantSonar> liste = SonarAPI.build().getComposants(OptionGetCompos.MINIMALE, null);
        System.out.println(liste.size());
        Map<String, ComposantBase> map = daoCompo.readAllMap();

        for (ComposantSonar compo : liste)
        {
            if (!map.containsKey(compo.getKey() + compo.getBranche()))
                System.out.println(compo.getKey() + " - " + compo.getBranche());
        }

        List<String> listeNomCompo = new ArrayList<>();
        for (ComposantSonar compo : liste)
        {
            listeNomCompo.add(compo.getNom());
            if (!compo.getBranche().equals("master"))
                System.out.println(compo.getNom() + " - " + compo.getBranche());
        }

        System.out.println("autre sens");
        for (ComposantBase compo : map.values())
        {
            if (!listeNomCompo.contains(compo.getNom()))
                System.out.println(compo.getNom());
        }
    }

    @Test
    @Disabled(TESTMANUEL)
    public void test() throws Exception
    {
        List<ComposantBase> liste = daoCompo.readAll();

        for (ComposantBase compo : liste)
        {
            if (compo.getDateLeakPeriod() == null)
                compo.setDateLeakPeriod(compo.getDerniereAnalyse());
        }

        Collections.sort(liste, (o1, o2) -> o2.getDateLeakPeriod().compareTo(o1.getDateLeakPeriod()));

        Map<String, Map<String, ComposantBase>> map = new HashMap<>();

        for (ComposantBase compo : liste)
        {
            if (compo.getLotRTC() == null || compo.getLotRTC().getProjetClarity() == null || compo.getAppli() == null)
                continue;

            String code = compo.getLotRTC().getProjetClarity().getCode();
            String appli = compo.getAppli().getCode();
            Map<String, ComposantBase> mapMap = map.get(code);
            if (mapMap == null)
                map.put(code, new HashMap<>());
            map.get(code).put(appli, compo);
        }

        File file = new File("d:\\test.xlsx");
        Workbook wb = WorkbookFactory.create(true);
        Sheet sheet = wb.createSheet();
        CellHelper helper = new CellHelper(wb);
        Row row = sheet.createRow(0);
        CellStyle centre = helper.getStyle(IndexedColors.AQUA, Bordure.BAS, HorizontalAlignment.CENTER);
        Cell cell = row.createCell(0);
        cell.setCellStyle(centre);
        cell.setCellValue("Code Clarity");
        cell = row.createCell(1);
        cell.setCellStyle(centre);
        cell.setCellValue("Libelle projet");
        cell = row.createCell(2);
        cell.setCellStyle(centre);
        cell.setCellValue("Code application");
        cell = row.createCell(3);
        cell.setCellStyle(centre);
        cell.setCellValue("Date première analyse");

        int i = 0;

        for (Map<String, ComposantBase> loop : map.values())
        {
            for (ComposantBase compo : loop.values())
            {
                i++;
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellValue(compo.getLotRTC().getProjetClarity().getCode());
                cell = row.createCell(1);
                cell.setCellValue(compo.getLotRTC().getProjetClarity().getLibelleProjet());
                cell = row.createCell(2);
                cell.setCellValue(compo.getAppli().getCode());
                cell = row.createCell(3);
                if (compo.getDateLeakPeriod() != null)
                    cell.setCellValue(compo.getDateLeakPeriod().toLocalDate().toString());
                else
                    cell.setCellValue(compo.getDerniereAnalyse().toLocalDate().toString());
            }
        }

        sheet.setAutoFilter(new CellRangeAddress(0, sheet.getRow(0).getLastCellNum(), 0, sheet.getLastRowNum()));
        sheet.createFreezePane(0, 1);

        for (int j = 0; j <= 4; j++)
        {
            sheet.autoSizeColumn(j);
        }

        try (OutputStream stream = Files.newOutputStream(file.toPath()))
        {
            wb.write(stream);
            wb.close();
        }
        catch (IOException e)
        {
            throw new TechnicalException("Erreur au moment de sauvegarder le fichier Excel :" + file.getName(), e);
        }
    }

    @Test
    @Disabled(TESTMANUEL)
    public void test2() throws EncryptedDocumentException, IOException
    {
        SonarAPI api = SonarAPI.build();
        List<ComposantBase> liste = daoCompo.readAll();

        Workbook wb = WorkbookFactory.create(new File("d:\\Liste_Composants_Tribu.xlsx"));

        ObjetSonar portfolio = new ObjetSonar("pfTribuDistrubutionCollaborateur", "Tribu Distribution Collaborateur", "Liste des composants de la tribu Distribution Collaborateur",
                TypeObjetSonar.PORTFOLIO);
        api.creerObjetSonar(portfolio);
        Sheet sheet = wb.getSheetAt(0);
        int size = sheet.getLastRowNum();
        int i = 0;
        for (Row row : sheet)
        {
            String nomCompo = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            for (ComposantBase compo : liste)
            {
                if (compo.getNom().contains(nomCompo))
                {
                    api.ajouterSousProjet(portfolio, compo.getKey());
                    break;
                }
            }
            System.out.println(i++ + " - " + size);
        }
        api.calculObjetSonar(portfolio);
    }

    @Test
    // @Disabled(TESTMANUEL)
    public void test3() throws EncryptedDocumentException, IOException
    {
        Workbook wb = WorkbookFactory.create(new File("d:\\Liste_Composants_Tribu.xlsx"));
        List<ComposantBase> liste = daoCompo.readAll();
        List<ComposantBase> listeUtile = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);
        for (Row row : sheet)
        {
            String nomCompo = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            for (ComposantBase compo : liste)
            {
                if (compo.getNom().contains(nomCompo))
                {
                    listeUtile.add(compo);
                    break;
                }
            }
        }

        System.out.println(listeUtile.size());

        DaoStatistiqueCompo daoSC = DaoFactory.getMySQLDao(StatistiqueCompo.class);
        Map<String, TreeMap<LocalDate, Integer>> map = new HashMap<>();

        // Récupération depuis la base de données des informations pour remplir la série
        List<StatistiqueCompo> ndlctc = ((DaoStatistiqueCompo) DaoFactory.getMySQLDao(StatistiqueCompo.class)).recupAllParType(StatistiqueCompoEnum.NEWLDCTOCOVER);

        // Création d'une map avec les données. Pour chaque composant on aura une liste des lignes par date
        for (StatistiqueCompo stat : ndlctc)
        {
            // On cherche dans la map, la treemap correspondante au composant, si on ne la trouve pas, on en crée une avec le bon comparateur intégré
            // Puis on ajoute la nouvelle valeur à celle-ci
            if (listeUtile.contains(stat.getCompo()))
            {
                TreeMap<LocalDate, Integer> treeMap = map.computeIfAbsent(stat.getCompo().getNom(), k -> new TreeMap<LocalDate, Integer>((o1, o2) -> o2.compareTo(o1)));
                treeMap.put(stat.getDate(), stat.getValeur());
            }
        }

        int retour = 0;
        for (TreeMap<LocalDate, Integer> donnees : map.values())
        {
            // Pour chaque treemap des composant, on cherche la dernière valeur la plus récente pour l'ajouter à la statistique
            Optional<Map.Entry<LocalDate, Integer>> optional = donnees.entrySet().stream().filter(e -> 
            { 
                return e.getKey().isBefore(LocalDate.of(2020, 4, 2)); 
            }).findFirst();
            if (optional.isPresent())
                retour += optional.get().getValue();
        }
        System.out.println(retour);
    }
}
