package ek.vetms.clinic.business.service.impl;

import ek.vetms.clinic.business.mappers.PetMapper;
import ek.vetms.clinic.business.repository.PetRepository;
import ek.vetms.clinic.business.repository.model.PetDAO;
import ek.vetms.clinic.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
public class PetServiceImplTest {
    @Mock
    PetRepository repository;
    @Mock
    PetMapper mapper;
    @InjectMocks
    PetServiceImpl service;
    private Pet pet;
    private PetDAO petDAO;
    
    @BeforeEach
    public void init(){
        pet = new Pet(1L, "Testy", "Test", 1);
        petDAO = new PetDAO(1L, "Testy", "Test", 1);
    }

    @Test
    void testFindPetByIdSuccess(){
        when(repository.findById(1L)).thenReturn(Optional.of(petDAO));
        when(mapper.petDAOtoPet(petDAO)).thenReturn(pet);

        assertEquals(Optional.of(pet), service.findPetById(1L));

        verify(repository, times(1)).findById(anyLong());
    }
    @Test
    void testFindPetByIdNotFound(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), service.findPetById(1L));

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void testSavePetSuccess(){
        when(mapper.petToPetDAO(pet)).thenReturn(petDAO);
        when(repository.save(petDAO)).thenReturn(petDAO);
        when(mapper.petDAOtoPet(petDAO)).thenReturn(pet);

        assertEquals(pet, service.savePet(pet));

        verify(mapper, times(1)).petToPetDAO(pet);
        verify(repository, times(1)).save(petDAO);
        verify(mapper, times(1)).petDAOtoPet(petDAO);
    }
    @Test
    void testSavePetBlankName(){
        Pet petBlankName = new Pet(1L, " ", "Blank", 1);

        assertThrows(IllegalArgumentException.class, () -> service.savePet(petBlankName));

        verify(repository, times(0)).save(petDAO);
    }
    @Test
    void testSavePetBlankSpecies(){
        Pet petBlankSpecies = new Pet(1L, "Blank", " ", 1);

        assertThrows(IllegalArgumentException.class, () -> service.savePet(petBlankSpecies));

        verify(repository, times(0)).save(petDAO);
    }
    @Test
    void testSavePetNullAge(){
        Pet petNullAge = new Pet(1L, "Null", "Null", null);

        assertThrows(IllegalArgumentException.class, () -> service.savePet(petNullAge));

        verify(repository, times(0)).save(petDAO);
    }
    @Test
    void testSavePetNull(){
        assertThrows(IllegalArgumentException.class, () -> service.savePet(null));

        verify(repository, times(0)).save(petDAO);
    }

    @Test
    void testEditPetByIdSuccess(){
        Pet updatedPet = new Pet(1L, "Update", "New", 3);
        PetDAO updatedPetDAO = new PetDAO(1L, "Update", "New", 3);

        when(repository.findById(1L)).thenReturn(Optional.of(updatedPetDAO));
        when(repository.save(any(PetDAO.class))).thenReturn(updatedPetDAO);
        when(mapper.petDAOtoPet(updatedPetDAO)).thenReturn(updatedPet);

        Optional<Pet> editedPet = service.editPetById(1L, updatedPet);

        assertTrue(editedPet.isPresent());
        assertEquals(updatedPet, editedPet.get());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(updatedPetDAO);
        verify(mapper, times(1)).petDAOtoPet(updatedPetDAO);
        verifyNoMoreInteractions(repository, mapper);
    }
    @Test
    void testEditPetByIdNotFound(){
        Pet updatedPet = new Pet(1L, "Update", "New", 3);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Pet> editedPet = service.editPetById(1L, updatedPet);

        assertFalse(editedPet.isPresent());

        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void testDeletePetByIdSuccess(){
        when(repository.findById(1L)).thenReturn(Optional.of(petDAO));
        doNothing().when(repository).delete(petDAO);
        when(mapper.petDAOtoPet(petDAO)).thenReturn(new Pet());
        
        Optional<Pet> deletedPet = service.deletePetById(1L);
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(petDAO);
        verify(mapper, times(1)).petDAOtoPet(petDAO);
        
        assertTrue(deletedPet.isPresent());
        assertEquals(new Pet(), deletedPet.get());
    }
    @Test
    void testDeletePetByIdNotFound(){
        when(repository.existsById(1L)).thenReturn(false);
        
        Optional<Pet> deletedPet = service.deletePetById(1L);

        assertFalse(deletedPet.isPresent());
    }
}
