package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mjz
 * @create 2023-02-23-15:49
 * @description
 */
@Mapper
public interface DiscussPostMapper {
    // 分页, 最新时间排序
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param 注解用于参数取别名的，
    // 如果只有一个参数，并且在 <if> 里面使用，则必须使用别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
