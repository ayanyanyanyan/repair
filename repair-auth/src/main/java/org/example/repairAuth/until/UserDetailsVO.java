package org.example.repairAuth.until;

import io.micrometer.common.util.StringUtils;
import lombok.Data;
import org.example.repairAuth.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsVO implements UserDetails {

    //当前登录用户
    private transient Users currentUserInfo;

    //当前权限
    private List<String> permissionValueList;
    //当前角色
    private List<String> authoritiesList;

    public void userDetailsVOs(Users currentUserInfo) {
        if (currentUserInfo!=null) {
            this.currentUserInfo = currentUserInfo;
        }
    }

    /**
     * 获取用户权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // 添加角色
        for (String role : authoritiesList) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        // 添加权限
        for (String permission : permissionValueList) {
            if (StringUtils.isEmpty(permission)) {
                continue;
            }
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }

        return grantedAuthorities;
    }


    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    /**
     * 获取用户名
     * @return
     */
    @Override
    public String getUsername() {
        return currentUserInfo.getUserName();
    }


    /**
     * 用户账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * 用户账号是否被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}