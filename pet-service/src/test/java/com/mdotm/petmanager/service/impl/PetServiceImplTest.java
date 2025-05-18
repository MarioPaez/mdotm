package com.mdotm.petmanager.service.impl;

import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.exception.PetNotFoundException;
import com.mdotm.petmanager.exception.SortNotAllowedException;
import com.mdotm.petmanager.mapper.PetMapper;
import com.mdotm.petmanager.model.Pet;
import com.mdotm.petmanager.model.Species;
import com.mdotm.petmanager.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet1;
    private Pet pet2;
    private PetDto petDto1;
    private PetDto petDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pet1 = new Pet();
        pet1.setId("abc");
        pet1.setName("Fido");
        pet1.setSpecies(Species.DOG);
        pet1.setAge(5);

        pet2 = new Pet();
        pet2.setId("cba");
        pet2.setName("Mimi");
        pet2.setSpecies(Species.CAT);
        pet2.setAge(3);

        petDto1 = new PetDto();
        petDto1.setId("abc");
        petDto1.setName("Fido");
        pet1.setSpecies(Species.DOG);
        petDto1.setAge(5);

        petDto2 = new PetDto();
        petDto2.setId("cba");
        petDto2.setName("Mimi");
        pet2.setSpecies(Species.CAT);
        petDto2.setAge(3);
    }

    @Test
    void getAllPets_shouldReturnMappedDtos() {
        List<Pet> pets = List.of(pet1, pet2);
        when(petRepository.findAll()).thenReturn(pets);
        when(petMapper.toPetsDto(pets)).thenReturn(List.of(petDto1, petDto2));

        List<PetDto> result = petService.getAllPets();

        assertThat(result).hasSize(2).containsExactly(petDto1, petDto2);
        verify(petRepository).findAll();
        verify(petMapper).toPetsDto(pets);
    }

    @Test
    void getAllPetsPaged_shouldSortAndPage() {
        List<Pet> pets = List.of(pet1, pet2);
        when(petRepository.findAll()).thenReturn(pets);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.asc("name")));
        // La lista original tiene pet1("Fido"), pet2("Mimi") -> sorted asc por name => Fido, Mimi (ya ordenados)

        when(petMapper.toPetsDto(anyList())).thenAnswer(invocation -> {
            List<Pet> argument = invocation.getArgument(0);
            // Map manual para test
            List<PetDto> dtos = new ArrayList<>();
            for (Pet p : argument) {
                PetDto dto = new PetDto();
                dto.setId(p.getId());
                dto.setName(p.getName());
                dto.setSpecies(p.getSpecies());
                dto.setAge(p.getAge());
                dtos.add(dto);
            }
            return dtos;
        });

        Page<PetDto> result = petService.getAllPetsPaged(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Fido");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Mimi");
    }

    @Test
    void getAllPetsPaged_shouldThrowSortNotAllowedException() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Order.asc("forbiddenField")));

        assertThatThrownBy(() -> petService.getAllPetsPaged(pageable))
                .isInstanceOf(SortNotAllowedException.class)
                .hasMessageContaining("Sorting by field 'forbiddenField' is not allowed.");
    }

    @Test
    void getPetById_existingId_returnsDto() {
        when(petRepository.findById("abc")).thenReturn(Optional.of(pet1));
        when(petMapper.toPetDto(pet1)).thenReturn(petDto1);

        PetDto result = petService.getPetById("abc");

        assertThat(result).isEqualTo(petDto1);
    }

    @Test
    void getPetById_notFound_throwsException() {
        when(petRepository.findById("abc")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.getPetById("abc"))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("Pet with ID 1 not found");
    }

    @Test
    void createPet_shouldSetDatesAndReturnDto() {
        when(petMapper.toPet(petDto1)).thenReturn(pet1);
        when(petRepository.save(pet1)).thenAnswer(invocation -> {
            Pet p = invocation.getArgument(0);
            p.setId("abc");
            return p;
        });
        when(petMapper.toPetDto(pet1)).thenReturn(petDto1);

        PetDto result = petService.createPet(petDto1);

        assertThat(result).isEqualTo(petDto1);
        assertThat(pet1.getInsertedDate()).isNotNull();
        assertThat(pet1.getUpdatedDate()).isNotNull();
    }

    @Test
    void updatePet_existingId_shouldUpdateAndReturnDto() {
        when(petRepository.findById("abc")).thenReturn(Optional.of(pet1));
        when(petMapper.toPet(petDto1)).thenReturn(pet1);
        when(petRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(petMapper.toPetDto(any())).thenReturn(petDto1);

        PetDto result = petService.updatePet("abc", petDto1);

        assertThat(result).isEqualTo(petDto1);
        verify(petRepository).save(any());
    }

    @Test
    void updatePet_notFound_throwsException() {
        when(petRepository.findById("abc")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.updatePet("abc", petDto1))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("Pet with ID 1 not found");
    }

    @Test
    void deletePet_existingId_shouldDelete() {
        when(petRepository.findById("abc")).thenReturn(Optional.of(pet1));
        doNothing().when(petRepository).deleteById("abc");

        petService.deletePet("abc");

        verify(petRepository).deleteById("abc");
    }

    @Test
    void deletePet_notFound_throwsException() {
        when(petRepository.findById("abc")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.deletePet("abc"))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("Pet with ID 1 not found");
    }
}
