package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByPetsContains(Pet pet);

    List<Schedule> findByEmployeesContains(Employee employee);

    List<Schedule> findByEmployeesId(Long employeeId);
    List<Schedule> findByPetsId(Long petId);
    List<Schedule> findByPetsOwnerId(Long ownerId);
}
