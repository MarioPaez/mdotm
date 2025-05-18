package com.mdotm.petmanager.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Pet {
    @GeneratedValue
    @Id
    private Long id;
    private String name;
    private Species species;
    private Integer age;
    private String ownerName;
    private Date insertedDate;
    private Date updatedDate;
}
