////package com.udacity.jdnd.course3.critter.pet;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////@Service
////public class PetService {
////
////    @Autowired
////    private PetRepository petRepository;
////
////    public Pet getPetById(long id) {
////        return petRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Pet not found with ID " + id));
////    }
////}
//
//
//package com.udacity.jdnd.course3.critter.pet;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class PetService {
//
//    private final PetRepository petRepository;
//
//    public PetService(PetRepository petRepository) {
//        this.petRepository = petRepository;
//    }
//
//    public Pet savePet(Pet pet) {
//        return petRepository.save(pet);
//    }
//
//    public Pet getPetById(long id) {
//        return petRepository.findById(id).orElse(null);
//    }
//
//    public List<Pet> getPetsByOwner(long ownerId) {
//        return petRepository.findByOwnerId(ownerId);
//    }
//
//    public List<Pet> getAllPets() {
//        return petRepository.findAll();
//    }
//}


package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired private CustomerRepository customerRepository;

    @Transactional
    public Pet savePet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer owner = savedPet.getOwner();
        owner.getPets().add(savedPet);
        customerRepository.save(owner);

        return savedPet;
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id " + petId));
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwnerId(Long ownerId) {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getOwner() != null && pet.getOwner().getId() == ownerId)
                .toList();
    }

    public List<Pet> getPetsByOwner(Customer owner) {
        return petRepository.findByOwner(owner);
    }


}

