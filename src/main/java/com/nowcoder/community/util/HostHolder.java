package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author mjz
 * @create 2023-03-01-11:11
 * @description 持有用户信息，用于代替 session(线程隔离) 对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    // 清空线程
    public void clear() {
        users.remove();
    }
}
