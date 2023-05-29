package com.panch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panch.constants.SystemConstants;
import com.panch.domain.Article;
import com.panch.domain.ArticleTag;
import com.panch.domain.Category;
import com.panch.domain.ResponseResult;
import com.panch.domain.dto.AddArticleDto;
import com.panch.domain.vo.ArticleDetailVo;
import com.panch.domain.vo.ArticleListVo;
import com.panch.domain.vo.HotArticleVo;
import com.panch.domain.vo.PageVo;
import com.panch.mapper.ArticleMapper;
import com.panch.service.ArticleService;
import com.panch.service.ArticleTagService;
import com.panch.service.CategoryService;
import com.panch.utils.BeanCopyUtils;
import com.panch.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author xsj007
* @description 针对表【ph_article(文章表)】的数据库操作Service实现
* @createDate 2023-04-10 16:58:54
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);
        //将page中的数据封装到HotArticleVo中,再将HotArticleVo封装到ResponseResult中
        //方便前端使用，对数据进行封装
        List<Article> articles = page.getRecords();
//        List<HotArticleVo> articleVos= new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo Vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,Vo);
//            articleVos.add(Vo);
//        }
         List<HotArticleVo> hotArticleVoList= BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVoList);
    }

    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName 该处主要在Article类使用注解@Accessors(chain = true)
        // 进行链式调用，使得setCategoryName方法返回的是Article对象
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

//    @Override
//    public ResponseResult getArticleDetail(Long id) {
//        //根据id查询文章
//        Article article = getById(id);
//        //转换成VO
//        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
//        //根据ID查询分类名称
//        Long categoryId = articleDetailVo.getCategoryId();
//        Category category = categoryService.getById(categoryId);
//        if (category!=null){
//            articleDetailVo.setCategoryName(category.getName());
//        }
//
//        return ResponseResult.okResult(articleDetailVo);
//    }
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTIVLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}




