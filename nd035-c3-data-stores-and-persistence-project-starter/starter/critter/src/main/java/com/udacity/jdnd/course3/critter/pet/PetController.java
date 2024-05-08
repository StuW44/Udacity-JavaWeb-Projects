package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertDTOToPet(petDTO);
        Customer owner = customerService.findById(petDTO.getOwnerId());
        pet.setCustomer(owner);
        pet= petService.save(pet);
        return convertPetToDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToDTO(petService.findById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.list();
        return pets.stream().map(this::convertPetToDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getByOwnerId(ownerId);
        return  pets.stream().
                    map(this::convertPetToDTO).
                    collect(Collectors.toList());
    }
    private PetDTO convertPetToDTO(Pet pet)
    {
        PetDTO dto = new PetDTO();
        BeanUtils.copyProperties(pet,dto);
        if(pet.getCustomer() !=null)
              dto.setOwnerId(pet.getCustomer().getId());
        return  dto;
    }
    private Pet convertDTOToPet(PetDTO dto)
    {
        Pet pet = new Pet();
        BeanUtils.copyProperties(dto,pet);
        Customer cust=customerService.findById(dto.getOwnerId());
        pet.setCustomer(cust);
        return pet;
    }
}
