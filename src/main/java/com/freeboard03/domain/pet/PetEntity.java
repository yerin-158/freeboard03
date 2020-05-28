package com.freeboard03.domain.pet;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "pets")
@Getter
public class PetEntity {

    @Id
    private ObjectId id;

    private String kind;

    private String name;

    private int age;

    private List<PetEntity> sibling;

    @Builder
    public PetEntity(String kind, String name, int age){
        this.kind = kind;
        this.name = name;
        this.age = age;
    }

}
