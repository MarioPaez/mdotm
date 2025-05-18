package com.mdotm.petmanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdotm.petmanager.dto.PetDto;
import com.mdotm.petmanager.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner loadMockData(PetService petService, ObjectMapper objectMapper) {
        return args -> {
            InputStream inputStream = new ClassPathResource("mock_data.json").getInputStream();
            List<PetDto> pets = objectMapper.readValue(inputStream, new TypeReference<>() {});
            pets.forEach(petService::createPet);
            log.info("Mock data inserted via service: {} pets.", pets.size());
        };
    }

}
