package org.example.repairAuth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (UserRoles)实体类
 *
 * @author makejava
 * @since 2024-11-15 10:49:20
 */
@TableName("user_roles")
public class UserRoles implements Serializable {
    private static final long serialVersionUID = 850650059321230767L;
/**
     * 用户id
     */
    private Integer userId;
/**
     * 角色id
     */
    private Integer roleId;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}

