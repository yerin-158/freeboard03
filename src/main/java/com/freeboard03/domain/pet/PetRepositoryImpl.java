package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.util.List;

public class PetRepositoryImpl implements PetRepositoryImplCustom {

    private MongoOperations operations;

    final static String SIBLING = "sibling";

    @Autowired
    public PetRepositoryImpl(MongoTemplate template) {
        Assert.notNull(template, "MongoTemplate must not be null!");
        this.operations = template;
    }

    @Override
    public void updateSiblingsById(ObjectId id, List<PetEntity> siblings) {
        Query query = new Query(Criteria.where("_id").is(id));
        operations.updateFirst(query,
                Update.update(SIBLING, siblings),
                PetEntity.class
        );
    }
}
