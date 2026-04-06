package service;

import entity.Famille;
import java.util.List;

public interface FamilleService {

    List<Famille> listerParCatalogue(Long catalogueId);

    Famille trouverParId(Long id);

    void supprimer(Long id);   // ✅ suppression via service
    
    void modifier(Famille f); // ✅ modification via service

    void sauvegarder(Famille f); // ✅ sauvegarde via service
}
