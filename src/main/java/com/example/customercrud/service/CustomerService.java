package com.example.customercrud.service;

import com.example.customercrud.entity.Customer;
import com.example.customercrud.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer id) {
        log.info("Fetching customer by id: {}", id);
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        customer.setCreatedDate(LocalDateTime.now());
        log.info("Creating new customer: {}", customer);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(customerDetails.getName());
                    log.info("Updating customer with id: {}", id);
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public void deleteCustomer(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        log.info("Deleting customer with id: {}", id);
        customerRepository.deleteById(id);
    }
}
