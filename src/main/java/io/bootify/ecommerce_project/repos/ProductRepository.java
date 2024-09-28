package io.bootify.ecommerce_project.repos;

import io.bootify.ecommerce_project.domain.Category;
import io.bootify.ecommerce_project.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByCategory(Category category);

}
