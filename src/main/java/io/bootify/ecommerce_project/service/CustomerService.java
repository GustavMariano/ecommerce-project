package io.bootify.ecommerce_project.service;

import io.bootify.ecommerce_project.domain.Address;
import io.bootify.ecommerce_project.domain.Customer;
import io.bootify.ecommerce_project.model.AddressDTO;
import io.bootify.ecommerce_project.model.CustomerDTO;
import io.bootify.ecommerce_project.repos.AddressRepository;
import io.bootify.ecommerce_project.repos.CustomerRepository;
import io.bootify.ecommerce_project.util.NotFoundException;
import io.bootify.ecommerce_project.util.ReferencedWarning;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);

        customerRepository.save(customer);

        if (customerDTO.getAddresses() != null) {
            for (AddressDTO addressDTO : customerDTO.getAddresses()) {
                Address address = new Address();
                mapToAddressEntity(addressDTO, address);
                address.setCustomer(customer);
                addressRepository.save(address);
            }
        }

        return customer.getId();
    }

    @Transactional
    public void update(final Long id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        mapToEntity(customerDTO, customer);

        updateAddresses(customerDTO.getAddresses(), customer);

        customerRepository.save(customer);
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());

        if (customer.getCustomerAddresses() != null) {
            List<AddressDTO> addressDTOs = customer.getCustomerAddresses().stream()
                    .map(address -> mapToAddressDTO(address, new AddressDTO()))
                    .toList();
            customerDTO.setAddresses(addressDTOs);
        } else {
            customerDTO.setAddresses(null);
        }

        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Address customerAddress = addressRepository.findFirstByCustomer(customer);
        if (customerAddress != null) {
            referencedWarning.setKey("customer.address.customer.referenced");
            referencedWarning.addParam(customerAddress.getId());
            return referencedWarning;
        }
        return null;
    }

    private Address mapToAddressEntity(final AddressDTO addressDTO, final Address address) {
        address.setCep(addressDTO.getCep());
        address.setRua(addressDTO.getRua());
        address.setNumero(addressDTO.getNumero());
        address.setBairro(addressDTO.getBairro());
        address.setComplemento(addressDTO.getComplemento());
        address.setCidade(addressDTO.getCidade());
        address.setEstado(addressDTO.getEstado());
        return address;
    }

    private AddressDTO mapToAddressDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setCep(address.getCep());
        addressDTO.setRua(address.getRua());
        addressDTO.setNumero(address.getNumero());
        addressDTO.setBairro(address.getBairro());
        addressDTO.setComplemento(address.getComplemento());
        addressDTO.setCidade(address.getCidade());
        addressDTO.setEstado(address.getEstado());
        return addressDTO;
    }

    private void updateAddresses(List<AddressDTO> addressDTOs, Customer customer) {
        if (addressDTOs == null || addressDTOs.isEmpty()) {
            customer.getCustomerAddresses().clear();
            return;
        }

        Map<Long, Address> existingAddresses = customer.getCustomerAddresses().stream()
                .collect(Collectors.toMap(Address::getId, address -> address));

        for (AddressDTO addressDTO : addressDTOs) {
            Address address;
            if (addressDTO.getId() != null) {
                address = existingAddresses.remove(addressDTO.getId());
                if (address != null) {
                    address.setCep(addressDTO.getCep());
                    address.setRua(addressDTO.getRua());
                    address.setNumero(addressDTO.getNumero());
                    address.setBairro(addressDTO.getBairro());
                    address.setComplemento(addressDTO.getComplemento());
                    address.setCidade(addressDTO.getCidade());
                    address.setEstado(addressDTO.getEstado());
                } else {
                    throw new NotFoundException("Address with ID " + addressDTO.getId() + " not found");
                }
            } else {
                address = new Address();
                address.setCep(addressDTO.getCep());
                address.setRua(addressDTO.getRua());
                address.setNumero(addressDTO.getNumero());
                address.setBairro(addressDTO.getBairro());
                address.setComplemento(addressDTO.getComplemento());
                address.setCidade(addressDTO.getCidade());
                address.setEstado(addressDTO.getEstado());
                address.setCustomer(customer);
                customer.getCustomerAddresses().add(address);
            }
        }

        for (Address address : existingAddresses.values()) {
            address.setCustomer(null);
            customer.getCustomerAddresses().remove(address);
            addressRepository.delete(address);
        }
    }

}
