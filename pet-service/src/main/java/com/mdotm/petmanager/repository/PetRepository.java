package com.mdotm.petmanager.repository;

import com.mdotm.petmanager.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {

    Pet save(Pet pet);
    Optional<Pet> findById(String id);
    List<Pet> findAll();

    void deleteById(String id);
}
