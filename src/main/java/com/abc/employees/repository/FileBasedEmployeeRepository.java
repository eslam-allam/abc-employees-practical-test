package com.abc.employees.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.abc.employees.domain.Employee;
import com.abc.employees.model.EmployeeFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * EmployeeRepositoryImpl
 */
@Component
public class FileBasedEmployeeRepository implements EmployeeRepository {

    private final File repoDataFile;
    private final AtomicLong lastId;
    private final ReadWriteLock dbLock;
    private ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(FileBasedEmployeeRepository.class);

    private boolean createDataFile(Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            logger.error("Failed to create intermediate directories for data file", e);
            return false;
        }
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            logger.error("failed to create data file", e);
            return false;
        }
        return true;
    }

    private Long getLastDataId() {
        Long highestId = 0L;
        try (
                FileReader reader = new FileReader(this.repoDataFile);
                BufferedReader bufferedReader = new BufferedReader(reader);) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                highestId = getEmployeeIdFromJson(highestId, currentLine);
            }
            return highestId;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read data file", e);
        }
    }

    private Long getEmployeeIdFromJson(Long lastId, String currentLine) {
        try {
            Employee employee = mapper.readValue(currentLine, Employee.class);
            if (employee.id() == null) {
                throw new IllegalStateException("Found employee with missing id in data file");
            }
            if (employee.id() > lastId) {
                lastId = employee.id();
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Found invalid employee entry in data file");
        }
        return lastId;
    }

    public FileBasedEmployeeRepository(@Value("${spring.data.fs.path}") String dataPath) {
        if (dataPath == null || dataPath.isBlank()) {
            throw new IllegalArgumentException("No data path provided");
        }
        if (!dataPath.endsWith(".jsonl")) {
            throw new IllegalArgumentException("Only jsonl is supported for data path");
        }
        File dataFile = new File(dataPath);
        if (dataFile.exists()) {
            if (!dataFile.isFile()) {
                throw new IllegalStateException("Data path does not point to a file");
            }
            if (!dataFile.canRead()) {
                throw new IllegalStateException("Data file exists but is not readable");
            }
            if (!dataFile.canWrite()) {
                throw new IllegalStateException("Data file exists but is not writeable");
            }
        } else if (!createDataFile(dataFile.toPath())) {
            throw new IllegalStateException("Could not create data file");
        }
        this.repoDataFile = dataFile;
        this.lastId = new AtomicLong(getLastDataId());
        this.dbLock = new ReentrantReadWriteLock();
    }

    private List<Employee> filterDB(Predicate<Employee> predicate, boolean findone) throws IOException {
        this.dbLock.readLock().lock();
        List<Employee> employees = new ArrayList<>();
        try (
                FileReader reader = new FileReader(this.repoDataFile);
                BufferedReader bufferedReader = new BufferedReader(reader);) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                try {
                    Employee employee = mapper.readValue(currentLine, Employee.class);
                    if (employee.id() == null) {
                        throw new IllegalStateException("Found employee with missing id in data file");
                    }
                    if (predicate.test(employee)) {
                        employees.add(employee);
                        if (findone) {
                            return employees;
                        }
                    }
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException("Found invalid employee entry in data file");
                }
            }
            return employees;
        } finally {
            this.dbLock.readLock().unlock();
        }
    }

    @Override
    public Optional<Employee> findById(Long id) throws IOException {
        List<Employee> employees = filterDB(e -> e.id().equals(id), true);
        if (employees.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(employees.getFirst());
    }

    private boolean overwriteEmployee(Employee employee) throws IOException {
        this.dbLock.writeLock().lock();
        Path tmpFile = this.repoDataFile.toPath().resolveSibling(this.repoDataFile.getName() + ".tmp");
        Files.createFile(tmpFile);
        boolean found = false;
        if (employee.id() == null) {
            throw new IllegalStateException("cannot overwrite employee with null id");
        }
        try (
                FileReader reader = new FileReader(this.repoDataFile);
                BufferedReader bufferedReader = new BufferedReader(reader);
                BufferedWriter writer = Files.newBufferedWriter(tmpFile)) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                try {
                    Employee dbEmployee = mapper.readValue(currentLine, Employee.class);
                    if (employee.id() == null) {
                        throw new IllegalStateException("Found employee with missing id in data file");
                    }
                    if (dbEmployee.id().equals(employee.id())) {
                        writer.write(mapper.writeValueAsString(employee));
                        writer.newLine();
                        found = true;
                    } else {
                        writer.write(currentLine);
                        writer.newLine();
                    }
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException("Found invalid employee entry in data file");
                }
            }
            writer.flush();
            Files.move(tmpFile, this.repoDataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            this.dbLock.writeLock().unlock();
        }

        return found;
    }

    @Override
    public Long save(Employee employee) throws IOException {
        if (employee.id() == null) {
            Long newId = this.lastId.incrementAndGet();
            Employee newEmployee = new Employee(newId, employee.firstName(),
                    employee.lastName(), employee.dateOfBirth(),
                    employee.salary(), employee.joinDate(), employee.departement());
            this.dbLock.writeLock().lock();
            try (

                    BufferedWriter writer = Files.newBufferedWriter(this.repoDataFile.toPath(),
                            StandardOpenOption.APPEND);) {
                writer.write(mapper.writeValueAsString(newEmployee));
                writer.newLine();
            } finally {
                this.dbLock.writeLock().unlock();
            }
            return newId;
        }
        if (!overwriteEmployee(employee)) {
            throw new UnexpectedException("failed to update employee data");
        }
        return employee.id();
    }

    @Override
    public List<Employee> findByFilter(EmployeeFilter filter) throws IOException {
        List<Predicate<Employee>> filters = new ArrayList<>();
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            filters.add(e -> e.firstName().toLowerCase(Locale.ROOT).contains(filter.getName().toLowerCase(Locale.ROOT))
                    || e.lastName().toLowerCase(Locale.ROOT).contains(filter.getName().toLowerCase(Locale.ROOT)));
        }
        if (filter.getFromSalary() != null && !filter.getFromSalary().isEmpty()) {
            filters.add(
                    e -> e.salary().transform(Double::valueOf) >= filter.getFromSalary().transform(Double::valueOf));
        }
        if (filter.getToSalary() != null && !filter.getToSalary().isEmpty()) {
            filters.add(e -> e.salary().transform(Double::valueOf) <= filter.getToSalary().transform(Double::valueOf));
        }
        Predicate<Employee> combinedFilter = filters.stream().reduce(Predicate::and).orElse(e -> true);
        return filterDB(combinedFilter, false);
    }

}
