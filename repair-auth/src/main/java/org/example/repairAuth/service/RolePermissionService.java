package org.example.repairAuth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.repairAuth.entity.entity.RolePermissions;

import java.util.List;

/**
 * @author Ayan
 */
public interface RolePermissionService extends IService<RolePermissions> {
    //根据角色id查询权限id
    List<Integer> getIdByRoleId(Integer roleId);

    String getPermissionsById(Integer permissionId);
}
