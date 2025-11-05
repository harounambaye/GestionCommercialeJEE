package controller;

import entity.ActiviteCommerciale;
import entity.EntiteCommerciale;
import entity.Personne;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import service.ActiviteService;
import service.EntiteService;
import service.PersonneProfilService;

import java.io.Serializable;
import java.util.List;

@Named("entiteController")
@ViewScoped
public class EntiteController implements Serializable {

    @EJB private EntiteService entiteService;
    @EJB private ActiviteService activiteService;
    @EJB private PersonneProfilService personneProfilService;

    private List<ActiviteCommerciale> activites;
    private List<EntiteCommerciale> entites;
    private List<Personne> gestionnairesDisponibles;

    private Long activiteSelectionneeId;
    private String activiteSelectionneeNom;
    private EntiteCommerciale entiteEnEdition;
    private EntiteCommerciale entiteEnDetail; // ✅ pour le modal détail

    @PostConstruct
    public void init() {
        activites = activiteService.listerActivites();
        entiteEnEdition = new EntiteCommerciale();
    }

    // ✅ getters/setters
    public List<ActiviteCommerciale> getActivites() { return activites; }
    public List<EntiteCommerciale> getEntites() { return entites; }
    //public List<Personne> getGestionnairesDisponibles() { return gestionnairesDisponibles; }
    public Long getActiviteSelectionneeId() { return activiteSelectionneeId; }
    public void setActiviteSelectionneeId(Long id) { this.activiteSelectionneeId = id; }
    public String getActiviteSelectionneeNom() { return activiteSelectionneeNom; }
    public EntiteCommerciale getEntiteEnEdition() { return entiteEnEdition; }
    public void setEntiteEnEdition(EntiteCommerciale e) { this.entiteEnEdition = e; }
    public EntiteCommerciale getEntiteEnDetail() { return entiteEnDetail; }
    public void setEntiteEnDetail(EntiteCommerciale entiteEnDetail) { this.entiteEnDetail = entiteEnDetail; }

    public List<Personne> getGestionnairesDisponibles() {
        if (gestionnairesDisponibles == null) {
            gestionnairesDisponibles = personneProfilService.listerGestionnaires()
                .stream()
                .filter(Personne::isActif)
                .toList();
        }
        return gestionnairesDisponibles;
    }

 // ==============================================================
    // MÉTHODE : Préparer les détails d'une entité avant affichage du modal
    // ==============================================================
    public void preparerDetails(EntiteCommerciale e) {
        this.entiteEnDetail = e;
        System.out.println("🔍 Détails préparés pour l'entité : " + e.getNom());
    }
    
    // =====================================================
    // 🟩 Lister les entités pour une activité
    // =====================================================
    public void listerEntites() {
        if (activiteSelectionneeId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention", "Veuillez choisir une activité."));
            return;
        }

        ActiviteCommerciale activite = activiteService.trouverParId(activiteSelectionneeId);
        this.activiteSelectionnee = activite; // ✅ pour que le XHTML puisse accéder à .statut
        activiteSelectionneeNom = activite != null ? activite.getNom() : "";

        entites = entiteService.listerEntitesParActivite(activiteSelectionneeId);

        if (entites == null || entites.isEmpty()) {
            // 🔹 Si aucune entité → prépare le modal d’ajout
            preparerNouvelleEntite();
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ouvrirModalAjout", true);
        }
    }

    // =====================================================
    // 🟢 Préparer ajout d’une entité
    // =====================================================
    public void preparerNouvelleEntite() {
        entiteEnEdition = new EntiteCommerciale();
        // Récupère la liste des gestionnaires (profil = “Gestionnaire”)
        gestionnairesDisponibles = entiteService.listerGestionnaires();
    }

 // =====================================================
 // ➕ Préparer l’ajout d’une nouvelle entité
 // =====================================================
 public void preparerCreationEntite() {
     this.entiteEnEdition = new EntiteCommerciale();
     this.entiteEnEdition.setStatut(true); // par défaut actif

     // 🔹 Charger les gestionnaires
     this.gestionnairesDisponibles = new java.util.ArrayList<>(personneProfilService.listerGestionnaires());

     // 🔹 Récupérer l’activité sélectionnée
     if (activiteSelectionneeId != null) {
         ActiviteCommerciale activite = activiteService.trouverParId(activiteSelectionneeId);
         this.entiteEnEdition.setActivite(activite);
     }

     System.out.println("🆕 Préparation création entité pour activité ID = " + activiteSelectionneeId);
 }

    // =====================================================
    // 💾 Sauvegarde ou modification
    // =====================================================
    public void sauvegarderEntite() {
        try {
            entiteService.ajouterEntite(entiteEnEdition,
                    activiteSelectionneeId,
                    entiteEnEdition.getGestionnaire().getId());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Entité ajoutée avec succès !"));

            listerEntites();
            entiteEnEdition = new EntiteCommerciale();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
        }
    }
    
	 // =====================================================
	 // ✏️ Préparer la modification d'une entité
	 // =====================================================
    public void preparerEdition(EntiteCommerciale e) {
        this.entiteEnEdition = e;

        // ✅ Nom de l’activité actuelle
        this.activiteSelectionneeNom = e.getActivite() != null ? e.getActivite().getNom() : "Non définie";

        // ✅ Créer une copie modifiable des activités
        this.activites = new java.util.ArrayList<>(activiteService.listerActivites());

        // ✅ Mettre l’activité actuelle en tête de liste
        if (e.getActivite() != null) {
            this.activites.removeIf(a -> a.getId().equals(e.getActivite().getId()));
            this.activites.add(0, e.getActivite());
        }

        // ✅ Charger les gestionnaires depuis PersonneProfil
        this.gestionnairesDisponibles = new java.util.ArrayList<>(personneProfilService.listerGestionnaires());

        // ✅ Mettre le gestionnaire actuel en tête de liste
        if (e.getGestionnaire() != null) {
            this.gestionnairesDisponibles.removeIf(g -> g.getId().equals(e.getGestionnaire().getId()));
            this.gestionnairesDisponibles.add(0, e.getGestionnaire());
        }

        System.out.println("🛠️ Préparation de la modification pour : " + e.getNom() + " / Gestionnaire : " +
            (e.getGestionnaire() != null ? e.getGestionnaire().getPrenom() : "aucun"));
    }



	
	 // =====================================================
	 // 💾 Sauvegarder les modifications
	 // =====================================================
	 public void sauvegarderEdition() {
	     try {
	         entiteService.modifierEntite(entiteEnEdition);
	         FacesContext.getCurrentInstance().addMessage(null,
	                 new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Entité mise à jour avec succès !"));
	         listerEntites();
	     } catch (Exception ex) {
	         FacesContext.getCurrentInstance().addMessage(null,
	                 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", ex.getMessage()));
	     }
	 }
	 
	// =====================================================
	// 🗑️ Supprimer une entité commerciale
	// =====================================================
	 public void supprimerEntite(Long id) {
		    FacesContext context = FacesContext.getCurrentInstance();
		    String idStr = context.getExternalContext().getRequestParameterMap().get("entiteId");

		    if (idStr == null || idStr.isEmpty()) {
		        System.out.println("⚠️ Aucun ID transmis depuis JSF !");
		        return;
		    }

		    Long entiteId = Long.parseLong(idStr);
		    System.out.println("🧩 ID reçu depuis JSF = " + entiteId);

		    try {
		        EntiteCommerciale e = entiteService.trouverParId(entiteId);
		        if (e == null) {
		            System.out.println("⚠️ Entité introuvable avec ID " + entiteId);
		            FacesContext.getCurrentInstance().addMessage(null,
		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Introuvable",
		                            "Impossible de trouver l’entité à supprimer."));
		            return;
		        }

		        entiteService.supprimerEntite(entiteId);
		        System.out.println("🗑️ Entité supprimée : " + e.getNom());

		        FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_INFO,
		                        "Succès", "L’entité « " + e.getNom() + " » a été supprimée avec succès."));

		        listerEntites();
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,
		                        "Erreur", "Échec de la suppression : " + ex.getMessage()));
		    }
		}

	// =====================================================

	public void preparerCreationDepuisActivite(ActiviteCommerciale activite) {
	    if (activite != null) {
	        this.activiteSelectionneeId = activite.getId();
	        this.activiteSelectionneeNom = activite.getNom();

	        // 🆕 Nouvelle entité liée à cette activité
	        this.entiteEnEdition = new EntiteCommerciale();
	        this.entiteEnEdition.setActivite(activite);
	        this.entiteEnEdition.setStatut(true);

	        // ✅ Charger correctement les gestionnaires via PersonneProfilService
	        this.gestionnairesDisponibles = new java.util.ArrayList<>(personneProfilService.listerGestionnaires());

	        System.out.println("👥 Gestionnaires disponibles : " + gestionnairesDisponibles.size());
	        System.out.println("🆕 Préparation création entité pour activité : " + activite.getNom());
	    } else {
	        System.err.println("⚠️ Activité nulle dans preparerCreationDepuisActivite !");
	    }
	}
	
	// =====================================================
	// ⚙️ Ouvrir le modal de gestion des activités commerciales
	// =====================================================
	public void ouvrirGestionActivites() {
	    System.out.println("🧭 Ouverture du modal Gestion des Activités Commerciales");
	    // Pour l'instant, rien à faire de plus — la liste est déjà chargée dans activites
	}

	// =====================================================
	// 🟩 Préparer l’ajout d’une nouvelle activité commerciale
	// =====================================================
	private ActiviteCommerciale activiteEnEdition;

	public ActiviteCommerciale getActiviteEnEdition() {
	    return activiteEnEdition;
	}

	public void setActiviteEnEdition(ActiviteCommerciale activiteEnEdition) {
	    this.activiteEnEdition = activiteEnEdition;
	}

	public void preparerAjoutActivite() {
	    this.activiteEnEdition = new ActiviteCommerciale();
	    this.activiteEnEdition.setStatut(true); // Par défaut actif
	    System.out.println("🆕 Préparation ajout nouvelle activité commerciale");
	}

	// =====================================================
	// 💾 Enregistrer la nouvelle activité commerciale
	// =====================================================
	public void sauvegarderActivite() {
	    try {
	        activiteService.ajouterActivite(activiteEnEdition);
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO,
	                        "Succès", "Activité « " + activiteEnEdition.getNom() + " » ajoutée avec succès !"));
	        // Rafraîchir la liste
	        activites = activiteService.listerActivites();
	        activiteEnEdition = new ActiviteCommerciale();
	        System.out.println("✅ Activité ajoutée : " + activiteEnEdition.getNom());
	    } catch (Exception ex) {
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Erreur", "Impossible d’ajouter l’activité : " + ex.getMessage()));
	        ex.printStackTrace();
	    }
	}

	// =====================================================
	// ✏️ Préparer la modification d’une activité
	// =====================================================
	public void preparerEditionActivite(ActiviteCommerciale a) {
	    this.activiteEnEdition = a;
	    System.out.println("🛠️ Préparation modification activité : " + a.getNom());
	}

	// =====================================================
	// 💾 Sauvegarder la modification d’une activité
	// =====================================================
	public void sauvegarderEditionActivite() {
	    try {
	        activiteService.modifierActivite(activiteEnEdition);
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO,
	                        "Succès", "L’activité « " + activiteEnEdition.getNom() + " » a été mise à jour."));
	        activites = activiteService.listerActivites(); // refresh
	        System.out.println("✅ Activité modifiée : " + activiteEnEdition.getNom());
	    } catch (Exception ex) {
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Erreur", "Impossible de modifier l’activité : " + ex.getMessage()));
	        ex.printStackTrace();
	    }
	}
	// =====================================================
	// 🗑️ Supprimer une activité commerciale
	// =====================================================
	public void supprimerActivite(Long id) {
	    try {
	        ActiviteCommerciale a = activiteService.trouverParId(id);
	        if (a == null) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,
	                            "Introuvable", "Impossible de trouver l’activité à supprimer."));
	            return;
	        }

	        // ✅ Vérification sans toucher à la collection lazy
	        try {
	            activiteService.supprimerActivite(id);

	            activites = activiteService.listerActivites();
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,
	                            "Succès", "L’activité « " + a.getNom() + " » a été supprimée avec succès."));
	            System.out.println("🗑️ Activité supprimée : " + a.getNom());
	        } catch (RuntimeException ex) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,
	                            "Blocage", ex.getMessage()));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Erreur", "Échec de la suppression : " + e.getMessage()));
	    }
	}


	// =====================================================
	// 🟢 Activité sélectionnée (objet complet)
	// =====================================================
	private ActiviteCommerciale activiteSelectionnee;

	public ActiviteCommerciale getActiviteSelectionnee() {
	    return activiteSelectionnee;
	}

	public void setActiviteSelectionnee(ActiviteCommerciale activiteSelectionnee) {
	    this.activiteSelectionnee = activiteSelectionnee;
	}

	

}
