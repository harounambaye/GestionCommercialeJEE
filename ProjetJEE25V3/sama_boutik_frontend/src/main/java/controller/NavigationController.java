package controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("navController")
@SessionScoped
public class NavigationController implements Serializable {

    public String goToAccueil() {
        return "/vue/admin/adminAccueil.xhtml?faces-redirect=true";
    }

    public String goToEntite() {
        return "/vue/admin/gestionentite.xhtml?faces-redirect=true";
    }
    
    public String goToGestionnaire() {
        return "/vue/admin/gestionnaire.xhtml?faces-redirect=true";
    }
    
    public String goToActivite() {
        return "/vue/admin/activite.xhtml?faces-redirect=true";
    }
    
    public String goToUtilisateur() {
        return "/vue/admin/utilisateur.xhtml?faces-redirect=true";
    }
    
    public String logout() {
        // Tu pourras y ajouter une logique (session.invalidate(), etc.)
        return "/login.xhtml?faces-redirect=true";
    }
}
