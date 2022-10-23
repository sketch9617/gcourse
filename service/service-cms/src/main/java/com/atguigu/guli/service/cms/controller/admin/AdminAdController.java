package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController

@RequestMapping("/admin/cms/ad")
public class AdminAdController {

    @Autowired
    AdService adService;
    //1、查询所有
    @GetMapping
    public R queryAll(){
        return R.ok().data("items" , adService.list(new LambdaQueryWrapper<Ad>()
                .orderByDesc(Ad::getSort)));
    }
    //2、根据id查询
    @GetMapping("{id}")
    public R getById(@PathVariable("id")String id){
        return R.ok().data("item",adService.getById(id));
    }
    //3、更新ad
    @PutMapping
    public R updateById(@RequestBody Ad ad){
        adService.updateById(ad);
        return R.ok();
    }
    //4、新增ad
    @PostMapping
    public R save(@RequestBody Ad ad){
        adService.save(ad);
        return R.ok();
    }
    //5、删除ad
    @DeleteMapping("{id}")
    public R deleteById(@PathVariable("id")String id){
        adService.removeById(id);
        return R.ok();
    }
}

