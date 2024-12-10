package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Product;
import exercise.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping
    public List<Product> index(@RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) {
        if (min != null && max != null) {
            return productRepository.findProductsByPriceBetween(min, max, Sort.by("price").ascending());
        } else if (min != null) {
            return productRepository.findProductsByPriceGreaterThanEqual(min, Sort.by("price").ascending());
        } else if (max != null) {
            return productRepository.findProductsByPriceLessThanEqual(max, Sort.by("price").ascending());
        } else {
            return productRepository.findAll();
        }
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
