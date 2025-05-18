package com.mdotm.petmanager.controller;

import com.mdotm.petmanager.dto.PetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @GetMapping
    public ResponseEntity<List<PetDto>> getAllPets(){
        List<PetDto> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }
}
