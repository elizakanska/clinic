package ek.vetms.clinic.controller;

import ek.vetms.clinic.service.PetService;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.controller.errorHandling.NotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v2")
public class PetController {
    private final PetService service;
    @GetMapping("/pets")
    public List<Pet> findAll(){
        return service.findAll();
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> getPetById(@NonNull @PathVariable("id") Long id){
        return service.findPetById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Pet with id " + id + " not found."));
    }

    @PostMapping("/pets")
    public ResponseEntity<Pet> savePet(@Valid @RequestBody Pet pet) {
        return service.savePet(pet);
    }

    @PutMapping("/pets/{id}")
    public ResponseEntity<Pet> editPetById(@NonNull @PathVariable("id") Long id,@Valid @RequestBody Pet pet) {
        return service.editPetById(id, pet);
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<String> deletePetById(@NonNull @PathVariable("id") Long id) {
        service.deletePetById(id);
        return ResponseEntity.ok().build();
    }

}
