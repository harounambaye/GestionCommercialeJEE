// =====================================================================
// Sama_Boutik – DAO Interfaces & Implementations (Jakarta EE 10, JPA 3.0)
// Pattern: one minimal DAO per entity, matching the requested example.
// - Methods: save, findAll, findById
// - Impl: @Stateless EJB using @PersistenceContext(unitName = "samaBoutikPU")
// - Imports use jakarta.* (Jakarta EE 10 / TomEE 10)
// =====================================================================


// ---------------------------------------------------------------------
// src/main/java/dao/ProduitDao.java
// ---------------------------------------------------------------------
package dao;


import entity.Produit;
import java.util.List;


public interface ProduitDao {
void save(Produit produit);
List<Produit> findAll();
Produit findById(Long id);
}