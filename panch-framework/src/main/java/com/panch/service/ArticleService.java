package com.panch.service;

import com.panch.domain.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panch.domain.ResponseResult;
import com.panch.domain.dto.AddArticleDto;

/**
* @author xsj007
* @description 针对表【ph_article(文章表)】的数据库操作Service
* @createDate 2023-04-10 16:58:54
*/
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);
}
