package com.atguigu.guli.service.base.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
//@Accessors(chain = true)
public class BaseEntity {
    private String id;
    private Date gmtCreate;
    private Date gmtModified;

}
