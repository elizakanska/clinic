package ek.vetms.clinic.controller;

import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Controller
@RequestMapping("api/v3/pets")
public class PetController {
    private final PetService service;

    @GetMapping("/list")
    public String listPets(Model model){
        List<Pet> pets = service.findAll();
        model.addAttribute("pets", pets);

        return "pets/list-pets";
    }

    @GetMapping("/addForm")
    public String addForm(Model model){
        Pet pet = new Pet();

        model.addAttribute("pet", pet);

        return "pets/pet-form";
    }

    @GetMapping("/editForm")
    public String editForm(@RequestParam("petId") Long id, Model model){
        Optional<Pet> optionalPet = service.findPetById(id);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();
            model.addAttribute("pet", pet);
            return "pets/pet-form";
        } else {
            return "redirect:/petNotFound";
        }
    }

    @PostMapping("/save")
    public String savePet(@ModelAttribute("pet") Pet pet){
        service.savePet(pet);

        return "redirect:list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("petId") Long id){
        service.deletePetById(id);

        return "redirect:list";
    }
}
