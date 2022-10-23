package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/admin/edu/course")

public class AdminCourseController {
    @Autowired
    CourseService courseService;
    //6、更新课程的发布状态
    @PutMapping("publish/{id}")
    public R publish(@PathVariable("id")String id){
        courseService.update(new LambdaUpdateWrapper<Course>()
                    .set(Course::getStatus , "Normal")
                    .set(Course::getPublishTime , new Date())
                    .eq(Course::getId , id));
        return R.ok();
    }
    //5、根据id查询课程发布信息
    @GetMapping("getCoursePublishVo/{id}")
    public R getCoursePublishVo(@PathVariable("id")String id){
        AdminCourseItemVo courseItemVo = courseService.getCoursePublishVo(id);
        return R.ok().data("item",courseItemVo);
    }
    //4、根据id更新课程信息
    @PutMapping("updateById/{courseId}")
    public R updateById(@RequestBody AdminCourseInfoVo vo , @PathVariable("courseId")String courseId ){
        courseService.updateCourseVoById(vo,courseId);
        return R.ok();
    }
    //3、查询课程列表
    @GetMapping("queryPage/{pageNum}/{pageSize}")
    public R queryPage(@PathVariable("pageNum")Integer pageNum ,
                       @PathVariable("pageSize") Integer pageSize){
        Page<AdminCourseItemVo> page = courseService.queryCourseItemVoPage(pageNum,pageSize);
        return R.ok().data("page" , page);
    }

    //2、查询课程基本信息回显
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable("courseId")String courseId){
        AdminCourseInfoVo vo = courseService.getCourseInfo(courseId);
        return R.ok().data("item",vo);
    }
    //因为前端提交的数据需要存到多张表中  没有现有的bean可以接受  需要自定义javabean
    //1、保存课程基本信息
    @PostMapping("saveCourseInfo")
    public R saveCourseInfo(@RequestBody AdminCourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("id",courseId);
    }
}

