package controller;

import entity.Permission;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.PermissionService;
import java.io.Serializable;
import java.util.List;

@Named("permissionController")
@SessionScoped
public class PermissionController implements Serializable {

    @EJB
    private PermissionService permissionService;

    private List<Permission> permissions;
    private Permission nouvellePermission;
    private Permission permissionSelectionnee;

    @PostConstruct
    public void init() {
        rafraichirListe();
        nouvellePermission = new Permission();
    }

    public void rafraichirListe() {
        permissions = permissionService.listerPermissions();
    }

    public void ajouterPermission() {
        if (nouvellePermission.getNom() != null && !nouvellePermission.getNom().trim().isEmpty()) {
            permissionService.ajouterPermission(nouvellePermission);
            nouvellePermission = new Permission();
            rafraichirListe();
        }
    }

    public void supprimerPermission(Permission p) {
        try {
            permissionService.supprimerPermission(p.getId());
            rafraichirListe();
        } catch (IllegalStateException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Suppression impossible", e.getMessage()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Erreur serveur", "Une erreur est survenue pendant la suppression."));
        }
    }


    public void preparerModification(Permission p) {
        permissionSelectionnee = p;
    }

    public void modifierPermission() {
        if (permissionSelectionnee != null) {
            permissionService.modifierPermission(permissionSelectionnee);
            permissionSelectionnee = null;
            rafraichirListe();
        }
    }
    
 // Ouvre le modal de modification
    public void ouvrirEdition(Permission p) {
        permissionSelectionnee = new Permission();
        permissionSelectionnee.setId(p.getId());
        permissionSelectionnee.setNom(p.getNom());
    }

    // Enregistre la modification
    public void sauvegarderEdition() {
        if (permissionSelectionnee != null && permissionSelectionnee.getNom() != null && !permissionSelectionnee.getNom().trim().isEmpty()) {
            permissionService.modifierPermission(permissionSelectionnee);
            permissionSelectionnee = null;
            rafraichirListe();
        }
    }


    // Getters/Setters
    public List<Permission> getPermissions() { return permissions; }
    public Permission getNouvellePermission() { return nouvellePermission; }
    public void setNouvellePermission(Permission nouvellePermission) { this.nouvellePermission = nouvellePermission; }
    public Permission getPermissionSelectionnee() { return permissionSelectionnee; }
    public void setPermissionSelectionnee(Permission permissionSelectionnee) { this.permissionSelectionnee = permissionSelectionnee; }
}
