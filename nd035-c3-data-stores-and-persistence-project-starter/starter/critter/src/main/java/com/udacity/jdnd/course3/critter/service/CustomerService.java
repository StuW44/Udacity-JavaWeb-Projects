package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.CustomerRepository;
import com.udacity.jdnd.course3.critter.data.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer save(Customer customer){ return customerRepository.save(customer);}
    public Customer findById(Long id) {return customerRepository.findById(id).orElse(null);}
    public List<Customer> list() { return customerRepository.findAll(); }
}
