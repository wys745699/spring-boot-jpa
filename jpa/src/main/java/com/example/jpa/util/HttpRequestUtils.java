package com.example.jpa.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 所需jar包
 *         <dependency>
 *             <groupId>org.apache.httpcomponents</groupId>
 *             <artifactId>httpclient</artifactId>
 *             <optional>true</optional>
 *         </dependency>
 */
public class HttpRequestUtils {

    private static Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * http请求工具类，post请求
     * @param url    url
     * @param params 参数值 仅支持String和list两种类型
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> params) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        BufferedReader bufferedReader = null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            postRequestCheckParams(params, httpPost);
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            String output;
            bufferedReader=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),"utf-8"));
            StringBuilder stringBuilder=new StringBuilder();
            while ((output=bufferedReader.readLine())!=null){
                stringBuilder.append(output);
            }
            return stringBuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(bufferedReader!=null)
                bufferedReader.close();
        }
    }


    public static File httpPostReturnFile(String url, Map<String, Object> params,String pathName) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        FileOutputStream fileOutputStream=null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            postRequestCheckParams(params, httpPost);
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            byte[] bytes=readInputStream(httpResponse.getEntity().getContent());
            File file=new File(pathName);
            fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(fileOutputStream!=null)
                fileOutputStream.close();
        }
    }

    private static void postRequestCheckParams(Map<String, Object> params, HttpPost httpPost) throws JsonProcessingException {
        if (params != null) {
            //转换为json格式并打印，不需要的你们可以不要
            String jsonParams = objectMapper.writeValueAsString(params);
            log.info("参数值：{}", jsonParams);
            HttpEntity httpEntity = new StringEntity(jsonParams, "utf-8");
            httpPost.setEntity(httpEntity);
        }
    }

    /**
     * http请求工具类，get请求
     * @param url
     * @param params
     * @param resonseCharSet
     * @return
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> params,String ...resonseCharSet) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        BufferedReader bufferedReader = null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            url = getRequestCheckParams(url, params);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            String charSet="utf-8";
            if(resonseCharSet!=null && resonseCharSet.length>0)
                charSet=resonseCharSet[0];
            String output;
            bufferedReader=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(),charSet));

            StringBuilder dataBuilder=new StringBuilder();
            while ((output=bufferedReader.readLine())!=null){
                dataBuilder.append(output);
            }
            return dataBuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(bufferedReader!=null)
                bufferedReader.close();
        }
    }
    public static File httpGetReturnFile(String url, Map<String, Object> params,String pathName) throws Exception {
        DefaultHttpClient defaultHttpClient = null;
        FileOutputStream fileOutputStream = null;
        try {
            defaultHttpClient = new DefaultHttpClient();
            url = getRequestCheckParams(url, params);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                String errorLog="请求失败，errorCode:"+httpResponse.getStatusLine().getStatusCode();
                log.info(errorLog);
                throw new Exception(url+errorLog);
            }
            //读取返回信息
            byte[] bytes=readInputStream(httpResponse.getEntity().getContent());
            File file=new File(pathName);
            fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(bytes);
            return file;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if(defaultHttpClient!=null)
                defaultHttpClient.getConnectionManager().shutdown();
            if(fileOutputStream!=null)
                fileOutputStream.close();
        }
    }

    private static String getRequestCheckParams(String url, Map<String, Object> params) {
        if (params != null) {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<String> iterator = params.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                Object val = params.get(key);
                if (val instanceof List) {
                    List v = (List) val;
                    for (Object o : v) {
                        stringBuilder.append(key).append("=").append(o.toString()).append("&");
                    }
                } else {
                    stringBuilder.append(key).append("=").append(val.toString()).append("&");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            url = url + "?" + stringBuilder.toString();
            log.info("url:{}", url);
        }
        return url;
    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    //测试方法
    public static void main(String[] args) {
//        String url="这里填写你的地址";
//        String url="http://localhost:8080/test";
        String url="http://file.didaok.me/Balance%28magazine%29-01-2.3.001-bigpicture_01_16.jpg";
//        Map<String,Object> map=new HashMap<>();
//        map.put("index","aaa");//这里是参数值
        try {
            File file=httpGetReturnFile(url,null,"C:\\Users\\didaOK\\Desktop\\Balance%28magazine%29-01-2.3.001-bigpicture_01_16.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}