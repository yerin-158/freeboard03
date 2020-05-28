package com.freeboard03.domain.board;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback(value = false)
public class BoardServiceIntegrationTest {

    @Autowired
    private BoardService sut;
/*
    @Test
    public void update() {
        UserForm userForm = UserForm.builder().accountId("yerin").password("pass123").build();
        UserEntity userEntity = userForm.convertUserEntity();
        BoardForm boardForm = BoardForm.builder().title("제목입니다^^*").contents("오늘은 날씨가 좋네요").password("123!@#").build();
        BoardEntity updatedEntity = BoardEntity.builder().title("수정 후 제목입니다^^*").contents("수정후 내용이에요~ 날씨가 좋네요").password("123!@#").build();

        BoardEntity entity = sut.post(boardForm, userForm);
        sut.update(updatedEntity, entity.getId());

        assertThat(boardForm.getContents(), equalTo(updatedEntity.getContents()));
        assertThat(boardForm.getTitle(), equalTo(updatedEntity.getTitle()));
    }*/
}
