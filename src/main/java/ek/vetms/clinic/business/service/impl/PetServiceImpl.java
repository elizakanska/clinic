package ek.vetms.clinic.business.service.impl;

import ek.vetms.clinic.business.mappers.PetMapper;
import ek.vetms.clinic.business.repository.PetRepository;
import ek.vetms.clinic.business.repository.model.PetDAO;
import ek.vetms.clinic.business.service.PetService;
import ek.vetms.clinic.model.Pet;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Data
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository repository;
    private final PetMapper mapper;

    @Override
    public Optional<Pet> findPetById(Long id) {
        log.info("Looking for pet with id {}.", id);
        return repository.findById(id)
                .flatMap((x -> Optional.ofNullable(mapper.petDAOtoPet(x))));
    }

    @Override
    public Pet savePet(Pet pet) throws IllegalArgumentException {
        if (pet == null) {
            log.error("Received null Pet object in savePet method.");
            throw new IllegalArgumentException("Pet object cannot be null.");
        }

        if (pet.getName().isBlank() || pet.getSpecies().isBlank()) {
            log.error("Failed to save pet. Blank name or species. Pet object: {}", pet);
            throw new IllegalArgumentException("Name and species cannot be blank.");
        }

        if (pet.getAge() == null) {
            log.error("Failed to save pet. Null age. Pet object: {}", pet);
            throw new IllegalArgumentException("Age cannot be null.");
        }

        log.info("Saving new pet.");
        PetDAO petToSave = repository.save(mapper.petToPetDAO(pet));

        log.info("New pet saved with id {}.", petToSave.getId());
        return mapper.petDAOtoPet(petToSave);
    }


    @Override
    public Optional<Pet> editPetById(Long id, Pet pet) {
        log.info("Updating entry for pet with given id {}.", id);
        Optional<PetDAO> petToEdit = repository.findById(id);

        if (petToEdit.isPresent()) {
            PetDAO existingPet = petToEdit.get();

            existingPet.setName(pet.getName());
            existingPet.setSpecies(pet.getSpecies());
            existingPet.setAge(pet.getAge());

            PetDAO updatedPet = repository.save(existingPet);
            return Optional.ofNullable(mapper.petDAOtoPet(updatedPet));
        } else {
            log.info("Pet with id {} not found.", id);
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Pet> deletePetById(Long id) {
        log.info("Deleting pet with given id {}.", id);
        Optional<PetDAO> petToDelete = repository.findById(id);

        if (petToDelete.isPresent()) {
            PetDAO petDAO = petToDelete.get();
            repository.delete(petDAO);
            return Optional.ofNullable(mapper.petDAOtoPet(petDAO));
        } else {
            log.info("Pet with id {} not found.", id);
            return Optional.empty();
        }
    }
}
