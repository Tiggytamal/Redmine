package junit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.getField;

import java.time.LocalDate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import control.xml.ControlXML;
import dao.DaoApplication;
import dao.DaoChefService;
import dao.DaoComposantSonar;
import dao.DaoDateMaj;
import dao.DaoDefautAppli;
import dao.DaoDefautQualite;
import dao.DaoEdition;
import dao.DaoLotRTC;
import dao.DaoProduit;
import dao.DaoProjetClarity;
import dao.ListeDao;
import model.DataBaseXML;
import model.Info;
import model.ProprietesXML;
import model.bdd.ChefService;
import model.bdd.ComposantSonar;
import model.bdd.DateMaj;
import model.bdd.DefautAppli;
import model.bdd.DefautQualite;
import model.bdd.Edition;
import model.bdd.LotRTC;
import model.bdd.Produit;
import model.bdd.ProjetClarity;
import utilities.Statics;

/**
 * Classe de base de tous les tests Junit. Permet de récupérer les fichier de paramètres depuis les resources et de simuler une connexion utlisateur
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public abstract class JunitBase
{
    /*---------- ATTRIBUTS ----------*/

    protected static final ProprietesXML proprietes = new ControlXML().recupererXMLResources(ProprietesXML.class);
    protected static final Info info = new ControlXML().recupererXMLResources(Info.class);
    protected final LocalDate today = LocalDate.now();

    /** logger plantages de l'application */
    private static final Logger LOGPLANTAGE = LogManager.getLogger("plantage-log");

    private DataBaseXML dataBase;

    /*---------- CONSTRUCTEURS ----------*/

    protected JunitBase()
    {
        // Mock des fichiers de paramètres depuis les ressources
        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, info);

        dataBase = new ControlXML().recupererXMLResources(DataBaseXML.class);

        try
        {
            initMockDao();
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOGPLANTAGE.error(e);
        }
    }

    /*---------- METHODES ABSTRAITES ----------*/

    @Before
    public abstract void init() throws Exception;

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/

    @SuppressWarnings("unchecked")
    private void initMockDao() throws IllegalArgumentException, IllegalAccessException
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SonarLysaFX");
        
        // DAO Appli
        DaoApplication daoAppliMock = mock(DaoApplication.class);        

        // DAO ComposantSonar
        DaoComposantSonar daoCompoMock = mock(DaoComposantSonar.class);
        getField(DaoComposantSonar.class, "em").set(daoCompoMock, emf.createEntityManager());
        when(daoCompoMock.readAll()).thenReturn(dataBase.getCompos());
        when(daoCompoMock.persist(Mockito.any(ComposantSonar.class))).thenReturn(true);
        when(daoCompoMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO DateMaj
        DaoDateMaj daoDateMajMock = mock(DaoDateMaj.class);
        getField(DaoDateMaj.class, "em").set(daoDateMajMock, emf.createEntityManager());
        when(daoDateMajMock.persist(Mockito.any(DateMaj.class))).thenReturn(true);
        when(daoDateMajMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO LotRTC
        DaoLotRTC daoLotRTCMock = mock(DaoLotRTC.class);
        getField(DaoLotRTC.class, "em").set(daoLotRTCMock, emf.createEntityManager());
        when(daoLotRTCMock.readAll()).thenReturn(dataBase.getLotsRTC());
        when(daoLotRTCMock.persist(Mockito.any(LotRTC.class))).thenReturn(true);
        when(daoLotRTCMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO DefautQualite
        DaoDefautQualite daoDqMock = mock(DaoDefautQualite.class);
        getField(DaoDefautQualite.class, "em").set(daoDqMock, emf.createEntityManager());
        when(daoDqMock.readAll()).thenReturn(dataBase.getDqs());
        when(daoDqMock.persist(Mockito.any(DefautQualite.class))).thenReturn(true);
        when(daoDqMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO DefuatApplication
        DaoDefautAppli daoDefautAMock = mock(DaoDefautAppli.class);
        getField(DaoDefautAppli.class, "em").set(daoDefautAMock, emf.createEntityManager());
        when(daoDefautAMock.persist(Mockito.any(DefautAppli.class))).thenReturn(true);
        when(daoDefautAMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO Edition
        DaoEdition daoEditionMock = mock(DaoEdition.class);
        getField(DaoEdition.class, "em").set(daoEditionMock, emf.createEntityManager());
        when(daoEditionMock.recupEltParIndex(Statics.EDINCONNUE)).thenReturn(Edition.getEditionInconnue(Statics.EDINCONNUE));
        when(daoEditionMock.persist(Mockito.any(Edition.class))).thenReturn(true);
        when(daoEditionMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO Produit
        DaoProduit daoProduitMock = mock(DaoProduit.class);
        getField(DaoProduit.class, "em").set(daoProduitMock, emf.createEntityManager());
        when(daoProduitMock.persist(Mockito.any(Produit.class))).thenReturn(true);
        when(daoProduitMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO ProjetClatity
        DaoProjetClarity daoProjetClarityMock = mock(DaoProjetClarity.class);
        getField(DaoProjetClarity.class, "em").set(daoProjetClarityMock, emf.createEntityManager());
        when(daoProjetClarityMock.persist(Mockito.any(ProjetClarity.class))).thenReturn(true);
        when(daoProjetClarityMock.persist(Mockito.any(Iterable.class))).thenReturn(1);

        // DAO ChefService
        DaoChefService daoChefServ = mock(DaoChefService.class);
        getField(DaoChefService.class, "em").set(daoChefServ, emf.createEntityManager());
        when(daoChefServ.persist(Mockito.any(ChefService.class))).thenReturn(true);
        when(daoChefServ.persist(Mockito.any(Iterable.class))).thenReturn(1);

        Whitebox.setInternalState(ListeDao.class, daoAppliMock);
        Whitebox.setInternalState(ListeDao.class, daoCompoMock);
        Whitebox.setInternalState(ListeDao.class, daoDateMajMock);
        Whitebox.setInternalState(ListeDao.class, daoDqMock);
        Whitebox.setInternalState(ListeDao.class, daoDefautAMock);
        Whitebox.setInternalState(ListeDao.class, daoEditionMock);
        Whitebox.setInternalState(ListeDao.class, daoLotRTCMock);
        Whitebox.setInternalState(ListeDao.class, daoProduitMock);
        Whitebox.setInternalState(ListeDao.class, daoProjetClarityMock);
        Whitebox.setInternalState(ListeDao.class, daoChefServ);
    }

    /*---------- ACCESSEURS ----------*/
}
