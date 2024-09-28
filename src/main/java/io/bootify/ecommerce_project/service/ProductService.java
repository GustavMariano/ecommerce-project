package io.bootify.ecommerce_project.service;

import io.bootify.ecommerce_project.domain.Category;
import io.bootify.ecommerce_project.domain.Product;
import io.bootify.ecommerce_project.model.CategoryDTO;
import io.bootify.ecommerce_project.model.ProductDTO;
import io.bootify.ecommerce_project.repos.CategoryRepository;
import io.bootify.ecommerce_project.repos.ProductRepository;
import io.bootify.ecommerce_project.util.NotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(final ProductRepository productRepository,
            final CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());

        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            productDTO.setCategory(categoryDTO);
        } else {
            productDTO.setCategory(null);
        }

        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        if (productDTO.getCategory() != null) {
            final Category category = categoryRepository.findById(productDTO.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return product;
    }

}
