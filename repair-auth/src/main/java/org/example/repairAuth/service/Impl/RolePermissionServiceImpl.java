package org.example.repairAuth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.repairAuth.mapper.PermissionsMapper;
import org.example.repairAuth.mapper.RolePermissionsMapper;
import org.example.repairAuth.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ayan
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionsMapper, org.example.repairAuth.entity.entity.RolePermissions> implements RolePermissionService {

    @Autowired
    private RolePermissionsMapper RolePermissionsMapper;
    @Autowired
    private org.example.repairAuth.mapper.PermissionsMapper PermissionsMapper;
    //根据角色id查询权限id
    @Override
    public List<Integer> getIdByRoleId(Integer roleId){
        QueryWrapper<org.example.repairAuth.entity.entity.RolePermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);

        List<org.example.repairAuth.entity.entity.RolePermissions> list = RolePermissionsMapper.selectList(queryWrapper);

        return list.stream().map(org.example.repairAuth.entity.entity.RolePermissions::getPermissionId).toList();
    }
    @Override
    public String getPermissionsById(Integer permissionId){
        QueryWrapper<org.example.repairAuth.entity.entity.Permissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",permissionId);

        org.example.repairAuth.entity.entity.Permissions permission= PermissionsMapper.selectOne(queryWrapper);

        return permission.getName();
    }
}
