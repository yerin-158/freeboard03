package com.freeboard03.api.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.board.BoardEntity;
import com.freeboard03.domain.board.BoardRepository;
import com.freeboard03.domain.board.enums.SearchType;
import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
@Transactional
@WebAppConfiguration
@Rollback(value = false)
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

    /*
    @BeforeEach
    public void initMvc() {
        testUser = userRepository.findAll().get(0);
        UserForm userForm = UserForm.builder().accountId(testUser.getAccountId()).password(testUser.getPassword()).build();
        mockHttpSession.setAttribute("USER", userForm);

        testBoard = boardRepository.findAllByWriterId(testUser.getId()).get(0);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }*/

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
                .andExpect(content().json("{'contents':'"+boardForm.getContents()+"'}"));;
    }

    @Test
    @DisplayName("올바른 패스워드를 입력한 경우 데이터 수정이 가능하다.")
    public void updateTest() throws Exception {
        BoardForm updateForm = BoardForm.builder().title("제목을 입력하세요").contents("수정된 데이터입니다 ^^*").build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/api/boards/"+testBoard.getId())
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(status().isOk());
    }

    /*@Test
    @DisplayName("잘못된 패스워드를 입력한 경우 데이터는 삭제되지 않고 false를 반환한다.")
    public void deleteTest1() throws Exception {
        UserEntity wrongUser = userRepository.findAll().get(1);
        BoardEntity wrongBoard = boardRepository.findAllByWriterId(wrongUser.getId()).get(0);

        mvc.perform(delete("/api/boards/"+wrongBoard.getId())
                .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }*/

    @Test
    @DisplayName("올바른 패스워드를 입력한 경우 데이터를 삭제하고 true를 반환한다.")
    public void deleteTest2() throws Exception {
        mvc.perform(delete("/api/boards/"+testBoard.getId())
                .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("게시판 검색 테스트-타이틀")
    public void searchTest() throws Exception {
        String keyword = "test";
        mvc.perform(get("/api/boards?type="+SearchType.TITLE+"&keyword="+keyword)
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 검색 테스트-글 작성자")
    public void searchTest2() throws Exception {
        String keyword = "yerin";
        mvc.perform(get("/api/boards?type="+SearchType.WRITER+"&keyword="+keyword)
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }

}
