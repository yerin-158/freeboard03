package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;

import java.util.List;

public interface PetRepositoryImplCustom<T> {
    void findById(ObjectId id, List<T> siblings);
}
