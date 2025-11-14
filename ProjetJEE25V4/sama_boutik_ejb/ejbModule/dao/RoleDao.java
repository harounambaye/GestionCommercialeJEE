package dao;

import entity.Personne;
import entity.Permission;
import entity.Role;
import java.util.List;

public interface RoleDao {
    void save(Role role);
    void delete(Role role);
    Role findByPersonneAndPermission(Personne p, Permission perm);
    List<Role> findByPersonne(Personne p);
}
