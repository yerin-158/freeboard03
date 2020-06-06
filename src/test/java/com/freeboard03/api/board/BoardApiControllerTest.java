package com.freeboard03.api.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.board.BoardEntity;
import com.freeboard03.domain.board.BoardRepository;
import com.freeboard03.domain.board.enums.BoardExceptionType;
import com.freeboard03.domain.board.enums.SearchType;
import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import com.freeboard03.util.exception.FreeBoardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@WebAppConfiguration
public class BoardApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    @Autowired
    private MockHttpSession mockHttpSession;

    private UserEntity testUser;
    private BoardEntity testBoard;


    @BeforeEach
    public void initMvc() {
        testUser = userRepository.findAll().get(0);
        UserForm userForm = UserForm.builder().accountId(testUser.getAccountId()).password(testUser.getPassword()).build();
        mockHttpSession.setAttribute("USER", userForm);

        testBoard = boardRepository.findAllByWriter(testUser).get(0);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("trailing-slash test")
    public void trailingSlashTest() throws Exception {
        mvc.perform(get("/api/boards/")).andExpect(status().isOk());
    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(get("/api/boards")).andExpect(status().isOk());
    }

    @Test
    public void saveTest() throws Exception {
        BoardForm boardForm = BoardForm.builder().title("제목을 입력하세요").contents("내용입니다.").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/boards")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(boardForm)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'contents':'" + boardForm.getContents() + "'}"));
        ;
    }

    @Test
    @DisplayName("올바른 패스워드를 입력한 경우 데이터 수정이 가능하다.")
    public void updateTest() throws Exception {
        BoardForm updateForm = BoardForm.builder().title("제목을 입력하세요").contents("수정된 데이터입니다 ^^*").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/api/boards/" + testBoard.getId())
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("권한없는 계정으로 삭제 요청시 데이터는 삭제되지 않고 예외처리된다.")
    public void deleteTest1() throws Exception {
        UserEntity wrongUser = userRepository.findAll().get(1);
        BoardEntity wrongBoard = boardRepository.findAllByWriter(wrongUser).get(0);

        mvc.perform(delete("/api/boards/" + wrongBoard.getId())
                .session(mockHttpSession))
                .andExpect(result -> assertEquals(result.getResolvedException().getClass().getCanonicalName(), FreeBoardException.class.getCanonicalName()))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BoardExceptionType.NO_QUALIFICATION_USER.getErrorMessage()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("권한있는 계정으로 삭제 요청 시 삭제된다.")
    public void deleteTest2() throws Exception {
        mvc.perform(delete("/api/boards/" + testBoard.getId())
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 검색 테스트-타이틀")
    public void searchTest() throws Exception {
        final String TARGET = getRandomString();

        for (int i = 0; i < 20; i++) {
            BoardEntity entity = BoardEntity.builder().contents("content").title(TARGET + getRandomString()).writer(testUser).build();
            boardRepository.save(entity);
        }

        mvc.perform(get("/api/boards?type=" + SearchType.TITLE + "&keyword=" + TARGET + "&page=2&size=8")
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 검색 테스트-글 작성자")
    public void searchTest2() throws Exception {
        String keyword = testUser.getAccountId();
        mvc.perform(get("/api/boards?type=" + SearchType.WRITER + "&keyword=" + keyword)
                .session(mockHttpSession))
                .andExpect(status().isOk());
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
