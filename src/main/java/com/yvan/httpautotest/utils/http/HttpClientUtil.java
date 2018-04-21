package com.yvan.httpautotest.utils.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Function：http客户端工具类
 * Created by yawa1hz1 on 2018/4/20 11:17.
 */
public class HttpClientUtil {
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
        httpPost.setHeader("headerName","headerValue");
        try {
            httpPost.setEntity(new StringEntity("post request body"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            // 发送http请求，并获取
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            // 关闭连接
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HttpClientUtil util = new HttpClientUtil();
        util.testMethodPost();
    }
}

