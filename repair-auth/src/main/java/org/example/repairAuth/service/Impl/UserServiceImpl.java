package org.example.repairAuth.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.repairAuth.entity.Users;
import org.example.repairAuth.entity.entity.Roles;
import org.example.repairAuth.mapper.RolesMapper;
import org.example.repairAuth.mapper.UserMapper;
import org.example.repairAuth.service.RolePermissionService;
import org.example.repairAuth.service.UserService;
import org.example.repairAuth.service.UsersRolesService;
import org.example.repaircommon.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.repairAuth.entity.UserRoles;
import org.example.repairAuth.entity.entity.RolePermissions;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ayan
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {

    @Autowired
    private UsersRolesService usersrolesService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private RolePermissionService rolePermissionService;

    // 根据用户名查询用户
    @Override
    public Users queryUser(String username) {
        Users user = new Users();
        return userMapper.selectOne(new QueryWrapper<>(user).eq("user_name", username));
    }

    /**
     * 根据用户名查询用户权限
     * @author Ayan
     * @param username
     * @return
     */
    @Override
    public List<String> queryUserPermissions(String username) {
        Users user = queryUser(username);
        int userid = user.getId();
        //根据用户名查询角色id
        List<Integer> roleIds = usersrolesService.getIdByUserId(userid);
        //根据角色id查询权限id
        List<Integer> permissionsIds = new ArrayList<>();
        for(Integer roleId:roleIds)
        {
            List<Integer> rolePermissions = rolePermissionService.getIdByRoleId(roleId);
            permissionsIds.addAll(rolePermissions);
        }
        //根据权限id查询权限
        List<String> permissions = new ArrayList<>();
        for (Integer permissionId:permissionsIds)
        {
            String permission = rolePermissionService.getPermissionsById(permissionId);
            permissions.add(permission);
        }
        return permissions;
    }

    /**
     *根据用户名查询用户角色
     * @author Ayan
     * @param username
     * @return
     */
    @Override
    public List<String>QueryUserRole(String username)
    {
        List<String> roles = new ArrayList<>();
        Users user = queryUser(username);
        Integer userid = user.getId();
        //根据用户名查询角色id
        return getStrings(userid, roles);
    }

    /**
     * @deprecated 根据用户id查询用户角色
     * @author Ayan
     * @param userId
     * @return
     */
    @Override
    public List<String>QueryUserRoleByUserId(Integer userId)
    {
        List<String> roles = new ArrayList<>();
        //根据用户id查询角色id
        return getStrings(userId, roles);
    }

    private List<String> getStrings(Integer userId, List<String> roles) {
        List<Integer> roleIds = usersrolesService.getIdByUserId(userId);
        for (Integer roleId:roleIds)
        {
            Roles role = new Roles();
            String roleName = rolesMapper.selectOne(new QueryWrapper<>(role).eq("id",roleId)).getRoleName();
            roles.add(roleName);
        }
        return roles;
    }

    /**
     * 添加用户
     * @author Ayan
     * @param user
     */
    @Override
    public void saveUser(Users user)
    {
        Users users = new Users();
        users.setUserName(user.getUserName());
        users.setPassword(user.getPassword());
        users.setPhone(user.getPhone());
        users.setEmail(user.getEmail());
        users.setEnabled(1);
        userMapper.insert(users);
    }

    /**
     * 为用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     * @author Ayan
     */
    @Override
    public String allocateAuthority(Integer userId, List<Integer> roleIds)
    {
        Result<String> rs = new Result<>();
        UserRoles userRoles = new UserRoles();
        List<String> S = new ArrayList<>();
        for (Integer roleId:roleIds)
        {
            S.add("分配角色："+rolesMapper.selectById(roleId).getRoleName()+"\n");
            userRoles.setUserId(userId);
            userRoles.setRoleId(roleId);
            // 检查是否已存在相同的记录
            boolean exists = !usersrolesService.list(new QueryWrapper<UserRoles>()
                    .eq("user_id", userId)
                    .eq("role_id", roleId)).isEmpty();

            if (!exists) {
                // 不存在则插入
                usersrolesService.save(userRoles);
                S.add("角色分配成功！");
            } else {
                S.add("角色已存在!\n");
            }
        }
        return S.toString();
    }

    /**
     * 为用户分配权限
     *
     * @param userId
     * @param permissionsId
     * @return
     * @author Ayan
     */
    @Override
    public Result<String> allocatePermission(Integer userId, List<Integer> permissionsId)
    {
        Result<String> rs = new Result<>();
        //根据用户id查询角色id
        List<Integer> roleIds = usersrolesService.getIdByUserId(userId);
        for (Integer roleId:roleIds)
        {
            for (Integer permissionId:permissionsId)
            {
                RolePermissions rolePermission = new RolePermissions();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                // 检查是否已存在相同的记录
                boolean exists = !rolePermissionService.list(new QueryWrapper<RolePermissions>()
                        .eq("permission_id", permissionId)
                        .eq("role_id", roleId)).isEmpty();

                if (!exists) {
                    // 不存在则插入
                    rolePermissionService.save(rolePermission);
                    rs.setMsg("分配权限成功！");
                } else {
                    rs.setMsg("角色已存在");

                }
            }
        }
        return rs;
    }

    /**
     * &#064;description:  分页查询用户,根据当前登录用户的身份来返回数据
     * @param page
     * @param limit
     * @author Ayan
     */
    @Override
    public List<Users> getUserList(Integer page, Integer limit,String role)
    {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        IPage<Users> ipage = new Page<>();
        List<Users> list = new ArrayList<>();
        if ("ROLE_ADMIN".equals(role)){
             ipage = userMapper.selectPage(new Page<>(page, limit), queryWrapper);
             return ipage.getRecords();
        }
       if ("ROLE_ReportAdmin".equals(role)){
           String iden = "ROLE_Report";
           Integer roleId = rolesMapper.selectOne(new QueryWrapper<Roles>().eq("role_name",iden)).getId();
           List<Integer> userIds = usersrolesService.getIdByRoleId(roleId);
           for (Integer userId1:userIds)
           {
               queryWrapper.eq("id",userId1);
               list.add(userMapper.selectOne(queryWrapper));
           }
       }
        if ("ROLE_RepairAdmin".equals(role)){
            String iden = "ROLE_Repair";
            Integer roleId = rolesMapper.selectOne(new QueryWrapper<Roles>().eq("role_name",iden)).getId();
            List<Integer> userIds = usersrolesService.getIdByRoleId(roleId);
            for (Integer userId1:userIds)
            {
                queryWrapper.eq("id",userId1);
                list.add(userMapper.selectOne(queryWrapper));
            }
        }
        ipage.setCurrent(page);
        ipage.setSize(limit);
        ipage.setRecords(list);
        return ipage.getRecords();
    }
    public void deleteUser(Integer id)
    {

            // 继续使用 userDetails 对象
        userMapper.deleteById(id);
    }

}
