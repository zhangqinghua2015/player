package com.zqh.player.tools.common.util.net;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;


public class DingTalkUtils {

    //数据导入
    public static String IMPORT_ROBOT = "";
    //监控
    public static String WEBHOOK_TOKEN = "";

    public static void send(String msg)  {
        send(IMPORT_ROBOT, msg);
    }

    public static void send(String token,String msg)  {
        try{
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(token);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + msg + "\"}}";
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);
            httpclient.execute(httppost);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("钉钉推送失败"+ e);
        }

    }
}
