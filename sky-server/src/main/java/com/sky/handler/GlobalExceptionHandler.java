package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final RequestToViewNameTranslator requestToViewNameTranslator;

    public GlobalExceptionHandler(RequestToViewNameTranslator requestToViewNameTranslator) {
        this.requestToViewNameTranslator = requestToViewNameTranslator;
    }

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }


//    /* 1. 业务异常（已存在） */
//    @ExceptionHandler(BaseException.class)
//    public Result baseException(BaseException ex) {
//        log.error("业务异常：{}", ex.getMessage());
//        return Result.error(ex.getMessage());
//    }
//
//    /* 2. 数据库约束异常（已存在） */
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public Result sqlException(SQLIntegrityConstraintViolationException ex) {
//        String msg = ex.getMessage();
//        if (msg.contains("Duplicate entry")) {
//            String[] s = msg.split(" ");
//            return Result.error(s[2] + "已存在");
//        }
//        return Result.error("数据库操作失败");
//    }
//
//    /* 3. ⚠️ 兜底：把剩余所有异常都打印出来 */
//    @ExceptionHandler(Exception.class)
//    public Result exception(Exception ex) {
//        log.error("未知异常：", ex);   // ⬅️ 关键：把堆栈打印出来
//        return Result.error("服务器开小差了：" + ex.getMessage());
//    }

}
