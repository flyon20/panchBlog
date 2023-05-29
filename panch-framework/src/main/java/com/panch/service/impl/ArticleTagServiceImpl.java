package com.panch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panch.domain.ArticleTag;
import com.panch.service.ArticleTagService;
import com.panch.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author xsj007
* @description 针对表【ph_article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2023-04-24 17:38:58
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




