package ek.vetms.clinic.controller;

import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.entity.Visit;
import ek.vetms.clinic.service.PetService;
import ek.vetms.clinic.service.VisitService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("api/v3/visits")
public class VisitController2 {
    private final VisitService service;
    private final PetService petService;

    @GetMapping("/list")
    public String listVisits(Model model){
        List<Visit> visits = service.findAll();
        model.addAttribute("visits", visits);

        return "visits/list-visits";
    }

    @GetMapping("/addForm")
    public String addForm(Model model){
        List<Pet> pets = petService.findAll();
        Visit visit = new Visit();

        model.addAttribute("pets", pets);
        model.addAttribute("visit", visit);

        return "visits/visit-form";
    }

    @GetMapping("/editForm")
    public String editForm(@RequestParam("visitId") Long id, Model model){
        List<Pet> pets = petService.findAll();
        Optional<Visit> tempVisit = service.findVisitById(id);
        if (tempVisit.isPresent()) {
            Visit visit = tempVisit.get();
            model.addAttribute("pets", pets);
            model.addAttribute("visit", visit);
            return "visits/visit-form";
        } else {
            return "redirect:/visitNotFound";
        }
    }

    @PostMapping("/save")
    public String saveVisit(@ModelAttribute("visit") Visit visit){
        service.saveVisit(visit);

        return "redirect:list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("visitId") Long id){
        service.deleteVisitById(id);

        return "redirect:list";
    }
}
