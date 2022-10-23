package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("/api/vod")
public class ApiVodController {
    /*
        admin： 管理员RBAC方式管理权限 精确到每个接口管理员是否有权限访问
        api：  用户 登录和 会员 购买等判断
     */
    @Autowired
    VodService vodService;
    //2、获取视频播放凭证(用户使用)
    @GetMapping("getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable("videoId") String videoId){
        String playAuth = vodService.getPlayAuth(videoId);
        return R.ok().data("playAuth",playAuth);
    }
}
