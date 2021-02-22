package com.leon.flying.service;

import com.leon.flying.common.PageData;
import com.leon.flying.vo.CommentVO;

import java.util.List;

public interface CommentService {

    PageData getCommentList(Long postId, Integer pageNo, Integer pageSize, Long userId);

    List<CommentVO> getCommentList(Long postId, Integer pageNo, Integer pageSize);
}
