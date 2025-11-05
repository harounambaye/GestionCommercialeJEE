package converter;

import entity.Permission;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import service.PermissionService;

@FacesConverter(value = "permissionConverter", managed = true)
@RequestScoped
public class PermissionConverter implements Converter<Permission> {

    @Inject
    private PermissionService permissionService;

    @Override
    public String getAsString(FacesContext ctx, UIComponent cmp, Permission permission) {
        return (permission == null) ? "" : permission.getId().toString();
    }

    @Override
    public Permission getAsObject(FacesContext ctx, UIComponent cmp, String value) {
        if (value == null || value.isBlank()) return null;
        return permissionService.trouverParId(Long.valueOf(value));
    }
}
