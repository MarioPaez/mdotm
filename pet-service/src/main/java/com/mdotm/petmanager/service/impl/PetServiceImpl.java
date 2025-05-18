package com.mdotm.petmanager.service.impl;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.mapper.PetMapper;
import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.PetRepository;
import com.mdotm.petmanager.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return petMapper.toPetsDto(pets);
    }

    @Override
    public PetDto getPetById(Long id) {
        Pet pet = petRepository.findById(id).orElse(null);
        return petMapper.toPetDto(pet);
    }

    @Override
    public PetDto createPet(PetDto petDto) {
        Pet pet = petMapper.toPet(petDto);
        Date now = new Date();
        pet.setInsertedDate(now);
        pet.setUpdatedDate(now);
        petRepository.save(pet);
        return petMapper.toPetDto(pet);
    }

    @Override
    public PetDto updatePet(Long id, PetDto petDto) {

        Pet existingPet = petRepository.findById(id).orElse(null);

        Pet updatedPet = petMapper.toPet(petDto);

        updatedPet.setId(id);
        assert existingPet != null;
        updatedPet.setInsertedDate(existingPet.getInsertedDate());
        updatedPet.setUpdatedDate(new Date());
        petRepository.save(updatedPet);
        return petMapper.toPetDto(updatedPet);
    }

    @Override
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public Page<PetDto> getAllPetsPaged(Pageable pageable) {
        return null;
    }
}
