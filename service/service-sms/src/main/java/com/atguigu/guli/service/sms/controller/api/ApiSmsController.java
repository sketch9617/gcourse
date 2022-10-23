package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户端 短信模块")
@RestController
@RequestMapping("/api/sms")

public class ApiSmsController {
    @Autowired
    SmsService smsService;
    //1、发送短信验证码
    @GetMapping("sendMsg/{mobile}")
    public R sendMsg(@PathVariable("mobile")String mobile){
        //不需要返回结果：验证码是发送到用户的手机中的
        smsService.sendMsg(mobile);
        return R.ok();
    }
}
