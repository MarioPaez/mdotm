package com.mdotm.petmanager.service.impl;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.exception.PetNotFoundException;
import com.mdotm.petmanager.mapper.PetMapper;
import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.PetRepository;
import com.mdotm.petmanager.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    public List<PetDto> getAllPets() {
        log.info("Fetching all pets");
        List<Pet> pets = petRepository.findAll();
        log.info("Retrieved {} pets", pets.size());
        return petMapper.toPetsDto(pets);
    }

    @Override
    public PetDto getPetById(Long id) {
        log.info("Fetching pet with ID: {}", id);
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pet with ID {} not found", id);
                    return new PetNotFoundException("Pet with ID " + id + " not found");
                });
        PetDto dto = petMapper.toPetDto(pet);
        log.info("Pet found: {}", dto);
        return dto;
    }

    @Override
    public PetDto createPet(PetDto petDto) {
        log.info("Creating pet: {}", petDto);
        Pet pet = petMapper.toPet(petDto);
        Date now = new Date();
        pet.setInsertedDate(now);
        pet.setUpdatedDate(now);
        petRepository.save(pet);
        log.info("Pet created successfully with ID: {}", pet.getId());
        return petMapper.toPetDto(pet);
    }

    @Override
    public PetDto updatePet(Long id, PetDto petDto) {

        log.info("Updating pet with ID: {} -> {}", id, petDto);

        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update pet. Pet with ID {} not found", id);
                    return new PetNotFoundException("Pet with ID " + id + " not found");
                });

        Pet updatedPet = petMapper.toPet(petDto);

        updatedPet.setId(id);  // Important
        updatedPet.setInsertedDate(existingPet.getInsertedDate());
        updatedPet.setUpdatedDate(new Date());

        petRepository.save(updatedPet);

        log.info("Pet updated successfully with ID: {}", id);
        return petMapper.toPetDto(updatedPet);
    }

    @Override
    public void deletePet(Long id) {
        log.info("Deleting pet with ID: {}", id);
        if (petRepository.findById(id).isPresent()) {
            petRepository.deleteById(id);
            log.info("Pet with ID {} deleted", id);
        } else {
            log.warn("Pet with ID {} not found for deletion", id);
            throw new PetNotFoundException("Pet with ID " + id + " not found");
        }
    }

    @Override
    public Page<PetDto> getAllPetsPaged(Pageable pageable) {
        return null;
    }
}
