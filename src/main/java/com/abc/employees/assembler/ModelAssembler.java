package com.abc.employees.assembler;

import com.abc.employees.domain.Employee;
import com.abc.employees.model.EmployeeDto;

/**
 * ModelAssembler
 */
public class ModelAssembler {

    private ModelAssembler() {
    }

    public static Employee assemble(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .dateOfBirth(employeeDto.getDateOfBirth())
                .salary(employeeDto.getSalary())
                .joinDate(employeeDto.getJoinDate())
                .departement(employeeDto.getDepartement())
                .build();
    }
}
