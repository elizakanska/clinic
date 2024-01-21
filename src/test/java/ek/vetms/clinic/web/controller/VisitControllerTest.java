package ek.vetms.clinic.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ek.vetms.clinic.business.service.impl.PetServiceImpl;
import ek.vetms.clinic.business.service.impl.VisitServiceImpl;
import ek.vetms.clinic.model.Pet;
import ek.vetms.clinic.model.Visit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(VisitController.class)
public class VisitControllerTest {
    private final static String VISIT_URL = "/api/v1/visit";
    private final static String GET_URL = VISIT_URL + "/get";
    private final static String SAVE_URL = VISIT_URL + "/save";
    private final static String EDIT_URL = VISIT_URL + "/edit";
    private final static String DELETE_URL = VISIT_URL + "/delete";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VisitServiceImpl service;
    @MockBean
    private PetServiceImpl petService;
    private final Pet pet = new Pet(1L, "Test", "Test", 5);
    private final Visit visit = new Visit(1L, pet, "2024-03-01 9:15", "Test");
    private final Visit visitNullPet = new Visit(1L, null, "2024-03-01 9:15", "Test");
    private final Visit visitBlankTime = new Visit(1L, pet, " ", "Test");
    private final Visit visitBlankReason = new Visit(1L, pet, "2024-03-01 9:15", " ");

    @Test
    void testGetVisitByIdSuccess() throws Exception {
        when(service.findVisitById(1L)).thenReturn(Optional.of(visit));
        when(petService.findPetById(visit.getPet().getId())).thenReturn(Optional.of(pet));

        mockMvc.perform(get(GET_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pet.id").value(pet.getId()))
                .andExpect(jsonPath("$.pet.name").value(pet.getName()))
                .andExpect(jsonPath("$.pet.species").value(pet.getSpecies()))
                .andExpect(jsonPath("$.pet.age").value(pet.getAge()))
                .andExpect(jsonPath("$.time").value("2024-03-01 9:15"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(service, times(1)).findVisitById(1L);
        verify(petService, times(1)).findPetById(visit.getPet().getId());
    }
    @Test
    void testGetVisitByIdNotFound() throws Exception{
        when(service.findVisitById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(GET_URL + "/1"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findVisitById(1L);
    }

    @Test
    void testSaveVisitSuccess() throws Exception{
        when(petService.findPetById(eq(1L))).thenReturn(Optional.of(pet));
        when(service.saveVisit(new Visit(1L, pet, "2024-03-01 9:15", "Test")))
                .thenReturn(visit);

        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pet").value(pet))
                .andExpect(jsonPath("$.time").value("2024-03-01 9:15"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(petService, times(1)).findPetById(eq(1L));
        verify(service, times(1)).saveVisit(visit);
    }
    @Test
    void testSaveVisitPetNotFound() throws Exception {
        when(petService.findPetById(eq(visit.getPet().getId()))).thenReturn(Optional.empty());

        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(petService, times(1)).findPetById(eq(visit.getPet().getId()));
        verify(service, times(0)).saveVisit(any(Visit.class));
    }
    @Test
    void testSaveVisitNullPet() throws Exception{
        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visitNullPet))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }
    @Test
    void testSaveVisitBlankTime() throws Exception{
        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visitBlankTime))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }
    @Test
    void testSaveVisitBlankReason() throws Exception{
        mockMvc.perform(post(SAVE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visitBlankReason))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }

    @Test
    void testEditVisitByIdSuccess() throws Exception{
        Visit updatedVisit = new Visit(1L, pet, "2024-03-01 9:30", "Test");

        when(petService.findPetById(eq(1L))).thenReturn(Optional.of(pet));
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(Optional.of(updatedVisit));

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pet").value(pet))
                .andExpect(jsonPath("$.time").value("2024-03-01 9:30"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(petService, times(1)).findPetById(eq(1L));
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }
    @Test
    void testEditVisitByIdBadRequest() throws Exception{
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(Optional.empty());

        Visit updatedVisit = new Visit(2L, pet, "2024-03-01 9:30", "Test");

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }
    @Test
    void testEditVisitByIdNotFound() throws Exception{
        when(service.editVisitById(eq(1L), any(Visit.class)))
                .thenReturn(Optional.empty());

        Visit visitNotFound = new Visit(1L, pet, "2024-03-01 9:30", "NA");

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(visitNotFound))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }
    @Test
    void testEditVisitByIdPetNotFound() throws Exception {
        Visit updatedVisit = new Visit(1L, pet, "2024-03-01 9:30", "Test");

        when(petService.findPetById(eq(updatedVisit.getPet().getId()))).thenReturn(Optional.empty());
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(Optional.empty());

        mockMvc.perform(put(EDIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(JsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(petService, times(0)).findPetById(anyLong());
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }

    @Test
    void testDeleteVisitByIdSuccess(){
        when(service.deleteVisitById(1L)).thenReturn(Optional.of(visit));

        try {
            mockMvc.perform(delete(DELETE_URL + "/1"))
                    .andExpect(status().isOk());
            verify(service, times(1)).deleteVisitById(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void testDeleteVisitByIdNotFound(){
        when(service.deleteVisitById(2L)).thenReturn(Optional.empty());

        try {
            mockMvc.perform(delete(DELETE_URL + "/2"))
                    .andExpect(status().isNotFound());
            verify(service, times(1)).deleteVisitById(2L);
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
