package com.panch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panch.constants.SystemConstants;
import com.panch.domain.Link;
import com.panch.domain.ResponseResult;
import com.panch.domain.vo.LinkVo;
import com.panch.service.LinkService;
import com.panch.mapper.LinkMapper;
import com.panch.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
* @author xsj007
* @description 针对表【ph_link(友链)】的数据库操作Service实现
* @createDate 2023-04-15 10:42:25
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);

        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);

        return ResponseResult.okResult(linkVos);
    }
}




