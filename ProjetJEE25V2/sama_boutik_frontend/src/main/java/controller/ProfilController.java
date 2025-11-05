package controller;

import entity.Profil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import service.ProfilService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;
import java.util.List;

@Named("profilController")
@ViewScoped
public class ProfilController implements Serializable {

    @EJB
    private ProfilService profilService;

    private Profil nouveauProfil = new Profil();
    private Profil profilSelectionne;

    public Profil getNouveauProfil() { return nouveauProfil; }
    public void setNouveauProfil(Profil nouveauProfil) { this.nouveauProfil = nouveauProfil; }

    public Profil getProfilSelectionne() { return profilSelectionne; }
    public void setProfilSelectionne(Profil profilSelectionne) { this.profilSelectionne = profilSelectionne; }

    public List<Profil> getProfils() {
        return profilService.listerProfils();
    }

    public void ajouterProfil() {
        try {
            profilService.creerProfil(nouveauProfil);
            nouveauProfil = new Profil();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Profil ajouté avec succès !"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }

    public void ouvrirEdition(Profil p) { this.profilSelectionne = p; }

    public void sauvegarderEdition() {
        try {
            if (profilSelectionne != null && profilSelectionne.getId() != null) {
                Profil pFromDb = profilService.trouverParId(profilSelectionne.getId());
                pFromDb.setTitre(profilSelectionne.getTitre());
                pFromDb.setDescription(profilSelectionne.getDescription());
                profilService.modifierProfil(pFromDb);

                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Profil mis à jour avec succès !"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }


    public void supprimerProfil(Profil p) {
        try {
            profilService.supprimerProfil(p.getId());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Supprimé", "Profil supprimé avec succès."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }
    
    public void ouvrirEditionParId() {
        try {
            String idStr = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestParameterMap()
                    .get("profilId");

            if (idStr != null && !idStr.isEmpty()) {
                Long id = Long.valueOf(idStr);
                this.profilSelectionne = profilService.trouverParId(id);

                if (profilSelectionne != null) {
                    System.out.println("✅ Profil sélectionné : " + profilSelectionne.getTitre());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
