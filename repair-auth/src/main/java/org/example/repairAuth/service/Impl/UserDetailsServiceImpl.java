package org.example.repairAuth.service.Impl;

import io.micrometer.common.util.StringUtils;
import org.example.repairAuth.entity.Users;
import org.example.repairAuth.service.UserService;
import org.example.repairAuth.until.UserDetailsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;
    /**
     * 根据用户名查出用户详细信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            log.error("用户名不能为空");
            throw new UsernameNotFoundException("用户名不能为空");
        }
        log.info("进入用户验证---->UserDetailsServiceImpl");
        //根据用户名查询
        Users user = userService.queryUser(username);
        if (user == null) {
            log.error("用户不存在");
            throw new UsernameNotFoundException("用户不存在");
        }
        //获取当前用户的权限
        List<String> permissionsList = userService.queryUserPermissions(username);
        //获取当前用户的角色
        List<String> roleStringList = userService.QueryUserRole(username);
        UserDetailsVO securityUserVO = new UserDetailsVO();
        securityUserVO.setCurrentUserInfo(user);
        securityUserVO.setPermissionValueList(permissionsList);
        securityUserVO.setAuthoritiesList(roleStringList);
        //获取当前用户的权限
        securityUserVO.getAuthorities();
        //校验账户状态属性是否正常，
        log.info("验证数据"+securityUserVO.toString());
        return securityUserVO;
    }
    public UserDetailsVO getCurrentUser() {
        // 获取当前登录的用户信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsVO) {
            UserDetailsVO userDetails = (UserDetailsVO) principal;
            return userDetails;
        }
        return null;
    }
}