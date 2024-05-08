package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private long id;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<Employee> employees;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<Pet> pets;
    private LocalDate date;
    @ElementCollection
    private Set<EmployeeSkill> activities;
    @ElementCollection
    public Set<EmployeeSkill> getActivities() {
        return activities;
    }
    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public List<Pet> getPets() {
        return pets;
    }
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}