// -------------------------------------------------------------
// src/main/java/controller/EntiteController.java
// Jakarta EE 10 (TomEE 10) – JSF 3 + EJB Service injection
// Inspiré du modèle de ProduitController (100% compatible TomEE 10)
// -------------------------------------------------------------
package controller;

import entity.Entite;
import entity.ActiviteCommerciale;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import service.EntiteService;
import service.ActiviteCommercialeService;

@Named("entiteController")
@RequestScoped
public class EntiteController implements Serializable {

    private Entite entite = new Entite();
    private List<Entite> entites; // chargement lazy
    private Long activiteIdSelectionnee;

    @EJB
    private EntiteService entiteService;

    @EJB
    private ActiviteCommercialeService activiteService;

    // -------------------------------------------------------------
    // Action : Créer une entité commerciale
    // -------------------------------------------------------------
    public String creerEntite() {
        try {
            if (activiteIdSelectionnee != null) {
                ActiviteCommerciale activite = activiteService.trouverParId(activiteIdSelectionnee);
                entite.setActivite(activite);
            }
            entiteService.creerEntite(entite);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Entité créée avec succès."));
            // reset & refresh
            entite = new Entite();
            entites = null; // rechargement à la prochaine consultation
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
        return null; // rester sur la même page
    }
    
 // Sélection pour modification
    public void preparerModification(Entite e) {
        this.entite = e;
        this.activiteIdSelectionnee = e.getActivite() != null ? e.getActivite().getId() : null;
    }

    // Sauvegarde la modification
    public void modifierEntite() {
        try {
            if (activiteIdSelectionnee != null) {
                ActiviteCommerciale activite = activiteService.trouverParId(activiteIdSelectionnee);
                entite.setActivite(activite);
            }
            entiteService.modifierEntite(entite);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Entité modifiée avec succès"));
            entites = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }

    // Suppression
    public void supprimerEntite(Long id) {
        try {
            entiteService.supprimerEntite(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Entité supprimée"));
            entites = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }


    // -------------------------------------------------------------
    // Getter : Liste des entités
    // -------------------------------------------------------------
    public List<Entite> getEntites() {
        if (entites == null) {
            entites = entiteService.listerEntites();
        }
        return entites;
    }

    // -------------------------------------------------------------
    // Getter : Liste des activités commerciales (pour le selectOneMenu)
    // -------------------------------------------------------------
    public List<ActiviteCommerciale> getActivites() {
        return activiteService.listerActivites();
    }

    // -------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------
    public Entite getEntite() { return entite; }
    public void setEntite(Entite entite) { this.entite = entite; }

    public Long getActiviteIdSelectionnee() { return activiteIdSelectionnee; }
    public void setActiviteIdSelectionnee(Long activiteIdSelectionnee) { this.activiteIdSelectionnee = activiteIdSelectionnee; }

    public void setEntites(List<Entite> entites) { this.entites = entites; }
}
