package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.data.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    public Schedule save(Schedule schedule) {return scheduleRepository.save(schedule);}
    public List<Schedule> list() { return scheduleRepository.findAll();}
    public List<Schedule> getScheduleForPets(Long petId) { return scheduleRepository.findByPets_Id(petId);}
    public List<Schedule> getScheduleForEmployees(Long employeeId) { return scheduleRepository.findByEmployees_Id(employeeId);}
    public List<Schedule> getScheduleForCustomer(Customer customer)
    {
        return customer.getPets().
                stream().
                flatMap(pet -> {
                    return getScheduleForPets(pet.getId()).
                            stream();
                }).collect(Collectors.toList());
    }
}
