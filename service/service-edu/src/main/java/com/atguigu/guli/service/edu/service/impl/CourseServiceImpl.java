package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.atguigu.guli.service.edu.mapper.CourseDescriptionMapper;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseDescriptionMapper courseDescriptionMapper;
    @Override
    public String saveCourseInfo(AdminCourseInfoVo courseInfoVo) {
        //保存课程数据
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoVo,course);
        this.save(course);
        //保存课程简介数据
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionMapper.insert(courseDescription);
        //返回新增的课程id
        return course.getId();
    }

    @Override
    public AdminCourseInfoVo getCourseInfo(String courseId) {
        Course course = this.getById(courseId);
        CourseDescription courseDescription = courseDescriptionMapper.selectById(courseId);
        AdminCourseInfoVo vo = new AdminCourseInfoVo();
        BeanUtils.copyProperties(course , vo);
        vo.setDescription(courseDescription.getDescription());
        return vo;
    }

    @Override
    public Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize) {
        /*
            mp自定义分页查询时，如果配置了分页拦截器，只需要将分页的页码和pageSize设置给一个page对象并传入到
            自定义的mapper方法中，mp的分页拦截器会自动使用分页条件生成limit 条件
         */
        Page<AdminCourseItemVo> page = new Page<>(pageNum,pageSize);
        List<AdminCourseItemVo> data= baseMapper.selectCourseItemVoPage(page);
        page.setRecords(data);//自定义分页查询返回的结果时查询的数据集合  需要手动设置到page对象的分页数据集合中
        return page;
    }

    @Override
    public void updateCourseVoById(AdminCourseInfoVo vo, String courseId) {
        Course course= new Course();
        BeanUtils.copyProperties(vo , course);
        course.setId(courseId);
        baseMapper.updateById(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(vo.getDescription());
        courseDescription.setId(courseId);
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public AdminCourseItemVo getCoursePublishVo(String id) {
        /*
            自定义Mapper方法：使用QueryWrapper动态传参

         */
        QueryWrapper<Course> queryWrapper = new QueryWrapper();//泛型一般使用数据库存在的表对应的bean
        //id需要指定从哪张表获取
        queryWrapper.eq("t1.id" , id);
        return baseMapper.selectCoursePublishVo(queryWrapper);
    }

    @Override
    public List<Course> queryCoursesByCondition(ApiCourseQuery courseQuery) {

        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();

        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        Integer orderByColumn = courseQuery.getOrderByColumn();//0 1 2
        Integer type = courseQuery.getType();
        if(StringUtils.isNotEmpty(subjectParentId)){//一级分类id不为空
            queryWrapper.eq(Course::getSubjectParentId , subjectParentId);
        }
        if(StringUtils.isNotEmpty(subjectId)){//二级分类id不为空
            queryWrapper.eq(Course::getSubjectId , subjectId);
        }
        switch (orderByColumn){
//            case 0:  默认
//                break;
            case 1:
                if(type==1){
                    queryWrapper.orderByAsc(Course::getPublishTime);
                }else{
                    queryWrapper.orderByDesc(Course::getPublishTime);
                }
                break;
            case 2:
                if(type==1){
                    queryWrapper.orderByAsc(Course::getPrice);
                }else{
                    queryWrapper.orderByDesc(Course::getPrice);
                }
                break;
            default:
                if(type==1){
                    queryWrapper.orderByAsc(Course::getBuyCount);
                }else{
                    queryWrapper.orderByDesc(Course::getBuyCount);
                }
                break;
        }

        queryWrapper.eq(Course::getStatus,"Normal");

        return this.list(queryWrapper);
    }

    @Override
    public ApiCourseDetailVo getCourseDetailVo(String id) {
        ApiCourseDetailVo courseDetailVo = baseMapper.selectCourseDetailVoById(id);
        //更新课程的浏览数量
        courseDetailVo.setViewCount(courseDetailVo.getViewCount()+1);
        //持久化到数据库
        Course course = new Course();
        course.setId(id);
        course.setViewCount(courseDetailVo.getViewCount());
        baseMapper.updateById(course);
        return courseDetailVo;
    }
}
