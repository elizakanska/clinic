package ek.vetms.clinic.service;

import ek.vetms.clinic.entity.Visit;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    List<Visit> findAll();
    Optional<Visit> findVisitById(Long id);
    ResponseEntity<Visit> saveVisit(Visit Visit);
    ResponseEntity<Visit> editVisitById(Long id, Visit Visit);
    boolean deleteVisitById(Long id);
}
