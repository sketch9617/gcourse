package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice 处理异常后返回的结果会当做视图名称转发
@RestControllerAdvice //处理异常后的返回结果会转为json响应
@Slf4j
public class GlobalExceptionHandler {
    //自定义类型异常的处理器
    @ExceptionHandler(value = GuliException.class)
    public R exception(GuliException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.fail().message(e.getMsg()).code(e.getCode());
    }


    //声明某个方法处理那个异常
    @ExceptionHandler(value = Exception.class)//该注解声明的方法就是一个异常处理器
    public R exception(Exception e){
        //持久化错误日志
//        e.printStackTrace();
        //FATAL (崩溃的), ERROR(错误：生产环境使用) , WARN(警告) ,INFO(信息:使用最多) , DEBUG(调试：使用少，研究第三方框架时使用)
        log.error(ExceptionUtils.getStackTrace(e));//将异常对象的信息转未字符串
        return R.fail();
    }
    //以前抛出的系统提供的异常，只能获取到异常的堆栈信息和异常的message
    //只要创建一个自定义异常类对系统异常进行扩展，提供成员变量，记录出现异常的原因和错误状态码等信息
    //再创建一个自定义异常的处理器，就可以从异常对象中获取到code和message设置给R对象，返回给前端
    //具体异常的处理器：BadSqlGrammarException
    @ExceptionHandler(value = BadSqlGrammarException.class)//该注解声明的方法就是一个异常处理器
    public R exception(BadSqlGrammarException e){
        //持久化错误日志
//        e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

}
