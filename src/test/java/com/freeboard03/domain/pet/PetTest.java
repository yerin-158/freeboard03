package com.freeboard03.domain.pet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback
public class PetTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void insertTest(){
        PetEntity pet = PetEntity.builder().kind("CAT").name("나비").age(2).build();
        mongoTemplate.insert(pet);

        Query query = new Query(Criteria.where("_id").is(pet.getId()));

        PetEntity findPet = mongoTemplate.findOne(query, PetEntity.class, "pets");

        assertThat(pet.getId(), equalTo(findPet.getId()));
        assertThat(pet.getName(), equalTo(findPet.getName()));
        assertThat(pet.getKind(), equalTo(findPet.getKind()));
        assertThat(pet.getAge(), equalTo(findPet.getAge()));
    }
}
