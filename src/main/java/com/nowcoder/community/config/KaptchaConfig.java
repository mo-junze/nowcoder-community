package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author mjz
 * @create 2023-02-28-10:44
 * @description 配置验证码图片的配置类
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kapthcaProducer() {
        // 相对于在 application.properties/yml 中配置属性
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");   // 设置图片宽度，单位默认为像素
        properties.setProperty("kaptcha.image.height", "40");   // 设置图片高度，单位默认为像素
        properties.setProperty("kaptcha.textproducer.font.size", "32");   // 设置文本字体大小
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // 限定范围
        properties.setProperty("kaptcha.textproducer.char.length", "4"); // 限定长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise"); // 设置干扰类，防止机器人暴力破解

        DefaultKaptcha kaptcha = new DefaultKaptcha();  // 实例化 Producer 的实现类
        Config config = new Config(properties);   // 将要传给 kaptcha 的参数封装到 config 中
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
