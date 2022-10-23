package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.base.utils.JwtInfo;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.ApiMemberVo;
import com.atguigu.guli.service.ucenter.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@RestController
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {
    @Autowired
    MemberService memberService;

    @GetMapping("set")
    public R set(HttpSession session){
        session.setAttribute("skey" , "hehe");
        return R.ok();
    }
    @GetMapping("get")
    public R get(HttpSession session){
        System.out.println("skey : "+ session.getAttribute("skey"));
        return R.ok();
    }
    //4、创建订单查询用户个人信息
    @GetMapping("getMemberDto/{id}")//远程访问接口 不需要考虑鉴权，我们只考虑用户访问时的权限
    public R getMemberDto(@PathVariable("id")String id){
        Member member = memberService.getById(id);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member , memberDto);
        return R.ok().data("item", memberDto);
    }

    //3、解析token获取用户信息
    @GetMapping("auth/getUserInfo")// auth表示该接口登录后才可以访问
    public R getUserInfo(HttpServletRequest request){//请求报文对象
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);//从请求头中获取tokenkey对应的token
        return R.ok().data("item" , jwtInfo);
    }
    //2、用户认证
    @PostMapping("login")
    public R login(@RequestParam("mobile") String mobile ,
                   @RequestParam("password") String password){
        String token = memberService.login(mobile,password);
        return R.ok().data("token",token);
    }
    //1、注册
    @PostMapping("register")
    public R register(@RequestBody ApiMemberVo  memberVo){
        memberService.register(memberVo);
        return R.ok();
    }

}

