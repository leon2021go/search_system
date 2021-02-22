package com.leon.flying.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leon.flying.common.PageData;
import com.leon.flying.common.redis.RedisClient;
import com.leon.flying.dao.MzPostCommentDao;
import com.leon.flying.dao.MzUserDao;
import com.leon.flying.entity.MzPostCommentDO;
import com.leon.flying.entity.MzUserDO;
import com.leon.flying.service.CommentService;
import com.leon.flying.utils.ConvertUtil;
import com.leon.flying.utils.DateUtil;
import com.leon.flying.vo.CommentVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leon
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private MzPostCommentDao mzPostCommentDao;

    @Resource
    private MzUserDao mzUserDao;

//    @Resource
//    private RedisClient redisClient;

    @Override
    public PageData getCommentList(Long postId, Integer pageNo, Integer pageSize, Long userId) {
        Page page = PageHelper.startPage(pageNo, pageSize, true);
                List<MzPostCommentDO> commentDos = mzPostCommentDao.selectFirstLevelFromPostId(postId);
                if(CollectionUtils.isEmpty(commentDos)) {
                    PageData data = new PageData(10);
                    data.setPageData(pageNo, pageSize, page.getPages(), page.getTotal(), new ArrayList<>());
                    return data;
                }
                List<MzPostCommentDO> childComments = mzPostCommentDao.selectChildLevelFromPostId(postId);
                //List<CommentVO> comments = buildChildComment(commentDos);
                List<CommentVO> comments = buildChildComment(commentDos, childComments);
        PageData data = new PageData(10);
        data.setPageData(pageNo, pageSize, page.getPages(), page.getTotal(), comments);
        return data;
    }

    @Override
    public List<CommentVO> getCommentList(Long postId, Integer pageNo, Integer pageSize) {
//        Page page = PageHelper.startPage(pageNo, pageSize, true);
        List<MzPostCommentDO> childComments = mzPostCommentDao.selectAllCommentsFromPostId(postId);
        List<CommentVO> result = new ArrayList<>();
        for(MzPostCommentDO mzPostCommentDO : childComments){
            CommentVO commentVO = buildParentChildMappingEntry(mzPostCommentDO);
            result.add(commentVO);
        }
        return result;
    }

    private List<CommentVO> buildChildComment(List<MzPostCommentDO> commentDos){
        List<CommentVO> result = new ArrayList<>();
        for(MzPostCommentDO commentDO : commentDos){
            CommentVO commentVO = ConvertUtil.convertObject(commentDO, CommentVO.class);
            commentVO.setId(String.valueOf(commentDO.getId()));
            commentVO.setParentId(String.valueOf(commentDO.getParentId()));
            commentVO.setPostId(String.valueOf(commentDO.getPostId()));
            commentVO.setCommentorId(String.valueOf(commentDO.getCommentorId()));
            if(null != commentDO.getCommentTime()) {
                commentVO.setComment_time(DateUtil.sdf.format(commentDO.getCommentTime()));
            }
            List<MzPostCommentDO> mzPostCommentDOS = mzPostCommentDao.selectReplyCommentById(commentDO.getId());
            if(!CollectionUtils.isEmpty(mzPostCommentDOS)){
                commentVO.setChildComments(buildChildComment(mzPostCommentDOS));
            }else{
                commentVO.setChildComments(new ArrayList<>());
            }
            result.add(commentVO);
        }
        return result;
    }

    private List<CommentVO> buildChildComment(List<MzPostCommentDO> firstLevelDos, List<MzPostCommentDO> childCommentDos){
        Map<CommentVO, List<CommentVO>> resultMap = new HashMap<>();
        for(MzPostCommentDO mzPostCommentDO : childCommentDos){
            List<CommentVO> parents =  new ArrayList<>(resultMap.keySet());
            List<String> postIds = parents.stream().map(CommentVO :: getId).collect(Collectors.toList());
            if(postIds.contains(String.valueOf(mzPostCommentDO.getParentId()))) {
                for (int i = 0; i < parents.size(); i++) {
                    if (parents.get(i).getId().equals(String.valueOf(mzPostCommentDO.getParentId()))) {
                        List<CommentVO> existedList = resultMap.get(parents.get(i));
                        CommentVO commentVO =buildParentChildMappingEntry(mzPostCommentDO);
                        existedList.add(commentVO);
                        parents.get(i).setChildComments(existedList);
                        break;
                    }
                }
            }else{
                List<CommentVO> newList = new ArrayList<>();
                CommentVO commentVO = buildParentChildMappingEntry(mzPostCommentDO);
                newList.add(commentVO);
                for(MzPostCommentDO commentDO : firstLevelDos){
                    if(String.valueOf(commentDO.getId()).equals(commentVO.getParentId())){
                        CommentVO parentVO = buildParentChildMappingEntry(commentDO);
                        parentVO.setChildComments(newList);
                        resultMap.put(parentVO, newList);
                        break;
                    }
                }
            }
        }

        List<CommentVO> resultVOs = new ArrayList<>();
        for(MzPostCommentDO commentDO : firstLevelDos){
            CommentVO commentVO = buildParentChildMappingEntry(commentDO);
            List<CommentVO> selfVos = new ArrayList<>();
            for(CommentVO keyVos : resultMap.keySet()){
                if(keyVos.getId().equals(String.valueOf(commentDO.getId()))){
                    selfVos.add(keyVos);
                }
            }
            commentVO.setChildComments(selfVos);
            resultVOs.add(commentVO);
        }
        return resultVOs;
    }


    private CommentVO buildParentChildMappingEntry(MzPostCommentDO commentDO){
        CommentVO commentVO = ConvertUtil.convertObject(commentDO, CommentVO.class);
        commentVO.setId(String.valueOf(commentDO.getId()));
        commentVO.setParentId(String.valueOf(commentDO.getParentId()));
        commentVO.setPostId(String.valueOf(commentDO.getPostId()));
        commentVO.setCommentorId(String.valueOf(commentDO.getCommentorId()));
        if(null != commentDO.getCommentTime()) {
            commentVO.setComment_time(DateUtil.sdf.format(commentDO.getCommentTime()));
        }
        return commentVO;
    }

    private List<CommentVO>  buildUserInfo(List<CommentVO> comments, Map<Long, MzUserDO> userMap, Long userId){
        for(CommentVO commentVO : comments){
            MzUserDO mzUserDO = userMap.get(Long.parseLong(commentVO.getCommentorId()));
            if (mzUserDO.getId().equals(userId)) {
                commentVO.setCommentor(mzUserDO.getName() + "(我)");
            } else {
                commentVO.setCommentor(mzUserDO.getName());
            }
            commentVO.setCommentorAvatar(mzUserDO.getPic());
            if (!CollectionUtils.isEmpty(commentVO.getChildComments())) {
                buildUserInfo(commentVO.getChildComments(), userMap, userId);
            }
        }
        return comments;
    }
}
