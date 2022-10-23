package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public void queryPageByCondition(Page<Teacher> page, TeacherQuery teacherQuery) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();
        if(!StringUtils.isEmpty(name)){//姓名模糊查询
            queryWrapper.like(Teacher::getName,name);
        }
        if(level!=null){//头衔查询
            queryWrapper.eq(Teacher::getLevel,level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)){//入职时间范围查询
            queryWrapper.ge(Teacher::getJoinDate , joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le(Teacher::getJoinDate , joinDateEnd);
        }
        //mp将查询到的结果封装为对象的集合
        // 将查询的一条记录 字段值设置给对象对应的属性(调用set属性名方法设置)
        this.page(page,queryWrapper);
    }
}
