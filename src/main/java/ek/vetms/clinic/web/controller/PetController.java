package ek.vetms.clinic.web.controller;

import ek.vetms.clinic.business.service.PetService;
import ek.vetms.clinic.model.Pet;
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


import java.util.Optional;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/pet")
public class PetController {
    private final PetService service;

    @GetMapping("/get/{id}")
    public ResponseEntity<Pet> getPetById(@NonNull @PathVariable("id") Long id){
        Optional<Pet> petToFind = service.findPetById(id);

        if (petToFind.isPresent()) {
            log.info("Pet with id {} found.", id);
            return ResponseEntity.ok(petToFind.get());
        } else {
            log.info("Pet with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Pet> savePet(@Valid @RequestBody Pet pet,
                                       BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("New pet entry has error: {}.", bindingResult);
            return ResponseEntity.badRequest().build();
        } else {
            Pet result = service.savePet(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Pet> editPetById(@NonNull @PathVariable("id") Long id,
                                           @RequestBody Pet pet){
        Optional<Pet> petToEdit = service.editPetById(id, pet);

        if (!pet.getId().equals(id)) {
            log.info("Given id {} does not match the id in the request body.", id);
            return ResponseEntity.badRequest().build();
        } else if (!petToEdit.isPresent()) {
            log.info("Pet with id {} not found.", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Pet with id {} updated.", id);
            return ResponseEntity.status(HttpStatus.CREATED).body(petToEdit.get());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePetById(@NonNull @PathVariable("id") Long id){
        Optional<Pet> petToDelete = service.deletePetById(id);

        if (petToDelete.isPresent()) {
            log.info("Pet with id {} has been deleted.", id);
            return ResponseEntity.ok().build();
        } else {
            log.info("Pet with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
