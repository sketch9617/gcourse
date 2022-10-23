package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.config.OssProperties;
import com.atguigu.guli.service.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    OssProperties ossProperties;
    @Override
    public String upload(MultipartFile file,String module) {
        String filePath = file.getOriginalFilename();//获取上传文件的文件名
        String fileName = UUID.randomUUID().toString().replace("-", "")
                + filePath.substring(filePath.lastIndexOf("."));//获取文件名中的扩展名
        String objectName = module+ new DateTime().toString("/yyyy/MM/dd/") + fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());

        try {
            InputStream inputStream =  file.getInputStream();//获取上传的文件的输入流
            // 创建PutObject请求。
            ossClient.putObject(ossProperties.getBucketName(), objectName, inputStream);
            // 获取上传后的文件路径：https://xa220310-guli.oss-cn-chengdu.aliyuncs.com/avatar/2022/07/22/9043fcb86a09407cb9457faf6ac50c18.jpg
            String path = "https://" + ossProperties.getBucketName() + ".oss-cn-chengdu.aliyuncs.com/" + objectName;
            return path;
        } catch (Exception e) {
            //将编译时的异常捕获 转为运行时的异常抛出  因为配置过全局异常处理器，会被异常处理器处理返回失败的R对象
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR,e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void delete(String path, String module) {
        //代表要删除的文件在oss桶内的完整路径   包括 avatar/和后面的所有内容
        String objectName =  path.substring(path.lastIndexOf(module));
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(ossProperties.getBucketName(), objectName);
        } catch (Exception ce) {
            throw new GuliException(ResultCodeEnum.FILE_DELETE_ERROR,ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }


    }
}
