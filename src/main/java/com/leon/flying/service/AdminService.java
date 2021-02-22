package com.leon.flying.service;

import com.leon.flying.common.PageData;
import com.leon.flying.vo.PostDetailVO;

import java.util.List;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public interface AdminService {

    PageData listPosts(String petTag, String label, String content, Integer status, Integer pageNo, Integer pageSize);

    PostDetailVO getById(Long postId);
    List listRank(Integer type);

}