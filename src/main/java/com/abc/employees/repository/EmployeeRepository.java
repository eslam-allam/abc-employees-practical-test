package com.abc.employees.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.abc.employees.domain.Employee;
import com.abc.employees.model.EmployeeFilter;

/**
 * EmployeeRepository
 */
@Repository
public interface EmployeeRepository {

    Optional<Employee> findById(Long id) throws IOException;

    Long save(Employee employee) throws IOException;

    List<Employee> findByFilter(EmployeeFilter filter) throws IOException;
}
