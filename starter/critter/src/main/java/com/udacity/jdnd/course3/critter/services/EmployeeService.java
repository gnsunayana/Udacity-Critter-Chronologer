package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repositories.EmployeesRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeesRepository employeesRepository;

    public Employee getEmployeeById(long employeeId){
        Employee employee =  employeesRepository.getOne(employeeId);
        if(employee !=null)
            return employee;
        throw new EntityNotFoundException("Employee with id '"+ employeeId +"' not found.");
    }

    public List<Employee> getEmployeesForService(LocalDate date, Set<EmployeeSkill> skills){
        List<Employee> employees = employeesRepository.getAllByDaysAvailableContains(date.getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
        return employees;
    }

    public Employee saveEmployee(Employee employee){
        return employeesRepository.save(employee);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable,long employeeId){

        Optional<Employee> employee = employeesRepository.findById(employeeId);
        if(employee.isPresent()) {
            employee.get().setDaysAvailable(daysAvailable);
            employeesRepository.save(employee.get());
        } else {
            throw new EntityNotFoundException("Employee with id '"+ employeeId +"' not found.");
        }

    }
}

