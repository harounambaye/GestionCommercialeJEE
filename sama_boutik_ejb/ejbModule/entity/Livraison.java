package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "livraison")
public class Livraison implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idcommande", nullable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "idlivreur")
    private Employe livreur;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(length = 50)
    private String telephone;

    private LocalDateTime datelivraisonprevue;

    @Column(length = 50)
    private String statutlivraison = "EN ATTENTE VALIDATION";

    private LocalDateTime createdat;
    private LocalDateTime updatedat;

    public Livraison() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdat == null) createdat = now;
        if (updatedat == null) updatedat = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedat = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }

    public Employe getLivreur() { return livreur; }
    public void setLivreur(Employe livreur) { this.livreur = livreur; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDateTime getDatelivraisonprevue() { return datelivraisonprevue; }
    public void setDatelivraisonprevue(LocalDateTime datelivraisonprevue) { this.datelivraisonprevue = datelivraisonprevue; }

    public String getStatutlivraison() { return statutlivraison; }
    public void setStatutlivraison(String statutlivraison) { this.statutlivraison = statutlivraison; }

    public LocalDateTime getCreatedat() { return createdat; }
    public void setCreatedat(LocalDateTime createdat) { this.createdat = createdat; }

    public LocalDateTime getUpdatedat() { return updatedat; }
    public void setUpdatedat(LocalDateTime updatedat) { this.updatedat = updatedat; }
}
