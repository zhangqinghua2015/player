package com.zqh.player.tools.common.util.net;


import com.zqh.player.tools.common.util.transfer.EncodeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class HttpUtils {

    //可以获取http和https client
    private static CloseableHttpClient httpClient;

    private static ReentrantLock lock = new ReentrantLock();

    private static final int TIMEOUT_SECONDS = 20;

    private static final int POOL_SIZE = 20;

    private HttpUtils() {}

    public static String sendGetRequest(String reqURL) {
        return sendGetRequest(reqURL, null);
    }

    /**
     * 发送HTTP_GET请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址(含参数)
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendGetRequest(String reqURL, String decodeCharset) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(reqURL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;

    }

    /**
     * 如果reqUrl较为复杂(如包含某些特殊字符<, >等),推荐使用该方法，isNeedEncode设置为true
     *
     * @param reqURL
     * @param decodeCharset
     * @param isNeedEncode
     * @return
     */
    public static String sendGetRequest(String reqURL, String decodeCharset, boolean isNeedEncode) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = null;
            if (isNeedEncode) {
                URL url = new URL(reqURL);
                URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(),
                        url.getQuery(), null);
                httpGet = new HttpGet(uri);
            } else {
                httpGet = new HttpGet(reqURL);
            }
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity); // Consume response content
            }
        } catch (Exception e) {
            System.out.println("发送http的get请求发生异常,错误原因" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;

    }


    public static String sendGetJSONRequest(String reqURL, Map<String, String> requestHeadMap) {
        return sendGetSSLRequest(reqURL,requestHeadMap);
        /*CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(reqURL);
            if ((requestHeadMap != null) && (requestHeadMap.size() > 0)) {
                for (Map.Entry<String, String> entry : requestHeadMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();// 设置请求和传输超时时间
            httpGet.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.errorLog("HttpClientUtil.sendGetJSONRequest与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {

                }
            }
        }
        return null;*/
    }


    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 该方法在对请求数据的编码和响应数据的解码时,所采用的字符集均为UTF-8
     *
     * @param isEncoder 用于指明请求数据是否需要UTF-8编码,true为需要
     * @throws IOException
     */

    public static String sendPostRequest(String reqURL, String sendData,
                                         boolean isEncoder) throws Exception {
        return sendPostRequest(reqURL, sendData, isEncoder, null, null);

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param sendData      请求参数,若有多个参数则应拼接成param11=value11¶m22=value22¶m33=value33的形式后,
     *                      传入该参数中
     * @param isEncoder     请求数据是否需要encodeCharset编码,true为需要
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws IOException
     */

    public static String sendPostRequest(String reqURL, String sendData,
                                         boolean isEncoder, String encodeCharset, String decodeCharset)
            throws Exception {
        String responseContent = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        // httpPost.setHeader(HTTP.CONTENT_TYPE,
        // "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.setHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
                for (String str : sendData.split("&")) {
                    formParams.add(new BasicNameValuePair(str.substring(0,
                            str.indexOf("=")),
                            str.substring(str.indexOf("=") + 1)));
                }
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(
                        formParams, encodeCharset == null ? "UTF-8"
                                : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(sendData));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpClient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param jsonData      请求参数,{}json形式, 传入该参数中
     * @param isEncoder     请求数据是否需要encodeCharset编码,true为需要
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendPostRequest4Json(String reqURL, String jsonData,
                                              boolean isEncoder, String encodeCharset, String decodeCharset) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(reqURL);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        try {
            if (isEncoder) {
                httpPost.setEntity(new StringEntity(EncodeUtils.urlEncode(
                        jsonData, encodeCharset == null ? "UTF-8"
                                : encodeCharset)));
            } else {
                httpPost.setEntity(new StringEntity(jsonData));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * @param reqURL
     * @param data
     * @return
     * @throws IOException
     */
    public static String sendPostRequest4Json(String reqURL, String data)
            throws Exception {
        return sendPostRequest4Json(reqURL, data, null);
    }

    /**
     * @param reqURL
     * @param data
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String sendPostRequest4Json(String reqURL, String data,
                                              ContentType contentType) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        try {
            httpPost.setEntity(new StringEntity(data, contentType));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * @param reqURL
     * @param data
     * @param contentType
     * @return
     * @throws IOException
     */
    public static String sendPostRequest4Json(String reqURL, String data, Map<String, String> headPara,
                                              ContentType contentType) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        // 创建org.apache.http.client.methods.HttpGet
        if ((headPara != null) && (headPara.size() > 0)) {
            for (Map.Entry<String, String> entry : headPara.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            httpPost.setEntity(new StringEntity(data, contentType));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); // 创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {
            httpPost.setEntity(new StringEntity(URLEncodedUtils.format(
                    formParams, encodeCharset == null ? "UTF-8"
                            : encodeCharset)));
//			httpPost.setEntity(new UrlEncodedFormEntity(formParams,
//					encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequestUrlEncode(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); // 创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {

            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset,
                                         String decodeCharset, boolean isUrlEncode) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); // 创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {
            if (isUrlEncode) {
                httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                        encodeCharset == null ? "UTF-8" : encodeCharset));
            } else {
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(
                        formParams, encodeCharset == null ? "UTF-8"
                                : encodeCharset)));
            }
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param head          请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequest(String reqURL, Map<String, String> head,
                                         String data, String encodeCharset,
                                         String decodeCharset) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        if (head != null && head.size() > 0) {
            for (Map.Entry<String, String> entry : head.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            httpPost.setEntity(new StringEntity(data));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param head          请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     * @throws Exception
     */
    public static String sendPostRequest(String reqURL, Map<String, String> head,
                                         Map<String, String> params, String encodeCharset,
                                         String decodeCharset, boolean isUrlEncode) throws Exception {
        String responseContent = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        for (Map.Entry<String, String> entry : head.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); // 创建参数队列
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        try {
            if (isUrlEncode) {
                httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                        encodeCharset == null ? "UTF-8" : encodeCharset));
            } else {
                httpPost.setEntity(new StringEntity(URLEncodedUtils.format(
                        formParams, encodeCharset == null ? "UTF-8"
                                : encodeCharset)));
            }
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            httpclient.close();
        }
        return responseContent;
    }

    /**
     * 发送HTTPS_POST请求
     */

    public static String sendPostSSLRequest(String reqURL, Map<String, String> params) {
        return sendPostSSLRequest(reqURL, params, null, null);
    }

    /**
     * 发送HTTPS_GET请求
     */

    public static String sendGetSSLRequest(String reqURL,
                                           Map<String, String> params) {
        return sendGetSSLRequest(reqURL, params, null, null);
    }


    /**
     * 发送HTTPS_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendPostSSLRequest(String reqURL,
                                            Map<String, String> params, String encodeCharset,
                                            String decodeCharset) {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);
            List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    encodeCharset == null ? "UTF-8" : encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息为" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * 发送HTTPS_GET请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */

    public static String sendGetSSLRequest(String reqURL,
                                           Map<String, String> params, String encodeCharset,
                                           String decodeCharset) {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new TrustManager[]{xtm}, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", 443, socketFactory));
            HttpGet httpGet = new HttpGet(reqURL);
            if ((params != null) && (params.size() > 0)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息为" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;

    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据,其数据类型为<code>java.util.Map<String, String></code>
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     */

    private static String sendPostRequestByJava(String reqURL,
                                                Map<String, String> params) {
        StringBuilder sendData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sendData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (sendData.length() > 0) {
            sendData.setLength(sendData.length() - 1); // 删除最后一个&符号
        }
        return sendPostRequestByJava(reqURL, sendData.toString());
    }

    public static void sendPostRequest4InputStream(OutputStream out, String reqURL, String data, Map<String, String> headPara, ContentType contentType) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqURL);
        if ((headPara != null) && (headPara.size() > 0)) {
            for (Map.Entry<String, String> entry : headPara.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            httpPost.setEntity(new StringEntity(data, contentType));
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception("http连接协议异常,请稍后重试");
            }
            HttpEntity entity = response.getEntity();
            Header head = entity.getContentType();
            String responseContentType = head.getValue();
            if (!"application/pdf".equalsIgnoreCase(responseContentType)) {
                throw new Exception("不是pdf流");
            }
            if (entity.getContentLength() != -1
                    && entity.getContentLength() < 200) {// 文件肯定大于200了
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                if (StringUtils.hasText(responseContent)) {
                    throw new Exception(responseContent);
                }
            }
            InputStream in = entity.getContent();
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下" + e);
            throw e;
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    public static void sendGetRequest4InputStream(OutputStream out, String reqURL, Map<String, String> headPara) throws Exception {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
            if ((headPara != null) && (headPara.size() > 0)) {
                for (Map.Entry<String, String> entry : headPara.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000).setConnectTimeout(30000).build();// 设置请求和传输超时时间
            httpGet.setConfig(requestConfig);

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception("http连接协议异常,请稍后重试");
            }
            HttpEntity entity = response.getEntity(); // 获取响应实体
            Header head = entity.getContentType();
            String contentType = head.getValue();
            if (!"application/pdf".equalsIgnoreCase(contentType)) {
                throw new Exception("不是pdf流");
            }
            if (entity.getContentLength() != -1
                    && entity.getContentLength() < 200) {// 文件肯定大于200了
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                if (StringUtils.hasText(responseContent)) {
                    throw new Exception(responseContent);
                }
            }
            InputStream in = entity.getContent();
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    /**
     * 发送HTTP_POST请求
     * <p>
     * <p>
     * 本方法默认的连接超时时间为30秒,默认的读取超时时间为30秒
     *
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return 远程主机响应正文`HTTP状态码,如<code>"SUCCESS`200"</code><br>
     * 若通信过程中发生异常则返回"Failed`HTTP状态码",如<code>"Failed`500"</code>
     */

    public static String sendPostRequestByJava(String reqURL, String sendData) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; // 写
        InputStream in = null; // 读
        int httpStatusCode = 0; // 远程主机响应的HTTP状态码
        try {
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true); // 指示应用程序要将数据写入URL连接,其值默认为false
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); // 30秒连接超时
            httpURLConnection.setReadTimeout(30000); // 30秒读取超时
            out = httpURLConnection.getOutputStream();
            out.write(sendData.toString().getBytes());
            // 清空缓冲区,发送数据
            out.flush();
            // 获取HTTP状态码
            httpStatusCode = httpURLConnection.getResponseCode();
            // 该方法只能获取到[HTTP/1.0 200 OK]中的[OK]
            // 若对方响应的正文放在了返回报文的最后一行,则该方法获取不到正文,而只能获取到[OK],稍显遗憾
            // respData = httpURLConnection.getResponseMessage();
            // 处理返回结果

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String row = null;
            String respData = "";
            if ((row = br.readLine()) != null) { // readLine()方法在读到换行[\n]或回车[\r]时,即认为该行已终止
                respData = row; // HTTP协议POST方式的最后一行数据为正文数据
            }
            br.close();
            in = httpURLConnection.getInputStream();
            byte[] byteDatas = new byte[in.available()];
            in.read(byteDatas);
            return new String(byteDatas) + "`" + httpStatusCode;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Failed`" + httpStatusCode;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    System.out.println("关闭输出流时发生异常,堆栈信息如下" + e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    System.out.println("关闭输入流时发生异常,堆栈信息如下" + e);
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
    }

    public static void main(String[] args) {
        String s = sendGetRequest("https://nfh9.superboss.cc/index.jsp");
        System.out.println("========" + s);
    }

    public static String sendGetRequest(String reqURL, String decodeCharset,
                                        Map<String, String> headPara) {
        String responseContent = null; // 响应内容
        HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
        HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
        if ((headPara != null) && (headPara.size() > 0)) {
            for (Map.Entry<String, String> entry : headPara.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000).setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (null != entity) {
                responseContent = EntityUtils.toString(entity,
                        decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity); // Consume response content
            }
        } catch (Exception e) {
            System.out.println("该异常通常是网络原因引起的,如HTTP服务器未启动等,堆栈信息如下" + e);
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }
        return responseContent;
    }

    public static String postNew(String httpUrl, String xml) {
        String responseContent = null;
        LinkedHashMap<String, Object> result = null;
        try {
            //http://58.221.72.184:5868
            // http地址
            //String httpUrl = "http://192.168.0.244:8080/bjsearch/dianpingOrderBack.jhtml";
            //HttpPost连接对象
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(httpUrl);
            StringEntity myEntity = new StringEntity(xml, "UTF-8");
            httppost.setEntity(myEntity);
            httppost.addHeader("Content-Type", "text/xml");

            //httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.addHeader("Accept", "*/*");
            // httppost.setEntity(new StringEntity(content));
            // HttpResponse response = httpClient.execute(httpPost);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity,"utf-8");
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    public static String postXml(String httpUrl, String xml, Map<String, String> headPara) {
        String responseContent = null;
        LinkedHashMap<String, Object> result = null;
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(httpUrl);
            StringEntity myEntity = new StringEntity(xml, "UTF-8");
            httppost.setEntity(myEntity);
            httppost.addHeader("Accept", "*/*");
            httppost.addHeader("Content-Type", "text/xml");
            if ((headPara != null) && (headPara.size() > 0)) {
                for (Map.Entry<String, String> entry : headPara.entrySet()) {
                    httppost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            responseContent = EntityUtils.toString(resEntity,"utf-8");
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    private static final int DATA_CHUNK = 1024;

    public static void sendPostURL(String reqURL, int readTimeOut) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;    //读
        BufferedWriter writer = null;  //写
//		int httpStatusCode = 0; // 远程主机响应的HTTP状态码
        InputStream is = null;
        OutputStream os = null;
//		FileInputStream fis = null;
//		FileChannel channel=null;
//
//		FileOutputStream fos = null;
//		FileChannel fc = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0");
            httpURLConnection.setDoOutput(true); // 指示应用程序要将数据写入URL连接,其值默认为false
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); // 30秒连接超时
            httpURLConnection.setReadTimeout(readTimeOut); // 30秒读取超时
            File file = new File("/tmp/", "wishCsv.csv");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            // 处理返回结果
            os = new FileOutputStream(file);
            bos = new BufferedOutputStream(os);
            is = httpURLConnection.getInputStream();
            bis = new BufferedInputStream(is);
//			int available = bis.available();
            int size;
            byte[] byteArray = new byte[DATA_CHUNK];
            while ((size = bis.read(byteArray)) > 0) {
                bos.write(byteArray, 0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception ee) {
                System.out.println("关闭流时发生异常,堆栈信息如下" + ee);
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
    }

    public static String sendGetReq(String url, Map<String, String> requestHeadMap) {
        return sendGetReq(url,requestHeadMap,null,null,0);
    }

    public static String sendGetReq(String url, Map<String, String> requestHeadMap, String encodeCharset, String decodeCharset,int retryNum) {
        int i = 0;
        while (i<=retryNum){
            try {
                HttpClient httpclient = new DefaultHttpClient();
                //Secure Protocol implementation.
                SSLContext ctx = SSLContext.getInstance("SSL");
                //Implementation of a trust manager for X509 certificates
                X509TrustManager tm = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs,
                                                   String string) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs,
                                                   String string) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                ctx.init(null, new TrustManager[]{tm}, null);
                SSLSocketFactory ssf = new SSLSocketFactory(ctx);
                ClientConnectionManager ccm = httpclient.getConnectionManager();
                //register https protocol in httpclient's scheme registry
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(new Scheme("https", 443, ssf));
                HttpGet httpget = new HttpGet(url);
                if ((requestHeadMap != null) && (requestHeadMap.size() > 0)) {
                    for (Map.Entry<String, String> entry : requestHeadMap.entrySet()) {
                        httpget.setHeader(entry.getKey(), entry.getValue());
                    }
                }
//			ResponseHandler responseHandler = new BasicResponseHandler();
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                String responseContent = null;
                if (null != entity) {
                    responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                    EntityUtils.consume(entity);
                }
                return responseContent;
            } catch (SSLHandshakeException ex) {
                ex.printStackTrace();
                i++;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static String sendHttpsGetRequest(String url, Map<String, String> requestHeadMap, String encodeCharset, String decodeCharset) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //Secure Protocol implementation.
            SSLContext ctx = SSLContext.getInstance("SSL");
            //Implementation of a trust manager for X509 certificates
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs,
                                               String string) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ClientConnectionManager ccm = httpclient.getConnectionManager();
            //register https protocol in httpclient's scheme registry
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            HttpGet httpget = new HttpGet(url);
            if ((requestHeadMap != null) && (requestHeadMap.size() > 0)) {
                for (Map.Entry<String, String> entry : requestHeadMap.entrySet()) {
                    httpget.setHeader(entry.getKey(), entry.getValue());
                }
            }
            System.out.println("REQUEST:" + httpget.getURI());
//			ResponseHandler responseHandler = new BasicResponseHandler();
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String responseContent = null;
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
            return responseContent;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 发送HTTPS_POST请求
     * <p>
     * 该方法会自动关闭连接,释放资源
     *
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,其为null时默认采用UTF-8解码
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码
     * @return 远程主机响应正文
     */
    public static String sendHttpsPostRequest(String reqURL, Map<String, String> requestHeadMap, Map<String, String> params, String encodeCharset, String decodeCharset) {
        String responseContent = "";
        HttpClient httpClient = new DefaultHttpClient();
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{xtm}, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            httpClient.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);

            if ((requestHeadMap != null) && (requestHeadMap.size() > 0)) {
                for (Map.Entry<String, String> entry : requestHeadMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (params != null && params.size() > 0) {
                List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset == null ? "UTF-8" : encodeCharset));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            System.out.println("与[" + reqURL + "]通信过程中发生异常,堆栈信息为" + e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    /**
     * 获取https客户端
     * @return
     */
    public static CloseableHttpClient getSSLHttpClient() {
        lock.lock();
        try{
            if(httpClient==null){
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", f)
                        .build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(TIMEOUT_SECONDS * 1000)
                        .setConnectTimeout(TIMEOUT_SECONDS * 1000).build();
                SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
                        .setSoTimeout(15 * 1000).build();// 很重要,不然read堵塞
                httpClient = HttpClientBuilder.create().setMaxConnTotal(POOL_SIZE)
                        .setMaxConnPerRoute(POOL_SIZE)
                        .setConnectionManager(cm)
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultSocketConfig(socketConfig).build();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return httpClient;
    }

    /**
     * 获取http客户端
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        lock.lock();
        try{
            if(httpClient==null){
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(TIMEOUT_SECONDS * 1000)
                        .setConnectTimeout(TIMEOUT_SECONDS * 1000).build();
                SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
                        .setSoTimeout(15 * 1000).build();// 很重要,不然read堵塞
                httpClient = HttpClientBuilder.create().setMaxConnTotal(POOL_SIZE)
                        .setMaxConnPerRoute(POOL_SIZE)
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultSocketConfig(socketConfig).build();
            }
        }finally {
            lock.unlock();
        }

        return httpClient;
    }
}
