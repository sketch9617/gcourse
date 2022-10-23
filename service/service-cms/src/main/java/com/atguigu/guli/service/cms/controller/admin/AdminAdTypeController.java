package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.service.AdTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController

@RequestMapping("/admin/cms/ad-type")
public class AdminAdTypeController {
    @Autowired
    AdTypeService adTypeService;
    //1、查询所有
    @GetMapping
    public R queryAll(){
        return R.ok().data("items" , adTypeService.list());
    }
    //2、根据id查询
    @GetMapping("{id}")
    public R getById(@PathVariable("id")String id){
        return R.ok().data("item",adTypeService.getById(id));
    }
    //3、更新ad
    @PutMapping
    public R updateById(@RequestBody AdType adType){
        adTypeService.updateById(adType);
        return R.ok();
    }
    //4、新增ad
    @PostMapping
    public R save(@RequestBody AdType adType){
        adTypeService.save(adType);
        return R.ok();
    }
    //5、删除ad
    @DeleteMapping("{id}")
    public R deleteById(@PathVariable("id")String id){
        adTypeService.removeById(id);
        return R.ok();
    }
}

