package com.panch.service;

import com.panch.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panch.domain.ResponseResult;

/**
* @author xsj007
* @description 针对表【ph_comment(评论表)】的数据库操作Service
* @createDate 2023-04-17 00:13:08
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

}
