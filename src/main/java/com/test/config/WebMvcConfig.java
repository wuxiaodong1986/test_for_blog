//package com.test.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.GsonHttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurerAdapter
//{
//
//    /**
//     * 利用fastjson替换掉jackson，且解决中文乱码问题
//     * @param converters
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
////        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
////        FastJsonConfig fastJsonConfig = new FastJsonConfig();
////        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
////        //处理中文乱码问题
////        List<MediaType> fastMediaTypes = new ArrayList<>();
////        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
////        fastConverter.setSupportedMediaTypes(fastMediaTypes);
////        fastConverter.setFastJsonConfig(fastJsonConfig);
////        converters.add(fastConverter);
//
//        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
//        gsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
//        converters.add(gsonHttpMessageConverter);
//
//        System.out.println();
//    }
//}