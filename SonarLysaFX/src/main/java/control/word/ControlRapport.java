package control.word;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import model.InfoMail;
import model.ModelFactory;
import model.enums.Param;
import model.enums.TypeInfo;
import model.enums.TypeRapport;
import utilities.DateConvert;
import utilities.Statics;

/**
 * Controleur de création de rapport des traitements.
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 *
 */
public class ControlRapport extends AbstractControlWord
{
    /*---------- ATTRIBUTS ----------*/

    /** logger général */
    private static final Logger LOGGER = LogManager.getLogger("complet-log");
    
    // Constantes statiques
    private static final long MARGE = 960L;
    private static final String FONT = "Times New Roman";
    private static final int TITRESIZE = 18;
    private static final int CORPSSIZE = 12;
    private static final String TODAY = DateConvert.dateFrancais(LocalDate.now(), "dd MMMM YYYY");

    private TypeRapport typeRapport;
    private String extra;
    private Map<TypeInfo, List<InfoMail>> mapInfos;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlRapport(TypeRapport typeRapport)
    {
        // Appel controleur père avec fichier depuis paramètrage
        super(new File(Statics.proprietesXML.getMapParams().get(Param.ABSOLUTEPATHRAPPORT) + typeRapport.getNomFichier() + TODAY + DOCX));

        // Initialisation variables
        this.typeRapport = typeRapport;
        mapInfos = new EnumMap<>(TypeInfo.class);
        extra = Statics.EMPTY;

        for (TypeInfo type : TypeInfo.values())
        {
            mapInfos.put(type, new ArrayList<>());
        }
        initPage();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @Override
    public boolean creerFichier()
    {
        creerTitre();
        creerTexte();
        if (write())
        {
            LOGGER.info("Création du rapport OK.");
            return true;
        }
        return false;
    }

    /*---------- METHODES PRIVEES ----------*/

    private void creerTexte()
    {
        // Gection du paragraphe
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(CORPSSIZE);
        run.setFontFamily(FONT);
        
        // Gestion des infos
        for (TypeInfo type : TypeInfo.values())
        {
            List<InfoMail> lots = mapInfos.computeIfAbsent(type, n -> new ArrayList<>());
            if (lots.isEmpty())
                continue;

            run.setText(type.getTitre());
            run.addCarriageReturn();
            for (InfoMail info : lots)
            {
                run.setText(Statics.TIRET + info.getLot());
                if (!info.getInfoSupp().isEmpty() && !type.getLiens().isEmpty())
                    run.setText(type.getLiens() + info.getInfoSupp());

                run.addCarriageReturn();
            }
            run.addCarriageReturn();
        }

        // Ajout de données extra
        run.setText(extra);
    }
    
    private void initPage()
    {
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(MARGE));
        pageMar.setTop(BigInteger.valueOf(MARGE));
        pageMar.setRight(BigInteger.valueOf(MARGE));
        pageMar.setBottom(BigInteger.valueOf(MARGE));
    }
    
    private void creerTitre()
    {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(TITRESIZE);
        run.setBold(true);
        run.setText(typeRapport.getDebut());
        run.addCarriageReturn();
        run.setText(TODAY);   
        run.addCarriageReturn();
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Enregistre une information dans le mail de rapport
     * 
     * @param type
     * @param handler
     */
    public void addInfo(TypeInfo type, String lot, String infoSupp)
    {
        mapInfos.get(type).add(ModelFactory.getModelWithParams(InfoMail.class, lot, infoSupp));
    }

    /**
     * Ajoute des données sous forme d'une chaîne de caratères au mail.
     * 
     * @param extra
     */
    public void addExtra(String extra)
    {
        this.extra += extra;
    }
}
