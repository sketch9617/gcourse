package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String upload(MultipartFile file,String module);

    void delete(String path, String module);
}
