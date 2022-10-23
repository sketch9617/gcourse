package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController

@RequestMapping("/api/edu/teacher")
@Api(tags = "讲师模块") //描述controller的
public class ApiTeacherController {
    @Autowired
    TeacherService teacherService;
    //查询首页热门的4个讲师
    @GetMapping("getHotTeachers")
    public R getHotTeachers(){
        List<Teacher> teachers = teacherService.list(new LambdaQueryWrapper<Teacher>()
                .select(Teacher::getId, Teacher::getName, Teacher::getAvatar)
                .orderByDesc(Teacher::getSort)
                .last("limit 4"));
        return R.ok().data("items",teachers);
    }
    //查询讲师详情
    @GetMapping("{id}")
    public R queryTeacher(@PathVariable("id")String id){
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>()
                .eq(Teacher::getId, id)
                .select(Teacher::getAvatar, Teacher::getLevel, Teacher::getName, Teacher::getCareer,
                        Teacher::getIntro, Teacher::getId));
        return R.ok().data("item",teacher);
    }
    //查询所有讲师
    @ApiOperation(value = "查询所有讲师") //描述接口
    @GetMapping
    public R queryAll(){
        try {
//            int  i = 1/0;
            List<Teacher> teachers = teacherService.list();
            return R.ok().data("items" , teachers);
        } catch (Exception e) {
            return R.fail().message("除数不能为0");
        }
    }


}

