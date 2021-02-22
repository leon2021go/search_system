package com.leon.flying.controller;

import com.leon.flying.common.PageData;
import com.leon.flying.dao.MzPostCommentDao;
import com.leon.flying.dao.MzPostDao;
import com.leon.flying.entity.MzPostCommentDO;
import com.leon.flying.entity.MzPostDO;
import com.leon.flying.service.AdminService;
import com.leon.flying.service.CommentService;
import com.leon.flying.service.PostService;
import com.leon.flying.vo.CommentVO;
import com.leon.flying.vo.PostDetailVO;
import com.leon.flying.vo.PostListVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/poster")
public class PostController {
    @Resource
    MzPostDao mzPostDao;
    @Resource
    MzPostCommentDao mzPostCommentDao;

    @Resource
    AdminService adminService;

    @Resource
    PostService postService;

    @Resource
    CommentService commentService;

    @GetMapping("/list")
    public String  list(Model model,  String petTag, String label, String content, Integer status,
                        @RequestParam(defaultValue = "1") Integer pageNo,
                        @RequestParam(defaultValue = "10")Integer pageSize){

        PageData pageData = adminService.listPosts(petTag, label, content, status, pageNo, pageSize);
        model.addAttribute("pageData", pageData);
        return "post/list";
    }

    @GetMapping("/list/data")
    @ResponseBody
    public PageData  listData(Model model,  String petTag, String label, String content, Integer status,
                        @RequestParam(defaultValue = "1") Integer pageNo,
                        @RequestParam(defaultValue = "10")Integer pageSize){

        PageData pageData = adminService.listPosts(petTag, label, content, status, pageNo, pageSize);
        model.addAttribute("pageData", pageData);
        return pageData;
    }

    @GetMapping("/addposter")
    public String getById(){
        return "post/add";
    }

    @GetMapping("/get/{id}")
    public String getById(@PathVariable("id") Long id,  @RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10")Integer pageSize, Model model){
        PostDetailVO postDetailVO = adminService.getById(id);
        model.addAttribute("poster", postDetailVO);
        List<CommentVO> commentVOS = commentService.getCommentList(id, pageNo, pageSize);
        model.addAttribute("commentData", commentVOS);
        return "post/add";
    }

    @PostMapping("/add")
    public String add(PostListVO postListVO){
        MzPostDO mzPostDO = new MzPostDO();
        if(StringUtils.hasText(postListVO.getId())){
            mzPostDO.setId(Long.parseLong(postListVO.getId()));
            mzPostDO.setContent(postListVO.getContent());
            mzPostDO.setPetTag(postListVO.getPetTag());
            mzPostDO.setStatus((byte)0);
            mzPostDO.preUpdate(1315245290428698625L);
            int t = mzPostDao.updateByPrimaryKeySelective(mzPostDO);
            return "redirect:/poster/list";
        }
        mzPostDO.preInsert(1315245290428698625L);
        mzPostDO.setContent(postListVO.getContent());
        mzPostDO.setPetTag(postListVO.getPetTag());
        mzPostDO.setStatus((byte)0);
        mzPostDao.insertSelective(mzPostDO);
        return "redirect:/poster/list";
    }

    @PutMapping("/add")
    public String update(PostListVO postListVO){
        MzPostDO mzPostDO = new MzPostDO();
        mzPostDO.setId(Long.parseLong(postListVO.getId()));
        mzPostDO.setContent(postListVO.getContent());
        mzPostDO.setStatus((byte)0);
        mzPostDO.preUpdate(0L);
        mzPostDao.updateByPrimaryKeySelective(mzPostDO);
        return "redirect:/poster/list";
    }

    /**
     * 帖子置顶
     * @param id 帖子id
     * @param isTop 0 取消置顶 1 置顶
     * @return
     */
    @GetMapping("/top/{isTop}/{id}")
    public String setTop(Model model, @PathVariable("id") Long id, @PathVariable("isTop") Integer isTop, @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10")Integer pageSize){
        postService.setTop(id, isTop);
        return "redirect:/poster/list";
    }

    /**
     * 帖子屏蔽
     * @param id 帖子id
     * @param isDown 0 取消屏蔽 1 屏蔽
     * @return
     */
    @GetMapping("/down/{isDown}/{id}")
    public String setDown(Model model, @PathVariable("id") Long id, @PathVariable("isDown") Integer isDown, @RequestParam(defaultValue = "1") Integer pageNo,
                                      @RequestParam(defaultValue = "10")Integer pageSize){
        postService.setDown(id, isDown);
        return "redirect:/poster/list";
    }

    //删除帖子
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        MzPostDO mzPostDO = new MzPostDO();
        mzPostDO.setId(id);
        mzPostDO.setIsDel(1);
        mzPostDO.preUpdate(0L);
        mzPostDao.updateByPrimaryKeySelective(mzPostDO);
        return "redirect:/poster/list";
    }

    //删除帖子
    @GetMapping("/comment/del/{postId}/{id}")
    public String deleteComment(@PathVariable("postId") Long postId, @PathVariable("id") Long id, Model model){
        MzPostCommentDO mzPostCommentDO = new MzPostCommentDO();
        mzPostCommentDO.setId(id);
        mzPostCommentDO.setIsDel(1);
        mzPostCommentDO.preUpdate(0L);
        mzPostCommentDao.updateByPrimaryKeySelective(mzPostCommentDO);

        PostDetailVO postDetailVO = adminService.getById(postId);
        model.addAttribute("poster", postDetailVO);
        List<CommentVO> commentVOS = commentService.getCommentList(postId, 1, 10);
        model.addAttribute("commentData", commentVOS);

        return "post/add";
    }
}
