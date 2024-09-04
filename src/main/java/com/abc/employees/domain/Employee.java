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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder salary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder joinDate(String joinDate) {
            this.joinDate = joinDate;
            return this;
        }

        public Builder departement(String departement) {
            this.departement = departement;
            return this;
        }

        public Employee build() {
            return new Employee(id, firstName, lastName, dateOfBirth, salary, joinDate, departement);
        }
    }
}
