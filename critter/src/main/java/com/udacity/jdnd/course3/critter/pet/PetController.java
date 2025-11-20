package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        return convertPetToDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer owner = userService.getCustomerById(ownerId);
        List<Pet> pets = petService.getPetsByOwner(owner);

        return pets.stream()
                .map(this::convertPetToDTO)
                .toList();
    }

    private PetDTO convertPetToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());
        dto.setOwnerId(pet.getOwner().getId());
        return dto;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        Customer owner = userService.getCustomerById(petDTO.getOwnerId());
        pet.setOwner(owner);
        Pet savedPet = petService.savePet(pet);

        owner.getPets().add(savedPet);
        userService.saveCustomer(owner);
        return convertPetToDTO(savedPet);
    }
}

//    @PostMapping
//    public PetDTO savePet(@RequestBody PetDTO petDTO) {
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/{petId}")
//    public PetDTO getPet(@PathVariable long petId) {
//        throw new UnsupportedOperationException();
//    }

//    @GetMapping("/owner/{ownerId}")
//    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
//        throw new UnsupportedOperationException();
//    }