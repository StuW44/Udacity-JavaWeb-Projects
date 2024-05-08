package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
       Schedule schedule = convertDTOToSchedule(scheduleDTO);
       return convertScheduleToDTO(scheduleService.save(schedule));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList=scheduleService.list();
        return scheduleList.stream().map(
                this::convertScheduleToDTO
                ).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList=scheduleService.getScheduleForPets(petId);
        return scheduleList.stream().
                map(this::convertScheduleToDTO).
                collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList=scheduleService.getScheduleForEmployees(employeeId);
        return scheduleList.stream().
                map(this::convertScheduleToDTO).
                collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer=customerService.findById(customerId);

        return scheduleService.getScheduleForCustomer(customer).
                stream().
                map(this::convertScheduleToDTO).
                collect(Collectors.toList());
    }
    private ScheduleDTO convertScheduleToDTO(Schedule schedule)
    {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule,dto);
        if(schedule.getPets()!=null)
            dto.setPetIds(schedule.getPets().
                      stream().
                      map(Pet::getId).collect(Collectors.toList()));
        if(schedule.getEmployees()!=null)
            dto.setEmployeeIds(schedule.getEmployees().
                      stream().
                      map(Employee::getId).
                      collect(Collectors.toList()));
        return  dto;
    }
    private Schedule convertDTOToSchedule(ScheduleDTO dto)
    {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(dto,schedule);
        if(dto.getPetIds()!=null)
            schedule.setPets(petService.list(dto.getPetIds()));
        if(dto.getEmployeeIds()!=null)
            schedule.setEmployees(employeeService.list(dto.getEmployeeIds()));
        return schedule;
    }
}
