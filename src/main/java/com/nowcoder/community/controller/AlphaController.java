package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mjz
 * @create 2023-02-27-21:52
 * @description
 */
@Controller
@RequestMapping(value = "/alpha")
public class AlphaController {

    /**
     * 浏览器向服务器请求后，服务器为浏览器创建一个 session
     * 服务器响应浏览器时返回一个 cookie(自动生成)，里面存有该浏览器的 sessionId(自动生成)
     * 该浏览器下次请求时就会携带含有 sessionId的 cookie 给服务器
     * 服务器就会根据 它的 sessionId 找到浏览器自己的 session
     */

    // cookie示例
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建 cookie：一个 cookie实例只能有一对键值对(key、value均为字符串)
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置生效范围
        cookie.setPath("/community/alpha");
        // 设置 cookie的生存时间，单位：s
        cookie.setMaxAge(60 * 10);
        // 发送 cookie
        response.addCookie(cookie);

        return "set Cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get Cookie";
    }

    // session 示例

    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set Session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }
}
