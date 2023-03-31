package com.micro.service.filter;

import com.micro.service.data.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import yang.micro.exception.SDKException;
import yang.micro.exception.SDKExceptionEnums;

/**
 * @Classname GlobalErrorHandler
 * GlobalErrorHandler全局错误信息
 * @Version 1.0.0
 * @Date 2023/3/30 16:21
 * @Created by yangle
 */
@ControllerAdvice
@Log4j2
public class GlobalErrorHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handleException(Exception e) {
        log.info("全局异常处理生效!");
        if(e instanceof SDKException){
            return Response.error((SDKExceptionEnums) ((SDKException) e).getExceptionEnums());
        }else {
            return Response.error(SDKExceptionEnums.ERROR, e.getMessage());
        }
    }

}
