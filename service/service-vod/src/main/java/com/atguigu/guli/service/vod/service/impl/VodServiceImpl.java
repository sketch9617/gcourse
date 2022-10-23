package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.config.VodProperties;
import com.atguigu.guli.service.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class VodServiceImpl implements VodService {
    @Autowired
    VodProperties vodProperties;
    /*
    自动装配在对象初始化之后执行的，使用自动装配的对象掉方法初始化成员变量会出现空指针异常
     */
    private String accessKeyId;
    private String accessKeySecret;
    private String workflowId;
    private String regionId;

    @PostConstruct //在对象初始化之后调用
    public void init(){
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        workflowId = vodProperties.getWorkFlowId();
        regionId = vodProperties.getRegionId();
    }
    @Override
    public String upload(MultipartFile video) {

        try {
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,
                    accessKeySecret,
                    video.getOriginalFilename(),
                    video.getOriginalFilename(),
                    video.getInputStream());
            /* 视频描述(可选) */
//            request.setDescription("ff的视频");
//        request.setTemplateGroupId();设置模板组(转码加密)
            //配置工作流：
            request.setWorkflowId(workflowId);//设置工作流id
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            if (response.isSuccess()) {
                return response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                log.error("VideoId=" + response.getVideoId()+"ErrorCode=" + response.getCode()+"ErrorMessage=" + response.getMessage());
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
            }
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR,e);
        }

    }

    @Override
    public String getPlayAuth(String videoId) {

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            request.setAuthInfoTimeout(600L);//播放凭证的过期时间：默认是100秒
            response = client.getAcsResponse(request);
            //播放凭证
//            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            return  response.getPlayAuth();
        } catch (Exception e) {
//            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR , e);
        }
    }
}
