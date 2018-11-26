package junit;

import java.time.LocalDate;

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
    
    private DataBaseXML dataBase;

    /*---------- CONSTRUCTEURS ----------*/

    protected JunitBase()
    {
        // Mock des fichiers de paramètres depuis les ressources
        Whitebox.setInternalState(Statics.class, proprietes);
        Whitebox.setInternalState(Statics.class, info);
        
        dataBase = new ControlXML().recupererXMLResources(DataBaseXML.class);

        initMockDao();
    }

    /*---------- METHODES ABSTRAITES ----------*/

    @Before
    public abstract void init() throws Exception;
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    
    private void initMockDao()
    {
        // DAO Appli
        DaoApplication daoAppliMock = Mockito.mock(DaoApplication.class);
        
        // DAO ComposantSonar
        DaoComposantSonar daoCompoMock = Mockito.mock(DaoComposantSonar.class);
        Mockito.when(daoCompoMock.readAll()).thenReturn(dataBase.getCompos());
        
        // DAO DateMaj
        DaoDateMaj daoDateMajMock = Mockito.mock(DaoDateMaj.class);
        
        // DAO LotRTC
        DaoLotRTC daoLotRTCMock = Mockito.mock(DaoLotRTC.class);
        Mockito.when(daoLotRTCMock.readAll()).thenReturn(dataBase.getLotsRTC());
        
        // DAO DefautQualite
        DaoDefautQualite daoDqMock = Mockito.mock(DaoDefautQualite.class);
        Mockito.when(daoDqMock.readAll()).thenReturn(dataBase.getDqs());
        
        // DAO DefuatApplication
        DaoDefautAppli daoDefautAMock = Mockito.mock(DaoDefautAppli.class);
                
        // DAO Edition
        DaoEdition daoEdtionMock = Mockito.mock(DaoEdition.class);
        
        // DAO Produit
        DaoProduit daoProduitMock = Mockito.mock(DaoProduit.class);
        
        // DAO ProjetClatity
        DaoProjetClarity daoProjetClarityMock = Mockito.mock(DaoProjetClarity.class);
        
        // DAO ChefService
        DaoChefService daoChefServ = Mockito.mock(DaoChefService.class);
        
        Whitebox.setInternalState(ListeDao.class, daoAppliMock);
        Whitebox.setInternalState(ListeDao.class, daoCompoMock);
        Whitebox.setInternalState(ListeDao.class, daoDateMajMock);
        Whitebox.setInternalState(ListeDao.class, daoDqMock);
        Whitebox.setInternalState(ListeDao.class, daoDefautAMock);
        Whitebox.setInternalState(ListeDao.class, daoEdtionMock);
        Whitebox.setInternalState(ListeDao.class, daoLotRTCMock);
        Whitebox.setInternalState(ListeDao.class, daoProduitMock);
        Whitebox.setInternalState(ListeDao.class, daoProjetClarityMock);
        Whitebox.setInternalState(ListeDao.class, daoChefServ);
    }
    
    /*---------- ACCESSEURS ----------*/
}
