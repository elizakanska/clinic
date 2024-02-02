package ek.vetms.clinic.controller;

import ek.vetms.clinic.service.VisitService;
import ek.vetms.clinic.entity.Visit;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
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


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v2")
public class VisitController {
    private final VisitService service;

    @GetMapping("/visits")
    public List<Visit> findAll(){return service.findAll();}

    @GetMapping("/visits/{id}")
    public ResponseEntity<Visit> getVisitById(@NonNull @PathVariable("id") Long id) {
        Optional<Visit> visitToFind = service.findVisitById(id);

        return visitToFind.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/visits")
    public ResponseEntity<Visit> saveVisit(@Valid @RequestBody Visit visit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("New visit entry has error: {}.", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return service.saveVisit(visit);
    }

    @PutMapping("/visits/{id}")
    public ResponseEntity<Visit> editVisitById(@NonNull @PathVariable("id") Long id,
                                               @RequestBody Visit updatedVisit) {
        if (!Objects.equals(updatedVisit.getId(), id)) {
            log.info("Given id {} does not match the id in the request body.", id);
            return ResponseEntity.badRequest().build();
        }
        return service.editVisitById(id, updatedVisit);
    }

    @DeleteMapping("/visits/{id}")
    public ResponseEntity<String> deleteVisitById(@NonNull @PathVariable("id") Long id){
        if (service.deleteVisitById(id)) {
            log.info("Visit with id {} has been deleted.", id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

