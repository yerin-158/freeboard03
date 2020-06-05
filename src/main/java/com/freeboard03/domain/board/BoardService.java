package com.freeboard03.domain.board;

import com.freeboard03.api.board.BoardForm;
import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.board.entity.specs.BoardSpecs;
import com.freeboard03.domain.board.enums.BoardExceptionType;
import com.freeboard03.domain.board.enums.SearchType;
import com.freeboard03.domain.user.UserEntity;
import com.freeboard03.domain.user.UserRepository;
import com.freeboard03.domain.user.enums.UserExceptionType;
import com.freeboard03.domain.user.specification.HaveAdminRoles;
import com.freeboard03.domain.user.specification.IsWriterEqualToUserLoggedIn;
import com.freeboard03.util.PageUtil;
import com.freeboard03.util.exception.FreeBoardException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BoardService {

    private BoardRepository boardRepository;
    private BoardOperator boardOperator;
    private UserRepository userRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, BoardOperator boardOperator) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardOperator = boardOperator;
    }

    public Page<BoardEntity> get(Pageable pageable) {
        return boardRepository.findAll(PageUtil.convertToZeroBasePageWithSort(pageable));
    }

    public BoardEntity post(BoardForm boardForm, UserForm userForm) {
        UserEntity user = Optional.of(userRepository.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        return boardRepository.save(boardForm.convertBoardEntity(user));
    }

    public void update(BoardForm boardForm, UserForm userForm, ObjectId id) {
        UserEntity user = Optional.of(userRepository.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        BoardEntity target = Optional.of(boardRepository.findById(id).get()).orElseThrow(() -> new FreeBoardException(BoardExceptionType.NOT_FOUNT_CONTENTS));

        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) == false && HaveAdminRoles.confirm(user) == false) {
            throw new FreeBoardException(BoardExceptionType.NO_QUALIFICATION_USER);
        }

        target.update(boardForm.convertBoardEntity(target.getWriter()));
    }

    public void delete(ObjectId id, UserForm userForm) {
        UserEntity user = Optional.of(userRepository.findByAccountId(userForm.getAccountId())).orElseThrow(() -> new FreeBoardException(UserExceptionType.NOT_FOUND_USER));
        BoardEntity target = Optional.of(boardRepository.findById(id).get()).orElseThrow(() -> new FreeBoardException(BoardExceptionType.NOT_FOUNT_CONTENTS));

        if (IsWriterEqualToUserLoggedIn.confirm(target.getWriter(), user) == false && HaveAdminRoles.confirm(user) == false) {
            throw new FreeBoardException(BoardExceptionType.NO_QUALIFICATION_USER);
        }

        boardRepository.deleteById(id);
    }

    public Page<BoardEntity> search(Pageable pageable, String keyword, SearchType type) {
        if (type.equals(SearchType.WRITER)) {
            List<UserEntity> userEntityList = userRepository.findAllByAccountIdRegex(".*" + keyword + ".*");
            return boardRepository.findAllByWriterIn(userEntityList, PageUtil.convertToZeroBasePageWithSort(pageable));
        }
        return boardOperator.findAllByLike(type, keyword, PageUtil.convertToZeroBasePageWithSort(pageable));
    }
}
