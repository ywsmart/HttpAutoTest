package com.yvan.httpautotest.utils.http;

import com.yvan.httpautotest.model.http.HttpClientRequest;
import com.yvan.httpautotest.model.http.HttpClientResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Function：http客户端工具类初始化
 * Created by yawa1hz1 on 2018/4/20 11:17.
 */
public class HttpClientUtilInit {
    private static Logger logger = Logger.getLogger(HttpClientUtilInit.class);

    public void testMethod() {
        // 初始化http连接
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 定义唯一标识URL
        String url = "http://localhost:19090/getClassName?name=value";
        // 定义一个类型对象
        HttpGet httpGet = new HttpGet(url);
        try {
            // 发送http请求
            httpClient.execute(httpGet);
            // 关闭连接
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testMethodPost() {
        // 初始化http连接
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 定义唯一标识url
        String url = "http://localhost:19090/getClassName?name=value";
        // 定义一个类型对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("headerName", "headerValue");
        try {
            httpPost.setEntity(new StringEntity("post request body"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            // 发送http请求，并获取
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            logger.info(httpResponse.getStatusLine().toString().split(" ")[1]);
            Header[] headers = httpResponse.getAllHeaders();
            for (Header header :
                    headers) {
                logger.info(header.getName() + ": " + header.getValue());
            }
            HttpEntity entity = httpResponse.getEntity();
            InputStream inputStream = entity.getContent();
            String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            logger.info(body);
            // 关闭连接
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpClientResponse doPost(HttpClientRequest request) {
        HttpClientResponse httpClientResponse = new HttpClientResponse();

        // 初始化http连接
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 定义唯一标识url
        String url = request.getUrl();
        // 定义一个类型对象
        HttpPost httpPost = new HttpPost(url);
        Map<String, String> requestHeaders = request.getHeaders();
        for (String key :
                requestHeaders.keySet()) {
            httpPost.setHeader(key, requestHeaders.get(key));
        }

        try {
            ((HttpPost) httpPost).setEntity(new StringEntity(request.getBody()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            // 发送http请求，并获取
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            String statusCode = httpResponse.getStatusLine().toString();
            logger.info(httpResponse.getStatusLine().toString().split(" ")[1]);
            httpClientResponse.setStatusCode(statusCode);

            Header[] headers = httpResponse.getAllHeaders();
            Map<String, String> responseHeaders = new HashMap<String, String>();
            for (Header header :
                    headers) {
                logger.info(header.getName() + ": " + header.getValue());
                responseHeaders.put(header.getName(), header.getValue());
            }
            httpClientResponse.setHeaders(responseHeaders);
            HttpEntity entity = httpResponse.getEntity();
            InputStream inputStream = entity.getContent();
            String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            logger.info(body);
            httpClientResponse.setBody(body);
            // 关闭连接
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpClientResponse;
    }

    public static void main(String[] args) {
        HttpClientUtilInit util = new HttpClientUtilInit();
//        util.testMethodPost();
        HttpClientRequest requst = new HttpClientRequest();
        requst.setUrl("http://localhost:19090/getClassName?name=value");
        Map<String,String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("name","value01");
        requestHeaders.put("name","value02");
        requst.setHeaders(requestHeaders);
        requst.setBody("post body test string");

        util.doPost(requst);
    }
}

