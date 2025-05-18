package com.mdotm.petmanager.mapper;


import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.model.Pet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PetMapper {

    List<PetDto> toPetsDto(List<Pet> pets);

    PetDto toPetDto(Pet pet);

    Pet toPet(PetDto petDto);
}
