package com.abc.employees.domain;

/**
 * Employee
 */
public record Employee(
        Long id,
        String firstName,
        String lastName,
        String dateOfBirth,
        String salary,
        String joinDate,
        String departement) {
}
