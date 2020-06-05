package com.freeboard03.domain.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByAccountId(String accountId);
    List<UserEntity> findAllByAccountIdLike(String keyword);
}
