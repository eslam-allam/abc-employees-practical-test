package com.abc.employees.assembler;

import java.util.List;

import com.abc.employees.domain.Employee;
import com.abc.employees.model.EmployeeDto;

/**
 * DtoAssembler
 */
public class DtoAssembler {

    private DtoAssembler() {
    }

    public static EmployeeDto assemble(Employee employee) {
        return EmployeeDto.builder()
               .id(employee.id())
               .firstName(employee.firstName())
               .lastName(employee.lastName())
               .dateOfBirth(employee.dateOfBirth())
               .salary(employee.salary())
               .joinDate(employee.joinDate())
               .departement(employee.departement())
               .build();
    }

    public static List<EmployeeDto> assembleEmployees(List<Employee> employees) {
        return employees.stream()
               .map(DtoAssembler::assemble)
               .toList();
    }
}
