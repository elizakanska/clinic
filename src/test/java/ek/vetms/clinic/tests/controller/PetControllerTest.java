package ek.vetms.clinic.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ek.vetms.clinic.controller.PetController;
import ek.vetms.clinic.controller.errorHandling.NotFoundException;
import ek.vetms.clinic.service.impl.PetServiceImpl;
import ek.vetms.clinic.entity.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PetController.class)
public class PetControllerTest {
    private final String PET_URL = "/api/v2/pets";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetServiceImpl service;
    private final Pet pet = new Pet(1L, "Testy", "Test", 1);
    private final Pet petBlankName = new Pet(1L, " ", "Test", 1);
    private final Pet petBlankSpecies = new Pet(1L, "Testy", " ", 1);
    private final Pet petNullAge = new Pet(1L, "Testy", "Test", null);

    @Test
    void testFindAll() throws Exception {
        Pet pet1 = new Pet(1L, "Buddy", "Dog", 3);
        Pet pet2 = new Pet(2L, "Whiskers", "Cat", 2);
        List<Pet> petList = Arrays.asList(pet1, pet2);

        when(service.findAll()).thenReturn(petList);

        mockMvc.perform(get(PET_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Buddy"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Whiskers"));
        verify(service, times(1)).findAll();
    }
    @Test
    void testGetPetByIdSuccess() throws Exception{
        when(service.findPetById(1L)).thenReturn(Optional.of(pet));

        mockMvc.perform(get(PET_URL+ "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Testy"))
                .andExpect(jsonPath("$.species").value("Test"))
                .andExpect(jsonPath("$.age").value(1));
        verify(service, times(1)).findPetById(1L);
    }
    @Test
    void testGetPetByIdNotFound() throws Exception{
        when(service.findPetById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get( PET_URL + "/1"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findPetById(1L);      
    }

    @Test
    void testSavePetSuccess() throws Exception{
        when(service.savePet(any(Pet.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(pet));

        mockMvc.perform(post(PET_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(pet))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Testy"))
                .andExpect(jsonPath("$.species").value("Test"))
                .andExpect(jsonPath("$.age").value(1));
        verify(service, times(1)).savePet(pet);
    }
    @Test
    void testSavePetBlankName() throws Exception{
        when(service.savePet(petBlankName)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post(PET_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petBlankName))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petBlankName));
    }
    @Test
    void testSavePetBlankSpecies() throws Exception{
        when(service.savePet(petBlankSpecies)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post(PET_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petBlankSpecies))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petBlankSpecies));
    }
    @Test
    void testSavePetNullAge() throws Exception{
        when(service.savePet(petNullAge)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post(PET_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petNullAge))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petNullAge));
    }

    @Test
    void testEditPetByIdSuccess() throws Exception{
        Pet updatedPet = new Pet(1L, "Update", "New", 3);

        when(service.editPetById(eq(1L), any(Pet.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(updatedPet));

        mockMvc.perform(put(PET_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(updatedPet))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Update"))
                .andExpect(jsonPath("$.species").value("New"))
                .andExpect(jsonPath("$.age").value(3));
        verify(service, times(1)).editPetById(eq(1L), any(Pet.class));
    }
    @Test
    void testEditPetByIdBadRequest() throws Exception{
        when(service.editPetById(eq(1L), any(Pet.class))).thenReturn(ResponseEntity.badRequest().build());

        Pet updatedPet = new Pet(2L, "Testy", "Test", 1);

        mockMvc.perform(put(PET_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(updatedPet))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(1)).editPetById(eq(1L), any(Pet.class));
    }
    @Test
    void testEditPetByIdNotFound() throws Exception{
        when(service.editPetById(eq(1L), any(Pet.class)))
                .thenReturn(ResponseEntity.notFound().build());

        Pet petNotFound = new Pet(1L, "Not", "Found", 0);

        mockMvc.perform(put(PET_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petNotFound))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editPetById(eq(1L), any(Pet.class));
    }

    @Test
    void testDeletePetByIdSuccess() throws Exception{
        doNothing().when(service).deletePetById(1L);

        mockMvc.perform(delete(PET_URL + "/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).deletePetById(1L);
    }
    @Test
    void testDeletePetByIdNotFound() throws Exception {
        doThrow(NotFoundException.class).when(service).deletePetById(2L);

        mockMvc.perform(delete(PET_URL + "/2"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).deletePetById(2L);
    }

    private static String JsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
