package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author mjz
 * @create 2023-02-26-20:57
 * @description
 */
public class CommunityUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * MD5加密：只能加密，不能解密
     * @return 返回十六进制的字符串/null
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) { // 字符串为 null 、“”、空格均判断为空
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes()); // 返回十六进制的字符串
    }
}
