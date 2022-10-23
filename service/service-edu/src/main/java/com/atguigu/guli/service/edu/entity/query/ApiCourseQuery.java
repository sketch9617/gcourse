package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiCourseQuery {
    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;

    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;
    @ApiModelProperty(value = "排序字段: 0 默认销量排序,1 发布时间排序 ,2 价格排序")
    private Integer orderByColumn = 0;
    @ApiModelProperty(value = "排序方式: 0 默认降序排序,1 升序排列")
    private Integer type = 0;
}
