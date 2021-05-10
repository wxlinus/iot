package io.iot.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.RequeslimitDao;
import io.renren.modules.sys.entity.RequeslimitEntity;
import io.renren.modules.sys.service.RequeslimitService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("requeslimitService")
public class RequeslimitServiceImpl extends ServiceImpl<RequeslimitDao, RequeslimitEntity> implements RequeslimitService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RequeslimitEntity> page = this.page(
                new Query<RequeslimitEntity>().getPage(params),
                new QueryWrapper<RequeslimitEntity>()
        );

        return new PageUtils(page);
    }

}
