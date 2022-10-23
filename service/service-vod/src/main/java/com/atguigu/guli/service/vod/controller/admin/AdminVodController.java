package com.atguigu.guli.service.vod.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("/admin/vod")
public class AdminVodController {
    @Autowired
    VodService vodService;
    //1、上传视频：通过工作流处理
    /*
    多部件：
        多个部件提交
            每个部件就是一个文件表单项
     */
    @PostMapping("upload")
    public R upload(MultipartFile video){
        String videoId = vodService.upload(video);
        return R.ok().data("id",videoId);
    }
    //2、获取视频播放凭证(用户使用)
    @GetMapping("getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable("videoId") String videoId){
        String playAuth = vodService.getPlayAuth(videoId);
        return R.ok().data("playAuth",playAuth);
    }
}
