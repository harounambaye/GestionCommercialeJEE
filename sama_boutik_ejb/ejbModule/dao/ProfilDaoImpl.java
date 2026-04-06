package dao;

import entity.Profil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Stateless
public class ProfilDaoImpl implements ProfilDao {

    @PersistenceContext(unitName = "samaBoutikPU")
    private EntityManager em;

    @Override
    public void save(Profil profil) {
        em.persist(profil);
    }

    @Override
    public List<Profil> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Profil> cq = cb.createQuery(Profil.class);
        Root<Profil> root = cq.from(Profil.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public Profil findById(Long id) {
        return em.find(Profil.class, id);
    }

    @Override
    public void update(Profil profil) {
        if (profil == null || profil.getId() == null) return;

        Profil existing = em.find(Profil.class, profil.getId());
        if (existing != null) {
            // 🔹 Forcer la mise à jour champ par champ
            if (profil.getTitre() != null && !profil.getTitre().equals(existing.getTitre())) {
                existing.setTitre(profil.getTitre());
            }
            if (profil.getDescription() != null && !profil.getDescription().equals(existing.getDescription())) {
                existing.setDescription(profil.getDescription());
            }
            if (profil.getImg() != null && !profil.getImg().equals(existing.getImg())) {
                existing.setImg(profil.getImg());
            }
            if (profil.getUserN() != null && !profil.getUserN().equals(existing.getUserN())) {
                existing.setUserN(profil.getUserN());
            }

            // (Optionnel si tu veux rafraîchir la date manuellement)
            existing.setUpdatedAt(java.time.LocalDateTime.now());

            em.merge(existing);
            em.flush(); // ✅ force l’écriture immédiate
            System.out.println("✅ Profil mis à jour : " + existing.getTitre());
        } else {
            System.err.println("⚠️ Aucun profil trouvé avec ID " + profil.getId());
        }
    }


    @Override
    public void delete(Long id) {
        Profil p = em.find(Profil.class, id);
        if (p != null) em.remove(p);
    }
    
    
}
