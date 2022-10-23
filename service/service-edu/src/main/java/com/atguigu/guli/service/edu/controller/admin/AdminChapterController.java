package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 章操作的前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@RestController

@RequestMapping("/admin/edu/chapter")
public class AdminChapterController {

    @Autowired
    ChapterService chapterService;
    //5、查询章节嵌套集合
    @GetMapping("getChaptersAndVideos/{courseId}")
    public R getChaptersAndVideos(@PathVariable("courseId")String courseId){
        /*
            查询返回的数据是
                一个章 对应多个课时  的章的集合

                可以使用chapter类接收章节嵌套集合，但是需要在chapter中添加videos属性保存它的课时集合
         */
        List<Chapter> chapters = chapterService.getChaptersAndVideos(courseId);
        return R.ok().data("items" , chapters);
    }
    //1、新增章
    @PostMapping("save")
    public R save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return R.ok();
    }
    //2、删除章
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@PathVariable("id")String id){
        chapterService.removeById(id);
        return R.ok();
    }
    //3、查询指定id章
    @GetMapping("getById/{id}")
    public R getById(@PathVariable("id")String id){
        return R.ok().data("item",chapterService.getById(id));
    }
    //4、更新章
    @PutMapping("updateById")
    public R updateById(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return R.ok();
    }
}

