package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "livraison",
       indexes = {
           @Index(name = "idx_livraison_commande", columnList = "idCommande"),
           @Index(name = "idx_livraison_etat", columnList = "etat")
       })
public class Livraison implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCommande;

    @Column(nullable = false, length = 255)
    private String adresseLivraison;

    private LocalDate dateLivraisonPrevue;
    private LocalDate dateLivraisonReelle;

    @Column(length = 50)
    private String etat = "En attente de livraison";

    private Long idLivreur;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Livraison() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdCommande() { return idCommande; }
    public void setIdCommande(Long idCommande) { this.idCommande = idCommande; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public LocalDate getDateLivraisonPrevue() { return dateLivraisonPrevue; }
    public void setDateLivraisonPrevue(LocalDate dateLivraisonPrevue) { this.dateLivraisonPrevue = dateLivraisonPrevue; }

    public LocalDate getDateLivraisonReelle() { return dateLivraisonReelle; }
    public void setDateLivraisonReelle(LocalDate dateLivraisonReelle) { this.dateLivraisonReelle = dateLivraisonReelle; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public Long getIdLivreur() { return idLivreur; }
    public void setIdLivreur(Long idLivreur) { this.idLivreur = idLivreur; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
