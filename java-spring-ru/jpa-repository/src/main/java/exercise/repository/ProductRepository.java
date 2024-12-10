package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByPriceBetween(int priceAfter, int priceBefore, Sort sort);
    List<Product> findProductsByPriceGreaterThanEqual(int priceAfter, Sort sort);
    List<Product> findProductsByPriceLessThanEqual(int priceBefore, Sort sort);
    // END
}
