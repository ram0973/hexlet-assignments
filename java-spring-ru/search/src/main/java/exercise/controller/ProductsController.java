package exercise.controller;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductParamsDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;
import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.specification.ProductSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSpecification specBuilder;

    // BEGIN
    @GetMapping
    public List<ProductDTO> index(ProductParamsDTO paramsDTO,@RequestParam(defaultValue = "1") int page) {
        Specification<Product> spec = specBuilder.build(paramsDTO);
        Page<Product> products = productRepository.findAll(spec, PageRequest.of(page - 1, 10));
        List<ProductDTO> result = products.map(productMapper::map).toList();
        return result;

    }
    // END

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ProductDTO create(@Valid @RequestBody ProductCreateDTO productData) {
        Product product = productMapper.map(productData);
        productRepository.save(product);
        ProductDTO productDto = productMapper.map(product);
        return productDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO show(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found: " + id));
        ProductDTO productDto = productMapper.map(product);
        return productDto;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO update(@RequestBody @Valid ProductUpdateDTO productData, @PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found: " + id));

        productMapper.update(productData, product);
        productRepository.save(product);
        ProductDTO productDto = productMapper.map(product);
        return productDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
