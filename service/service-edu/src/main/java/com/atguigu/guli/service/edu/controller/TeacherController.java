package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/edu/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    //查询所有讲师
    @GetMapping("queryAll")
    public List<Teacher> queryAll(){
        return teacherService.list();
    }

}

