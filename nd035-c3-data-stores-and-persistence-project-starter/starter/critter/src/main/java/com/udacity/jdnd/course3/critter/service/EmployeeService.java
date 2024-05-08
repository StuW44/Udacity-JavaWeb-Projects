package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    public Employee save(Employee employee){return employeeRepository.save(employee);}
    public Employee findById(Long id) {return employeeRepository.findById(id).orElse(null);}
    public List<Employee> list() { return employeeRepository.findAll(); }

    public  List<Employee> list(List<Long> ids) { return  employeeRepository.findAllById(ids);}
    public List<Employee> findByDay(DayOfWeek dayOfWeek) { return employeeRepository.findAllByDayOfWeek(dayOfWeek);}
    public List<Employee> findEmployeesWithSkill(List<Employee> employees, Set<EmployeeSkill> skills)
    {
        return employees.stream().
                filter(employee -> employee.getSkills().
                        containsAll(skills)).
                collect(Collectors.toList());
    }
}
