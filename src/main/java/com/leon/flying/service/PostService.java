package com.leon.flying.service;

import com.github.pagehelper.PageInfo;
import com.leon.flying.common.PageData;
import com.leon.flying.vo.PostVO;
import java.util.HashMap;

/**
 * 帖子服务
 * @author leon
 */
public interface PostService {

    int addPost(PostVO postVO, Long userId);

    PageData getPostList(String title, Integer pageNo, Integer pageSize, Long userId);

    PageData getTopPostList(Integer pageNo, Integer pageSize, Long userId);
    Integer setTop(Long id, Integer isTop);

    Integer setDown(Long id, Integer isDown);
}
