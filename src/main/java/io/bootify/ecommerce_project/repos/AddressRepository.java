package io.bootify.ecommerce_project.repos;

import io.bootify.ecommerce_project.domain.Address;
import io.bootify.ecommerce_project.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findFirstByCustomer(Customer customer);

}
