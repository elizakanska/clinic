package ek.vetms.clinic.tests.service.impl;

import ek.vetms.clinic.repository.VisitRepository;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.entity.Visit;
import ek.vetms.clinic.service.impl.PetServiceImpl;
import ek.vetms.clinic.service.impl.VisitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitServiceImplTest {
    @Mock
    PetServiceImpl petService;
    @Mock
    VisitRepository repository;
    @InjectMocks
    VisitServiceImpl visitService;
    private Visit visit;
    private Pet pet;

    @Test
    void testGetDataAnnotations() {
        assertNotNull(visitService.getRepository());
        assertNotNull(visitService.getPetService());
    }

    @BeforeEach
    public void init(){
        pet = new Pet(1L, "Test", "Test", 5);
        visit = new Visit(1L, pet, LocalDateTime.of(2024,3,1,9,0), "Test");
    }

    @Test
    void testFindAll() {
        List<Visit> visitList = Collections.singletonList(visit);
        when(repository.findAll()).thenReturn(visitList);

        List<Visit> result = visitService.findAll();

        assertEquals(visitList, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindVisitByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(petService.findPetById(1L)).thenReturn(Optional.of(pet));

        Optional<Visit> result = visitService.findVisitById(1L);

        assertTrue(result.isPresent());
        assertEquals(visit, result.get());
        assertEquals(pet, result.get().getPet());

        verify(repository, times(1)).findById(1L);
        verify(petService, times(1)).findPetById(1L);
    }
    @Test
    void testFindVisitByIdPetNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(petService.findPetById(visit.getPet().getId())).thenReturn(Optional.empty());

        Optional<Visit> result = visitService.findVisitById(1L);

        assertTrue(result.isEmpty());

        verify(repository, times(1)).findById(1L);
        verify(petService, times(1)).findPetById(anyLong());
    }
    @Test
    void testFindVisitByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Visit> result = visitService.findVisitById(1L);

        assertTrue(result.isEmpty());

        verify(repository, times(1)).findById(1L);
        verify(petService, never()).findPetById(anyLong());
    }

    @Test
    void testSaveVisitSuccess() {
        when(petService.findPetById(1L)).thenReturn(Optional.of(pet));
        when(repository.save(any(Visit.class))).thenReturn(visit);

        ResponseEntity<Visit> result = visitService.saveVisit(visit);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(visit, result.getBody());
        assertEquals(pet, result.getBody().getPet());

        verify(petService, times(1)).findPetById(1L);
        verify(repository, times(1)).save(visit);
    }
    @Test
    void testSaveVisitPetNotFound() {
        Visit visitWithNotFoundPet = new Visit(1L, new Pet(1L, "NotFound", "Pet", 5), LocalDateTime.of(2024, 3, 1, 9, 15), "Test");
        when(petService.findPetById(visitWithNotFoundPet.getPet().getId())).thenReturn(Optional.empty());

        ResponseEntity<Visit> result = visitService.saveVisit(visitWithNotFoundPet);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(petService).findPetById(visitWithNotFoundPet.getPet().getId());
        verify(repository, times(0)).save(any(Visit.class));
    }
    @Test
    void testSaveVisitNullTime() {
        Visit visitNullTime = new Visit(1L, pet, null, "Test");

        ResponseEntity<Visit> result = visitService.saveVisit(visitNullTime);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(repository, times(0)).save(any(Visit.class));
    }
    @Test
    void testSaveVisitBlankReason() {
        Visit visitBlankReason = new Visit(1L, pet, LocalDateTime.of(2024, 3, 1, 9, 15), "");

        ResponseEntity<Visit> result = visitService.saveVisit(visitBlankReason);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verify(repository, times(0)).save(any(Visit.class));
    }

    @Test
    void testEditVisitByIdSuccess() {
        Visit updatedVisit = new Visit(1L, pet, LocalDateTime.of(2024, 3, 1, 10, 0), "Updated Reason");

        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(petService.findPetById(pet.getId())).thenReturn(Optional.of(pet));
        when(repository.save(visit)).thenReturn(updatedVisit);

        ResponseEntity<Visit> result = visitService.editVisitById(1L, updatedVisit);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(updatedVisit, result.getBody());

        verify(repository, times(1)).save(visit);
    }
    @Test
    void testEditVisitByIdNotFound() {
        Visit updatedVisit = new Visit(1L, pet, LocalDateTime.of(2024, 3, 1, 10, 0), "Updated Reason");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Visit> result = visitService.editVisitById(1L, updatedVisit);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(repository, times(0)).save(any(Visit.class));
    }
    @Test
    void testEditVisitByIdPetNotFound() {
        Visit updatedVisit = new Visit(1L, pet, LocalDateTime.of(2024, 3, 1, 10, 0), "Updated Reason");

        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(petService.findPetById(updatedVisit.getPet().getId())).thenReturn(Optional.empty());  // Use updatedVisit instead of visit

        ResponseEntity<Visit> result = visitService.editVisitById(1L, updatedVisit);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(repository, times(1)).findById(1L);
        verify(petService, times(1)).findPetById(updatedVisit.getPet().getId());
        verify(repository, never()).save(any(Visit.class));
    }


    @Test
    void testDeleteVisitByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));

        boolean result = visitService.deleteVisitById(1L);

        assertTrue(result);

        verify(repository, times(1)).delete(visit);
    }
    @Test
    void testDeleteVisitByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean result = visitService.deleteVisitById(1L);

        assertFalse(result);

        verify(repository, times(0)).delete(any(Visit.class));
    }

}