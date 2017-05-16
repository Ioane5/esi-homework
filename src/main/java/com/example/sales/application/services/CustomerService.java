package com.example.sales.application.services;

import com.example.common.application.exceptions.CustomerNotFoundException;
import com.example.common.application.exceptions.UniqueCustomerViolationException;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public Customer createCustomer(String email) throws UniqueCustomerViolationException {
        String id = IdentifierFactory.nextId();
        String token = IdentifierFactory.nextId();
        try {
            return customerRepo.save(Customer.of(id, token, email));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueCustomerViolationException("Customer email should be unique");
        }
    }

    public Customer retrieveCustomer(String token) throws CustomerNotFoundException {
        Customer customer = customerRepo.findByToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with token: " + token + " not found");
        }
        return customer;
    }
}
