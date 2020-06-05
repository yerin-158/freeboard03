package com.freeboard03.domain.board;

import com.freeboard03.api.board.BoardForm;
import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
public class BoardServiceIntegrationTest {

    @Autowired
    private BoardService sut;

    @Autowired
    private UserRepository userRepository;

    private List<UserEntity> writer;

    @BeforeEach
    private void init(){
        this.writer = userRepository.findAll().subList(0,5);
    }

    @Test
    public void save() {
        UserForm userForm = UserForm.builder().accountId(writer.get(1).getAccountId()).password(writer.get(1).getPassword()).build();
        BoardForm boardForm = BoardForm.builder().title("제목입니다^^*").contents("오늘은 날씨가 좋네요").build();
        sut.post(boardForm, userForm);

    }
}
