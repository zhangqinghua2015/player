package com.zqh.player.tools.common.util.net;

import com.zqh.player.tools.common.exception.PlayerException;
import lombok.Data;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by orange on 2018/9/30.
 */
public class EmailUtils {

    private MailGunConfig mailGunConfig;
    private MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();

    public EmailUtils(MailGunConfig mailGunConfig) {
        this.mailGunConfig = mailGunConfig;
    }

    /**
     * @param email
     * @param subject
     * @param html
     */
    public void send(String email, String subject, String html) {
        send(email, subject, html, EmailType.MAIL_GUN);
    }

    /**
     * @param email
     * @param subject
     * @param content
     */
    public void send(String email, String subject, String content, EmailType emailType) {
        int retry = 0;
        while (retry < 3) {
            if (emailType == EmailType.MAIL_GUN) {
                if(sendByMailGun(email, subject, content)){
                    return;
                }
            } else {
                throw new RuntimeException("未知的邮件类型");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry++;
        }
        throw new PlayerException("email send fail");
    }

    public boolean sendByMailGun(String email, String subject, String content) {
        boolean suc = false;
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("api", mailGunConfig.getMG_APIKEY()));
        CloseableHttpClient createDefault = HttpClients.custom().setSslcontext(mySSLSocketFactory.getSSLContext()).setDefaultCredentialsProvider(credsProvider).build();
        HttpPost post = new HttpPost(mailGunConfig.getMG_API_URL());

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("from", mailGunConfig.getMG_FROM()));
        formParams.add(new BasicNameValuePair("to", email));
        formParams.add(new BasicNameValuePair("subject", subject));
        formParams.add(new BasicNameValuePair("html", content));
        CloseableHttpResponse result = null;
        String respMsg = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            result = createDefault.execute(post);
            int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException(statusCode + "");
            }
            respMsg = EntityUtils.toString(result.getEntity());
            suc = true;
            System.out.println(MessageFormat.format("发送邮件成功,email:{},subject:{}", email, subject));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(MessageFormat.format("send email fail,email:{},subject:{},result:{}", email, subject, respMsg, e));
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return suc;
    }

    enum EmailType {
        MAIL_GUN
    }

    @Data
    public static class MailGunConfig {
        private String MG_APIKEY;
        private String MG_API_URL;
        private String MG_FROM;

        public MailGunConfig() {
        }
    }
}
