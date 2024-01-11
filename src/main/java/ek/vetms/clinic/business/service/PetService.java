package ek.vetms.clinic.business.service;

import ek.vetms.clinic.model.Pet;

import java.util.Optional;

public interface PetService {
    Optional<Pet> findPetById(Long id);
    Pet savePet(Pet pet) throws IllegalArgumentException;
    Optional<Pet> editPetById(Long id, Pet pet);
    Optional<Pet> deletePetById(Long id);
}
