package io.iot.modules.sysuser.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.iot.common.utils.PageUtils;
import io.iot.common.utils.Query;

import io.iot.modules.sysuser.dao.UserDao;
import io.iot.modules.sysuser.entity.UserEntity;
import io.iot.modules.sysuser.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String getRole(String userName) {
        return baseMapper.getRole(userName);
    }

}
