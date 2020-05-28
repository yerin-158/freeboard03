package com.freeboard03.util.exception;

import com.freeboard03.domain.BaseExceptionType;
import lombok.Getter;

public class FreeBoardException extends RuntimeException {

    @Getter
    private BaseExceptionType exceptionType;

    public FreeBoardException(BaseExceptionType exceptionType){
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

}
