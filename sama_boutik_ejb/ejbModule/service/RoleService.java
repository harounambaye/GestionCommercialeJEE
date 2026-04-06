package service;

import entity.Personne;
import entity.Permission;
import entity.Role;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface RoleService {
    void ajouterRole(Personne p, Permission perm);
    void supprimerRole(Role role);
    List<Role> listerRolesParPersonne(Personne p);
    Personne trouverPersonneParId(Long id);
}
