package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(AdminCourseInfoVo courseInfoVo);

    AdminCourseInfoVo getCourseInfo(String courseId);

    Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize);

    void updateCourseVoById(AdminCourseInfoVo vo, String courseId);

    AdminCourseItemVo getCoursePublishVo(String id);

    List<Course> queryCoursesByCondition(ApiCourseQuery courseQuery);

    ApiCourseDetailVo getCourseDetailVo(String id);
}
