package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author mjz
 * @create 2023-03-02-12:05
 * @description 拦截带 @LoginRequired 标识的方法
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截器的目标是不是一个方法，而不是类或静态资源
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler; // 转型
            Method method = handlerMethod.getMethod(); // 获取方法
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class); // 获取该方法的注解
            if (loginRequired != null && hostHolder.getUser() == null) { // 该方法标注了注解，但没有登陆就访问
                response.sendRedirect(request.getContextPath() + "/login"); // 重定向
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
