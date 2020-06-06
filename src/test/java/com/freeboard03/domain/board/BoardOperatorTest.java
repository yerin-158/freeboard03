package com.freeboard03.domain.board;

import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
class BoardOperatorTest {

    @Autowired
    private BoardOperator sut;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void updateTest() {
        BoardEntity boardEntity = getTestData();
        boardRepository.save(boardEntity);

        String updatedContents = getRandomString();
        BoardEntity updatedEntity = BoardEntity.builder().contents(updatedContents).build();

        sut.update(boardEntity.getId(), updatedEntity);
        BoardEntity selectedEntity = boardRepository.findById(boardEntity.getId()).get();

        assertEquals(selectedEntity.getTitle(),boardEntity.getTitle());
        assertEquals(selectedEntity.getWriter().getId().toString(), boardEntity.getWriter().getId().toString());
        assertEquals(selectedEntity.getContents(), updatedContents);
    }

    private BoardEntity getTestData() {
        UserEntity userEntity = userRepository.findAll().get(0);
        return BoardEntity.builder().title("title").contents("contents").writer(userEntity).build();

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
