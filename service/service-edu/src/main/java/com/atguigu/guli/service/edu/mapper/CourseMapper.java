package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface CourseMapper extends BaseMapper<Course> {
//    @Select("select   ")
    List<AdminCourseItemVo> selectCourseItemVoPage(Page<AdminCourseItemVo> page);
    // 查询课程发布数据
    AdminCourseItemVo selectCoursePublishVo(@Param(value = "ew") QueryWrapper<Course> queryWrapper);

    ApiCourseDetailVo selectCourseDetailVoById(String id);
}
