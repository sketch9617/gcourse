package com.atguigu.guli.service.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberDto {
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "id")//swagger 生成的文档中隐藏该属性
    private String id;
}
