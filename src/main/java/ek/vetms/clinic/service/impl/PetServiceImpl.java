package ek.vetms.clinic.service.impl;

import ek.vetms.clinic.controller.errorHandling.NotFoundException;
import ek.vetms.clinic.repository.PetRepository;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.service.PetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository repository;

    @Override
    public List<Pet> findAll() {return repository.findAllByOrderByNameAsc();}

    @Override
    public Optional<Pet> findPetById(Long id) {
        log.info("Looking for pet with id {}.", id);
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<Pet> savePet(Pet pet) {
        log.info("Saving new pet.");
        Pet savedPet = repository.save(pet);
        log.info("New pet saved with id {}.", savedPet.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }


    @Override
    public ResponseEntity<Pet> editPetById(Long id, Pet pet) {
        log.info("Updating entry for pet with given id {}.", id);

        Optional<Pet> petToEdit = repository.findById(id);

        if (!pet.getId().equals(id)) {
            log.info("Given id {} does not match the id in the request body.", id);
            return ResponseEntity.badRequest().build();
        } else if (petToEdit.isPresent()) {
            Pet existingPet = petToEdit.get();

            existingPet.setName(pet.getName());
            existingPet.setSpecies(pet.getSpecies());
            existingPet.setAge(pet.getAge());

            Pet updatedPet = repository.save(existingPet);
            log.info("Pet with id {} updated.", id);

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedPet);
        } else {
            log.info("Pet with id {} not found.", id);
            throw new NotFoundException("Pet with id " + id + " not found.");
        }
    }

    @Transactional
    public void deletePetById(Long id) {
        log.info("Deleting pet with given id {}.", id);
        Optional<Pet> petToDelete = repository.findById(id);

        petToDelete.ifPresent(pet -> {
            repository.delete(pet);
            log.info("Pet with id {} deleted.", id);
        });

        if (petToDelete.isEmpty()) {
            log.info("Pet with id {} not found.", id);
            throw new NotFoundException("Pet with id " + id + " not found.");
        }
    }
}
