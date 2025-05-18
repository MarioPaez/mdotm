package com.mdotm.petmanager.service.impl;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.exception.PetNotFoundException;
import com.mdotm.petmanager.exception.SortNotAllowedException;
import com.mdotm.petmanager.mapper.PetMapper;
import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.repository.PetRepository;
import com.mdotm.petmanager.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    private final String NAME = "name";
    private final String SPECIES = "species";
    private final String AGE = "age";
    private final Set<String> ALLOWED_SORT_FIELDS = Set.of(NAME, SPECIES, AGE);

    @Override
    public List<PetDto> getAllPets() {
        log.info("Fetching all pets");
        List<Pet> pets = petRepository.findAll();
        log.info("Retrieved {} pets", pets.size());
        return petMapper.toPetsDto(pets);
    }

    @Override
    public Page<PetDto> getAllPetsPaged(Pageable pageable) {
        log.info("Fetching all pets paged with sorting: {}", pageable.getSort());

        List<Pet> pets = new ArrayList<>(petRepository.findAll());
        // Validate allowed sorting fields
        for (Sort.Order order : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                log.warn("Attempt to sort by forbidden field: {}", order.getProperty());
                throw new SortNotAllowedException("Sorting by field '" + order.getProperty() + "' is not allowed.");
            }
        }
        pageable.getSort().stream()
                .map(order -> {

                    Comparator<Pet> c = switch (order.getProperty()) {
                        case NAME ->
                                Comparator.comparing(Pet::getName, Comparator.nullsLast(String::compareToIgnoreCase));
                        case SPECIES ->
                                Comparator.comparing(Pet::getSpecies, Comparator.nullsLast(Comparator.naturalOrder()));
                        case AGE -> Comparator.comparing(Pet::getAge, Comparator.nullsLast(Integer::compareTo));
                        default -> null;
                    };
                    if (c == null) return null;
                    return order.isAscending() ? c : c.reversed();
                })
                .filter(Objects::nonNull)
                .reduce(Comparator::thenComparing).ifPresent(pets::sort);


        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), pets.size());
        List<Pet> pageContent = pets.subList(start, end);

        return new PageImpl<>(petMapper.toPetsDto(pageContent), pageable, pets.size());
    }

    @Override
    public PetDto getPetById(String id) {
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
    public PetDto updatePet(String id, PetDto petDto) {

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
    public void deletePet(String id) {
        log.info("Deleting pet with ID: {}", id);
        if (petRepository.findById(id).isPresent()) {
            petRepository.deleteById(id);
            log.info("Pet with ID {} deleted", id);
        } else {
            log.warn("Pet with ID {} not found for deletion", id);
            throw new PetNotFoundException("Pet with ID " + id + " not found");
        }
    }
}
