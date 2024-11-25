package org.example.repairAuth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.repairAuth.entity.UserRoles;

import java.util.List;

/**
 * @author Ayan
 */
public interface UsersRolesService extends IService<UserRoles> {
    List<Integer> getIdByUserId(Integer userId);


    //根据角色id查询用户id
    List<Integer> getIdByRoleId(Integer roleId);
}
