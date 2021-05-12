package io.iot.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.iot.common.utils.PageUtils;
import io.iot.modules.user.entity.UserEntity;

import java.util.Map;

/**
 * 用户信息表
 *
 * @author weixiang
 * @email ${email}
 * @date 2021-05-11 15:09:48
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

