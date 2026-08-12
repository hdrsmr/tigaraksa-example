package com.example.customercrud.controller;

import com.example.customercrud.entity.Customer;
import com.example.customercrud.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer CRUD operations")
@Log4j2
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping("/testInsertHazel")
    public void test() {

        long startTime = System.nanoTime();
        customerService.insertHazel();
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        log.info("Get InsertHazel membutuhkan waktu: {} ms", durationMs);
    }

    @GetMapping("/getAllFromDB")
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        long startTime = System.currentTimeMillis();
        List<Customer> datas = customerService.getAllCustomers();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Get all customers DB membutuhkan waktu: {} ms", duration);

        return ResponseEntity.ok(datas);
    }


    @GetMapping("/getAllFromHazelCast")
    @Operation(summary = "Get all customers Hazelcast")
    public ResponseEntity<List<Customer>> getAllCustomersHazel() {
        long startTime = System.currentTimeMillis();
        List<Customer> datas = customerService.getAllCustomersHazel();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Get all customers HAZELCAST membutuhkan waktu: {} ms", duration);


        return ResponseEntity.ok(datas);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by id")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by id")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
