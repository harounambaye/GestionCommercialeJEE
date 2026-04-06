package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock",
       indexes = @Index(name = "idx_stock_nom", columnList = "nom"))
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idEntiteCommerciale", foreignKey = @ForeignKey(name = "fk_stock_entite"))
    private EntiteCommerciale entiteCommerciale;

    @Column(nullable = false, length = 150)
    private String nom;

    private String localisation;
    private Integer capaciteMax;
    
    // ✅ VÉRIFICATION : Utiliser "etat" comme dans votre code actuel
    private Boolean etat = Boolean.TRUE;

    // ✅ NOUVEAU : Relation avec les lots
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lot> lots = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (etat == null) {
            etat = Boolean.TRUE;
        }
    }

    @PreUpdate
    public void preUpdate() { 
        updatedAt = LocalDateTime.now(); 
    }

    // ✅ Méthodes utilitaires
    /**
     * Ajoute un lot au stock
     */
    public void ajouterLot(Lot lot) {
        lots.add(lot);
        lot.setStock(this);
    }

    /**
     * Retire un lot du stock
     */
    public void retirerLot(Lot lot) {
        lots.remove(lot);
        lot.setStock(null);
    }

    /**
     * Calcule la quantité totale en stock (tous lots confondus)
     */
    public Integer getQuantiteTotale() {
        return lots.stream()
            .mapToInt(lot -> lot.getQuantiteRestante() != null ? lot.getQuantiteRestante() : 0)
            .sum();
    }

    /**
     * Compte le nombre d'articles différents dans le stock
     */
    public long getNombreArticlesDifferents() {
        return lots.stream()
            .flatMap(lot -> lot.getArticles().stream())
            .map(article -> article.getProduit().getId())
            .distinct()
            .count();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EntiteCommerciale getEntiteCommerciale() { return entiteCommerciale; }
    public void setEntiteCommerciale(EntiteCommerciale entiteCommerciale) { 
        this.entiteCommerciale = entiteCommerciale; 
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }

    public Boolean getEtat() { return etat; }
    public void setEtat(Boolean etat) { this.etat = etat; }

    public List<Lot> getLots() { return lots; }
    public void setLots(List<Lot> lots) { this.lots = lots; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}