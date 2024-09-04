package com.abc.employees.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * EmployeeDto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = EmployeeDto.Builder.class)
public class EmployeeDto {
    
    private Long id;

    @NotBlank(message = "first name cannot be empty")
    private String firstName;

    @NotBlank(message = "last name cannot be empty")
    private String lastName;

    @NotBlank(message = "date of birth cannot be empty")
    private String dateOfBirth;

    @Min(value = 0, message = "salary must be a positive number")
    private String salary;

    @NotBlank(message = "join date cannot be empty")
    private String joinDate;

    @NotBlank(message = "department cannot be empty")
    private String departement;

    private EmployeeDto(EmployeeDto.Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.dateOfBirth = builder.dateOfBirth;
        this.salary = builder.salary;
        this.joinDate = builder.joinDate;
        this.departement = builder.departement;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSalary() {
        return salary;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public String getDepartement() {
        return departement;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String salary;
        private String joinDate;
        private String departement;

        @JsonSetter
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        @JsonSetter
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        @JsonSetter
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @JsonSetter
        public Builder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        @JsonSetter
        public Builder salary(String salary) {
            this.salary = salary;
            return this;
        }

        @JsonSetter
        public Builder joinDate(String joinDate) {
            this.joinDate = joinDate;
            return this;
        }

        @JsonSetter
        public Builder departement(String departement) {
            this.departement = departement;
            return this;
        }

        public EmployeeDto build() {
            return new EmployeeDto(this);
        }
    }
}
