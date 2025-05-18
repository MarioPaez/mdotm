package com.mdotm.petmanager.repository;

import com.mdotm.petmanager.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPetRepository extends MongoRepository<Pet, String> {
}
