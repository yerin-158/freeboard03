package com.freeboard01.domain.user.specification;

import com.freeboard01.domain.user.UserEntity;

public class IsWriterEqualToUserLoggedIn {

    public static boolean confirm(UserEntity writer, UserEntity loginUser) {
        return writer.getAccountId() == loginUser.getAccountId();
    }
}
