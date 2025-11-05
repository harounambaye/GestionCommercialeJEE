package controller;

import entity.Personne;
import entity.Permission;
import entity.Role;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import service.RoleService;
import service.PermissionService;

@Named("roleController")
@ViewScoped
public class RoleController implements Serializable {

    @EJB private RoleService roleService;
    @EJB private PermissionService permissionService;

    private Personne utilisateurSelectionne;
    private Permission permissionSelectionnee;
    private List<Permission> toutesPermissions;
    private List<Role> rolesUtilisateur;

    @PostConstruct
    public void init() {
        toutesPermissions = permissionService.listerPermissions();
    }

    public void chargerRolesUtilisateur(Long idPersonne) {
        utilisateurSelectionne = roleService.trouverPersonneParId(idPersonne);
        rolesUtilisateur = roleService.listerRolesParPersonne(utilisateurSelectionne);
    }

    public void ajouterPermission() {
        if (utilisateurSelectionne == null || permissionSelectionnee == null) {
            System.out.println("⚠️ utilisateurSelectionne ou permissionSelectionnee null");
            return;
        }
        roleService.ajouterRole(utilisateurSelectionne, permissionSelectionnee);
        rolesUtilisateur = roleService.listerRolesParPersonne(utilisateurSelectionne);
        System.out.println("✅ Permission " + permissionSelectionnee.getNom() +
                           " ajoutée pour " + utilisateurSelectionne.getNom());
        permissionSelectionnee = null;
    }

    public void supprimerPermission(Role role) {
        roleService.supprimerRole(role);
        rolesUtilisateur = roleService.listerRolesParPersonne(utilisateurSelectionne);
    }

    // Getters / Setters
    public Personne getUtilisateurSelectionne() { return utilisateurSelectionne; }
    public List<Role> getRolesUtilisateur() { return rolesUtilisateur; }
    public List<Permission> getToutesPermissions() { return toutesPermissions; }
    public Permission getPermissionSelectionnee() { return permissionSelectionnee; }
    public void setPermissionSelectionnee(Permission permissionSelectionnee) { this.permissionSelectionnee = permissionSelectionnee; }
}
