package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activitecommerciale")
public class ActiviteCommerciale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nom;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean statut = Boolean.TRUE;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(
    	    mappedBy = "activite",
    	    cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
    	    orphanRemoval = true,
    	    fetch = FetchType.LAZY
    	)
    private List<EntiteCommerciale> entites;

    // Constructors
    public ActiviteCommerciale() {}

    public ActiviteCommerciale(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.statut = true;
    }

    // Callbacks
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getStatut() { return statut; }
    public void setStatut(Boolean statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<EntiteCommerciale> getEntites() { return entites; }
    public void setEntites(List<EntiteCommerciale> entites) { this.entites = entites; }
}
