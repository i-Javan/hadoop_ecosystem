package com.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * @param url
     * @param method
     * @param params
     * @return // get 请求
     * public <T> T getForObject();
     * public <T> ResponseEntity<T> getForEntity();
     * <p>
     * // head 请求
     * public HttpHeaders headForHeaders();
     * <p>
     * // post 请求
     * public URI postForLocation();
     * public <T> T postForObject();
     * public <T> ResponseEntity<T> postForEntity();
     * <p>
     * // put 请求
     * public void put();
     * <p>
     * // pathch
     * public <T> T patchForObject
     * <p>
     * // delete
     * public void delete()
     * <p>
     * // options
     * public Set<HttpMethod> optionsForAllow
     * <p>
     * // exchange
     * public <T> ResponseEntity<T> exchange()
     */
    public Map client(String url, HttpMethod method, MultiValueMap<String, Object> params) {
        logger.info("请求接口：{}，{}，{}，{}", url, method, params.toString());
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap> entity = new HttpEntity(params,headers);
        ResponseEntity<Map> exchange = template.exchange(url, method, entity, Map.class);
        return exchange.getBody();
    }

    public Map get(String url) {
        logger.info("请求get接口：{}", url);
        RestTemplate template = new RestTemplate();
        String str= template.getForObject(url, String.class);
        return new Gson().fromJson(str,Map.class);
    }

    public String post(String url,MultiValueMap<String, Object> params) {
        logger.info("请求post接口：{}", url);
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> formEntity = new HttpEntity<String>(params.toString(), headers);
        ResponseEntity<String> response1 = template.postForEntity(url,formEntity,String.class);
        return response1.getBody();
    }
}
