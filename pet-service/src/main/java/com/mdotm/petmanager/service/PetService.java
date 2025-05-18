package com.mdotm.petmanager.service;

import com.mdotm.petmanager.dto.PetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {

    List<PetDto> getAllPets();
    PetDto getPetById(String id);
    PetDto createPet(PetDto petDto);
    PetDto updatePet(String id, PetDto petDto);
    void deletePet(String id);
    Page<PetDto> getAllPetsPaged(Pageable pageable);

}
