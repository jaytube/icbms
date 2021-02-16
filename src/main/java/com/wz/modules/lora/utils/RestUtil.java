package com.wz.modules.lora.utils;

import com.alibaba.fastjson.JSON;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.service.LoRaCommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: RestUtil
 */
@Component
@Slf4j
public class RestUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoRaCommandService loRaCommandService;

    public static final String HTTP_HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * JWT TOKEN值
     */
    private static final String _jwt_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb3JhLWFwcC1zZXJ2ZXIiLCJhdWQiOiJ" +
            "sb3JhLWFwcC1zZXJ2ZXIiLCJuYmYiOjE1Mzc0MDgzMDMsImV4cCI6MzMwOTQzMTcxMDMsInN1YiI6I" +
            "nVzZXIiLCJ1c2VybmFtZSI6ImFkbWluIn0.14eVliflc5oG5FJXIphEfcWbc5A4DxzTk-u5AMaIsJc";

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doGet(String url) {
        log.info("【doGet】【请求URL】：{}", url);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("grpc-metadata-authorization", _jwt_token);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doGet】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doPost(String url, Map<String, Object> params) {
        log.info("【doPost】【请求URL】：{}", url);
        log.info("【doPost】【请求入参】：{}", params);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("grpc-metadata-authorization", _jwt_token);
        HttpEntity<Map> requestEntity = new HttpEntity<>(params, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doPost】【请求响应】：{}", response);
        return response(url, params, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doGetNoToken(String url) {
        log.info("【doGetNoToken】【请求URL】：{}", url);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        HttpStatus statusCode = exchange.getStatusCode();
        Map response = exchange.getBody();
        log.info("【doGetNoToken】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doPostFormDataNoToken(String url, Map<String, Object> paramsMap) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            params.add(entry.getKey(), entry.getValue().toString());
        }
        log.info("【doPostFormDataNoToken】【请求URL】：{}", url);
        log.info("【doPostFormDataNoToken】【请求入参】：{}", params);
        RestTemplate restTemplate = new RestTemplate();
        //设置请求头(注意会产生中文乱码)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        //发送请求，设置请求返回数据格式为String
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
        Map response = responseEntity.getBody();
        log.info("【doPostFormDataNoToken】【请求响应】：{}", response);
        return response(url, paramsMap, responseEntity);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doGetWithToken(String gatewayIp, String url) {
        log.info("【doGetWithToken】【请求URL】：{}", url);
        HttpHeaders requestHeaders = createHeader(gatewayIp);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doGetWithToken】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doGetWithToken(String url, HttpHeaders requestHeaders) {
        log.info("【doGetWithToken】【请求URL】：{}", url);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doGetWithToken】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doPostWithToken(String gatewayIp, String url, Map<String, Object> params) {
        log.info("【doPostWithToken】【请求URL】：{}", url);
        log.info("【doPostWithToken】【请求入参】：{}", params);
        HttpHeaders requestHeaders = createHeader(gatewayIp);
        HttpEntity<Map> requestEntity = new HttpEntity<>(params, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doPostWithToken】【请求响应】：{}", response);
        return response(url, params, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doDeleteWithToken(String gatewayIp, String url) {
        log.info("【doDeleteWithToken】【请求URL】：{}", url);
        HttpHeaders requestHeaders = createHeader(gatewayIp);
        HttpEntity<Map> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doDeleteWithToken】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    @Retryable(value = RestClientException.class, maxAttempts = 2,
            backoff = @Backoff(delay = 500L, multiplier = 2))
    public CommonResponse<Map> doDeleteWithToken(String gatewayIp, String url, List<Map> body) {
        log.info("【doDeleteWithToken】【请求URL】：{}", url);
        HttpHeaders requestHeaders = createHeader(gatewayIp);
        HttpEntity<List> requestEntity = new HttpEntity<>(body, requestHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Map.class);
        Map response = exchange.getBody();
        log.info("【doDeleteWithToken】【请求响应】：{}", response);
        return response(url, null, exchange);
    }

    private CommonResponse<Map> response(String url, Map<String, Object> params, ResponseEntity<Map> exchange) {
        if (exchange.getStatusCode() == HttpStatus.OK) {
            return CommonResponse.success(exchange.getBody());
        } else {
            String message;
            if (MapUtils.isEmpty(params)) {
                message = "[URL]:" + url;
            } else {
                String paramsStr = JSON.toJSONString(params);
                message = "[URL]:" + url + "[PARAMS]:" + paramsStr;
            }
            return new CommonResponse(exchange.getStatusCodeValue(), message, exchange.getBody());
        }
    }

    private HttpHeaders createHeader(String gatewayIp) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", HTTP_HEADER_CONTENT_TYPE);
        requestHeaders.add("Authorization", loRaCommandService.getRedisToken(gatewayIp));
        requestHeaders.add("Tenant", loRaCommandService.getDbInstanceFromRedis(gatewayIp, "cluing"));
        return requestHeaders;
    }

}