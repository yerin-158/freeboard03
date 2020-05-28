package com.freeboard03.api.board;


import com.freeboard03.domain.board.BoardService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
public class BoardApiControllerUnitTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardApiController sut;

    /*@Test
    public void postTest1(){
        UserForm userForm = UserForm.builder().accountId("yerin").password("pass123").build();
        UserEntity userEntity = userForm.convertUserEntity();
        BoardForm boardForm = BoardForm.builder().title("session test").build();
        BoardEntity boardEntity = boardForm.convertBoardEntity(userEntity);

        HttpSession session = mock(HttpSession.class);

        given(session.getAttribute("USER")).willReturn(userForm);
        given(boardService.post(boardForm, userForm)).willReturn(boardEntity);

        sut.post(boardForm);
    }*/
}
