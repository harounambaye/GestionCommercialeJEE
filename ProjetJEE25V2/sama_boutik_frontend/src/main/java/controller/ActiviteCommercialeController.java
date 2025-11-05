package controller;

import entity.ActiviteCommerciale;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;
import service.ActiviteCommercialeService;

@Named("activiteController")
@RequestScoped
public class ActiviteCommercialeController {

    private ActiviteCommerciale activite = new ActiviteCommerciale();
    private List<ActiviteCommerciale> activites;

    @EJB
    private ActiviteCommercialeService activiteService;

    public String creerActivite() {
        try {
            activiteService.creerActivite(activite);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Activité créée."));
            activite = new ActiviteCommerciale();
            activites = null; // forcer le rechargement
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
        return null;
    }

    public List<ActiviteCommerciale> getActivites() {
        if (activites == null) {
            activites = activiteService.listerActivites();
        }
        return activites;
    }

    // Getters / Setters
    public ActiviteCommerciale getActivite() { return activite; }
    public void setActivite(ActiviteCommerciale activite) { this.activite = activite; }
}
