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
import service.PersonneProfilService; 

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
    
    @EJB
    private PersonneProfilService personneProfilService;
    
    private String termeRecherche; // 🔍 champ de recherche

    // --- Getters/Setters ---
    public String getTermeRecherche() { return termeRecherche; }
    public void setTermeRecherche(String termeRecherche) { this.termeRecherche = termeRecherche; }


    // Charger les utilisateurs
    public List<Personne> getUtilisateurs() {
        if (profilFiltreId != null) {
            utilisateurs = utilisateurService.listerUtilisateursParProfil(profilFiltreId);
        } else {
            utilisateurs = utilisateurService.listerUtilisateurs();
        }
        return utilisateurs;
    }

    // ✅ CORRECTION : on récupère les profils existants depuis la table personneprofil
    public List<Profil> getProfils() {
        profils = profilService.listerProfils(); // 🔁 recharge à chaque appel
        return profils;
    }


    // Filtrage dynamique
    public void filtrerParProfil() {
        try {
            if (profilFiltreId != null) {
                // 🔥 On interroge la table de liaison PersonneProfil
                utilisateurs = utilisateurService.listerUtilisateursParProfilViaPersonneProfil(profilFiltreId);
            } else {
                utilisateurs = utilisateurService.listerUtilisateurs();
            }
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
    
    /************************************************************************ */
    /************************ MODIFIER UN UTILISATEUR *************************/
    /************************************************************************ */
    
    private Personne utilisateurEnEdition;
    private Long profilEditionId;
    
    private Profil profilActuelEdition;

    public Profil getProfilActuelEdition() {
        return profilActuelEdition;
    }

    public void setProfilActuelEdition(Profil profilActuelEdition) {
        this.profilActuelEdition = profilActuelEdition;
    }

    // Ouvre le modal de modification
    public void preparerEdition(Personne u) {
        this.utilisateurEnEdition = new Personne();
        this.utilisateurEnEdition.setId(u.getId());
        this.utilisateurEnEdition.setPrenom(u.getPrenom());
        this.utilisateurEnEdition.setNom(u.getNom());
        this.utilisateurEnEdition.setEmail(u.getEmail());
        this.utilisateurEnEdition.setAdresse(u.getAdresse());
        this.utilisateurEnEdition.setLieuNaissance(u.getLieuNaissance());
        this.utilisateurEnEdition.setDateNaissance(u.getDateNaissance());
        this.utilisateurEnEdition.setActif(u.isActif());
        
        // Récupération du profil actuel
     // Récupération du profil actuel
        Profil profilActuel = utilisateurService.trouverProfilParPersonne(u.getId());
        if (profilActuel != null) {
            this.profilEditionId = profilActuel.getId();
            this.profilActuelEdition = profilActuel;
        }

    }

    // Enregistre les modifications
    public void sauvegarderEdition() {
        try {
            Profil profil = null;
            if (profilEditionId != null) {
                profil = profilService.trouverParId(profilEditionId);
            }

            // 🔹 Mise à jour des infos personnelles
            utilisateurService.mettreAJour(utilisateurEnEdition);

            // 🔹 Gestion du profil : suppression de l'ancien avant ajout
            if (profil != null) {
                personneProfilService.supprimerProfilsDePersonne(utilisateurEnEdition);
                personneProfilService.ajouterProfil(utilisateurEnEdition, profil);
            }

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Succès", "Utilisateur mis à jour et profil réassigné avec succès."));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erreur", "Impossible de modifier l'utilisateur."));
            e.printStackTrace();
        }
    }


    // Getters / Setters
    public Personne getUtilisateurEnEdition() { return utilisateurEnEdition; }
    public void setUtilisateurEnEdition(Personne utilisateurEnEdition) { this.utilisateurEnEdition = utilisateurEnEdition; }
    public Long getProfilEditionId() { return profilEditionId; }
    public void setProfilEditionId(Long profilEditionId) { this.profilEditionId = profilEditionId; }

    
    /************************************************************************ */
    /****************  TOGGLE STATUT ACTIF / INACTIF ***********************/
    /************************************************************************ */
    public void toggleStatut(Personne p) {
        try {
            p.setActif(!p.isActif()); // 🔁 inversion du statut
            utilisateurService.mettreAJour(p);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                                 "Statut mis à jour",
                                 p.isActif() ? "L'utilisateur est maintenant actif." : "L'utilisateur a été désactivé."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                 "Erreur", "Impossible de changer le statut."));
            e.printStackTrace();
        }
    }

    /************************************************************************ */
    /****************** Recherche par nom, prénom ou email ****************** */
    /************************************************************************ */
    public void rechercherUtilisateurs() {
        try {
            if (termeRecherche == null || termeRecherche.trim().isEmpty()) {
                utilisateurs = utilisateurService.listerUtilisateurs();
            } else {
                utilisateurs = utilisateurService.rechercherUtilisateurs(termeRecherche.trim());
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur recherche", e.getMessage()));
        }
    }
    
    /************************************************************************ */
    /****************  GESTION DE LA PAGINATION SUIVANT / PRECEDENT ***********************/
    /************************************************************************ */

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
