package com.yvan.httpautotest.http;

import com.yvan.httpautotest.model.http.HttpClientRequest;
import com.yvan.httpautotest.model.http.HttpClientResponse;
import com.yvan.httpautotest.utils.http.HttpClientUtil;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Function：HttpClientUtil测试类
 * Created by YangWang on 2018-04-22 17:50.
 */
public class HttpClientUtilTest {
    private static Logger logger = Logger.getLogger(HttpClientUtilTest.class);

    @Test
    public void testMethod() {
        HttpClientRequest requst = new HttpClientRequest();
        requst.setUrl("http://localhost:19090/getClassName?name=value");
        Map<String,String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("name01","value01");
        requestHeaders.put("name02","value02");
        requst.setHeaders(requestHeaders);
        requst.setBody("post body test string");

        HttpClientResponse response = HttpClientUtil.doPost(requst);
        Assert.assertEquals("{\"id\": \"\", \"className\": \"testClassName\"}",response.getBody());
    }

    @Test
    public void testFormatUrl(){
        String url ="http://localhost:19090/testurl";
        Map<String,String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("name01","value02");
        requestHeaders.put("name02","value01");
        HttpClientUtil util = new HttpClientUtil();
        logger.info(util.formatUrl(url,requestHeaders));
    }
}
