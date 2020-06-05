package com.freeboard03.domain.user;

import com.freeboard03.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
public class UserMongoRepositoryTest {

    @Autowired
    private UserRepository sut;

    private UserEntity user;

    @BeforeEach
    private void init(){
        user = UserEntity.builder().role(UserRole.NORMAL).accountId(getRandomString()).password("pass").build();
    }

    @Test
    public void saveTest(){
        sut.save(user);

        UserEntity findUser = sut.findById(user.getId()).get();

        assertThat(findUser.getAccountId(), equalTo(user.getAccountId()));
    }

    private String getRandomString() {
        String id = "";
        for (int i = 0; i < 10; i++) {
            double dValue = Math.random();
            if (i % 2 == 0) {
                id += (char) ((dValue * 26) + 65);   // 대문자
                continue;
            }
            id += (char) ((dValue * 26) + 97); // 소문자
        }
        return id;
    }


}
