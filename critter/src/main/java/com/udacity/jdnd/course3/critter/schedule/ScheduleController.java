package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        Schedule schedule = new Schedule();
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        List<Employee> employees = scheduleDTO.getEmployeeIds().stream()
                .map(id -> userService.getEmployeeById(id))
                .toList();
        schedule.setEmployees(employees);

        List<Pet> pets = scheduleDTO.getPetIds().stream()
                .map(id -> petService.getPetById(id))
                .toList();
        schedule.setPets(pets);

        Schedule savedSchedule = scheduleService.saveSchedule(schedule);

        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(savedSchedule.getId());
        dto.setDate(savedSchedule.getDate());
        dto.setActivities(savedSchedule.getActivities());
        dto.setEmployeeIds(employees.stream().map(Employee::getId).toList());
        dto.setPetIds(pets.stream().map(Pet::getId).toList());

        return dto;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream()
                .map(this::convertScheduleToDTO)
                .toList();
    }


    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.getScheduleForPet(petId).stream()
                .map(this::convertScheduleToDTO)
                .toList();
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.getScheduleForEmployee(employeeId).stream()
                .map(this::convertScheduleToDTO)
                .toList();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = userService.getCustomerById(customerId);

        List<Long> petIds = customer.getPets() != null
                ? customer.getPets().stream().map(Pet::getId).toList()
                : List.of(); // empty list if no pets

        List<Schedule> schedules = petIds.stream()
                .flatMap(id -> scheduleService.getScheduleForPet(id).stream())
                .distinct()
                .toList();

        return schedules.stream()
                .map(this::convertScheduleToDTO)
                .toList();
    }

    private ScheduleDTO convertScheduleToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setActivities(schedule.getActivities());
        dto.setEmployeeIds(schedule.getEmployees().stream().map(e -> e.getId()).toList());
        dto.setPetIds(schedule.getPets().stream().map(p -> p.getId()).toList());
        return dto;
    }
}




//    @PostMapping
//    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
//        throw new UnsupportedOperationException();
//    }


//    @GetMapping
//    public List<ScheduleDTO> getAllSchedules() {
//        throw new UnsupportedOperationException();
//    }


//    @GetMapping("/pet/{petId}")
//    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/employee/{employeeId}")
//    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/customer/{customerId}")
//    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
//        throw new UnsupportedOperationException();
//    }


//    @GetMapping("/customer/{customerId}")
//    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
//        // 1. Get all pets for the customer
//        Customer customer = userService.getCustomerById(customerId);
//        List<Long> petIds = customer.getPets().stream()
//                .map(Pet::getId)
//                .toList();
//
//        // 2. Get all schedules that include any of the customer's pets
//        List<Schedule> schedules = petIds.stream()
//                .flatMap(id -> scheduleService.getScheduleForPet(id).stream())
//                .distinct()
//                .toList();
//
//        // 3. Convert to DTOs
//        return schedules.stream()
//                .map(this::convertScheduleToDTO)
//                .toList();
//    }

