package com.mdotm.petmanager.controller;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.exception.PetNotFoundException;
import com.mdotm.petmanager.model.Species;
import com.mdotm.petmanager.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private static PetDto createPet1() {
        PetDto pet1 = new PetDto();
        pet1.setId(1L);
        pet1.setName("Fido");
        pet1.setSpecies(Species.DOG);
        return pet1;
    }

    private static PetDto createPet2() {
        PetDto pet2 = new PetDto();
        pet2.setId(2L);
        pet2.setName("Mimi");
        pet2.setSpecies(Species.RABBIT);
        return pet2;
    }

    @Test
    void getAllPets_shouldReturnList() throws Exception {
        PetDto pet1 = createPet1();
        PetDto pet2 = createPet2();

        List<PetDto> pets = List.of(pet1, pet2);
        Mockito.when(petService.getAllPets()).thenReturn(pets);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Fido")))
                .andExpect(jsonPath("$[1].species", is("RABBIT")));
    }



    @Test
    void getAllPetsPaged_shouldReturnPaged() throws Exception {
        List<PetDto> pets = List.of(createPet1());
        Page<PetDto> page = new PageImpl<>(pets, PageRequest.of(0, 1), 1);
        Mockito.when(petService.getAllPetsPaged(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/pets/paged?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Fido")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void getPetById_shouldReturnPet() throws Exception {
        PetDto pet = createPet1();
        Mockito.when(petService.getPetById(1L)).thenReturn(pet);

        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Fido")))
                .andExpect(jsonPath("$.species", is("DOG")));
    }

    @Test
    void createPet_shouldReturnCreatedPet() throws Exception {
        PetDto pet1 = createPet1();
        PetDto pet1Prima = createPet1();
        pet1.setId(null);
        Mockito.when(petService.createPet(any(PetDto.class))).thenReturn(pet1Prima);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Fido")));
    }

    @Test
    void updatePet_shouldReturnUpdatedPet() throws Exception {
        PetDto pet = new PetDto();
        pet.setName("Fido");
        pet.setSpecies(Species.DOG);
        PetDto updatedPet = createPet1();
        updatedPet.setName("FidoUpdated");
        Mockito.when(petService.updatePet(eq(1L), any(PetDto.class))).thenReturn(updatedPet);

        mockMvc.perform(put("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("FidoUpdated")));
    }

    @Test
    void deletePet_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPetById_shouldReturnNotFound_whenPetDoesNotExist() throws Exception {
        Mockito.when(petService.getPetById(999L)).thenThrow(new PetNotFoundException("Pet not found"));

        mockMvc.perform(get("/pets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPet_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        PetDto invalidPet = new PetDto();

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPet)))
                .andExpect(status().isBadRequest());
    }


}
