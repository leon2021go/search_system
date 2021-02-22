package com.leon.flying.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leon.flying.common.PageData;
import com.leon.flying.dao.MzPostDao;
import com.leon.flying.entity.MzPostDO;
import com.leon.flying.service.AdminService;
import com.leon.flying.so.PosterSO;
import com.leon.flying.utils.ConvertUtil;
import com.leon.flying.utils.DateUtil;
import com.leon.flying.vo.PostDetailVO;
import com.leon.flying.vo.PostListVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理服务
 *
 * @author leon
 * @since 2020/8/6
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private MzPostDao mzPostDao;

    @Override
    public PageData listPosts(String petTag, String label, String content, Integer status, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo, pageSize, true);
        List<PosterSO> list = mzPostDao.listPosts(petTag, label, content, status);
        List<PostListVO> postListVO = new ArrayList<>();
        for (PosterSO so : list){
            PostListVO vo = ConvertUtil.convertObject(so, PostListVO.class);
            vo.setId(String.valueOf(so.getId()));
            if(null != so.getCreateTime()) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_SS_TIME);
                vo.setCreateTime(sdf.format(so.getCreateTime()));
            }
            if(null == vo.getIsTop()){
                vo.setIsTop(0);
            }
            if(null == vo.getStatus()){
                vo.setStatus(0);
            }
            postListVO.add(vo);
        }
        PageData data = new PageData(10);
        data.setPageData(pageNo, pageSize, page.getPages(), page.getTotal(), postListVO);
        return data;
    }

    @Override
    public PostDetailVO getById(Long postId) {
        PosterSO mzPostSO = mzPostDao.getByPrimaryKey(postId);
        PostDetailVO postDetailVO = ConvertUtil.convertObject(mzPostSO, PostDetailVO.class);
        if(null != mzPostSO.getCreateTime()) {
            postDetailVO.setCreateTime(DateUtil.sdf.format(mzPostSO.getCreateTime()));
        }
        //List<CommentVO> comments = commentService.getCommentList(postId, pageNo, pageSize);
        //postDetailVO.setCommentList(comments);
        postDetailVO.setId(String.valueOf(postId));
        return postDetailVO;
    }

    @Override
    public List listRank(Integer type) {
        return null;
    }
}