package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;

import java.util.List;

public interface PetRepositoryImplCustom {
    void updateSiblingsById(ObjectId id, List<PetEntity> siblings);
}
