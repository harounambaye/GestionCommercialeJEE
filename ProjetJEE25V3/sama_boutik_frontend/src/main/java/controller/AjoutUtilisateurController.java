package controller;

import entity.Personne;
import entity.Profil;
import entity.Permission;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.PersonneService;
import service.PersonneProfilService;
import service.PermissionService;
import service.RoleService;
import service.ProfilService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("ajoutUtilisateurController")
@RequestScoped
public class AjoutUtilisateurController implements Serializable {

    @EJB private PersonneService personneService;
    @EJB private PersonneProfilService personneProfilService;
    @EJB private RoleService roleService;
    @EJB private PermissionService permissionService;
    @EJB private ProfilService profilService;

    private Personne nouvelUtilisateur = new Personne();
    private Long profilSelectionneId;
    private Map<Long, Boolean> permissionsSelectionneesMap = new HashMap<>();

    // === Getters / Setters ===
    public Personne getNouvelUtilisateur() { return nouvelUtilisateur; }
    public void setNouvelUtilisateur(Personne nouvelUtilisateur) { this.nouvelUtilisateur = nouvelUtilisateur; }

    public Long getProfilSelectionneId() { return profilSelectionneId; }
    public void setProfilSelectionneId(Long id) { this.profilSelectionneId = id; }

    public Map<Long, Boolean> getPermissionsSelectionneesMap() { return permissionsSelectionneesMap; }

    // === Attribution du profil ===
    public void attribuerProfil() {
        try {
            if (nouvelUtilisateur.getId() != null && profilSelectionneId != null) {
                Profil profil = profilService.trouverParId(profilSelectionneId);
                personneProfilService.ajouterProfil(nouvelUtilisateur, profil);

                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Succès",
                        "Profil attribué à l’utilisateur."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Données manquantes",
                        "Utilisateur ou profil non défini."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erreur",
                    e.getMessage()));
        }
    }


    // === Enregistrement complet ===
    public void enregistrerUtilisateurComplet() {
        try {
            // Sauvegarde de la personne
            personneService.ajouterUtilisateur(nouvelUtilisateur);

            // Ajout du profil si sélectionné
            if (profilSelectionneId != null) {
                Profil profil = profilService.trouverParId(profilSelectionneId);
                personneProfilService.ajouterProfil(nouvelUtilisateur, profil);
            }

            // Ajout des permissions cochées
            List<Permission> toutes = permissionService.listerPermissions();
            for (Permission perm : toutes) {
                Boolean checked = permissionsSelectionneesMap.get(perm.getId());
                if (Boolean.TRUE.equals(checked)) {
                    roleService.ajouterRole(nouvelUtilisateur, perm);
                }
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès",
                            "Utilisateur, profil et rôles enregistrés avec succès !"));

            // Réinitialisation
            nouvelUtilisateur = new Personne();
            profilSelectionneId = null;
            permissionsSelectionneesMap.clear();

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur serveur", e.getMessage()));
        }
    }
    
    
}
