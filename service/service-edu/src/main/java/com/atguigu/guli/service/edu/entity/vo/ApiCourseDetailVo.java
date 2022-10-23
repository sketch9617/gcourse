package com.atguigu.guli.service.edu.entity.vo;

import com.atguigu.guli.service.edu.entity.Chapter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ApiCourseDetailVo {
    @ApiModelProperty(value = "id" , hidden = true)//swagger 生成的文档中隐藏该属性
    private String id;
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;
    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;
    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;
    @ApiModelProperty(value = "销售数量")
    private Long buyCount;
    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;
    //课程简介
    @ApiModelProperty(value = "课程简介")
    private String description;
    //讲师数据
    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer teacherLevel;
    @ApiModelProperty(value = "讲师头像")
    private String teacherAvatar;

    //课程分类
    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;
    @ApiModelProperty(value = "课程专业父级名称")
    private String subjectParentTitle;
    @ApiModelProperty(value = "课程专业2级标题")
    private String subjectTitle;
    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;
    //一个课程有多个章  一个章有多个课时
    //一对多：List
    //章节嵌套集合
    @ApiModelProperty(value = "章节列表")
    private List<Chapter> chapters;

}
