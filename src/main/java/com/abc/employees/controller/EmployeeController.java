package com.abc.employees.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.employees.assembler.DtoAssembler;
import com.abc.employees.assembler.ModelAssembler;
import com.abc.employees.domain.Employee;
import com.abc.employees.exception.EmployeeNotFoundException;
import com.abc.employees.model.EmployeeDto;
import com.abc.employees.model.EmployeeFilter;
import com.abc.employees.service.EmployeeService;

import jakarta.validation.Valid;

/**
 * EmployeeController
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto createEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws IOException {
        Long employeeId = this.employeeService.createEmployee(ModelAssembler.assemble(employeeDto));
        return DtoAssembler.assemble(Employee.builder().id(employeeId).build());
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto updateEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws IOException, EmployeeNotFoundException {
        Employee employee = ModelAssembler.assemble(employeeDto);
        this.employeeService.updateEmployee(employee);
        return DtoAssembler.assemble(employee);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto getEmployeeById(@PathVariable Long id) throws EmployeeNotFoundException, IOException {
        return DtoAssembler.assemble(this.employeeService.getEmployeeById(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> filterEmployees(@Valid EmployeeFilter filter) throws IOException {
        return DtoAssembler.assembleEmployees(this.employeeService.filterEmployees(filter));
    }
}
