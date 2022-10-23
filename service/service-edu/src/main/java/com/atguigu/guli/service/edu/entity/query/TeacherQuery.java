package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherQuery {

    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "入驻起始时间")
    private String joinDateBegin;
    @ApiModelProperty(value = "入驻截止时间")
    private String joinDateEnd;
}
