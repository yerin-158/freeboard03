package com.freeboard03.domain.board;

import com.freeboard03.domain.user.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<BoardEntity, ObjectId> {
    List<BoardEntity> findAllByWriter(UserEntity userEntity);
    Page<BoardEntity> findAllByWriterIn(List<UserEntity> userEntityList, Pageable pageable);
}
