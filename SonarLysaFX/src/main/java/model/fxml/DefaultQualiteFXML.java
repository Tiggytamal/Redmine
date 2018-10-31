package model.fxml;

import javafx.beans.property.SimpleStringProperty;
import model.bdd.DefautQualite;
import model.bdd.LotRTC;
import model.bdd.ProjetClarity;
import model.interfaces.AbstractModele;
import utilities.Statics;

import static utilities.Statics.EMPTY;

public class DefaultQualiteFXML extends AbstractModele implements ModeleFXML
{
    /*---------- ATTRIBUTS ----------*/

    private final SimpleStringProperty direction = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty departement = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty service = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty respServ = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty codeClarity = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty libelleClarity = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty cpiLot = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty edition = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty lotRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty liensLot = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty typeVersion = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty codeProjetRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty groupe = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateMajRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty etatLotRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty numeroAnoRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty liensAno = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty securite = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty action = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateDetection = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateCreation = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateRelance = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty dateReso = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty etatRTC = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty remarque = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty etatDefault = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty typeDefault = new SimpleStringProperty(EMPTY);
    private final SimpleStringProperty matiere = new SimpleStringProperty(EMPTY);

    /*---------- CONSTRUCTEURS ----------*/

    public DefaultQualiteFXML(DefautQualite dq)
    {
        if (dq.getLotRTC() != null)
        {
            LotRTC lot = dq.getLotRTC();
            setLotRTC(lot.getLot());
            setCpiLot(lot.getCpiProjet());
            setEdition(lot.getEdition());
            setCodeProjetRTC(lot.getProjetRTC());
            if (lot.getDateMajEtat() != null)
                setDateMajRTC(lot.getDateMajEtat().toString());
            if (lot.getEtatLot() != null)
                setEtatLotRTC(lot.getEtatLot().getValeur());
            if (lot.getGroupe() != null)
                setGroupe(lot.getGroupe().getValeur());

            if (lot.getProjetClarity() != null)
            {
                ProjetClarity ProjetClarity = lot.getProjetClarity();
                setCodeClarity(ProjetClarity.getCode());
                setLibelleClarity(ProjetClarity.getLibelleProjet());
                setDepartement(ProjetClarity.getDepartement());
                setDirection(ProjetClarity.getDirection());
                setService(ProjetClarity.getService());

                if (ProjetClarity.getChefService() != null)
                    setRespServ(ProjetClarity.getChefService().getNom());
            }
        }

        setLiensLot(dq.getLiensLot());
        setNumeroAnoRTC(String.valueOf(dq.getNumeroAnoRTC()));
        setLiensAno(dq.getLiensAno());
        setEtatRTC(dq.getEtatRTC());
        if (dq.isSecurite())
            setSecurite(Statics.X);
        else
            setSecurite(EMPTY);
        setRemarque(dq.getRemarque());
        if (dq.getTypeVersion() != null)
            setTypeVersion(dq.getTypeVersion().toString());
        if (dq.getDateCreation() != null)
            setDateCreation(dq.getDateCreation().toString());
        if (dq.getDateDetection() != null)
            setDateDetection(dq.getDateDetection().toString());
        if (dq.getDateRelance() != null)
            setDateRelance(dq.getDateRelance().toString());
        if (dq.getDateReso() != null)
            setDateReso(dq.getDateReso().toString());
        if (dq.getEtatDefaut() != null)
            setEtatDefault(dq.getEtatDefaut().toString());
        if (dq.getAction() != null)
            setAction(dq.getAction().getValeur());
        if (dq.getTypeDefaut() != null)
            setTypeDefault(dq.getTypeDefaut().toString());
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    public String getLotRTC()
    {
        return lotRTC.get();
    }

    public void setLotRTC(String lotRTC)
    {
        this.lotRTC.set(lotRTC);
    }

    public String getLiensLot()
    {
        return liensLot.get();
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot.set(liensLot);
    }

    public String getNumeroAnoRTC()
    {
        return numeroAnoRTC.get();
    }

    public void setNumeroAnoRTC(String numeroAnoRTC)
    {
        this.numeroAnoRTC.set(numeroAnoRTC);
    }

    public String getLiensAno()
    {
        return liensAno.get();
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno.set(liensAno);
    }

    public String getEtatRTC()
    {
        return etatRTC.get();
    }

    public void setEtatRTC(String etatRTC)
    {
        this.etatRTC.set(etatRTC);
    }

    public String getSecurite()
    {
        return securite.get();
    }

    public void setSecurite(String securite)
    {
        this.securite.set(securite);
    }

    public String getRemarque()
    {
        return remarque.get();
    }

    public void setRemarque(String remarque)
    {
        this.remarque.set(remarque);
    }

    public String getTypeVersion()
    {
        return typeVersion.get();
    }

    public void setTypeVersion(String typeVersion)
    {
        this.typeVersion.set(typeVersion);
    }

    public String getDateCreation()
    {
        return dateCreation.get();
    }

    public void setDateCreation(String dateCreation)
    {
        this.dateCreation.set(dateCreation);
    }

    public String getDateDetection()
    {
        return dateDetection.get();
    }

    public void setDateDetection(String dateDetection)
    {
        this.dateDetection.set(dateDetection);
    }

    public String getDateRelance()
    {
        return dateRelance.get();
    }

    public void setDateRelance(String dateRelance)
    {
        this.dateRelance.set(dateRelance);
    }

    public String getDateReso()
    {
        return dateReso.get();
    }

    public void setDateReso(String dateReso)
    {
        this.dateReso.set(dateReso);
    }

    public String getEtatDefault()
    {
        return etatDefault.get();
    }

    public void setEtatDefault(String etatDefault)
    {
        this.etatDefault.set(etatDefault);
    }

    public String getAction()
    {
        return action.get();
    }

    public void setAction(String action)
    {
        this.action.set(action);
    }

    public String getTypeDefault()
    {
        return typeDefault.get();
    }

    public void setTypeDefault(String typeDefault)
    {
        this.typeDefault.set(typeDefault);
    }

    public String getDirection()
    {
        return direction.get();
    }

    public void setDirection(String direction)
    {
        this.direction.set(direction);
    }

    public String getDepartement()
    {
        return departement.get();
    }

    public void setDepartement(String departement)
    {
        this.departement.set(departement);
    }

    public String getService()
    {
        return service.get();
    }

    public void setService(String service)
    {
        this.service.set(service);
    }

    public String getRespServ()
    {
        return respServ.get();
    }

    public void setRespServ(String respServ)
    {
        this.respServ.set(respServ);
    }

    public String getCodeClarity()
    {
        return codeClarity.get();
    }

    public void setCodeClarity(String codeClarity)
    {
        this.codeClarity.set(codeClarity);
    }

    public String getLibelleClarity()
    {
        return libelleClarity.get();
    }

    public void setLibelleClarity(String libelleClarity)
    {
        this.libelleClarity.set(libelleClarity);
    }

    public String getCpiLot()
    {
        return cpiLot.get();
    }

    public void setCpiLot(String cpiLot)
    {
        this.cpiLot.set(cpiLot);
    }

    public String getEdition()
    {
        return edition.get();
    }

    public void setEdition(String edition)
    {
        this.edition.set(edition);
    }

    public String getGroupe()
    {
        return groupe.get();
    }

    public void setGroupe(String groupe)
    {
        this.groupe.set(groupe);
    }

    public String getMatiere()
    {
        return matiere.get();
    }

    public void setMatiere(String matiere)
    {
        this.matiere.set(matiere);
    }

    public String getCodeProjetRTC()
    {
        return codeProjetRTC.get();
    }

    public void setCodeProjetRTC(String codeProjetRTC)
    {
        this.codeProjetRTC.set(codeProjetRTC);
    }

    public String getDateMajRTC()
    {
        return dateMajRTC.get();
    }

    public void setDateMajRTC(String dateMajRTC)
    {
        this.dateMajRTC.set(dateMajRTC);
    }

    public String getEtatLotRTC()
    {
        return etatLotRTC.get();
    }

    public void setEtatLotRTC(String etatLotRTC)
    {
        this.etatLotRTC.set(etatLotRTC);
    }
}
