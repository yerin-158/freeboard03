package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRefactoringRepository extends MongoRepository<PetEntity, ObjectId>, PetRepositoryImplCustom<PetEntity> {
    List<PetEntity> findAllByKind(String kind);

    List<PetEntity> deleteAllByKind(String kind);

    List<PetEntity> findAllByIdIn(List<ObjectId> ids);
}
