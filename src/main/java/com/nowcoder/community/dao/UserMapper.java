package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mjz
 * @create 2023-02-23-0:43
 * @description
 */

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    // 新增用户
    int insertUser(User user);

    // 更新状态
    int updateStatus(int id, int status);

    // 更新头像路径
    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
