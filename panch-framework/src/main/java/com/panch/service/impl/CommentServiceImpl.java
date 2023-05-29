package com.panch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panch.constants.SystemConstants;
import com.panch.domain.Comment;
import com.panch.domain.ResponseResult;
import com.panch.domain.vo.CommentVo;
import com.panch.domain.vo.PageVo;
import com.panch.enums.AppHttpCodeEnum;
import com.panch.exception.SystemException;
import com.panch.service.CommentService;
import com.panch.mapper.CommentMapper;
import com.panch.service.UserService;
import com.panch.utils.BeanCopyUtils;
import com.panch.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author xsj007
* @description 针对表【ph_comment(评论表)】的数据库操作Service实现
* @createDate 2023-04-17 00:13:08
*/
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Autowired
    private UserService userService;

    //评论列表
    @Override
    public ResponseResult commentList(String commentType ,Long articleId, Integer pageNum, Integer pageSize) {
       //查询对应文章根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        //对parentId根评论判断
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_STATUS_ROOT);
        //对评论类型进行判断
        queryWrapper.eq(Comment::getType, commentType);

        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询对应文章的子评论,stream流表示
        commentVoList.stream()
                .map(commentVo -> new AbstractMap.SimpleEntry<>(commentVo, getChildren(commentVo.getId())))
                .forEach(entry -> entry.getKey().setChildren(entry.getValue()));

//        for (CommentVo commentVo : commentVoList) {
//            //查询对应的子评论
//            List<CommentVo> children = getChildren(commentVo.getId());
//            //赋值
//            commentVo.setChildren(children);
//        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    //评论回复
    @Override
    public ResponseResult addComment(Comment comment) {
        //对评论内容进行判断
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }


    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合,Stream流 V2.0
        commentVos = commentVos.stream()
                .map(commentVo -> {
                    String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
                    commentVo.setUsername(nickName);
                    if (commentVo.getToCommentUserId() != -1) {
                        String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                        commentVo.setToCommentUserName(toCommentUserName);
                    }
                    return commentVo;
                })
                .collect(Collectors.toList());
        //遍历vo集合,Stream流 V1.0
//        Stream<CommentVo> stream = commentVos.stream();
//        //通过creatyBy查询用户的昵称并赋值
//        stream.forEach(commentVo -> {
//            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
//            commentVo.setUsername(nickName);
//            //通过toCommentUserId查询用户的昵称并赋值
//            //如果toCommentUserId不为-1才进行查询
//            if(commentVo.getToCommentUserId()!=-1){
//                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
//                commentVo.setToCommentUserName(toCommentUserName);
//            }
//        });

        //for循环实现查询用户昵称
//        for (CommentVo commentVo : commentVos) {
//            //通过creatyBy查询用户的昵称并赋值
//            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
//            commentVo.setUsername(nickName);
//            //通过toCommentUserId查询用户的昵称并赋值
//            //如果toCommentUserId不为-1才进行查询
//            if(commentVo.getToCommentUserId()!=-1){
//                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
//                commentVo.setToCommentUserName(toCommentUserName);
//            }
//        }
        return commentVos;
    }
}




