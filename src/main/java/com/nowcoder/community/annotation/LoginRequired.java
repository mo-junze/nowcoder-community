package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mjz
 * @create 2023-03-02-11:56
 * @description 自定义注解，标识某个方法需不需要在登陆状态下才能访问
 */
@Target(ElementType.METHOD) // 指定用来描述方法
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface LoginRequired {
    // 可以不写内容，仅起到标识作用。可用拦截器拦截带标识的方法
}
