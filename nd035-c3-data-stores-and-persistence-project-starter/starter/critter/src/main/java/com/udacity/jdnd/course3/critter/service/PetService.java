package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.CustomerRepository;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;
    public Pet save(Pet pet)
    {
        Pet newPet = petRepository.save(pet);
        Customer customer = newPet.getCustomer();
        if(customer!=null) {
            List<Pet> custPets = customer.getPets();
            if (custPets == null)
                custPets = new ArrayList<Pet>();
            custPets.add(newPet);
            customer.setPets(custPets);
            customerRepository.save(customer);
        }
        return newPet;
    }
    public Pet findById(Long id)
    {
        return petRepository.findById(id).orElse(null);
    }
    public List<Pet> list() {return petRepository.findAll();}
    public List<Pet> list(List<Long> ids) {return petRepository.findAllById(ids);}
    public List<Pet> getByOwnerId(Long ownerId) {return petRepository.getAllByOwnerId(ownerId);}

}
