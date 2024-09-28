package io.bootify.ecommerce_project.repos;

import io.bootify.ecommerce_project.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
