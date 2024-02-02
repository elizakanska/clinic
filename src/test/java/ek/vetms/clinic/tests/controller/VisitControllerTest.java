package ek.vetms.clinic.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ek.vetms.clinic.controller.VisitController;
import ek.vetms.clinic.service.impl.PetServiceImpl;
import ek.vetms.clinic.service.impl.VisitServiceImpl;
import ek.vetms.clinic.entity.Pet;
import ek.vetms.clinic.entity.Visit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    private final static String VISIT_URL = "/api/v2/visits";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VisitServiceImpl service;
    @MockBean
    private PetServiceImpl petService;
    private final Pet pet = new Pet(1L, "Test", "Test", 5);
    private final Visit visit = new Visit(1L, pet, LocalDateTime.of(2024,3,1,9,0), "Test");
    private final Visit visitNullPet = new Visit(1L, null, LocalDateTime.of(2024,3,1,9,0), "Test");
    private final Visit visitNullTime = new Visit(1L, pet, null, "Test");
    private final Visit visitBlankReason = new Visit(1L, pet, LocalDateTime.of(2024,3,1,9,0), " ");

    @Test
    void testFindAllVisits() throws Exception {
        Visit visit1 = new Visit(1L, new Pet(1L, "Test1", "Test", 3), LocalDateTime.now(), "Routine Checkup");
        Visit visit2 = new Visit(2L, new Pet(2L, "Test2", "Test", 5), LocalDateTime.now(), "Vaccination");
        List<Visit> visitList = Arrays.asList(visit1, visit2);

        when(service.findAll()).thenReturn(visitList);

        mockMvc.perform(get(VISIT_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].pet.id").value(1L))
                .andExpect(jsonPath("$[0].pet.name").value("Test1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].pet.id").value(2L))
                .andExpect(jsonPath("$[1].pet.name").value("Test2"));
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetVisitByIdSuccess() throws Exception {
        when(service.findVisitById(1L)).thenReturn(Optional.of(visit));

        mockMvc.perform(get(VISIT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pet.id").value(1L))
                .andExpect(jsonPath("$.pet.name").value("Test"))
                .andExpect(jsonPath("$.pet.species").value("Test"))
                .andExpect(jsonPath("$.pet.age").value(5))
                .andExpect(jsonPath("$.time").value("2024-03-01 09:00"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(service, times(1)).findVisitById(1L);
    }
    @Test
    void testGetVisitByIdNotFound() throws Exception{
        when(service.findVisitById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(VISIT_URL + "/1"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findVisitById(1L);
    }

    @Test
    void testSaveVisitSuccess() throws Exception{
        when(petService.findPetById(eq(1L))).thenReturn(Optional.of(pet));
        when(service.saveVisit(any(Visit.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(visit));

        mockMvc.perform(post(VISIT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(visit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pet").value(pet))
                .andExpect(jsonPath("$.time").value("2024-03-01 09:00"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(service, times(1)).saveVisit(visit);
    }
    @Test
    void testSaveVisitNullPet() throws Exception {
        when(petService.findPetById(eq(null))).thenReturn(Optional.empty());

        mockMvc.perform(post(VISIT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(visitNullPet))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }
    @Test
    void testSaveVisitNullTime() throws Exception{
        mockMvc.perform(post(VISIT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(visitNullTime))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }
    @Test
    void testSaveVisitBlankReason() throws Exception{
        mockMvc.perform(post(VISIT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(visitBlankReason))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).saveVisit(any(Visit.class));
    }

    @Test
    void testEditVisitByIdSuccess() throws Exception{
        Visit updatedVisit = new Visit(1L, pet, LocalDateTime.of(2024, 3, 1, 9, 30), "Test");

        when(petService.findPetById(eq(1L))).thenReturn(Optional.of(pet));
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(updatedVisit));

        mockMvc.perform(put(VISIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pet").value(pet))
                .andExpect(jsonPath("$.time").value("2024-03-01 09:30"))
                .andExpect(jsonPath("$.reason").value("Test"));
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }
    @Test
    void testEditVisitByIdBadRequest() throws Exception {
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(ResponseEntity.badRequest().build());

        Pet pet = new Pet(1L, "Test", "Test", 5);
        Visit updatedVisit = new Visit(2L, pet, LocalDateTime.of(2024, 3, 1, 9, 30), "Test");

        mockMvc.perform(put(VISIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editVisitById(eq(1L), any(Visit.class));
    }

    @Test
    void testEditVisitByIdMismatch() throws Exception{
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(ResponseEntity.notFound().build());

        Pet pet = new Pet(1L, "Test", "Test", 5);
        Visit updatedVisit = new Visit(2L, pet, LocalDateTime.of(2024, 3, 1, 9, 30), "Test");

        mockMvc.perform(put(VISIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editVisitById(eq(1L), any(Visit.class));
    }
    @Test
    void testEditVisitByIdPetNotFound() throws Exception {
        Visit updatedVisit = new Visit(1L, pet, LocalDateTime.of(2024, 3,1,9,30), "Test");

        when(petService.findPetById(eq(updatedVisit.getPet().getId()))).thenReturn(Optional.empty());
        when(service.editVisitById(eq(1L), any(Visit.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put(VISIT_URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(jsonString(updatedVisit))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(petService, times(0)).findPetById(anyLong());
        verify(service, times(1)).editVisitById(eq(1L), any(Visit.class));
    }

    @Test
    void testDeleteVisitByIdSuccess() throws Exception{
        when(service.deleteVisitById(1L)).thenReturn(true);

        mockMvc.perform(delete(VISIT_URL + "/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteVisitById(1L);
    }
    @Test
    void testDeleteVisitByIdNotFound() throws Exception{
        when(service.deleteVisitById(2L)).thenReturn(false);

        mockMvc.perform(delete(VISIT_URL + "/2"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).deleteVisitById(2L);
    }

    private static String jsonString(final Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}