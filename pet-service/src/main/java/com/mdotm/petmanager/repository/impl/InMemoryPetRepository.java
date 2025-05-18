package com.mdotm.petmanager.repository.impl;

import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.PetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inMemory")
public class InMemoryPetRepository implements PetRepository {

    private final Map<String, Pet> inMemoryDataBase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            pet.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        inMemoryDataBase.put(pet.getId(), pet);
        return pet;
    }

    @Override
    public Optional<Pet> findById(String id) {
        return Optional.ofNullable(inMemoryDataBase.get(id));
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(inMemoryDataBase.values());
    }

    @Override
    public void deleteById(String id) {
        inMemoryDataBase.remove(id);
    }
}
