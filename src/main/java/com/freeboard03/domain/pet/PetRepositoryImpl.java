package com.freeboard03.domain.pet;

import com.mongodb.client.result.UpdateResult;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

public class PetRepositoryImpl implements PetRepositoryImplCustom<PetEntity> {

    private final MongoOperations operations;

    final static String SIBLING = "sibling";

    @Autowired
    public PetRepositoryImpl(MongoTemplate template) {
        Assert.notNull(template, "MongoTemplate must not be null!");
        this.operations = template;
    }

    @Override
    public void findById(ObjectId id, List<PetEntity> siblings) {
        Query query = new Query(Criteria.where("_id").is(id));
        UpdateResult result = operations.updateFirst(query,
                Update.update(SIBLING, siblings),
                PetEntity.class
        );

    }
}
