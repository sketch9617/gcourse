package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
 //跨域
@RequestMapping("admin/oss")
public class AdminOssController {
    @Autowired
    OssService ossService;

    //测试接口
    @PostMapping("test")
//    public R test(String username,String password){
    public R test(@RequestBody BaseEntity baseEntity){
//        System.out.println("username = " + username);
//        System.out.println("password = " + password);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(baseEntity);
        return R.ok();
    }


    //1、上传文件到oss
    @PostMapping("upload")
    public R upload(MultipartFile file,String module){//file 表示前端上传文件表单项的name值必须为file
        String path  = ossService.upload(file,module);
        return R.ok().data("path",path);//返回上传成功的文件路径
    }
    //2、删除oss指定路径的文件
    @DeleteMapping("delete")
    public R delete(String path , String module){
        ossService.delete(path,module);
        return R.ok();
    }
}
