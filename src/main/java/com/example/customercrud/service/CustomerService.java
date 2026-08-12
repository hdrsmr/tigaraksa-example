package com.example.customercrud.service;

import com.example.customercrud.entity.Customer;
import com.example.customercrud.repository.CustomerRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final HazelcastInstance hazelcastInstance;

    private final IMap<Integer, Customer> customerCache;

    private final IMap<String, List<Customer>> customerListCache;

    public CustomerService(CustomerRepository customerRepository, HazelcastInstance hazelcastInstance) {
        this.customerRepository = customerRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.customerCache = hazelcastInstance.getMap("customer-cache");
        this.customerListCache = hazelcastInstance.getMap("customer-list-cache");
    }

    public List<Customer> getAllCustomersHazel() {

        List<Customer> list = customerListCache.get("allData");
        if (list != null && !list.isEmpty()) {
            log.info("getAll data From Hazelcast");
            return list;
        }

        return null;
    }

    public void insertHazel() {
        log.info("getAll data From Database, Insert To Hazelcast");
        List<Customer> list = customerRepository.findAll();
        customerListCache.put(
                "allData",
                list,
                5,
                TimeUnit.MINUTES
        );


    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = customerRepository.findAll();
        return list;
    }


    public Optional<Customer> getCustomerById(Integer id) {

        // 1. Cek Hazelcast
        Customer customer = customerCache.get(id);

        if (customer != null) {
            log.info("GET customer FROM HAZELCAST {}", customer);
            return Optional.of(customer);
        }

        // 2. Tidak ada di Hazelcast
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        customer = customerOptional.get();

        log.info("GET customer FROM DATABASE {}", customer);

        // 3. Simpan ke Hazelcast selama 5 menit
        saveCustomerCache(customer);

        return Optional.of(customer);
    }

    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        Customer custom = customerRepository.save(customer);

        if (custom.getId() != null) {
            saveCustomerCache(custom);
        }


        return custom;
    }

    private void saveCustomerCache(Customer customer) {
        log.info("Save Customer {} To Hazelcast", customer.getName());
        customerCache.put(
                customer.getId(),
                customer,
                5,
                TimeUnit.MINUTES
        );


    }

    public Customer updateCustomer(Integer id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(customerDetails.getName());
                    log.info("Updating customer with id: {}", id);
                    Customer cus = customerRepository.save(customer);
                    saveCustomerCache(cus);
                    return cus;
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
