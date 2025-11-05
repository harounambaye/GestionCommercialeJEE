// -------------------------------------------------------------
// src/main/java/entity/Profil.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "profil")
public class Profil implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userN", length = 100)
    private String userN;

    @Column(length = 255)
    private String img;

    @Lob
    private String description;

    @Column(length = 100)
    private String titre;

    @ManyToOne
    @JoinColumn(name = "idRole", foreignKey = @ForeignKey(name = "fk_profil_role"))
    private Role role;

    @OneToOne
    @JoinColumn(name = "idPersonne", unique = true, foreignKey = @ForeignKey(name = "fk_profil_personne"))
    private Personne personne;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public Profil() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserN() { return userN; }
    public void setUserN(String userN) { this.userN = userN; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Personne getPersonne() { return personne; }
    public void setPersonne(Personne personne) { this.personne = personne; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}