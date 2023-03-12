package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author mjz
 * @create 2023-03-01-10:45
 * @description 定义登录信息拦截器
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 在请求开始前，暂存用户信息（需要显示用户信息）
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 cookie 中获取凭证
        String ticket = CookieUtil.getCookieValue(request, "ticket");

        if (ticket != null) { // 已登录
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效：凭证不为空，凭证有效，凭证不过期
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户（暂存）在多线程下应考虑隔离性问题
                hostHolder.setUser(user);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 在 thymeleafEngine 启动前，将 User 封装到 ModelAndView 中，供模板引擎渲染
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();

        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 模板渲染完后，清理用户信息，保证线程不继续占用内存
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
