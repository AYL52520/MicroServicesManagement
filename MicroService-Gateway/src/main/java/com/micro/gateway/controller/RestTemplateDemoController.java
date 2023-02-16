package com.micro.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.micro.gateway.constant.InMemoryResource;
import com.micro.gateway.constant.PaspspRet;
import com.micro.gateway.data.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;

/**
 * @Classname RestTemplateDemoController
 * 测试RestTemplate的使用
 * @Version 1.0.0
 * @Date 2023/2/5 16:06
 * @Created by yangle
 */
@Log4j2
@RestController
public class RestTemplateDemoController {

    @Autowired
    private RestTemplate restTemplate;
    /**
     * 用于测试restTemp发送文件请求
     */
    @PostMapping("/dome3")
    public Response m3(@RequestBody JSONObject params) throws Exception{

        final String url = "http://localhost:8888/hello/m3";
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // 设置请求体，注意是 LinkedMultiValueMap
        // 下面两个流从文件服务下载，这边省略（注意最后关闭流）

       try( InputStream fis2 = new FileInputStream(new File("D://1.png"));
            InputStream fis1 = new FileInputStream(new File("D://1.png"))){

        InMemoryResource resource1 = new InMemoryResource("file1.jpg", "description1", FileCopyUtils.copyToByteArray(fis1), System.currentTimeMillis());

        InMemoryResource resource2 = new InMemoryResource("file2.jpg","description2", FileCopyUtils.copyToByteArray(fis2), System.currentTimeMillis());
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", resource1);
        form.add("file", resource2);
        form.add("param1","value1");

        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        JSONObject s = restTemplate.postForObject(url, files, JSONObject.class);

        return Response.ok(s);
       }catch (IOException e){
           //失败时返回错误原因
           log.error("文件上传时出现异常{}",e.getMessage());
           return Response.error(PaspspRet.SDK_UPLOAD_ERROR);
       }
    }

    //也可用于下载文件
    private InputStream downLoadVideoFromVod(String url)  {
        byte[] bytes = restTemplate.getForObject(url, byte[].class);
        return new ByteArrayInputStream(bytes);
    }
}
