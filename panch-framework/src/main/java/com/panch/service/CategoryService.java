package com.panch.service;

import com.panch.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panch.domain.ResponseResult;
import com.panch.domain.vo.CategoryVo;

import java.util.List;

/**
* @author xsj007
* @description 针对表【ph_category(分类表)】的数据库操作Service
* @createDate 2023-04-12 17:02:59
*/
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();
}
