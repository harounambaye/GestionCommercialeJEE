// ---------------------------------------------------------------------
// src/main/java/dao/ProduitDaoImpl.java
// ---------------------------------------------------------------------
/*package dao;


import entity.Produit;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;


@Stateless
public class ProduitDaoImpl implements ProduitDao {


@PersistenceContext(unitName = "samaBoutikPU")
private EntityManager em;


@Override
public void save(Produit produit) {
em.persist(produit);
}


@Override
public List<Produit> findAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Produit> cq = cb.createQuery(Produit.class);
    Root<Produit> root = cq.from(Produit.class);
    cq.select(root);
    return em.createQuery(cq).getResultList();
}



@Override
public Produit findById(Long id) {
return em.find(Produit.class, id);
}
}*/