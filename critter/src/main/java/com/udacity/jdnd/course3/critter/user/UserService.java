package com.udacity.jdnd.course3.critter.user;
import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.stereotype.Service;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public UserService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void setAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee emp = getEmployeeById(employeeId);
        if (emp == null) {
            throw new RuntimeException("Employee not found: " + employeeId);
        }
        emp.setDaysAvailable(daysAvailable);
        employeeRepository.save(emp);
    }

    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        DayOfWeek day = date.getDayOfWeek();
        // load all employees and filter by availability+skills
        return employeeRepository.findAll().stream()
                .filter(e -> {
                    Set<EmployeeSkill> empSkills = e.getSkills();
                    Set<DayOfWeek> available = e.getDaysAvailable();
                    return available != null && available.contains(day)
                            && empSkills != null && empSkills.containsAll(skills);
                })
                .collect(Collectors.toList());
    }

    public Customer getOwnerByPetId(Long petId){
        Pet p = petRepository.findById(petId).orElse(null);
        return p==null?null:p.getOwner();
    }
}

