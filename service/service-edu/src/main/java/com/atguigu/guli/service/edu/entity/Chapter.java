package com.atguigu.guli.service.edu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_chapter")
@ApiModel(value="Chapter对象", description="课程")
public class Chapter extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "显示排序")
    private Integer sort;
    @TableField(exist = false)
    private List<Video> videos;
}
