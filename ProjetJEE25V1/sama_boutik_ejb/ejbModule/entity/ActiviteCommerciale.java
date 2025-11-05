package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "activitecommerciale")
public class ActiviteCommerciale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
    private String description;

    // 🔗 Relation inverse vers Entite
    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    private List<Entite> entites;

    // ==========================
    //   Constructeurs
    // ==========================
    public ActiviteCommerciale() {}

    public ActiviteCommerciale(String code, String libelle, String description) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
    }

    // ==========================
    //   Getters / Setters
    // ==========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Entite> getEntites() {
        return entites;
    }

    public void setEntites(List<Entite> entites) {
        this.entites = entites;
    }

    // ==========================
    //   toString
    // ==========================
    @Override
    public String toString() {
        return libelle != null ? libelle : code;
    }
}
