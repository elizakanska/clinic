package ek.vetms.clinic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ek.vetms.clinic.business.service.impl.PetServiceImpl;
import ek.vetms.clinic.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
public class PetControllerTest {
private final static String PET_URL = "/api/v1/pet";
private final static String GET_URL = PET_URL + "/get";
    private final static String SAVE_URL = PET_URL + "/save";
    private final static String EDIT_URL = PET_URL + "/edit";
    private final static String DELETE_URL = PET_URL + "/delete";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetServiceImpl service;
    private final Pet pet = new Pet(1L, "Testy", "Test", 1);
    private final Pet petBlankName = new Pet(1L, " ", "Test", 1);
    private final Pet petBlankSpecies = new Pet(1L, "Testy", " ", 1);
    private final Pet petNullAge = new Pet(1L, "Testy", "Test", null);

    @Test
    void testGetPetByIdSuccess() throws Exception{
        when(service.findPetById(1L)).thenReturn(Optional.of(pet));

        mockMvc.perform(get(GET_URL + "/1"))
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

        mockMvc.perform(get(GET_URL + "/1"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findPetById(1L);      
    }

    @Test
    void testSavePetSuccess() throws Exception{
        when(service.savePet(new Pet(1L, "Testy", "Test", 1)))
                .thenReturn(pet);

        mockMvc.perform(post(SAVE_URL)
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
        when(service.savePet(petBlankName)).thenReturn(petBlankName);

        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petBlankName))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petBlankName));
    }
    @Test
    void testSavePetBlankSpecies() throws Exception{
        when(service.savePet(petBlankSpecies)).thenReturn(petBlankSpecies);

        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petBlankSpecies))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petBlankSpecies));
    }
    @Test
    void testSavePetNullAge() throws Exception{
        when(service.savePet(petNullAge)).thenReturn(petNullAge);

        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petNullAge))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).savePet(eq(petNullAge));
    }

    @Test
    void testEditPetByIdSuccess() throws Exception{
        Pet updatedPet = new Pet(1L, "Update", "New", 3);

        when(service.editPetById(eq(1L), any(Pet.class))).thenReturn(Optional.of(updatedPet));

        mockMvc.perform(put(EDIT_URL + "/1")
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
        when(service.editPetById(eq(1L), any(Pet.class))).thenReturn(Optional.empty());

        Pet petToEdit = new Pet(2L, "Testy", "Test", 1);

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petToEdit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(1)).editPetById(eq(1L), any(Pet.class));
    }
    @Test
    void testEditPetByIdNotFound() throws Exception{
        when(service.editPetById(eq(1L), any(Pet.class)))
                .thenReturn(Optional.empty());

        Pet petNotFound = new Pet(1L, "Not", "Found", 0);

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(petNotFound))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editPetById(eq(1L), any(Pet.class));
    }

    @Test
    void testDeletePetByIdSuccess(){
        when(service.deletePetById(1L)).thenReturn(Optional.of(pet));

        try {
            mockMvc.perform(delete(DELETE_URL + "/1"))
                    .andExpect(status().isOk());
            verify(service, times(1)).deletePetById(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void testDeletePetByIdNotFound(){
        when(service.deletePetById(2L)).thenReturn(Optional.empty());

        try {
            mockMvc.perform(delete(DELETE_URL + "/2"))
                    .andExpect(status().isNotFound());
            verify(service, times(1)).deletePetById(2L);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String JsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
