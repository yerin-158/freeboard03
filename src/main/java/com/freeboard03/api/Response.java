package com.freeboard03.api;

import com.freeboard03.domain.BaseExceptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Response<T> {

    private boolean hasError;
    private BaseExceptionType exceptionType;
    private T responseDto;

    public boolean hasError(){
        return hasError;
    }

    public static Response ok () {
        Response response = new Response();
        response.hasError = false;
        return response;
    }

    public static<T> Response ok (T responseDto){
        Response response = new Response();
        response.hasError = false;
        response.responseDto = responseDto;
        return response;
    }

    public static Response error (BaseExceptionType exceptionType){
        Response response = new Response();
        response.hasError = true;
        response.exceptionType = exceptionType;
        return response;
    }

}
