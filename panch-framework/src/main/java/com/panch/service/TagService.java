package com.panch.service;

import com.panch.domain.ResponseResult;
import com.panch.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panch.domain.dto.TagListDto;
import com.panch.domain.vo.PageVo;
import com.panch.domain.vo.TagVo;

import java.util.List;

/**
* @author xsj007
* @description 针对表【ph_tag(标签)】的数据库操作Service
* @createDate 2023-04-23 00:08:04
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagVo> listAllTag();
}
