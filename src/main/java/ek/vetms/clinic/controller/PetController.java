package ek.vetms.clinic.controller;

import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("pets")
public class PetController {
    private final PetService service;

    @GetMapping("/list")
    public String listPets(Model model) {
        List<Pet> pets = service.findAll();
        model.addAttribute("pets", pets);
        return "pets/list-pets";
    }

    @GetMapping("/data/{petId}")
    @ResponseBody
    public ResponseEntity<Pet> getPetData(@PathVariable("petId") Long id) {
        Optional<Pet> optionalPet = service.findPetById(id);
        return optionalPet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> savePet(@RequestBody Pet pet) {
        service.savePet(pet);
        return ResponseEntity.ok("Pet saved successfully!");
    }

    @DeleteMapping("/delete/{petId}")
    @ResponseBody
    public ResponseEntity<String> deletePet(@PathVariable("petId") Long id) {
        service.deletePetById(id);
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
