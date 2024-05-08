package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.EmployeeRepository;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertDTOToCustomer(customerDTO);
        customer= customerService.save(customer);
        return convertCustomerToDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.list().stream().
                map(this::convertCustomerToDTO).
                collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
       Pet pet = petService.findById(petId);
       return  convertCustomerToDTO(customerService.findById(pet.getCustomer().getId()));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO)
    {
        Employee employee = convertDTOToEmployee(employeeDTO);
        employee = employeeService.save(employee);
        return convertEmployeeToDTO(employee);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId)
    {
        Employee employee = employeeService.findById(employeeId);
        return convertEmployeeToDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        employee.setDaysOfWeekAvailable(daysAvailable);
        employeeService.save(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findByDay(employeeDTO.getDate().getDayOfWeek());
        return employeeService.findEmployeesWithSkill(employees,employeeDTO.getSkills()).
                stream().
                map(this::convertEmployeeToDTO).
                collect(Collectors.toList());
    }
    private CustomerDTO convertCustomerToDTO(Customer customer)
    {
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer,dto);
        if(customer.getPets()!=null) {
            List<Long> petIds = customer.getPets().stream().
                    map(Pet::getId).
                    collect(Collectors.toList());
            dto.setPetIds(petIds);
        }
        return  dto;
    }
    private Customer convertDTOToCustomer(CustomerDTO dto)
    {
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto,customer);
        if(dto.getPetIds()!=null) {
            customer.setPets(petService.list(dto.getPetIds()));
        }
        return customer;
    }
    private EmployeeDTO convertEmployeeToDTO(Employee employee)
    {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee,dto);
        dto.setDaysAvailable(employee.getDaysOfWeekAvailable());
        return dto;
    }
    private Employee convertDTOToEmployee(EmployeeDTO dto)
    {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto,employee);
        employee.setDaysOfWeekAvailable(dto.getDaysAvailable());
        return employee;
    }
}
