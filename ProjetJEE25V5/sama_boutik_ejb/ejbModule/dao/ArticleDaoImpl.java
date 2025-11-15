package dao;

import entity.Article;
import entity.Lot;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ArticleDaoImpl implements ArticleDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Article article) {
        em.persist(article);
    }

    @Override
    public List<Article> findAll() {
        return em.createQuery("SELECT a FROM Article a ORDER BY a.createdAt DESC", Article.class)
                 .getResultList();
    }

    @Override
    public Article findById(Long id) {
        return em.find(Article.class, id);
    }

    @Override
    public void update(Article article) {
        em.merge(article);
    }

    @Override
    public void delete(Long id) {
        Article a = em.find(Article.class, id);
        if (a != null) em.remove(a);
    }

    @Override
    public List<Article> findByLot(Lot lot) {
        try {
            // ✅ FETCH JOIN pour charger les produits en même temps
            return em.createQuery(
                "SELECT a FROM Article a " +
                "LEFT JOIN FETCH a.produit " +
                "WHERE a.lot.id = :lotId " +
                "ORDER BY a.createdAt DESC",
                Article.class
            )
            .setParameter("lotId", lot.getId())
            .getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erreur findByLot: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public long countByLot(Lot lot) {
        try {
            return em.createQuery(
                "SELECT COUNT(a) FROM Article a WHERE a.lot.id = :lotId",
                Long.class
            )
            .setParameter("lotId", lot.getId())
            .getSingleResult();
        } catch (Exception e) {
            System.err.println("❌ Erreur countByLot: " + e.getMessage());
            return 0L;
        }
    }
}