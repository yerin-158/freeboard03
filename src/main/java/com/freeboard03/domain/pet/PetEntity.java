package com.freeboard03.domain.pet;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Document(collection = "pets")
@Getter
public class PetEntity {

    @Id
    @Setter
    private ObjectId id;

    private String kind;

    private String name;

    private int age;

    private List<PetEntity> sibling;

    @Builder
    public PetEntity(String kind, String name, int age, List<PetEntity> sibling){
        this.kind = kind;
        this.name = name;
        this.age = age;
        this.sibling = sibling;
    }

    public void update(PetEntity updatedPet){
        Optional.ofNullable(updatedPet.getKind()).ifPresent(none -> this.kind = updatedPet.getKind());
        Optional.ofNullable(updatedPet.getName()).ifPresent(none -> this.name = updatedPet.getName());
        Optional.ofNullable(updatedPet.getAge()).ifPresent(none -> this.age = updatedPet.getAge());
        Optional.ofNullable(updatedPet.getSibling()).ifPresent(none -> this.sibling = updatedPet.getSibling());
    }

}
