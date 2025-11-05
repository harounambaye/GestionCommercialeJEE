/*package converter;

import entity.Profil;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.naming.InitialContext;
import jakarta.naming.NamingException;
import service.ProfilService;

@FacesConverter(value = "profilConverter", managed = false)
public class ProfilConverter implements Converter<Profil> {

    private ProfilService lookupService() {
        try {
            // 🔥 Nom JNDI EJB — TomEE l’expose ainsi :
            return (ProfilService) new InitialContext().lookup("java:global/sama_boutik_ejb/ProfilServiceImpl!service.ProfilService");
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Profil getAsObject(FacesContext ctx, UIComponent cmp, String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            ProfilService profilService = lookupService();
            return profilService != null ? profilService.trouverParId(Long.valueOf(value)) : null;
        } catch (Exception e) {
            System.err.println("⚠️ Erreur conversion Profil : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent cmp, Profil profil) {
        if (profil == null) return "";
        return profil.getId() != null ? profil.getId().toString() : "";
    }
}
*/