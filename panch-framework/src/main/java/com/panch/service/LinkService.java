package com.panch.service;

import com.panch.domain.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panch.domain.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @author xsj007
* @description 针对表【ph_link(友链)】的数据库操作Service
* @createDate 2023-04-15 10:42:25
*/
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
