package com.mdotm.petmanager.repository.impl;

import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.MongoPetRepository;
import com.mdotm.petmanager.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public  class MongoPetRepositoryAdapter implements PetRepository {

    private final MongoPetRepository mongoPetRepository;

    @Override
    public Pet save(Pet pet) {
        return mongoPetRepository.save(pet);
    }

    @Override
    public Optional<Pet> findById(String id) {
        return mongoPetRepository.findById(id);
    }

    @Override
    public List<Pet> findAll() {
        return mongoPetRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        mongoPetRepository.deleteById(id);
    }
}