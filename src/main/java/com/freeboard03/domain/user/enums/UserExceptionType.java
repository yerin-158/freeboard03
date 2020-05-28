package com.freeboard03.domain.user.enums;

import com.freeboard03.domain.BaseExceptionType;
import lombok.Getter;

@Getter
public enum UserExceptionType implements BaseExceptionType {

    NOT_FOUND_USER(1001, 200, "해당하는 사용자가 존재하지 않습니다."),
    DUPLICATED_USER(1002, 200, "이미 존재하는 사용자 아이디입니다."),
    WRONG_PASSWORD(1003, 200, "패스워드를 잘못 입력하였습니다."),
    LOGIN_INFORMATION_NOT_FOUND(1004, 200, "로그인 정보를 찾을 수 없습니다. (세션 만료)");

    private int errorCode;
    private int httpStatus;
    private String errorMessage;

    UserExceptionType(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
