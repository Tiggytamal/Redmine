package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFComment;
import org.junit.Test;

import model.Anomalie;
import model.InfoClarity;
import model.LotSuiviRTC;
import model.ModelFactory;
import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.TypeAction;
import utilities.Statics;

public class TestAnomalie extends AbstractTestModel<Anomalie>
{
    /*---------- ATTRIBUTS ----------*/

    private XSSFComment comment;
    
    /*---------- CONSTRUCTEURS ----------*/

    public TestAnomalie()
    {
        comment = new XSSFComment(null, null, null);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnomlieWithLot()
    {
        // Test création anomalie depuis LotSuiviRTC
        handler = ModelFactory.getModelWithParams(Anomalie.class, ModelFactory.getModel(LotSuiviRTC.class));
        assertNotNull(handler);
    }
    
    @Test
    public void testMajDepuisPic()
    {
        // Création lotPic
        LotSuiviRTC lotRTC = ModelFactory.getModel(LotSuiviRTC.class);
        String cpi = "cpi";
        lotRTC.setCpiProjet(cpi);
        String edition = "edition";
        lotRTC.setEdition(edition);
        String libProjet = "libProjet";
        lotRTC.setLibelle(libProjet);
        String clarity = "clarity";
        lotRTC.setProjetClarity(clarity);
        String lot = "123456";
        lotRTC.setLot(lot);
        lotRTC.setEtatLot(EtatLot.NOUVEAU);
        
        // Test
        handler.majDepuisRTC(lotRTC);
        assertEquals(cpi, handler.getCpiProjet());
        assertEquals(edition, handler.getEdition());
        assertEquals(libProjet, handler.getLibelleProjet());
        assertEquals(clarity, handler.getProjetClarity());
        assertEquals("Lot " + lot, handler.getLot());
        assertEquals(EtatLot.NOUVEAU, handler.getEtatLot());       
    }
    
    @Test
    public void testMajDepuisClarity()
    {
        // Création Clarity
        InfoClarity infoClarity = ModelFactory.getModel(InfoClarity.class);
        String direction = "direction";
        infoClarity.setDirection(direction);
        String departement = "departement";
        infoClarity.setDepartement(departement);
        String service = "service";
        infoClarity.setService(service);
        
        // Test
        handler.majDepuisClarity(infoClarity);
        assertEquals(direction, handler.getDirection());
        assertEquals(departement, handler.getDepartement());
        assertEquals(service, handler.getService());
    }
    
    @Test
    public void testToString()
    {
        // Test sans initialisation
        handler = ModelFactory.getModel(Anomalie.class);
        String string = handler.toString();
        System.out.println(string);
        assertTrue(string.contains("direction=<null>"));
        assertTrue(string.contains("departement=<null>"));
        assertTrue(string.contains("responsableService=<null>"));
        assertTrue(string.contains("projetClarity=<null>"));
        assertTrue(string.contains("libelleProjet=<null>"));
        assertTrue(string.contains("cpiProjet=<null>"));
        assertTrue(string.contains("edition=<null>"));
        assertTrue(string.contains("lot=<null>"));
        assertTrue(string.contains("liensLot=<null>"));
        assertTrue(string.contains("etatLot=<null>"));
        assertTrue(string.contains("numeroAnomalie=0"));
        assertTrue(string.contains("liensAno=<null>"));
        assertTrue(string.contains("etat=<null>"));
        assertTrue(string.contains("typeAssemblage=<null>"));
        assertTrue(string.contains("securite=<null>"));
        assertTrue(string.contains("remarque=<null>"));
        assertTrue(string.contains("version=<null>"));
        assertTrue(string.contains("dateCreation=<null>"));
        assertTrue(string.contains("dateDetection=<null>"));
        assertTrue(string.contains("dateRelance=<null>"));
        assertTrue(string.contains("dateReso=<null>"));
        assertTrue(string.contains("dateMajEtat=<null>"));
        assertTrue(string.contains("traitee=false"));
        assertTrue(string.contains("matieres=[]"));
        assertTrue(string.contains("projetRTC=<null>"));
        assertTrue(string.contains("action=<null>"));
        assertTrue(string.contains("npc=<null>"));

        
        // Test avec initialisation
        LocalDate date = LocalDate.now();
        String dateString = date.toString();
        handler.setDirection("direction");
        handler.setDepartement("departement");
        handler.setService("service");
        handler.setResponsableService("respservice");
        handler.setProjetClarity("clarity");
        handler.setLibelleProjet("libelle");
        handler.setCpiProjet("cpi");
        handler.setEdition("edition");
        handler.setLot("123456");
        handler.setEtatLot(EtatLot.DEVTU);
        handler.setNumeroAnomalie(12);
        handler.setLiensAno("https");
        handler.setLiensLot("https2");
        handler.setEtat("etat");
        handler.setTypeAssemblage("type");
        handler.setSecurite(Statics.X);
        handler.setRemarque("remarque");
        handler.setVersion("version");
        handler.setDateCreation(date);
        handler.setDateDetection(date);
        handler.setDateRelance(date);
        handler.setDateReso(date);
        handler.setDateMajEtat(date);
        handler.setMatieresString("JAVA");
        handler.setProjetRTC("RTC");
        handler.setAction(TypeAction.ABANDONNER);
        handler.setNpc("npc");
        
        string = handler.toString();
        System.out.println(string);
        assertTrue(string.contains("direction=direction"));
        assertTrue(string.contains("departement=departement"));
        assertTrue(string.contains("responsableService=respservice"));
        assertTrue(string.contains("projetClarity=clarity"));
        assertTrue(string.contains("libelleProjet=libelle"));
        assertTrue(string.contains("cpiProjet=cpi"));
        assertTrue(string.contains("edition=edition"));
        assertTrue(string.contains("lot=123456"));
        assertTrue(string.contains("liensLot=https2"));
        assertTrue(string.contains("etatLot=DEVTU"));
        assertTrue(string.contains("numeroAnomalie=12"));
        assertTrue(string.contains("liensAno=https"));
        assertTrue(string.contains("etat=etat"));
        assertTrue(string.contains("typeAssemblage=type"));
        assertTrue(string.contains("securite=X"));
        assertTrue(string.contains("remarque=remarque"));
        assertTrue(string.contains("version=version"));
        assertTrue(string.contains("dateCreation=" + dateString));
        assertTrue(string.contains("dateDetection=" + dateString));
        assertTrue(string.contains("dateRelance=" + dateString));
        assertTrue(string.contains("dateReso=" + dateString));
        assertTrue(string.contains("dateMajEtat=" + dateString));
        assertTrue(string.contains("traitee=false"));
        assertTrue(string.contains("matieres=[JAVA]"));
        assertTrue(string.contains("projetRTC=RTC"));
        assertTrue(string.contains("action=ABANDONNER"));
        assertTrue(string.contains("npc=npc"));
    }
    
    @Test
    public void testCalculTraitee()
    {
        // Avec une objet juste initialisé, le booleén doit être à faux.
        handler.calculTraitee();
        assertFalse(handler.isTraitee());
        
        // Test avec remarque non nulle.
        handler.setRemarque(EMPTY);
        handler.calculTraitee();
        assertFalse(handler.isTraitee());
        
        // Test avec remarque non vide.
        handler.setRemarque("remarque");
        handler.calculTraitee();
        assertTrue(handler.isTraitee());
        
        // test avec numéro d'anomalie non 0.
        handler.setRemarque(null);
        handler.setNumeroAnomalie(10);
        handler.calculTraitee();
        assertTrue(handler.isTraitee());
        
        // Test avec les deux bons
        handler.setRemarque("rem");
        handler.calculTraitee();
        assertTrue(handler.isTraitee());
    }
    
    @Test
    public void testGetMatieresString()
    {
        // Initialisation
        Set<Matiere> matieres = new HashSet<>();
        matieres.add(Matiere.JAVA);
        matieres.add(Matiere.DATASTAGE);
        handler.setMatieres(matieres);
        
        // Test
        assertTrue(handler.getMatieresString().contains(Matiere.JAVA.toString()));
        assertTrue(handler.getMatieresString().contains(Matiere.DATASTAGE.toString()));
        assertTrue(handler.getMatieresString().contains(" - "));
    }
    
    @Test
    public void testSetMatieresString()
    {
        // Test avec string vide ou null
        handler.setMatieresString(null);
        assertEquals(0, handler.getMatieres().size());
        handler.setMatieresString(EMPTY);
        assertEquals(0, handler.getMatieres().size());
        
        
        // Test avec une matière
        String matiere = "JAVA";
        handler.setMatieresString(matiere);
        assertEquals(Matiere.JAVA, handler.getMatieres().iterator().next());
        
        // Test avec deux matières
        String matieres = "JAVA - DATASTAGE";
        handler.setMatieresString(matieres);
        assertTrue(handler.getMatieres().contains(Matiere.JAVA));
        assertTrue(handler.getMatieres().contains(Matiere.DATASTAGE));
    }
    
    @Test
    public void testGetDirection()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDirection());
        
        // Test setter et getter
        String direction = "Direction";
        handler.setDirection(direction);
        assertEquals(direction, handler.getDirection());       
    }
    
    @Test
    public void testGetDepartement()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getDepartement());
        
        // Test setter et getter
        String string = "Departement";
        handler.setDepartement(string);
        assertEquals(string, handler.getDepartement());       
    }
    
    @Test
    public void testGetService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getService());
        
        // Test setter et getter
        String string = "Service";
        handler.setService(string);
        assertEquals(string, handler.getService());       
    }
    
    @Test
    public void testGetResponsableService()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getResponsableService());
        
        // Test setter et getter
        String string = "RespService";
        handler.setResponsableService(string);
        assertEquals(string, handler.getResponsableService());       
    }
    
    @Test
    public void testGetProjetClarity()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getProjetClarity());
        
        // Test setter et getter
        String string = "projetClarity";
        handler.setProjetClarity(string);
        assertEquals(string, handler.getProjetClarity());       
    }
    
    @Test
    public void testGetLibelleProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLibelleProjet());
        
        // Test setter et getter
        String string = "libelleProjet";
        handler.setLibelleProjet(string);
        assertEquals(string, handler.getLibelleProjet());       
    }
    
    @Test
    public void testGetCpiProjet()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getCpiProjet());
        
        // Test setter et getter
        String string = "cpiProjet";
        handler.setCpiProjet(string);
        assertEquals(string, handler.getCpiProjet());       
    }
    
    @Test
    public void testGetEdition()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEdition());
        
        // Test setter et getter
        String string = "edition";
        handler.setEdition(string);
        assertEquals(string, handler.getEdition());       
    }
    
    @Test
    public void testGetLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLot());
        
        // Test setter et getter
        String string = "lot";
        handler.setLot(string);
        assertEquals(string, handler.getLot());       
    }
    
    @Test
    public void testGetNumeroAnomalie()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getNumeroAnomalie());
        
        // Test setter et getter
        int integer = 12;
        handler.setNumeroAnomalie(integer);
        assertEquals(integer, handler.getNumeroAnomalie());       
    }
    
    @Test
    public void testGetEtat()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEtat());
        
        // Test setter et getter
        String string = "etat";
        handler.setEtat(string);
        assertEquals(string, handler.getEtat());       
    }
    
    @Test
    public void testGetSecurite()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getSecurite());
        
        // Test setter et getter
        String string = "securite";
        handler.setSecurite(string);
        assertEquals(string, handler.getSecurite());       
    }
    
    @Test
    public void testGetRemarque()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getRemarque());
        
        // Test setter et getter
        String string = "remarque";
        handler.setRemarque(string);
        assertEquals(string, handler.getRemarque());       
    }
    
    @Test
    public void testGetLiensLot()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLiensLot());
        
        // Test setter et getter
        String string = "liensLot";
        handler.setLiensLot(string);
        assertEquals(string, handler.getLiensLot());       
    }
    
    @Test
    public void testGetLiensAno()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getLiensAno());
        
        // Test setter et getter
        String string = "liensAno";
        handler.setLiensAno(string);
        assertEquals(string, handler.getLiensAno());       
    }
    
    @Test
    public void testGetTypeAssemblage()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getTypeAssemblage());
        
        // Test setter et getter
        String string = "typeAssemblage";
        handler.setTypeAssemblage(string);
        assertEquals(string, handler.getTypeAssemblage());       
    }
    
    @Test
    public void testGetVersion()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getVersion());
        
        // Test setter et getter
        String string = "version";
        handler.setVersion(string);
        assertEquals(string, handler.getVersion());       
    }
    
    @Test
    public void testGetProjetRTC()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getProjetRTC());
        
        // Test setter et getter
        String string = "projetRTC";
        handler.setProjetRTC(string);
        assertEquals(string, handler.getProjetRTC());       
    }
    
    @Test
    public void testGetEnvironnement()
    {
        // test valeur vide ou nulle
        assertEquals(EtatLot.INCONNU, handler.getEtatLot());
        
        // Test setter et getter
        EtatLot env = EtatLot.DEVTU;
        handler.setEtatLot(env);
        assertEquals(env, handler.getEtatLot());       
    }
    
    @Test
    public void testGetAction()
    {
        // test valeur vide ou nulle
        assertEquals(TypeAction.VIDE, handler.getAction());
        
        // Test setter et getter
        TypeAction action = TypeAction.CREER;
        handler.setAction(action);
        assertEquals(action, handler.getAction());       
    }
    
    @Test
    public void testGetDateCreation()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateCreation());
        
        // Test setter et getter
        handler.setDateCreation(today);
        assertEquals(today, handler.getDateCreation());       
    }
    
    @Test
    public void testGetDateDetection()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateDetection());
        
        // Test setter et getter
        handler.setDateDetection(today);
        assertEquals(today, handler.getDateDetection());       
    }
    
    @Test
    public void testGetDateRelance()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateRelance());
        
        // Test setter et getter
        handler.setDateRelance(today);
        assertEquals(today, handler.getDateRelance());       
    }
    
    @Test
    public void testGetDateReso()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateReso());
        
        // Test setter et getter
        handler.setDateReso(today);
        assertEquals(today, handler.getDateReso());       
    }
    
    @Test
    public void testGetDateMajEtat()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateMajEtat());
        
        // Test setter et getter
        handler.setDateMajEtat(today);
        assertEquals(today, handler.getDateMajEtat());       
    }
    
    @Test
    public void testGetNpc()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getNpc());
        
        // Test setter et getter
        String npc = "npc";
        handler.setNpc(npc);
        assertEquals(npc, handler.getNpc());       
    }
    
    /* ---------- TEST COMMENTAIRES ---------- */
    
    @Test
    public void testGetDirectionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDirectionComment());
        
        // Test setter et getter
        handler.setDirectionComment(comment);
        assertEquals(comment, handler.getDirectionComment());   
    }
    
    @Test
    public void testGetDepartementComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDepartementComment());
        
        // Test setter et getter
        handler.setDepartementComment(comment);
        assertEquals(comment, handler.getDepartementComment());   
    }
    
    @Test
    public void testGetServiceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getServiceComment());
        
        // Test setter et getter
        handler.setServiceComment(comment);
        assertEquals(comment, handler.getServiceComment());   
    }
    
    @Test
    public void testGetResponsableServiceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getResponsableServiceComment());
        
        // Test setter et getter
        handler.setResponsableServiceComment(comment);
        assertEquals(comment, handler.getResponsableServiceComment());   
    }
    
    @Test
    public void testGetProjetClarityComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getProjetClarityComment());
        
        // Test setter et getter
        handler.setProjetClarityComment(comment);
        assertEquals(comment, handler.getProjetClarityComment());   
    }
    
    @Test
    public void testGetLibelleProjetComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getLibelleProjetComment());
        
        // Test setter et getter
        handler.setLibelleProjetComment(comment);
        assertEquals(comment, handler.getLibelleProjetComment());   
    }
    
    @Test
    public void testGetCpiProjetComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getCpiProjetComment());
        
        // Test setter et getter
        handler.setCpiProjetComment(comment);
        assertEquals(comment, handler.getCpiProjetComment());   
    }
    
    @Test
    public void testGetEditionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getEditionComment());
        
        // Test setter et getter
        handler.setEditionComment(comment);
        assertEquals(comment, handler.getEditionComment());   
    }
    
    @Test
    public void testGetLotComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getLotComment());
        
        // Test setter et getter
        handler.setLotComment(comment);
        assertEquals(comment, handler.getLotComment());   
    }
    
    @Test
    public void testGetLiensLotComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getLiensLotComment());
        
        // Test setter et getter
        handler.setLiensLotComment(comment);
        assertEquals(comment, handler.getLiensLotComment());   
    }
    
    @Test
    public void testGetEnvironnementComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getEtatLotComment());
        
        // Test setter et getter
        handler.setEtatLotComment(comment);
        assertEquals(comment, handler.getEtatLotComment());   
    }
    
    @Test
    public void testGetNumeroAnomalieComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getNumeroAnomalieComment());
        
        // Test setter et getter
        handler.setNumeroAnomalieComment(comment);
        assertEquals(comment, handler.getNumeroAnomalieComment());   
    }
    
    @Test
    public void testGetLiensAnoComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getLiensAnoComment());
        
        // Test setter et getter
        handler.setLiensAnoComment(comment);
        assertEquals(comment, handler.getLiensAnoComment());   
    }
    
    @Test
    public void testGetEtatComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getEtatComment());
        
        // Test setter et getter
        handler.setEtatComment(comment);
        assertEquals(comment, handler.getEtatComment());   
    }
    
    @Test
    public void testGetTypeAssemblageComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getTypeAssemblageComment());
        
        // Test setter et getter
        handler.setTypeAssemblageComment(comment);
        assertEquals(comment, handler.getTypeAssemblageComment());   
    }
    
    @Test
    public void testGetSecuriteComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getSecuriteComment());
        
        // Test setter et getter
        handler.setSecuriteComment(comment);
        assertEquals(comment, handler.getSecuriteComment());   
    }
    
    @Test
    public void testGetRemarqueComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getRemarqueComment());
        
        // Test setter et getter
        handler.setRemarqueComment(comment);
        assertEquals(comment, handler.getRemarqueComment());   
    }
    
    @Test
    public void testGetVersionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getVersionComment());
        
        // Test setter et getter
        handler.setVersionComment(comment);
        assertEquals(comment, handler.getVersionComment());   
    }
    
    @Test
    public void testGetDateCreationComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateCreationComment());
        
        // Test setter et getter
        handler.setDateCreationComment(comment);
        assertEquals(comment, handler.getDateCreationComment());   
    }
    
    @Test
    public void testGetDateDetectionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateDetectionComment());
        
        // Test setter et getter
        handler.setDateDetectionComment(comment);
        assertEquals(comment, handler.getDateDetectionComment());   
    }
    
    @Test
    public void testGetDateResoComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateResoComment());
        
        // Test setter et getter
        handler.setDateResoComment(comment);
        assertEquals(comment, handler.getDateResoComment());   
    }
    
    @Test
    public void testGetDateMajEtatComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateMajEtatComment());
        
        // Test setter et getter
        handler.setDateMajEtatComment(comment);
        assertEquals(comment, handler.getDateMajEtatComment());   
    }
    
    @Test
    public void testGetDateRelanceComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getDateRelanceComment());
        
        // Test setter et getter
        handler.setDateRelanceComment(comment);
        assertEquals(comment, handler.getDateRelanceComment());   
    }
    
    @Test
    public void testGetMatieresComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getMatieresComment());
        
        // Test setter et getter
        handler.setMatieresComment(comment);
        assertEquals(comment, handler.getMatieresComment());   
    }
    
    @Test
    public void testGetProjetRTCComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getProjetRTCComment());
        
        // Test setter et getter
        handler.setProjetRTCComment(comment);
        assertEquals(comment, handler.getProjetRTCComment());   
    }
    
    @Test
    public void testGetActionComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getActionComment());
        
        // Test setter et getter
        handler.setActionComment(comment);
        assertEquals(comment, handler.getActionComment());   
    }
    
    @Test
    public void testGetNpcComment()
    {
        // test valeur vide ou nulle
        assertEquals(null, handler.getNpcComment());
        
        // Test setter et getter
        handler.setNpcComment(comment);
        assertEquals(comment, handler.getNpcComment());   
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
