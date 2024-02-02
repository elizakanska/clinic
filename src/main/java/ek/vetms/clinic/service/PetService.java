package ek.vetms.clinic.service;

import ek.vetms.clinic.entity.Pet;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface PetService {
    List<Pet> findAll();
    Optional<Pet> findPetById(Long id);
    ResponseEntity<Pet> savePet(Pet pet);
    ResponseEntity<Pet> editPetById(Long id, Pet pet);
    void deletePetById(Long id);
}
