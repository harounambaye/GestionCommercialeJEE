package dao;

import entity.Article;
import entity.Lot;
import java.util.List;

public interface ArticleDao {
    void save(Article article);
    List<Article> findAll();
    Article findById(Long id);
    void update(Article article);
    void delete(Long id);
    List<Article> findByLot(Lot lot);
    long countByLot(Lot lot);
}