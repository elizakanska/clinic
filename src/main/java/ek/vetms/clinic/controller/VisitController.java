package ek.vetms.clinic.controller;

import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.entity.Visit;
import ek.vetms.clinic.service.PetService;
import ek.vetms.clinic.service.VisitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("visits")
public class VisitController {
    private final VisitService service;
    private final PetService petService;

    @GetMapping("/list")
    public String listVisits(Model model) {
        List<Visit> visits = service.findAll();
        model.addAttribute("visits", visits);
        return "visits/list-visits";
    }

    @GetMapping("/data/{visitId}")
    @ResponseBody
    public ResponseEntity<Visit> getVisitData(@PathVariable("visitId") Long id) {
        Optional<Visit> tempVisit = service.findVisitById(id);
        return tempVisit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pets")
    @ResponseBody
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.findAll();
        return ResponseEntity.ok(pets);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveVisit(@RequestBody Visit visit) {
        service.saveVisit(visit);
        return ResponseEntity.ok("Visit saved successfully!");
    }

    @DeleteMapping("/delete/{visitId}")
    @ResponseBody
    public ResponseEntity<String> deleteVisit(@PathVariable("visitId") Long id) {
        service.deleteVisitById(id);
        return ResponseEntity.ok("Visit deleted successfully!");
    }
}
