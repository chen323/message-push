package com.chen.msgpush.utils;

import com.chen.msgpush.constant.ResponseStatusCode;
import com.chen.msgpush.exception.ApiBussException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author chen
 **/
@Slf4j
@Component
public class HttpUtil {


    @Resource
    private RestTemplate restTemplate;

    /**
     * @param serverUrl 接口地址
     * @param jsonParam json格式的接口入参
     * @Description Post方式调用外部接口
     */
    public String doPost(String serverUrl, String jsonParam) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("调用第三方接口serverUrl={},param={}", serverUrl, jsonParam);
            String postReturnValue = restTemplate.postForObject(serverUrl, jsonParam, String.class);
            // 执行耗时
            log.info("调用第三方接口耗时 : {} ms", System.currentTimeMillis() - startTime);
            return postReturnValue;
        } catch (Exception e) {
            log.error("调用第三方接口serverUrl={},param={},耗时={} ms,出错:", serverUrl, jsonParam, System.currentTimeMillis() - startTime, e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(), e.getMessage(), e);
        }
    }

    /**
     * @param serverUrl 接口地址
     * @param mapParam  form提交
     * @return
     */
    public String doPost(String serverUrl, HttpEntity mapParam) {
        try {
            log.info("调用第三方接口serverUrl={},param={}", serverUrl, mapParam);
            return restTemplate.postForObject(serverUrl, mapParam, String.class);
        } catch (Exception e) {
            log.error("调用第三方接口serverUrl={},param={}出错:", serverUrl, mapParam, e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(), e.getMessage(), e);
        }
    }

    /**
     * 使用restTemplate上传文件
     *
     * @param serverUrl
     * @param file
     * @return
     */
    public String doUpload(String serverUrl, MultipartFile file) {
        try {
            log.info("调用第三方接口serverUrl={},param={}", serverUrl, file.getName());

            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("multipart/form-data");
            // 设置请求的格式类型
            headers.setContentType(type);
            MultiValueMap request = new LinkedMultiValueMap(1);
            ByteArrayResource is = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            request.add("file", is);
            HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(request, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUrl, files, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("调用第三方接口serverUrl={},param={}出错:", serverUrl, file.getName(), e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(), e.getMessage(), e);
        }

    }


    /**
     * @param serverUrl 接口地址
     * @param params    接口入参
     * @Description Get方式调用外部接口
     */
    public String doGet(String serverUrl, Map<String, String> params) {
        try {
            serverUrl = this.getServerUrl(serverUrl, params);
            log.info("调用第三方接口serverUrl={}", serverUrl);
            return restTemplate.getForObject(serverUrl, String.class);
        } catch (Exception e) {
            log.error("调用第三方接口serverUrl={}出错:", serverUrl, e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(), e.getMessage(), e);
        }
    }

    /**
     * @param serverUrl 接口地址
     * @Description Get方式调用外部接口
     */
    public String doGet(String serverUrl) {
        long startTime = System.currentTimeMillis();
        try {
            serverUrl = this.getServerUrl(serverUrl, null);
            log.info("调用第三方接口serverUrl={}", serverUrl);
            String getReturnValue = restTemplate.getForObject(serverUrl, String.class);
            log.info("调用接口响应：{}", getReturnValue);
            // 执行耗时
            log.info("调用第三方接口耗时 : {} ms", System.currentTimeMillis() - startTime);
            return getReturnValue;
        } catch (Exception e) {
            log.error("调用第三方接口serverUrl={},耗时={} ms,出错:", serverUrl, System.currentTimeMillis() - startTime, e);
            throw new ApiBussException(ResponseStatusCode.ABNORMAL.getCode(), e.getMessage(), e);
        }
    }


    /**
     * @Description get请求拼接参数
     */
    public String getServerUrl(String serverUrl, Map<String, String> map) {
        if (null == map) {
            return serverUrl;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(serverUrl).append("?");
        map.forEach(
                (k, v) -> sb.append(k).append("=").append(v).append("&")
        );
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
