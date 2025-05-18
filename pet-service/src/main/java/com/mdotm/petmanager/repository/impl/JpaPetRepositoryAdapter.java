package com.mdotm.petmanager.repository.impl;

import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.JpaPetRepository;
import com.mdotm.petmanager.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("jpa")
public class JpaPetRepositoryAdapter implements PetRepository {

    private final JpaPetRepository jpaPetRepository;

    @Override
    public Pet save(Pet pet) {
        return jpaPetRepository.save(pet);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return jpaPetRepository.findById(id);
    }

    @Override
    public List<Pet> findAll() {
        return jpaPetRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaPetRepository.deleteById(id);
    }
}
