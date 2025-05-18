package com.mdotm.petmanager.model;


import lombok.Data;

import java.util.Date;

@Data
public class Pet {
    private Long id;
    private String name;
    private Species species;
    private Integer age;
    private String ownerName;
    private Date insertedDate;
    private Date updatedDate;
}
