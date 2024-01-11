package ek.vetms.clinic.business.service;

import ek.vetms.clinic.model.Visit;

import java.util.Optional;

public interface VisitService {
    Optional<Visit> findVisitById(Long id);
    Visit saveVisit(Visit Visit) throws IllegalArgumentException;
    Optional<Visit> editVisitById(Long id, Visit Visit);
    Optional<Visit> deleteVisitById(Long id);
}
