package ek.vetms.clinic.business.service.impl;

import ek.vetms.clinic.business.mappers.VisitMapper;
import ek.vetms.clinic.business.repository.VisitRepository;
import ek.vetms.clinic.business.repository.model.VisitDAO;
import ek.vetms.clinic.model.Pet;
import ek.vetms.clinic.model.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class VisitServiceImplTest {
    @Mock
    PetServiceImpl petService;
    @Mock
    VisitRepository visitRepository;
    @Mock
    VisitMapper mapper;
    @InjectMocks
    VisitServiceImpl visitService;
    private Visit visit;
    private VisitDAO visitDAO;
    private Pet pet;

    @BeforeEach
    public void init(){
        pet = new Pet(1L, "Test", "Test", 5);
        visit = new Visit(1L, pet, "2024-03-01 9:15", "Test");
        visitDAO = new VisitDAO(1L, 1L, LocalDateTime.of(2024,3,1,9,15), "Test");
    }

    @Test
    void testFindVisitByIdSuccess(){
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visitDAO));
        when(mapper.visitDAOtoVisit(visitDAO)).thenReturn(visit);

        assertEquals(Optional.of(visit), visitService.findVisitById(1L));

        verify(visitRepository, times(1)).findById(anyLong());
    }
    @Test
    void testFindVisitByIdNotFound(){
        when(visitRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), visitService.findVisitById(1L));

        verify(visitRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveVisitSuccess(){
        when(petService.savePet(any())).thenReturn(pet);

        when(mapper.visitToVisitDAO(visit)).thenReturn(visitDAO);
        when(visitRepository.save(visitDAO)).thenReturn(visitDAO);
        when(mapper.visitDAOtoVisit(visitDAO)).thenReturn(visit);

        assertEquals(visit, visitService.saveVisit(visit));

        verify(mapper, times(1)).visitToVisitDAO(visit);
        verify(visitRepository, times(1)).save(visitDAO);
        verify(mapper, times(1)).visitDAOtoVisit(visitDAO);
    }
    @Test
    void testSaveVisitNullPet(){
        Visit visitNullPet = new Visit(1L, null, "2024-03-01 9:15", "Test");

        assertThrows(IllegalArgumentException.class, () -> visitService.saveVisit(visitNullPet));

        verify(visitRepository, times(0)).save(visitDAO);
    }
    @Test
    void testSaveVisitBlankTime(){
        Visit visitBlankTime = new Visit(1L, pet, " ", "Test");

        assertThrows(IllegalArgumentException.class, () -> visitService.saveVisit(visitBlankTime));

        verify(visitRepository, times(0)).save(visitDAO);
    }
    @Test
    void testSaveVisitBlankReason(){
        Visit visitBlankReason = new Visit(1L, pet, "2024-03-01 9:15", " ");

        assertThrows(IllegalArgumentException.class, () -> visitService.saveVisit(visitBlankReason));

        verify(visitRepository, times(0)).save(visitDAO);
    }
    @Test
    void testSaveVisitNull(){
        assertThrows(IllegalArgumentException.class, () -> visitService.saveVisit(null));

        verify(visitRepository, times(0)).save(visitDAO);
    }

    @Test
    void testEditVisitByIdSuccess(){
        Visit updatedVisit = new Visit(1L, pet, "2024-03-01 9:30", "Test");
        VisitDAO updatedVisitDAO = new VisitDAO(1L, 1L, LocalDateTime.of(2024,3,1,9,30), "Test");

        when(visitRepository.findById(1L)).thenReturn(Optional.of(updatedVisitDAO));
        when(visitRepository.save(any(VisitDAO.class))).thenReturn(updatedVisitDAO);
        when(mapper.visitDAOtoVisit(updatedVisitDAO)).thenReturn(updatedVisit);

        Optional<Visit> editedVisit = visitService.editVisitById(1L, updatedVisit);

        assertTrue(editedVisit.isPresent());
        assertEquals(updatedVisit, editedVisit.get());

        verify(visitRepository, times(1)).findById(1L);
        verify(visitRepository, times(1)).save(updatedVisitDAO);
        verify(mapper, times(1)).visitDAOtoVisit(updatedVisitDAO);
        verifyNoMoreInteractions(visitRepository, mapper);
    }
    @Test
    void testEditVisitByIdNotFound(){
        Visit updatedVisit = new Visit(1L, pet, "2024-03-01 9:15", "Update");

        when(visitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Visit> editedVisit = visitService.editVisitById(1L, updatedVisit);

        assertFalse(editedVisit.isPresent());

        verify(visitRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(visitRepository, mapper);
    }

    @Test
    void testDeleteVisitByIdSuccess(){
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visitDAO));
        doNothing().when(visitRepository).delete(visitDAO);
        when(mapper.visitDAOtoVisit(visitDAO)).thenReturn(new Visit());

        Optional<Visit> deletedVisit = visitService.deleteVisitById(1L);

        verify(visitRepository, times(1)).findById(1L);
        verify(visitRepository, times(1)).delete(visitDAO);
        verify(mapper, times(1)).visitDAOtoVisit(visitDAO);

        assertTrue(deletedVisit.isPresent());
        assertEquals(new Visit(), deletedVisit.get());
    }
    @Test
    void testDeleteVisitByIdNotFound(){
        when(visitRepository.existsById(1L)).thenReturn(false);

        Optional<Visit> deletedVisit = visitService.deleteVisitById(1L);

        assertFalse(deletedVisit.isPresent());
    }
}
