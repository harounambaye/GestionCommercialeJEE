// =============================================================
// Sama_Boutik – JPA Entities (Jakarta EE 10, Hibernate 5.6)
// Notes:
// - Imports use jakarta.persistence (Jakarta EE 10).
// - SERIAL -> GenerationType.IDENTITY.
// - DATE -> java.time.LocalDate, TIMESTAMP -> java.time.LocalDateTime.
// - DECIMAL -> java.math.BigDecimal with precision/scale.
// - Only owning sides are mapped to keep things simple and robust.
// =============================================================

// -------------------------------------------------------------
// src/main/java/entity/Personne.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "personne",
       indexes = {@Index(name = "idx_personne_nom", columnList = "nom")})
public class Personne implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String nom;

    private Integer age;

    @Column(length = 255)
    private String adresse;

    @Column(unique = true, length = 150)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, length = 100)
    private String matricule;

    @Column(name = "datenaissance")
    private LocalDate dateNaissance;

    @Column(name = "lieunaissance", length = 150)
    private String lieuNaissance;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "personne")
    private Profil profil;
    
    @Column(nullable = false)
    private boolean actif = true;

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }


    public Personne() {}

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

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getLieuNaissance() { return lieuNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }

    public Profil getProfil() { return profil; }
    public void setProfil(Profil profil) { this.profil = profil; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
