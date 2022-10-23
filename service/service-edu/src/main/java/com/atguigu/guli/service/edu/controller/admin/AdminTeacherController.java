package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.feign.OssClient;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/admin/edu/teacher")
@Api(tags = "讲师管理模块") //描述controller的
@Slf4j
 //允许跨域的配置
public class AdminTeacherController {
    @Autowired
    TeacherService teacherService;
    @Autowired
    OssClient ossClient;
    //测试远程访问的接口
    @GetMapping("test")
    public R test(){
//        ossClient.test("FF","123456");
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId("11212312");
        baseEntity.setGmtCreate(new Date());
        baseEntity.setGmtModified(new Date());
        ossClient.test(baseEntity);
        return R.ok();
    }




    //批量删除讲师的接口
    @DeleteMapping("batchDel")
    public R batchDel(@RequestBody List<String> ids){//对象类型直接接受表示pojo入参，List 是接口 springmvc创建对象时会失败
        teacherService.removeByIds(ids);
        return R.ok();
    }
    //修改为带条件的分页查询：
    //分页查询
    @GetMapping("queryPage/{pageNum}/{pageSize}")
    public R queryPage(@PathVariable("pageNum")Integer pageNum ,
                       @PathVariable("pageSize")Integer pageSize,
                       TeacherQuery teacherQuery){//pojo入参：url?name=zhangsan&level=1...
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        teacherService.queryPageByCondition(page,teacherQuery);
        return R.ok().data("page",page);
    }

    //查询所有讲师
    @ApiOperation(value = "查询所有讲师") //描述接口
    @GetMapping("queryAll")
    public R queryAll(){
        List<Teacher> teachers = teacherService.list();
        return R.ok().data("items" , teachers);

    }
    //根据id查询讲师
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getById/{id}")
    public R queryById(@ApiParam(value = "讲师id",required = true,defaultValue = "1") @PathVariable("id")String id){
        return R.ok().data("item",teacherService.getById(id));
    }
    //根据id删除讲师: 增删改一般不会返回数据
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam(value = "讲师id")@PathVariable("id")String id){
        //删除讲师后删除oss、中的头像文件
        Teacher teacher = teacherService.getById(id);
        boolean b = teacherService.removeById(id);
        if(b && !StringUtils.isEmpty(teacher.getAvatar())){//讲师数据删除成功：只要讲师数据库数据删除成功 头像删除成功失败都可以
            //删除讲师头像：尽量保证头像能够成功删除  如果有异常在降级兜底方法中可以保存删除失败的头像地址稍后再做处理
            ossClient.delete(teacher.getAvatar(),"avatar");
        }
        return R.ok();
    }
    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@RequestBody Teacher teacher){
        teacherService.save(teacher);
        return R.ok();
    }
//    @CacheEvict(value = "ads" , key="'cache'" )  使用 value+key拼接 清空键对应的一个缓存
    @CacheEvict(value = {"ads"} , allEntries = true) //清空ads模块下所有的缓存
    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R update(@RequestBody Teacher teacher){
        teacherService.updateById(teacher);
        return R.ok();
    }
}

