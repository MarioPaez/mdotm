package com.mdotm.petmanager.repository;

import com.mdotm.petmanager.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPetRepository extends JpaRepository<Pet, Long> {
}
