package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Controller //必须使用Controller  接口方法需要返回重定向的响应
@RequestMapping("/api/ucenter/wx")
public class ApiWxController {

    @Autowired
    MemberService memberService;
    //1、获取wx登录二维码 wx登录请求
    @GetMapping("login")
    public String wxlogin(HttpSession session){
        return memberService.wxLogin(session);
    }

    //http://localhost:8160/api/ucenter/wx/callback
    @GetMapping("callback")
    public String callback(@RequestParam("code")String code,@RequestParam("state")String state ,
                           HttpSession session){
        return memberService.wxCallback(code,state,session);

    }
}
