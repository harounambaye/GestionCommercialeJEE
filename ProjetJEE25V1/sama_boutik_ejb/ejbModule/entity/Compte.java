// -------------------------------------------------------------
// src/main/java/entity/Compte.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "compte",
       indexes = {@Index(name = "idx_compte_login", columnList = "login")})
public class Compte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "status")
    private Boolean status = Boolean.TRUE;

    @OneToOne
    @JoinColumn(name = "idPersonne", unique = true, foreignKey = @ForeignKey(name = "fk_compte_personne"))
    private Personne personne;

    public Compte() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public Personne getPersonne() { return personne; }
    public void setPersonne(Personne personne) { this.personne = personne; }
}
