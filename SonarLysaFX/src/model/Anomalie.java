package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Comment;

import model.enums.Environnement;
import model.enums.Matiere;

/**
 * Classe de mod�le qui correspond aux donn�es du fichier Excel des anomalies.
 * 
 * @author ETP8137 - Gr�goire mathon
 * @since 1.0
 */
public class Anomalie
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
    private Environnement environnement;
    private Comment environnementComment;
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
    private LocalDate dateRelance;
    private Comment dateRelanceComment;
    private boolean traitee;
    private Set<Matiere> matieres;

    /*---------- CONSTRUCTEURS ----------*/

    public Anomalie()
    {
        matieres = new HashSet<>();
    }

    public Anomalie(LotSuiviPic lot)
    {
        this();
        majDepuisPic(lot);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void majDepuisPic(LotSuiviPic lot)
    {
        setCpiProjet(lot.getCpiProjet());
        setEdition(lot.getEdition());
        setLibelleProjet(lot.getLibelle());
        setProjetClarity(lot.getProjetClarity());
        setLot("Lot " + lot.getLot());
        setEnvironnement(calculerEnvironnement(lot));
    }
    
    /**
     * Permet de v�rifier si une anomalie a �t� trait�e ou non. C'est-�-dire si il y a un num�ro d'anomalie ou un commentaire.
     * @return
     */
    public boolean calculTraitee()
    {
        traitee = (remarque != null && !remarque.isEmpty()) || (numeroAnomalie != 0);
        return traitee;
    }
    
    /**
     * Retourne la liste des matieres de l'anomalie sous forme d'une chaine de caract�res enregistrable dans Excel
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
    
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de valoriser l'environnement par rapport aux dates de publication
     * 
     * @param lot
     * @return
     */
    private Environnement calculerEnvironnement(LotSuiviPic lot)
    {
        if (lot.getLivraison() != null)
            return Environnement.EDITION;
        if (lot.getVmoa() != null)
            return Environnement.VMOA;
        if (lot.getVmoe() != null)
            return Environnement.VMOE;
        if (lot.getTfon() != null)
            return Environnement.TFON;
        if (lot.getDevtu() != null)
            return Environnement.DEVTU;
        return Environnement.NOUVEAU;
    }

    /*---------- ACCESSEURS ----------*/

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDepartement()
    {
        return departement;
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getResponsableService()
    {
        return responsableService;
    }

    public void setResponsableService(String responsableService)
    {
        this.responsableService = responsableService;
    }

    public String getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(String projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    public String getLibelleProjet()
    {
        return libelleProjet;
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }

    public String getCpiProjet()
    {
        return cpiProjet;
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }
    
    public String getEdition()
    {
        return edition;
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }
    
    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public Environnement getEnvironnement()
    {
        return environnement;
    }

    public void setEnvironnement(Environnement environnement)
    {
        this.environnement = environnement;
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
        return etat;
    }

    public void setEtat(String etat)
    {
        this.etat = etat;
    }

    public String getSecurite()
    {
        return securite;
    }

    public void setSecurite(String securite)
    {
        this.securite = securite;
    }

    public String getRemarque()
    {
        return remarque;
    }

    public void setRemarque(String remarque)
    {
        this.remarque = remarque;
    }

    public String getLiensLot()
    {
        return liensLot;
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot = liensLot;
    }

    public String getLiensAno()
    {
        return liensAno;
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno = liensAno;
    }

    public String getTypeAssemblage()
    {
        return typeAssemblage;
    }

    public void setTypeAssemblage(String typeAssemblage)
    {
        this.typeAssemblage = typeAssemblage;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public void setDateCreation(LocalDate dateCreation)
    {
        this.dateCreation = dateCreation;
    }
    
    public LocalDate getDateCreation()
    {
        return dateCreation;
    }
    
    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }
    
    public LocalDate getDateRelance()
    {
        return dateRelance;
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

    public Comment getEnvironnementComment()
    {
        return environnementComment;
    }

    public void setEnvironnementComment(Comment environnementComment)
    {
        this.environnementComment = environnementComment;
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

    public Comment getDateRelanceComment()
    {
        return dateRelanceComment;
    }

    public void setDateRelanceComment(Comment dateRelanceComment)
    {
        this.dateRelanceComment = dateRelanceComment;
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
}