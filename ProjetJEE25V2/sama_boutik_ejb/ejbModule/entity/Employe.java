// -------------------------------------------------------------
// src/main/java/entity/Employe.java
// -------------------------------------------------------------
package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "employe")
public class Employe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idPersonne", unique = true, foreignKey = @ForeignKey(name = "fk_employe_personne"))
    private Personne personne;

    @Column(length = 100)
    private String poste;

    private LocalDate dateEmbauche;

    public Employe() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Personne getPersonne() { return personne; }
    public void setPersonne(Personne personne) { this.personne = personne; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
}
