// =============================================================
// Interface EmployeDao
// =============================================================
package dao;

import entity.Employe;
import entity.EntiteCommerciale;
import java.util.List;

public interface EmployeDao {
    void save(Employe employe);
    void update(Employe employe);
    Employe findById(Long id);
    List<Employe> findByEntiteCommerciale(EntiteCommerciale entite);
    List<Employe> findActifsByEntite(EntiteCommerciale entite);
    Employe findByPersonneId(Long personneId);
    long countByEntite(EntiteCommerciale entite);
}