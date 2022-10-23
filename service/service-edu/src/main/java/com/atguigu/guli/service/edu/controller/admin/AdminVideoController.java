package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController

@RequestMapping("/admin/edu/video")
public class AdminVideoController {
    @Autowired
    VideoService videoService;
    //1、新增章
    @PostMapping("save")
    public R save(@RequestBody Video video){
        videoService.save(video);
        return R.ok();
    }
    //2、删除章
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@PathVariable("id")String id){
        videoService.removeById(id);
        return R.ok();
    }
    //3、查询指定id章
    @GetMapping("getById/{id}")
    public R getById(@PathVariable("id")String id){
        return R.ok().data("item",videoService.getById(id));
    }
    //4、更新章
    @PutMapping("updateById")
    public R updateById(@RequestBody Video video){
        videoService.updateById(video);
        return R.ok();
    }
}

