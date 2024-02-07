package ek.vetms.clinic.service.impl;

import ek.vetms.clinic.repository.VisitRepository;
import ek.vetms.clinic.service.VisitService;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.entity.Visit;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Data
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {
    private final VisitRepository repository;
    private final PetServiceImpl petService;

    @Override
    public List<Visit> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Visit> findVisitById(Long id) {
        log.info("Looking for visit with id {}.", id);
        Optional<Visit> visitToFind = repository.findById(id);

        if (visitToFind.isPresent()) {
            log.info("Visit with id {} found.", id);

            Visit visit = visitToFind.get();
            Optional<Pet> petDetails = petService.findPetById(visit.getPet().getId());

            if (petDetails.isPresent()) {
                log.info("Pet details found: {}", petDetails.get());
                visit.setPet(petDetails.get());
                return Optional.of(visit);
            } else {
                log.info("Pet with id {} not found.", visit.getPet().getId());
                return Optional.empty();
            }
        } else {
            log.info("Visit with id {} not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<Visit> saveVisit(Visit visit) {
        Optional<Pet> pet = petService.findPetById(visit.getPet().getId());

        if (pet.isPresent()) {
            visit.setPet(pet.get());
            Visit savedVisit = repository.save(visit);
            log.info("New visit saved with id {}.", savedVisit.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVisit);
        } else {
            log.info("Pet with id {} not found.", visit.getPet().getId());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Visit> editVisitById(Long id, Visit visit) {
        log.info("Updating entry for visit with given id {}.", id);

        Optional<Visit> visitToEdit = repository.findById(id);

        if (visitToEdit.isEmpty()) {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        Optional<Pet> petDetails = petService.findPetById(visit.getPet().getId());

        if (petDetails.isEmpty()) {
            log.info("Pet with id {} not found.", visit.getPet().getId());
            return ResponseEntity.notFound().build();
        }

        Visit existingVisit = visitToEdit.get();
        existingVisit.setPet(visit.getPet());
        existingVisit.setTime(visit.getTime());
        existingVisit.setReason(visit.getReason());

        Visit updatedVisit = repository.save(existingVisit);
        log.info("Visit with id {} updated.", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedVisit);
    }

    @Transactional
    public boolean deleteVisitById(Long id) {
        log.info("Deleting visit with given id {}.", id);
        Optional<Visit> visitToDelete = repository.findById(id);

        if (visitToDelete.isPresent()) {
            Visit visit = visitToDelete.get();
            repository.delete(visit);
            return true;
        } else {
            log.info("Visit with id {} not found.", id);
            return false;
        }
    }
}