package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

//继承运行时异常，避免以后抛出自定义异常时还需要强制处理
@Data
public class GuliException extends RuntimeException {
    private String msg;
    private Integer code;

    public GuliException(String message,Integer code){
        super(message);//将接受的msg设置给父类的message属性
        this.msg = message;
        this.code = code;
    }
    public GuliException(String message,Integer code,Exception e){
        super(e);//将接受的msg设置给父类的message属性
        this.msg = message;
        this.code = code;
    }
    public GuliException(ResultCodeEnum codeEnum){
        super(codeEnum.getMessage());//将接受的msg设置给父类的message属性
        this.msg = codeEnum.getMessage();
        this.code = codeEnum.getCode();
    }
    //参数3：为了接受出现的真正的异常对象，方便异常处理器获取异常的堆栈日志消息输出
    public GuliException(ResultCodeEnum codeEnum,Exception e){
        super(e);//将接受的msg设置给父类的message属性
        this.msg = codeEnum.getMessage();
        this.code = codeEnum.getCode();
    }
}
