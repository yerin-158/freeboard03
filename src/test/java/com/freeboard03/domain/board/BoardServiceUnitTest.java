package com.freeboard03.domain.board;

import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import com.freeboard03.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class BoardServiceUnitTest {

    @InjectMocks
    private BoardService sut;

    @Mock
    private BoardRepository mockBoardRepo;

    @Mock
    private UserRepository mockUserRepo;

    @Test
    @DisplayName("로그인한 유저와 글을 작성한 유저가 다를 경우 삭제를 진행하지 않는다.")
    public void delete1() {
        UserEntity writer = UserEntity.builder().accountId("mockUser").password("mockPass").build();
        UserForm userLoggedIn = UserForm.builder().accountId("wrongUser").password("wrongUser").build();
        BoardEntity boardEntity = BoardEntity.builder().contents("contents").title("title").writer(writer).build();

        given(mockUserRepo.findByAccountId(anyString())).willReturn(userLoggedIn.convertUserEntity());
        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(boardEntity));

        sut.delete(anyLong(), userLoggedIn);
        verify(mockBoardRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("로그인한 유저와 글을 작성한 유저가 동일할 경우 삭제를 수행한다.")
    public void delete2() {
        final String PASSWORD = "myPass";

        UserForm userForm = UserForm.builder().accountId("mockUser").password(PASSWORD).build();
        UserEntity userLoggedIn = userForm.convertUserEntity();
        BoardEntity boardEntity = BoardEntity.builder().writer(userLoggedIn).contents("contents").title("title").build();

        given(mockUserRepo.findByAccountId(anyString())).willReturn(userLoggedIn);
        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(boardEntity));
        doNothing().when(mockBoardRepo).deleteById(anyLong());

        sut.delete(anyLong(), userForm);
        verify(mockBoardRepo, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("관리자 계정일 경우 삭제를 수행한다.")
    public void delete3() {
        UserForm userLoggedIn = UserForm.builder().accountId("admin").build();
        UserEntity userLoggedInEntity = userLoggedIn.convertUserEntity();
        userLoggedInEntity.setRole(UserRole.ADMIN);
        UserEntity writer = UserEntity.builder().accountId("mockUser").password("mockPass").build();

        BoardEntity boardEntity = BoardEntity.builder().writer(writer).contents("contents").title("title").build();

        given(mockUserRepo.findByAccountId(anyString())).willReturn(userLoggedInEntity);
        given(mockBoardRepo.findById(anyLong())).willReturn(Optional.of(boardEntity));

        sut.delete(anyLong(), userLoggedIn);
        verify(mockBoardRepo, times(1)).deleteById(anyLong());
    }

}
