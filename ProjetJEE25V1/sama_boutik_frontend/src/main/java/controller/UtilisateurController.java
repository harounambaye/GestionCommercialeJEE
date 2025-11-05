package controller;

import entity.Profil;
import entity.Personne;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import service.UtilisateurService;
import service.ProfilService;

@Named("utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    private Personne utilisateurSelectionne;
    private Long profilFiltreId;
    private List<Personne> utilisateurs;
    private List<Profil> profils;

    @EJB
    private UtilisateurService utilisateurService;

    @EJB
    private ProfilService profilService;

    // Charger les utilisateurs
    public List<Personne> getUtilisateurs() {
        if (profilFiltreId != null) {
            utilisateurs = utilisateurService.listerUtilisateursParProfil(profilFiltreId);
        } else {
            utilisateurs = utilisateurService.listerUtilisateurs();
        }
        return utilisateurs;
    }

    public List<Profil> getProfils() {
        if (profils == null) {
            profils = profilService.listerProfils();
        }
        return profils;
    }

    // Filtrage dynamique
    public void filtrerParProfil() {
        try {
            utilisateurs = utilisateurService.listerUtilisateursParProfil(profilFiltreId);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }

    // Getters / Setters
    public Long getProfilFiltreId() { return profilFiltreId; }
    public void setProfilFiltreId(Long profilFiltreId) { this.profilFiltreId = profilFiltreId; }

    public Personne getUtilisateurSelectionne() { return utilisateurSelectionne; }
    public void setUtilisateurSelectionne(Personne utilisateurSelectionne) { this.utilisateurSelectionne = utilisateurSelectionne; }

    public List<Personne> getUtilisateursList() { return utilisateurs; }
    
    
    /***********PAGINATION  SUIVANT / PRECEDENT *************/
    private int pageCourante = 1;
    private int taillePage = 10;

    public List<Personne> getUtilisateursPage() {
        // ✅ Charge la liste si elle n'est pas encore initialisée
        if (utilisateurs == null) {
            if (profilFiltreId != null) {
                utilisateurs = utilisateurService.listerUtilisateursParProfil(profilFiltreId);
            } else {
                utilisateurs = utilisateurService.listerUtilisateurs();
            }
        }

        if (utilisateurs.isEmpty()) return utilisateurs; // évite erreur sur subList()

        int start = (pageCourante - 1) * taillePage;
        int end = Math.min(start + taillePage, utilisateurs.size());
        return utilisateurs.subList(start, end);
    }


    public void pageSuivante() {
        if (pageCourante < getTotalPages()) pageCourante++;
    }

    public void pagePrecedente() {
        if (pageCourante > 1) pageCourante--;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) utilisateurs.size() / taillePage);
    }

    public boolean isHasNext() {
        return pageCourante < getTotalPages();
    }

    public boolean isHasPrev() {
        return pageCourante > 1;
    }

    public int getPageCourante() {
        return pageCourante;
    }
    
}
