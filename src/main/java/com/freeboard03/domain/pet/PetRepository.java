package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<PetEntity, ObjectId> {
    List<PetEntity> findAllByKind(String kind);

    List<PetEntity> deleteAllByKind(String kind);

    List<PetEntity> findAllByIdIn(List<ObjectId> ids);
}
