package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController

@RequestMapping("/admin/edu/subject")
public class AdminSubjectController {
    @Autowired
    SubjectService subjectService;
    //6、新增
    @PostMapping("save")
    public R save(@RequestBody Subject subject){
        subjectService.save(subject);
        return R.ok();
    }
    //5、根据id更新课程分类
    @PutMapping("updateById")
    public R updateById(@RequestBody Subject subject){
        subjectService.updateById(subject);
        return R.ok();
    }
    //4、根据id查询指定课程分类
    @GetMapping("getById/{id}")
    public R getById(@PathVariable("id")String id){
        return R.ok().data("item" , subjectService.getById(id));
    }
    //3、根据id删除课程分类
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@PathVariable("id")String id){
        //如果删除的是1级分类 它有二级分类不能直接删除
        long count = subjectService.count(new LambdaQueryWrapper<Subject>()
                .eq(Subject::getParentId, id));
        if(count>0){
            return R.fail().message("存在子分类");
        }
        subjectService.removeById(id);
        return R.ok();
    }


    //2、查询课程分类嵌套集合
    //一级分类：   二级分类集合
    @GetMapping("getNestedSubjects")
    public R getNestedSubjects(){
        // 返回 一级分类的集合 一级分类对象的children属性值就是它的二级分类集合
        List<Subject> pSubjects = subjectService.getNestedSubjects();
        return R.ok().data("items" , pSubjects);
    }
    //1、导入课程分类接口：前端提交的表单项name值为subjects
    @PostMapping("import")
    public R importSubjects(MultipartFile subjects){
        subjectService.importSubjects(subjects);

        return R.ok();
    }

}

