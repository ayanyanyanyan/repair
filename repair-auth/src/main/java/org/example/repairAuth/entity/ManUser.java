package org.example.repairAuth.entity;

import lombok.Data;

import java.util.List;

/**
 * 当前登录用户
 */
@Data
public class ManUser {
    // 用户名
    private String username;

    // 密码
    private String password;
    // 角色列表
    private List<String> roles;

    // 权限列表
    private List<String> permissions;
    // 账号是否启用
    private boolean enabled;

    // 账号是否过期
    private boolean accountNonExpired;

    // 账号是否锁定
    private boolean accountNonLocked;

    // 密码是否过期
    private boolean credentialsNonExpired;
}
