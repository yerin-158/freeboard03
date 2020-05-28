package com.freeboard03.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByAccountId(String accountId);
    List<UserEntity> findAllByAccountIdLike(String keyword);
}
