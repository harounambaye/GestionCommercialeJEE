// -------------------------------------------------------------
// src/main/java/entity/Role.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "role",
       uniqueConstraints = @UniqueConstraint(columnNames = {"idPersonne", "idPermission"}))
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Lien vers la personne concernée
    @ManyToOne(optional = false)
    @JoinColumn(name = "idPersonne", foreignKey = @ForeignKey(name = "fk_role_personne"))
    private Personne personne;

    // ✅ Lien vers la permission concernée
    @ManyToOne(optional = false)
    @JoinColumn(name = "idPermission", foreignKey = @ForeignKey(name = "fk_role_permission"))
    private Permission permission;

    // ✅ Active ou non pour cet utilisateur
    @Column(nullable = false)
    private Boolean etat = Boolean.FALSE;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public Role() {}

    // 🔹 Audit automatique
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

    // 🔹 Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Personne getPersonne() { return personne; }
    public void setPersonne(Personne personne) { this.personne = personne; }

    public Permission getPermission() { return permission; }
    public void setPermission(Permission permission) { this.permission = permission; }

    public Boolean getEtat() { return etat; }
    public void setEtat(Boolean etat) { this.etat = etat; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
