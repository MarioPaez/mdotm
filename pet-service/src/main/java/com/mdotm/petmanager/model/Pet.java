package com.mdotm.petmanager.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document(collection = "pets")
public class Pet {
    @MongoId
    private String id;
    private String name;
    private Species species;
    private Integer age;
    private String ownerName;
    private Date insertedDate;
    private Date updatedDate;
}
