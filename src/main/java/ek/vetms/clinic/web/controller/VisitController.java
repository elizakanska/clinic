package ek.vetms.clinic.web.controller;

import ek.vetms.clinic.business.service.PetService;
import ek.vetms.clinic.business.service.VisitService;
import ek.vetms.clinic.model.Pet;
import ek.vetms.clinic.model.Visit;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.Objects;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/visit")
public class VisitController {
    private final VisitService visitService;
    private final PetService petService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Visit> getVisitById(@NonNull @PathVariable("id") Long id){
        Optional<Visit> visitToFind = visitService.findVisitById(id);

        if (visitToFind.isPresent()) {
            log.info("Visit with id {} found.", id);

            Visit visit = visitToFind.get();
            Optional<Pet> petDetails = petService.findPetById(visit.getPet().getId());

            if (petDetails.isPresent()) {
                log.info("Pet details found: {}", petDetails.get());
                visit.setPet(petDetails.get());
                return ResponseEntity.ok(visit);
            } else {
                log.info("Pet with id {} not found.", visit.getPet().getId());
                return ResponseEntity.notFound().build();
            }
        } else {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Visit> saveVisit(@Valid @RequestBody Visit visit,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("New visit entry has error: {}.", bindingResult);
            return ResponseEntity.badRequest().build();
        } else {
            Optional<Pet> pet = petService.findPetById(visit.getPet().getId());

            if (pet.isPresent()) {
                visit.setPet(pet.get());
                Visit result = visitService.saveVisit(visit);

                Optional<Pet> petDetails = petService.findPetById(visit.getPet().getId());

                if (petDetails.isPresent()) {
                    result.setPet(petDetails.get());
                    return ResponseEntity.status(HttpStatus.CREATED).body(result);
                } else {
                    log.info("Pet details not found for id {}.", visit.getPet().getId());
                    return ResponseEntity.notFound().build();
                }
            } else {
                log.info("Pet with id {} not found.", visit.getPet().getId());
                return ResponseEntity.notFound().build();
            }
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Visit> editVisitById(@NonNull @PathVariable("id") Long id,
                                               @RequestBody Visit updatedVisit) {
        if (!Objects.equals(updatedVisit.getId(), id)) {
            log.info("Given id {} does not match the id in the request body.", id);
            return ResponseEntity.badRequest().build();
        }

        Optional<Visit> visitToEdit = visitService.editVisitById(id, updatedVisit);

        if (!visitToEdit.isPresent()) {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        Optional<Pet> petDetails = petService.findPetById(updatedVisit.getPet().getId());

        if (petDetails.isPresent()) {
            Visit editedVisit = visitToEdit.get();
            editedVisit.setPet(petDetails.get());

            log.info("Visit with id {} updated.", id);
            return ResponseEntity.status(HttpStatus.CREATED).body(editedVisit);
        } else {
            log.info("Pet details not found for id {}.", updatedVisit.getPet().getId());
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVisitById(@NonNull @PathVariable("id") Long id){
        Optional<Visit> visitToDelete = visitService.deleteVisitById(id);

        if (visitToDelete.isPresent()) {
            log.info("Visit with id {} has been deleted.", id);
            return ResponseEntity.ok().build();
        } else {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}

