package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Comment;

import model.enums.EtatLot;
import model.enums.Matiere;
import model.enums.TypeAction;

/**
 * Classe de modèle qui correspond aux données du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Grégoire mathon
 * @since 1.0
 */
public class Anomalie implements Modele
{
    /*---------- ATTRIBUTS ----------*/

    private String direction;
    private Comment directionComment;
    private String departement;
    private Comment departementComment;
    private String service;
    private Comment serviceComment;
    private String responsableService;
    private Comment responsableServiceComment;
    private String projetClarity;
    private Comment projetClarityComment;
    private String libelleProjet;
    private Comment libelleProjetComment;
    private String cpiProjet;
    private Comment cpiProjetComment;
    private String edition;
    private Comment editionComment;
    private String lot;
    private Comment lotComment;
    private String liensLot;
    private Comment liensLotComment;
    private EtatLot etatLot;
    private Comment etatLotComment;
    private int numeroAnomalie;
    private Comment numeroAnomalieComment;
    private String liensAno;
    private Comment liensAnoComment;
    private String etat;
    private Comment etatComment;
    private String typeAssemblage;
    private Comment typeAssemblageComment;
    private String securite;
    private Comment securiteComment;
    private String remarque;
    private Comment remarqueComment;
    private String version;
    private Comment versionComment;
    private LocalDate dateCreation;
    private Comment dateCreationComment;
    private LocalDate dateDetection;
    private Comment dateDetectionComment;
    private LocalDate dateRelance;
    private Comment dateRelanceComment;
    private LocalDate dateReso;
    private Comment dateResoComment;
    private LocalDate dateMajEtat;
    private Comment dateMajEtatComment;
    private boolean traitee;
    private Set<Matiere> matieres;
    private Comment matieresComment;
    private String projetRTC;
    private Comment projetRTCComment;
    private TypeAction action;
    private Comment actionComment;
    private String npc;
    private Comment npcComment;

    /*---------- CONSTRUCTEURS ----------*/

    Anomalie()
    {
        matieres = new HashSet<>();
    }

    Anomalie(LotSuiviRTC lot)
    {
        this();
        majDepuisRTC(lot);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    public final Anomalie majDepuisRTC(LotSuiviRTC lotRTC)
    {
        setCpiProjet(lotRTC.getCpiProjet());
        setEdition(lotRTC.getEdition());
        setLibelleProjet(lotRTC.getLibelle());
        setProjetClarity(lotRTC.getProjetClarity());
        setLot("Lot " + lotRTC.getLot());
        setEtatLot(lotRTC.getEtatLot());
        setProjetRTC(lotRTC.getProjetRTC());
        setDateMajEtat(lotRTC.getDateMajEtat());
        return this;
    }

    public Anomalie majDepuisClarity(InfoClarity info)
    {
        setDepartement(info.getDepartement());
        setDirection(info.getDirection());
        setService(info.getService());
        return this;
    }
    
    @Override
    public String toString()
    {
        return "Anomalie du lot " + getLot() + " - RTC = " + getNumeroAnomalie();
    }

    /**
     * Permet de vérifier si une anomalie a été traitée ou non. C'est-à-dire si il y a un numéro d'anomalie ou un commentaire.
     * 
     * @return
     */
    public boolean calculTraitee()
    {
        traitee = (remarque != null && !remarque.isEmpty()) || (numeroAnomalie != 0);
        return traitee;
    }

    /**
     * Retourne la liste des matieres de l'anomalie sous forme d'une chaine de caractères enregistrable dans Excel
     * 
     * @return
     */
    public String getMatieresString()
    {
        StringBuilder builder = new StringBuilder();

        for (Iterator<Matiere> iter = matieres.iterator(); iter.hasNext();)
        {
            builder.append(iter.next().toString());
            if (iter.hasNext())
                builder.append(" - ");
        }
        return builder.toString();
    }

    /**
     * Remplie la liste des matières depuis une chaine de caractères. Cahque matière doit être séparées par un "-".
     * 
     */
    public void setMatieresString(String matieresString)
    {
        if (matieresString == null || matieresString.isEmpty())
            return;
        matieres.clear();
        for (String matiere : matieresString.split("-"))
        {
            matieres.add(Matiere.from(matiere.trim()));
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    /*---------- ACCESSEURS ----------*/

    public String getDirection()
    {
        return getString(direction);
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDepartement()
    {
        return getString(departement);
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getService()
    {
        return getString(service);
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getResponsableService()
    {
        return getString(responsableService);
    }

    public void setResponsableService(String responsableService)
    {
        this.responsableService = responsableService;
    }

    public String getProjetClarity()
    {
        return getString(projetClarity);
    }

    public void setProjetClarity(String projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    public String getLibelleProjet()
    {
        return getString(libelleProjet);
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }

    public String getCpiProjet()
    {
        return getString(cpiProjet);
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    public String getEdition()
    {
        return getString(edition);
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public String getLot()
    {
        return getString(lot);
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public EtatLot getEtatLot()
    {
        return etatLot != null ? etatLot : EtatLot.INCONNU;
    }

    public void setEtatLot(EtatLot etatLot)
    {
        this.etatLot = etatLot;
    }

    public int getNumeroAnomalie()
    {
        return numeroAnomalie;
    }

    public void setNumeroAnomalie(int numeroAnomalie)
    {
        this.numeroAnomalie = numeroAnomalie;
    }

    public String getEtat()
    {
        return getString(etat);
    }

    public void setEtat(String etat)
    {
        this.etat = etat;
    }

    public String getSecurite()
    {
        return getString(securite);
    }

    public void setSecurite(String securite)
    {
        this.securite = securite;
    }

    public String getRemarque()
    {
        return getString(remarque);
    }

    public void setRemarque(String remarque)
    {
        this.remarque = remarque;
    }

    public String getLiensLot()
    {
        return getString(liensLot);
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot = liensLot;
    }

    public String getLiensAno()
    {
        return getString(liensAno);
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno = liensAno;
    }

    public String getTypeAssemblage()
    {
        return getString(typeAssemblage);
    }

    public void setTypeAssemblage(String typeAssemblage)
    {
        this.typeAssemblage = typeAssemblage;
    }

    public String getVersion()
    {
        return getString(version);
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public LocalDate getDateCreation()
    {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation)
    {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateDetection()
    {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection)
    {
        this.dateDetection = dateDetection;
    }

    public LocalDate getDateRelance()
    {
        return dateRelance;
    }

    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }
    
    public LocalDate getDateReso()
    {
        return dateReso;
    }
    
    public void setDateReso(LocalDate dateReso)
    {
        this.dateReso = dateReso;
    }
    
    public LocalDate getDateMajEtat()
    {
        return dateMajEtat;
    }

    public void setDateMajEtat(LocalDate dateMajEtat)
    {
        this.dateMajEtat = dateMajEtat;
    }

    public String getProjetRTC()
    {
        return getString(projetRTC);
    }

    public void setProjetRTC(String projetRTC)
    {
        this.projetRTC = projetRTC;
    }
    
    public TypeAction getAction()
    {
        return action;
    }

    public void setAction(TypeAction action)
    {
        this.action = action;
    }

    public boolean isTraitee()
    {
        return traitee;
    }

    public Set<Matiere> getMatieres()
    {
        return matieres;
    }

    public void setMatieres(Set<Matiere> matieres)
    {
        this.matieres = matieres;
    }
    
    public String getNpc()
    {
        return getString(npc);
    }
    
    public void setNpc(String npc)
    {
        this.npc = npc;
    }

    /*---------- ACCESSEURS COMMENTAIRES ----------*/

    public Comment getDirectionComment()
    {
        return directionComment;
    }

    public void setDirectionComment(Comment directionComment)
    {
        this.directionComment = directionComment;
    }

    public Comment getDepartementComment()
    {
        return departementComment;
    }

    public void setDepartementComment(Comment departementComment)
    {
        this.departementComment = departementComment;
    }

    public Comment getServiceComment()
    {
        return serviceComment;
    }

    public void setServiceComment(Comment serviceComment)
    {
        this.serviceComment = serviceComment;
    }

    public Comment getResponsableServiceComment()
    {
        return responsableServiceComment;
    }

    public void setResponsableServiceComment(Comment responsableServiceComment)
    {
        this.responsableServiceComment = responsableServiceComment;
    }

    public Comment getProjetClarityComment()
    {
        return projetClarityComment;
    }

    public void setProjetClarityComment(Comment projetClarityComment)
    {
        this.projetClarityComment = projetClarityComment;
    }

    public Comment getLibelleProjetComment()
    {
        return libelleProjetComment;
    }

    public void setLibelleProjetComment(Comment libelleProjetComment)
    {
        this.libelleProjetComment = libelleProjetComment;
    }

    public Comment getCpiProjetComment()
    {
        return cpiProjetComment;
    }

    public void setCpiProjetComment(Comment cpiProjetComment)
    {
        this.cpiProjetComment = cpiProjetComment;
    }

    public Comment getEditionComment()
    {
        return editionComment;
    }

    public void setEditionComment(Comment editionComment)
    {
        this.editionComment = editionComment;
    }

    public Comment getLotComment()
    {
        return lotComment;
    }

    public void setLotComment(Comment lotComment)
    {
        this.lotComment = lotComment;
    }

    public Comment getLiensLotComment()
    {
        return liensLotComment;
    }

    public void setLiensLotComment(Comment liensLotComment)
    {
        this.liensLotComment = liensLotComment;
    }

    public Comment getEtatLotComment()
    {
        return etatLotComment;
    }

    public void setEtatLotComment(Comment etatLotComment)
    {
        this.etatLotComment = etatLotComment;
    }

    public Comment getNumeroAnomalieComment()
    {
        return numeroAnomalieComment;
    }

    public void setNumeroAnomalieComment(Comment numeroAnomalieComment)
    {
        this.numeroAnomalieComment = numeroAnomalieComment;
    }

    public Comment getLiensAnoComment()
    {
        return liensAnoComment;
    }

    public void setLiensAnoComment(Comment liensAnoComment)
    {
        this.liensAnoComment = liensAnoComment;
    }

    public Comment getEtatComment()
    {
        return etatComment;
    }

    public void setEtatComment(Comment etatComment)
    {
        this.etatComment = etatComment;
    }

    public Comment getTypeAssemblageComment()
    {
        return typeAssemblageComment;
    }

    public void setTypeAssemblageComment(Comment typeAssemblageComment)
    {
        this.typeAssemblageComment = typeAssemblageComment;
    }

    public Comment getSecuriteComment()
    {
        return securiteComment;
    }

    public void setSecuriteComment(Comment securiteComment)
    {
        this.securiteComment = securiteComment;
    }

    public Comment getRemarqueComment()
    {
        return remarqueComment;
    }

    public void setRemarqueComment(Comment remarqueComment)
    {
        this.remarqueComment = remarqueComment;
    }

    public Comment getVersionComment()
    {
        return versionComment;
    }

    public void setVersionComment(Comment versionComment)
    {
        this.versionComment = versionComment;
    }

    public Comment getDateCreationComment()
    {
        return dateCreationComment;
    }

    public void setDateCreationComment(Comment dateCreationComment)
    {
        this.dateCreationComment = dateCreationComment;
    }

    public Comment getDateDetectionComment()
    {
        return dateDetectionComment;
    }

    public void setDateDetectionComment(Comment dateDetectionComment)
    {
        this.dateDetectionComment = dateDetectionComment;
    }

    public Comment getDateRelanceComment()
    {
        return dateRelanceComment;
    }

    public void setDateRelanceComment(Comment dateRelanceComment)
    {
        this.dateRelanceComment = dateRelanceComment;
    }
    
    public Comment getDateResoComment()
    {
        return dateResoComment;
    }
    
    public void setDateResoComment(Comment dateResoComment)
    {
        this.dateResoComment = dateResoComment;
    }
    
    public Comment getDateMajEtatComment()
    {
        return dateMajEtatComment;
    }

    public void setDateMajEtatComment(Comment dateMajEtatComment)
    {
        this.dateMajEtatComment = dateMajEtatComment;
    }

    public Comment getMatieresComment()
    {
        return matieresComment;
    }

    public void setMatieresComment(Comment matieresComment)
    {
        this.matieresComment = matieresComment;
    }
    
    public Comment getProjetRTCComment()
    {
        return projetRTCComment;
    }

    public void setProjetRTCComment(Comment projetRTCComment)
    {
        this.projetRTCComment = projetRTCComment;
    }

    public Comment getActionComment()
    {
        return actionComment;
    }

    public void setActionComment(Comment actionComment)
    {
        this.actionComment = actionComment;
    }
    
    public Comment getNpcComment()
    {
        return npcComment;
    }
    
    public void setNpcComment(Comment npcComment)
    {
        this.npcComment = npcComment;
    }
}
