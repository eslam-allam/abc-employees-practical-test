package com.abc.employees.model;

import jakarta.validation.constraints.Min;

/**
 * EmployeeFilter
 */
public class EmployeeFilter {

    private String name;

    @Min(value = 0, message = "from salary must be a positive number")
    private String fromSalary;

    @Min(value = 0, message = "to salary must be a positive number")
    private String toSalary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromSalary() {
        return fromSalary;
    }

    public void setFromSalary(String fromSalary) {
        this.fromSalary = fromSalary;
    }

    public String getToSalary() {
        return toSalary;
    }

    public void setToSalary(String toSalary) {
        this.toSalary = toSalary;
    }

}
