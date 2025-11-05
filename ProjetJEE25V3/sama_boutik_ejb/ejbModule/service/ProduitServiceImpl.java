// ---------------------------------------------------------------------
// src/main/java/service/ProduitServiceImpl.java
// ---------------------------------------------------------------------
package service;


import dao.ProduitDao;
import entity.Produit;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.util.List;


@Stateless
public class ProduitServiceImpl implements ProduitService {


@EJB
private ProduitDao produitDao;


@Override
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void creerProduit(Produit produit) {
	produitDao.save(produit);
}


@Override
public List<Produit> listerProduits() {
	return produitDao.findAll();
}
}