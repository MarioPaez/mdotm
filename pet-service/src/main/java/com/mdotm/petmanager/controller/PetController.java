package com.mdotm.petmanager.controller;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<PetDto>> getAllPets(){
        List<PetDto> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("paged")
    public ResponseEntity<Page<PetDto>> getAllPetsPaged(Pageable pageable) {
        return ResponseEntity.ok(petService.getAllPetsPaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@Valid @RequestBody PetDto petDto) {
        PetDto created = petService.createPet(petDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(
            @PathVariable Long id,
            @Valid @RequestBody PetDto petDto) {
        PetDto updated = petService.updatePet(id, petDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }


}
