package com.abc.employees.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abc.employees.domain.Employee;
import com.abc.employees.exception.EmployeeNotFoundException;
import com.abc.employees.model.EmployeeFilter;
import com.abc.employees.repository.EmployeeRepository;

/**
 * EmployeeService
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Long createEmployee(Employee employee) throws IOException {
        if (employee.id() != null) {
            throw new IllegalArgumentException("cannot create an employee with an existing id");
        }
        return employeeRepository.save(employee);
    }

    public Long updateEmployee(Employee employee) throws IOException, EmployeeNotFoundException {
        if (employee.id() == null) {
            throw new EmployeeNotFoundException("cannot update an employee without an id");
        }
        this.employeeRepository.findById(employee.id()).orElseThrow(() -> new IllegalArgumentException("The provided id does not exist"));
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) throws IOException, EmployeeNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("the provided id does not exist"));
    }

    public List<Employee> filterEmployees(EmployeeFilter filter) throws IOException {
        return this.employeeRepository.findByFilter(filter);
    }
}
