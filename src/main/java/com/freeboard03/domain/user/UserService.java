package com.freeboard03.domain.user;

import com.freeboard03.api.user.UserForm;
import com.freeboard03.domain.user.enums.UserExceptionType;
import com.freeboard03.domain.user.enums.UserRole;
import com.freeboard03.util.exception.FreeBoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRole findUserRole(UserForm user){
        return userRepository.findByAccountId(user.getAccountId()).getRole();
    }

    public void join(UserForm user) {
        if (userRepository.findByAccountId(user.getAccountId()) != null){
            throw new FreeBoardException(UserExceptionType.DUPLICATED_USER);
        }
        UserEntity newUser = user.convertUserEntity();
        newUser.setRole(UserRole.NORMAL);
        userRepository.save(newUser);
    }

    public void login(UserForm user) {
        UserEntity userEntity = userRepository.findByAccountId(user.getAccountId());
        if (userEntity == null){
            throw new FreeBoardException(UserExceptionType.NOT_FOUND_USER);
        }
        if (userEntity.getPassword().equals(user.getPassword()) == false){
            throw new FreeBoardException(UserExceptionType.WRONG_PASSWORD);
        }
    }

}
