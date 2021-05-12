package io.iot.modules.sysuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.iot.common.utils.PageUtils;
import io.iot.modules.sysuser.entity.RoleEntity;

import java.util.Map;

/**
 * 角色表
 *
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:45
 */
public interface RoleService extends IService<RoleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

