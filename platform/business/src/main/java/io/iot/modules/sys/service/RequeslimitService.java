package io.iot.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.RequeslimitEntity;

import java.util.Map;

/**
 * 黑白名单表
 *
 * @author xupeng
 * @email ${email}
 * @date 2020-03-25 08:59:46
 */
public interface RequeslimitService extends IService<RequeslimitEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

