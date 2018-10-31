package junit.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.Statics.EMPTY;

import org.junit.Test;

import model.ModelFactory;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.enums.TypeAction;

public class TestAnomalie extends AbstractTestModel<DefautQualite>
{
    /*---------- ATTRIBUTS ----------*/    
    /*---------- CONSTRUCTEURS ----------*/    
    /*---------- METHODES PUBLIQUES ----------*/

    @Test
    public void testAnomlieWithLot()
    {
        // Test création anomalie depuis LotSuiviRTC
        handler = ModelFactory.getModelWithParams(DefautQualite.class, ModelFactory.getModel(LotRTC.class));
        assertNotNull(handler);
    }
    
    @Test
    public void testMajDepuisPic()
    {
//        // Création lotPic
//        LotRTC lotRTC = ModelFactory.getModel(LotRTC.class);
//        String cpi = "cpi";
//        lotRTC.setCpiProjet(cpi);
//        String edition = "edition";
//        lotRTC.setEdition(edition);
//        String libProjet = "libProjet";
//        lotRTC.setLibelle(libProjet);
//        String clarity = "clarity";
//        lotRTC.setProjetClarity(ModelFactory.getModelWithParams(ProjetClarity.class, clarity));
//        String lot = "123456";
//        lotRTC.setLot(lot);
//        lotRTC.setEtatLot(EtatLot.NOUVEAU);
//        
//        // Test
//        handler.majDepuisRTC(lotRTC);
//        assertEquals(cpi, handler.getCpiProjet());
//        assertEquals(edition, handler.getEdition());
//        assertEquals(libProjet, handler.getLibelleProjet());
//        assertEquals(clarity, handler.getProjetClarity());
//        assertEquals("Lot " + lot, handler.getLotRTC());
//        assertEquals(EtatLot.NOUVEAU, handler.getEtatLot());       
    }
    
    @Test
    public void testMajDepuisClarity()
    {
//        // Création Clarity
//        ProjetClarity infoClarity = ModelFactory.getModel(ProjetClarity.class);
//        String direction = "direction";
//        infoClarity.setDirection(direction);
//        String departement = "departement";
//        infoClarity.setDepartement(departement);
//        String service = "service";
//        infoClarity.setService(service);
//        
//        // Test
//        handler.majDepuisClarity(infoClarity);
//        assertEquals(direction, handler.getDirection());
//        assertEquals(departement, handler.getDepartement());
//        assertEquals(service, handler.getService());
    }
    
    @Test
    public void testToString()
    {
        // Test sans initialisation
        handler = ModelFactory.getModel(DefautQualite.class);
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
//        LocalDate date = LocalDate.now();
//        String dateString = date.toString();
//        handler.setDirection("direction");
//        handler.setDepartement("departement");
//        handler.setService("service");
//        handler.setResponsableService("respservice");
//        handler.setProjetClarity("clarity");
//        handler.setLibelleProjet("libelle");
//        handler.setCpiProjet("cpi");
//        handler.setEdition("edition");
//        handler.setLot("123456");
//        handler.setEtatLot(EtatLot.DEVTU);
//        handler.setNumeroAnoRTC(12);
//        handler.setLiensAno("https");
//        handler.setLiensLot("https2");
//        handler.setEtatRTC("etat");
//        handler.setTypeAssemblage("type");
//        handler.setSecurite(Statics.X);
//        handler.setRemarque("remarque");
//        handler.setVersion("version");
//        handler.setDateCreation(date);
//        handler.setDateDetection(date);
//        handler.setDateRelance(date);
//        handler.setDateReso(date);
//        handler.setDateMajEtat(date);
//        handler.setMatieresString("JAVA");
//        handler.setProjetRTC("RTC");
//        handler.setAction(TypeAction.ABANDONNER);
//        handler.setGroupe("npc");
        
//        string = handler.toString();
//        System.out.println(string);
//        assertTrue(string.contains("direction=direction"));
//        assertTrue(string.contains("departement=departement"));
//        assertTrue(string.contains("responsableService=respservice"));
//        assertTrue(string.contains("projetClarity=clarity"));
//        assertTrue(string.contains("libelleProjet=libelle"));
//        assertTrue(string.contains("cpiProjet=cpi"));
//        assertTrue(string.contains("edition=edition"));
//        assertTrue(string.contains("lot=123456"));
//        assertTrue(string.contains("liensLot=https2"));
//        assertTrue(string.contains("etatLot=DEVTU"));
//        assertTrue(string.contains("numeroAnomalie=12"));
//        assertTrue(string.contains("liensAno=https"));
//        assertTrue(string.contains("etat=etat"));
//        assertTrue(string.contains("typeAssemblage=type"));
//        assertTrue(string.contains("securite=X"));
//        assertTrue(string.contains("remarque=remarque"));
//        assertTrue(string.contains("version=version"));
//        assertTrue(string.contains("dateCreation=" + dateString));
//        assertTrue(string.contains("dateDetection=" + dateString));
//        assertTrue(string.contains("dateRelance=" + dateString));
//        assertTrue(string.contains("dateReso=" + dateString));
//        assertTrue(string.contains("dateMajEtat=" + dateString));
//        assertTrue(string.contains("traitee=false"));
//        assertTrue(string.contains("matieres=[JAVA]"));
//        assertTrue(string.contains("projetRTC=RTC"));
//        assertTrue(string.contains("action=ABANDONNER"));
//        assertTrue(string.contains("npc=npc"));
    }
    
    @Test
    public void testCalculTraitee()
    {
        // Avec une objet juste initialisé, le booleén doit être à faux.
        assertFalse(handler.calculTraitee());
        
        // Test avec remarque non nulle.
        handler.setRemarque(EMPTY);
        assertFalse(handler.calculTraitee());
        
        // Test avec remarque non vide.
        handler.setRemarque("remarque");
        assertTrue(handler.calculTraitee());
        
        // test avec numéro d'anomalie non 0.
        handler.setRemarque(null);
        handler.setNumeroAnoRTC(10);
        assertTrue(handler.calculTraitee());
        
        // Test avec les deux bons
        handler.setRemarque("rem");
        assertTrue(handler.calculTraitee());
    }
    
//    @Test
//    public void testGetMatieresString()
//    {
//        // Initialisation
//        Set<Matiere> matieres = new HashSet<>();
//        matieres.add(Matiere.JAVA);
//        matieres.add(Matiere.DATASTAGE);
//        handler.setMatieres(matieres);
//        
//        // Test
//        assertTrue(handler.getMatieresString().contains(Matiere.JAVA.toString()));
//        assertTrue(handler.getMatieresString().contains(Matiere.DATASTAGE.toString()));
//        assertTrue(handler.getMatieresString().contains(" - "));
//    }
//    
//    @Test
//    public void testSetMatieresString()
//    {
//        // Test avec string vide ou null
//        handler.setMatieresString(null);
//        assertEquals(0, handler.getMatieres().size());
//        handler.setMatieresString(EMPTY);
//        assertEquals(0, handler.getMatieres().size());
//        
//        
//        // Test avec une matière
//        String matiere = "JAVA";
//        handler.setMatieresString(matiere);
//        assertEquals(Matiere.JAVA, handler.getMatieres().iterator().next());
//        
//        // Test avec deux matières
//        String matieres = "JAVA - DATASTAGE";
//        handler.setMatieresString(matieres);
//        assertTrue(handler.getMatieres().contains(Matiere.JAVA));
//        assertTrue(handler.getMatieres().contains(Matiere.DATASTAGE));
//    }
    
//    @Test
//    public void testGetDirection()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getDirection());
//        
//        // Test setter et getter
//        String direction = "Direction";
//        handler.setDirection(direction);
//        assertEquals(direction, handler.getDirection());       
//    }
//    
//    @Test
//    public void testGetDepartement()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getDepartement());
//        
//        // Test setter et getter
//        String string = "Departement";
//        handler.setDepartement(string);
//        assertEquals(string, handler.getDepartement());       
//    }
//    
//    @Test
//    public void testGetService()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getService());
//        
//        // Test setter et getter
//        String string = "Service";
//        handler.setService(string);
//        assertEquals(string, handler.getService());       
//    }
//    
//    @Test
//    public void testGetResponsableService()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getResponsableService());
//        
//        // Test setter et getter
//        String string = "RespService";
//        handler.setResponsableService(string);
//        assertEquals(string, handler.getResponsableService());       
//    }
//    
//    @Test
//    public void testGetProjetClarity()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getProjetClarity());
//        
//        // Test setter et getter
//        String string = "projetClarity";
//        handler.setProjetClarity(string);
//        assertEquals(string, handler.getProjetClarity());       
//    }
//    
//    @Test
//    public void testGetLibelleProjet()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getLibelleProjet());
//        
//        // Test setter et getter
//        String string = "libelleProjet";
//        handler.setLibelleProjet(string);
//        assertEquals(string, handler.getLibelleProjet());       
//    }
//    
//    @Test
//    public void testGetCpiProjet()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getCpiProjet());
//        
//        // Test setter et getter
//        String string = "cpiProjet";
//        handler.setCpiProjet(string);
//        assertEquals(string, handler.getCpiProjet());       
//    }
//    
//    @Test
//    public void testGetEdition()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getEdition());
//        
//        // Test setter et getter
//        String string = "edition";
//        handler.setEdition(string);
//        assertEquals(string, handler.getEdition());       
//    }
    
//    @Test
//    public void testGetLot()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getLotRTC());
//        
//        // Test setter et getter
//        String string = "lot";
//        handler.setLot(string);
//        assertEquals(string, handler.getLotRTC());       
//    }
    
    @Test
    public void testGetNumeroAnomalie()
    {
        // test valeur vide ou nulle
        assertEquals(0, handler.getNumeroAnoRTC());
        
        // Test setter et getter
        int integer = 12;
        handler.setNumeroAnoRTC(integer);
        assertEquals(integer, handler.getNumeroAnoRTC());       
    }
    
    @Test
    public void testGetEtat()
    {
        // test valeur vide ou nulle
        assertEquals(EMPTY, handler.getEtatRTC());
        
        // Test setter et getter
        String string = "etat";
        handler.setEtatRTC(string);
        assertEquals(string, handler.getEtatRTC());       
    }
    
//    @Test
//    public void testGetSecurite()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.isSecurite());
//        
//        // Test setter et getter
//        String string = "securite";
//        handler.setSecurite(string);
//        assertEquals(string, handler.isSecurite());       
//    }
    
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
    
//    @Test
//    public void testGetTypeAssemblage()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getTypeAssemblage());
//        
//        // Test setter et getter
//        String string = "typeAssemblage";
//        handler.setTypeAssemblage(string);
//        assertEquals(string, handler.getTypeAssemblage());       
//    }
//    
//    @Test
//    public void testGetVersion()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getVersion());
//        
//        // Test setter et getter
//        String string = "version";
//        handler.setVersion(string);
//        assertEquals(string, handler.getVersion());       
//    }
//    
//    @Test
//    public void testGetProjetRTC()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getProjetRTC());
//        
//        // Test setter et getter
//        String string = "projetRTC";
//        handler.setProjetRTC(string);
//        assertEquals(string, handler.getProjetRTC());       
//    }
//    
//    @Test
//    public void testGetEnvironnement()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EtatLot.INCONNU, handler.getEtatLot());
//        
//        // Test setter et getter
//        EtatLot env = EtatLot.DEVTU;
//        handler.setEtatLot(env);
//        assertEquals(env, handler.getEtatLot());       
//    }
    
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
    
//    @Test
//    public void testGetDateMajEtat()
//    {
//        // test valeur vide ou nulle
//        assertEquals(null, handler.getDateMajEtat());
//        
//        // Test setter et getter
//        handler.setDateMajEtat(today);
//        assertEquals(today, handler.getDateMajEtat());       
//    }
//    
//    @Test
//    public void testGetNpc()
//    {
//        // test valeur vide ou nulle
//        assertEquals(EMPTY, handler.getGroupe());
//        
//        // Test setter et getter
//        String npc = "npc";
//        handler.setGroupe(npc);
//        assertEquals(npc, handler.getGroupe());       
//    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
