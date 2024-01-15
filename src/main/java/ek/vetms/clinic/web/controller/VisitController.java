package ek.vetms.clinic.web.controller;

import ek.vetms.clinic.business.service.VisitService;
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


import java.util.Optional;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/status")
public class VisitController {
    private final VisitService service;

    @GetMapping("/getVisit/{id}")
    public ResponseEntity<Visit> getVisitById(@NonNull @PathVariable("id") Long id){
        Optional<Visit> visitToFind = service.findVisitById(id);

        if (visitToFind.isPresent()) {
            log.info("Visit with id {} found.", id);
            return ResponseEntity.ok(visitToFind.get());
        } else {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/newVisit")
    public ResponseEntity<Visit> saveVisit(@Valid @RequestBody Visit visit,
                                       BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("New visit entry has error: {}.", bindingResult);
            return ResponseEntity.badRequest().build();
        } else {
            Visit result = service.saveVisit(visit);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }

    @PutMapping("/editVisit/{id}")
    public ResponseEntity<Visit> editVisitById(@NonNull @PathVariable("id") Long id,
                                           @RequestBody Visit visit){
        Optional<Visit> visitToEdit = service.editVisitById(id, visit);

        if (!visit.getId().equals(id)) {
            log.info("Given id {} does not match the id in the request body.", id);
            return ResponseEntity.badRequest().build();
        } else if (!visitToEdit.isPresent()) {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("Visit with id {} updated.", id);
            return ResponseEntity.status(HttpStatus.CREATED).body(visitToEdit.get());
        }
    }

    @DeleteMapping("/deleteVisit/{id}")
    public ResponseEntity<String> deleteVisitById(@NonNull @PathVariable("id") Long id){
        Optional<Visit> visitToDelete = service.deleteVisitById(id);

        if (visitToDelete.isPresent()) {
            log.info("Visit with id {} has been deleted.", id);
            return ResponseEntity.ok().build();
        } else {
            log.info("Visit with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

}
