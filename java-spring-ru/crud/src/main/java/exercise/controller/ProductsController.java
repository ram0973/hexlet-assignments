package exercise.controller;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;
import exercise.model.Category;
import exercise.model.Product;
import exercise.repository.CategoryRepository;
import exercise.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    // BEGIN
    // GET /products – просмотр списка всех товаров
    //GET /products/{id} – просмотр конкретного товара
    //POST /products – добавление нового товара. При указании id несуществующей категории должен вернуться ответ с кодом 400 Bad request
    //PUT /products/{id} – редактирование товара. При редактировании мы должны иметь возможность поменять название, цену и категорию товара. При указании id несуществующей категории также должен вернуться ответ с кодом 400 Bad request
    //DELETE /products/{id} – удаление товара
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> result = products.stream().map((p) -> productMapper.map(p)).toList();
        return result;
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product with " + id + " not found"));
        ProductDTO productDTO = productMapper.map(product);
        return productDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody ProductCreateDTO productDTO) {
        categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Category with " + productDTO.getCategoryId() + " not found"));
        Product product = productMapper.map(productDTO);
        ProductDTO createdProduct = productMapper.map(productRepository.save(product));
        return createdProduct;
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@RequestBody ProductUpdateDTO productData, @PathVariable long id) {
        Long categoryId = productData.getCategoryId().orElse(null);
        if (categoryId != null) {
            categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Category with " + productData.getCategoryId() + " not found"));
        }
        Product product = productRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Product with " + id + " not found"));
        productMapper.update(productData, product);
        productRepository.save(product);
        ProductDTO productDTO = productMapper.map(product);
        return productDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable long id) {
        productRepository.deleteById(id);
    }
    // END
}
