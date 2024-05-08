package com.udacity.jdnd.course3.critter.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {
    @Query("Select p from Pet p where :ownerId = p.customer.id")
    List<Pet> getAllByOwnerId(Long ownerId);
}
