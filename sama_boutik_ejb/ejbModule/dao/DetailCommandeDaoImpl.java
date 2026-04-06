package dao;

import entity.DetailCommande;
import entity.Commande;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class DetailCommandeDaoImpl implements DetailCommandeDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(DetailCommande detail) {
        em.persist(detail);
    }

    @Override
    public List<DetailCommande> findByCommande(Commande commande) {
    	 try {
    	        // ❌ ANCIENNE VERSION (ne fonctionne pas avec OpenJPA)
    	        // SELECT d FROM DetailCommande d LEFT JOIN FETCH d.article a LEFT JOIN FETCH a.produit WHERE d.commande = :commande
    	        
    	        // ✅ NOUVELLE VERSION (compatible OpenJPA)
    	        return em.createQuery("""
    	            SELECT d FROM DetailCommande d 
    	            LEFT JOIN FETCH d.article 
    	            WHERE d.commande = :commande
    	        """, DetailCommande.class)
    	            .setParameter("commande", commande)
    	            .getResultList();
    	        
    	    } catch (Exception e) {
    	        System.err.println("❌ Erreur findByCommande: " + e.getMessage());
    	        e.printStackTrace();
    	        return new ArrayList<>();
    	    }
    	}

    @Override
    public void update(DetailCommande detail) {
        em.merge(detail);
    }

    @Override
    public void delete(Long id) {
        DetailCommande detail = em.find(DetailCommande.class, id);
        if (detail != null) {
            em.remove(detail);
        }
    }
}