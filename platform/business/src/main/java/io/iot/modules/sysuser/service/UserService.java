package io.iot.modules.sysuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.iot.common.utils.PageUtils;
import io.iot.modules.sysuser.entity.UserEntity;

import java.util.Map;

/**
 * 系统用户
 *
 * @author weixiang
 * @email test@gmail.com
 * @date 2021-05-12 14:49:44
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);
    String getRole(String userName);
}

