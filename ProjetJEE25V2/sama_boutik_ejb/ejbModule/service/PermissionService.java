package service;

import entity.Permission;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface PermissionService {
    List<Permission> listerPermissions();
    Permission trouverParId(Long id);
    void modifierPermission(Permission permission);
    void ajouterPermission(Permission permission);
    void supprimerPermission(Long id);
    
}
