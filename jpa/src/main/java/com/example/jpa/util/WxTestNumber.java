package com.example.jpa.util;






import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class WxTestNumber {



    public static String urlConnect(String stringUrl,String requestMethod){
        try {
            InputStream inputStream = getInputStream(stringUrl, requestMethod);
            BufferedReader in = null;
            String result = "";
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    inputStream, "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getInputStream(String stringUrl, String requestMethod){
        HttpURLConnection conn=null;
        InputStream inputStream=null;
        try {
            URL url=new URL(stringUrl);
            conn=(HttpURLConnection) url.openConnection();
            //设置post/get方法
            conn.setRequestMethod(requestMethod);
            //不使用缓存
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //读取超时时间
            conn.setReadTimeout(8000);
            //连接超时时间
            conn.setConnectTimeout(8000);
            //这一句很重要，设置不要302自动跳转，后面会讲解到
            conn.setInstanceFollowRedirects(false);
            int responseCode = conn.getResponseCode();
            inputStream=conn.getInputStream();
            
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }

        return inputStream;
    }





    public static String postConnectWithJson(JSONObject jsonParam, String urls) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            // 开始连接请求
            conn.connect();
            OutputStream out=conn.getOutputStream();
            OutputStream dataOut = new DataOutputStream(out) ;
            // 写入请求的字符串
            dataOut.write((jsonParam.toString()).getBytes());
            out.flush();
            out.close();
            dataOut.flush();
            dataOut.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");

            }

        } catch (Exception e) {

        }

        return sb.toString();

    }

    public static void main(String[] args) {
        String url="http://localhost:8080/test";
        Map<String,Object> map=new HashMap<>();
        map.put("index","aaa");//这里是参数值
        try {
            System.out.println(postConnectWithJson(new JSONObject(map), url));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
