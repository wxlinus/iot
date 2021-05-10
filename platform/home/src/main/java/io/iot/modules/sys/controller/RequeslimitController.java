package io.iot.modules.sys.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.RequeslimitEntity;
import io.renren.modules.sys.service.RequeslimitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 黑白名单表
 *
 * @author xupeng
 * @email ${email}
 * @date 2020-03-25 08:59:46
 */
@RestController
@RequestMapping("sys/requeslimit")
public class RequeslimitController {
    @Autowired
    private RequeslimitService requeslimitService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:requeslimit:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = requeslimitService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:requeslimit:info")
    public R info(@PathVariable("id") Long id){
        RequeslimitEntity requeslimit = requeslimitService.getById(id);

        return R.ok().put("requeslimit", requeslimit);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:requeslimit:save")
    public R save(@RequestBody RequeslimitEntity requeslimit){
        requeslimitService.save(requeslimit);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:requeslimit:update")
    public R update(@RequestBody RequeslimitEntity requeslimit){
        ValidatorUtils.validateEntity(requeslimit);
        requeslimitService.updateById(requeslimit);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:requeslimit:delete")
    public R delete(@RequestBody Long[] ids){
        requeslimitService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
