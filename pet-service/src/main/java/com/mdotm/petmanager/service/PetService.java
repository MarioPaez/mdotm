package com.mdotm.petmanager.service;

import com.mdotm.petmanager.dto.PetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {

    List<PetDto> getAllPets();
    PetDto getPetById(Long id);
    PetDto createPet(PetDto petDto);
    PetDto updatePet(Long id, PetDto petDto);
    void deletePet(Long id);
    Page<PetDto> getAllPetsPaged(Pageable pageable);

}
