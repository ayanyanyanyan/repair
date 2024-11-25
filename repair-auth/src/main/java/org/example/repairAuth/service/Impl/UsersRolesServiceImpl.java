package org.example.repairAuth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.repairAuth.entity.UserRoles;
import org.example.repairAuth.mapper.UsersRolesMapper;
import org.example.repairAuth.service.UsersRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Ayan
 */
@Service
public class UsersRolesServiceImpl extends ServiceImpl<UsersRolesMapper,UserRoles> implements UsersRolesService {

    @Autowired
    private UsersRolesMapper usersRolesMapper;

    //根据用户id查询角色id
    @Override
    public List<Integer> getIdByUserId(Integer userId) {
        QueryWrapper<UserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);

        List<UserRoles> list = usersRolesMapper.selectList(queryWrapper);

        return list.stream().map(UserRoles::getRoleId).toList();
    }
    //根据角色id查询用户id
    @Override
    public List<Integer> getIdByRoleId(Integer roleId) {
        QueryWrapper<UserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);

        List<UserRoles> list = usersRolesMapper.selectList(queryWrapper);

        return list.stream().map(UserRoles::getUserId).toList();
    }

}
