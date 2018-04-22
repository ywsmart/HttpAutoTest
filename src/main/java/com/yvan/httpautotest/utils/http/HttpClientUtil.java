package com.yvan.httpautotest.utils.http;

import com.yvan.httpautotest.model.http.HttpClientRequest;
import com.yvan.httpautotest.model.http.HttpClientResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Function：http工具类
 * Created by YangWang on 2018-04-22 15:59.
 */
public class HttpClientUtil {
    private CloseableHttpClient httpClient;
    private static Logger logger = Logger.getLogger(HttpClientUtil.class);

    public static HttpClientResponse doGet(HttpClientRequest httpClientRequest) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        httpClientUtil.init();
        HttpGet httpGet = new HttpGet(httpClientRequest.getUrl());
        return httpClientUtil.sendRequest(httpGet, httpClientRequest);
    }

    public static HttpClientResponse doPost(HttpClientRequest httpClientRequest) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        httpClientUtil.init();
        HttpPost httpPost = new HttpPost(httpClientRequest.getUrl());
        return httpClientUtil.sendRequest(httpPost, httpClientRequest);
    }

    public static HttpClientResponse doPut(HttpClientRequest httpClientRequest) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        httpClientUtil.init();
        HttpPut httpPut = new HttpPut(httpClientRequest.getUrl());
        return httpClientUtil.sendRequest(httpPut, httpClientRequest);
    }

    public static HttpClientResponse doDelete(HttpClientRequest httpClientRequest) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        httpClientUtil.init();
        HttpDelete httpDelete = new HttpDelete(httpClientRequest.getUrl());
        return httpClientUtil.sendRequest(httpDelete, httpClientRequest);
    }

    /**
     * 初始化http连接
     */
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        logger.info("Start init http connection!");
    }

    /**
     * 发送http请求
     */
    private HttpClientResponse sendRequest(HttpRequestBase httpRequestBase, HttpClientRequest httpClientRequest) {
        HttpClientResponse httpClientResponse = new HttpClientResponse();
        String encodingOfBody = "ISO-8859-1";
        Map<String, String> requestHeaders = httpClientRequest.getHeaders();
        for (String key :
                requestHeaders.keySet()) {
            httpRequestBase.setHeader(key, requestHeaders.get(key));
            if ("content-type".equals(key.toLowerCase())) {
                String contentType = requestHeaders.get(key);
                if (contentType.split(":").length >= 2) {
                    encodingOfBody = contentType.split(":")[1].split("=")[1];
                }
            }
        }
        // 判断httpRequestBase是否为HttpEntityEnclosingRequestBase的实例，是则添加body
        if (httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(new StringEntity(httpClientRequest.getBody(), encodingOfBody));
        }
        try {
            // 发送http请求，并获取
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequestBase);
            String statusCode = httpResponse.getStatusLine().toString().split(" ")[1];
            logger.info(httpResponse.getStatusLine().toString());
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
            this.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return httpClientResponse;
    }

    /**
     * 关闭http连接
     */
    private void close() {
        try {
            httpClient.close();
            logger.info("Close http connection successfully!");
        } catch (IOException e) {
            logger.error("Close http connection failed.");
            logger.error(e.getMessage());
        }
    }

    public String formatUrl(String url, Map<String, String> queryParams) {
        String result = "";
        StringBuilder params = new StringBuilder();
        for (String key :
                queryParams.keySet()) {
            params.append(key).append("=").append(queryParams.get(key)).append("&");
        }
        result += (url +"?"+params.substring(0,params.length()-1));
        return result;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
