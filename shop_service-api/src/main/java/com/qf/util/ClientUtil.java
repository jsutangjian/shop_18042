package com.qf.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientUtil {

    public static String sendWithJsonPost(String url,String json){
        //创建httpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建post
        HttpPost httpPost = new HttpPost(url);
        try {
            //获取响应体之前，要给他做一些设置，设置响应头，响应体
            //设置响应头的格式
            httpPost.setHeader(new BasicHeader("Content-Type","application/json"));
            httpPost.setEntity(new StringEntity(json,"utf-8"));//设置相应内容的编码
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            //获得响应体
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseResult = EntityUtils.toString(httpEntity);
            return responseResult;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String sendPostAndHeader(String url, Map<String,String> params,
                                           Map<String, String> header){
        //创建httpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建post
        //设置请求头
        HttpPost httpPost = new HttpPost(url);
        if(header != null){
            for(Map.Entry<String,String> head:header.entrySet()){
                httpPost.addHeader(head.getKey(),head.getValue());
            }
        }
        //设置请求体
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        //传过来的参数，转化为表单需要的数据
        for(Map.Entry<String,String> param:params.entrySet()){
            paramList.add(new BasicNameValuePair(param.getKey(),param.getValue()));
        }
        //把请求体设置进去
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList,"utf-8"));
            //发送post请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //获得响应体
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity);
            return responseEntity;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpClient!=null){
                try {
                    httpClient.close();//响应客户端一定要关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
