package com.example.sales.application.services;

import com.example.common.application.exceptions.CustomerNotFoundException;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public Customer createCustomer(String email) {
        String id = IdentifierFactory.nextId();
        String token = IdentifierFactory.nextId();
        return customerRepo.save(Customer.of(id, token, email));
    }

    public Customer retrieveCustomer(String token) throws CustomerNotFoundException {
        Customer customer = customerRepo.findByToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with token: " + token + " not found");
        }
        return customer;
    }
}
