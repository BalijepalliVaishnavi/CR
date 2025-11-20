package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import com.udacity.jdnd.course3.critter.user.UserService;
/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        Customer savedCustomer = userService.saveCustomer(customer);
        CustomerDTO dto = new CustomerDTO();
        dto.setId(savedCustomer.getId());
        dto.setName(savedCustomer.getName());
        dto.setPhoneNumber(savedCustomer.getPhoneNumber());
        dto.setNotes(savedCustomer.getNotes());
        return dto;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = userService.getAllCustomers();
        return customers.stream().map(customer -> {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setPhoneNumber(customer.getPhoneNumber());
            dto.setNotes(customer.getNotes());
            if (customer.getPets() != null) {
                List<Long> petIds = customer.getPets().stream()
                        .map(pet -> pet.getId())
                        .toList();
                dto.setPetIds(petIds);
            }
            return dto;
        }).toList();
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        Customer owner = pet.getOwner();
        CustomerDTO dto = new CustomerDTO();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setPhoneNumber(owner.getPhoneNumber());
        dto.setNotes(owner.getNotes());
        if (owner.getPets() != null) {
            List<Long> petIds = owner.getPets().stream()
                    .map(Pet::getId)
                    .toList();
            dto.setPetIds(petIds);
        }
        return dto;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = dtoToEmployee(employeeDTO);
        Employee saved = userService.saveEmployee(employee);
        return employeeToDTO(saved);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.getEmployeeById(employeeId);
        return employeeToDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO requestDTO) {
        LocalDate date = requestDTO.getDate();
        Set<EmployeeSkill> skills = requestDTO.getSkills();
        List<Employee> matches = userService.findEmployeesForService(date, skills);
        return matches.stream().map(this::employeeToDTO).collect(Collectors.toList());
    }


    private Employee dtoToEmployee(EmployeeDTO dto) {
        Employee e = new Employee();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setSkills(dto.getSkills());
        e.setDaysAvailable(dto.getDaysAvailable());
        return e;
    }

    private EmployeeDTO employeeToDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setSkills(e.getSkills());
        dto.setDaysAvailable(e.getDaysAvailable());
        return dto;
    }

}

//    @PostMapping("/customer")
//    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/customer")
//    public List<CustomerDTO> getAllCustomers(){
//        throw new UnsupportedOperationException();
//    }
//
//    @GetMapping("/customer/pet/{petId}")
//    public CustomerDTO getOwnerByPet(@PathVariable long petId){
//        throw new UnsupportedOperationException();
//    }

//    @PostMapping("/employee")
//    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
//        throw new UnsupportedOperationException();
//    }

//    @PostMapping("/employee/{employeeId}")
//    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
//        throw new UnsupportedOperationException();
//    }

//    @PutMapping("/employee/{employeeId}")
//    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/employee/availability")
//    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
//        throw new UnsupportedOperationException();
//    }