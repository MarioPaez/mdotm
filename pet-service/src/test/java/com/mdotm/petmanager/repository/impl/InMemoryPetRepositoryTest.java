package com.mdotm.petmanager.repository.impl;

import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.model.Species;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class InMemoryPetRepositoryTest {

    private InMemoryPetRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepository();
    }

    @Test
    void save_shouldAssignIdAndStorePet() {
        Pet pet = new Pet();
        pet.setName("Fido");
        pet.setSpecies(Species.DOG);

        Pet saved = repository.save(pet);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Fido");

        Optional<Pet> retrieved = repository.findById(saved.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Fido");
    }

    @Test
    void save_shouldUpdateExistingPet() {
        Pet pet = new Pet();
        pet.setName("Fido");
        pet.setSpecies(Species.DOG);
        Pet saved = repository.save(pet);

        saved.setName("Fido Updated");
        Pet updated = repository.save(saved);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getName()).isEqualTo("Fido Updated");

        Optional<Pet> retrieved = repository.findById(saved.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Fido Updated");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        Optional<Pet> pet = repository.findById("999");
        assertThat(pet).isNotPresent();
    }

    @Test
    void findAll_shouldReturnAllPets() {
        Pet pet1 = new Pet();
        pet1.setName("Fido");
        pet1.setSpecies(Species.DOG);
        repository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("Mimi");
        pet2.setSpecies(Species.CAT);
        repository.save(pet2);

        List<Pet> allPets = repository.findAll();

        assertThat(allPets).hasSize(2)
                .extracting(Pet::getName)
                .containsExactlyInAnyOrder("Fido", "Mimi");
    }

    @Test
    void deleteById_shouldRemovePet() {
        Pet pet = new Pet();
        pet.setName("Fido");
        pet.setSpecies(Species.DOG);
        Pet saved = repository.save(pet);

        repository.deleteById(saved.getId());

        Optional<Pet> retrieved = repository.findById(saved.getId());
        assertThat(retrieved).isNotPresent();
    }
}
