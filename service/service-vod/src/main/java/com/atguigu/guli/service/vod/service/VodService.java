package com.atguigu.guli.service.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VodService {
    String upload(MultipartFile video);

    String getPlayAuth(String videoId);
}
