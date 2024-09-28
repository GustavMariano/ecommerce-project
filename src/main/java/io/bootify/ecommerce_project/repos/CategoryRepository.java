package io.bootify.ecommerce_project.repos;

import io.bootify.ecommerce_project.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
