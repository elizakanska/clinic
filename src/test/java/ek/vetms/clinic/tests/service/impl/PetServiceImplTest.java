package ek.vetms.clinic.tests.service.impl;

import ek.vetms.clinic.controller.errorHandling.NotFoundException;
import ek.vetms.clinic.repository.PetRepository;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.service.impl.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {
    @Mock
    PetRepository repository;
    @InjectMocks
    PetServiceImpl service;
    private Pet pet;
    
    @BeforeEach
    public void init(){
        pet = new Pet(1L, "Testy", "Test", 1);
    }

    @Test
    public void testFindAll() {
        Pet pet1 = new Pet(1L, "Buddy", "Dog", 3);
        Pet pet2 = new Pet(2L, "Whiskers", "Cat", 2);
        List<Pet> expectedPets = Arrays.asList(pet1, pet2);

        when(repository.findAll()).thenReturn(expectedPets);

        List<Pet> actualPets = service.findAll();

        assertEquals(expectedPets.size(), actualPets.size());
        for (int i = 0; i < expectedPets.size(); i++) {
            Pet expectedPet = expectedPets.get(i);
            Pet actualPet = actualPets.get(i);

            assertEquals(expectedPet.getId(), actualPet.getId());
            assertEquals(expectedPet.getName(), actualPet.getName());
            assertEquals(expectedPet.getSpecies(), actualPet.getSpecies());
            assertEquals(expectedPet.getAge(), actualPet.getAge());
        }
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testFindPetByIdSuccess() {
        when(repository.findById(pet.getId())).thenReturn(Optional.of(pet));

        Optional<Pet> actualPetOptional = service.findPetById(pet.getId());

        assertTrue(actualPetOptional.isPresent());
        Pet actualPet = actualPetOptional.get();
        assertEquals(pet, actualPet);

        verify(repository, times(1)).findById(pet.getId());
    }
    @Test
    public void testFindPetByIdNotFound() {
        when(repository.findById(pet.getId())).thenReturn(Optional.empty());

        Optional<Pet> actualPetOptional = service.findPetById(pet.getId());

        assertFalse(actualPetOptional.isPresent());

        verify(repository, times(1)).findById(pet.getId());
    }

    @Test
    void testSavePetSuccess(){
        when(repository.save(pet)).thenReturn(pet);

        ResponseEntity<Pet> responseEntity = service.savePet(pet);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(pet, responseEntity.getBody());

        verify(repository, times(1)).save(pet);

    }
    @Test
    void testSavePetBlankName(){
        Pet petBlankName = new Pet(1L, " ", "Blank", 1);

        assertThrows(NullPointerException.class, () -> service.savePet(petBlankName));

        verify(repository, times(1)).save(petBlankName);
    }
    @Test
    void testSavePetBlankSpecies(){
        Pet petBlankSpecies = new Pet(1L, "Blank", " ", 1);

        assertThrows(NullPointerException.class, () -> service.savePet(petBlankSpecies));

        verify(repository, times(1)).save(petBlankSpecies);
    }
    @Test
    void testSavePetNullAge(){
        Pet petNullAge = new Pet(1L, "Null", "Null", null);

        assertThrows(NullPointerException.class, () -> service.savePet(petNullAge));

        verify(repository, times(1)).save(petNullAge);
    }
    @Test
    void testSavePetNull(){
        assertThrows(NullPointerException.class, () -> service.savePet(null));

        verify(repository, times(0)).save(pet);
    }

    @Test
    void testEditPetByIdSuccess(){
        Pet updatedPet = new Pet(1L, "Update", "New", 3);

        when(repository.findById(1L)).thenReturn(Optional.of(updatedPet));
        when(repository.save(any(Pet.class))).thenReturn(updatedPet);

        ResponseEntity<Pet> responseEntity = service.editPetById(1L, updatedPet);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(updatedPet, responseEntity.getBody());

        verify(repository, times(1)).save(updatedPet);
    }
    @Test
    void testEditPetByIdBadRequest() {
        Pet updatedPet = new Pet(2L, "Updated", "Updated", 4);

        when(repository.findById(1L)).thenReturn(Optional.of(pet));

        ResponseEntity<Pet> responseEntity = service.editPetById(1L, updatedPet);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(repository, times(0)).save(any());
    }
    @Test
    void testEditPetByIdNotFound(){
        Pet updatedPet = new Pet(1L, "Update", "New", 3);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.editPetById(1L, updatedPet));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testDeletePetByIdSuccess(){
        when(repository.findById(1L)).thenReturn(Optional.of(pet));
        
        assertDoesNotThrow(() -> service.deletePetById(1L));
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(pet);
    }
    @Test
    void testDeletePetByIdNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deletePetById(1L));

        verify(repository, times(0)).delete(any());
    }
}
