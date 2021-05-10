/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 */

package io.iot.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId
    private Long deptId;
    /**
     * 上级部门ID，一级部门为0
     */
    private Long parentId;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 上级部门名称
     */
    @TableField(exist = false)
    private String parentName;
    private Integer orderNum;
    @TableLogic
    private Integer delFlag;
    /**
     * ztree属性
     */
    @TableField(exist = false)
    private Boolean open;
    @TableField(exist = false)
    private List<?> list;

    //费用总计
    @TableField(exist = false)
    private BigDecimal totalCost;

    // 本部门
    @TableField(exist = false)
    private BigDecimal selfTotal;

    // 子部门
    @TableField(exist = false)
    private BigDecimal subTotal;


}
