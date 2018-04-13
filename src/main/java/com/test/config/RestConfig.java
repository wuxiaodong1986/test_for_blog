package com.test.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * RestTemplate配置类
 * RestTemplate使用教程 http://www.jianshu.com/p/c9644755dd5e
 * Created by 吴晓冬 on 2017/8/8.
 */
@Configuration
public class RestConfig
{
    @Bean(name="restTemplate")
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) throws Exception
    {
        HttpClient httpClient = getWeshareHttpClient();

        RestTemplate restTemplate = restTemplateBuilder.requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient)).build();

        //获取RestTemplate默认配置好的所有转换器
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        return restTemplate;
    }

    @Bean
    public HttpClient getWeshareHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
                return true;
            }
        }).build();

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).setConnectionRequestTimeout(120000).setRedirectsEnabled(false).build();

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext)).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 将最大连接数增加到200
        connectionManager.setMaxTotal(200);
        // 将每个路由基础的连接增加到40
        connectionManager.setDefaultMaxPerRoute(40);

        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connectionManager);

        clientBuilder.setDefaultRequestConfig(requestConfig);
        return clientBuilder.build();
    }
}
