package org.example.repairAuth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.repairAuth.entity.Users;
import org.example.repaircommon.untils.Result;

import java.util.List;

/**
 * @author Ayan
 */
public interface UserService extends IService<Users> {
    Users queryUser(String username);

    List<String> queryUserPermissions(String username);

    List<String>QueryUserRole(String username);

    List<String>QueryUserRoleByUserId(Integer userId);

    void saveUser(Users user);

    String allocateAuthority(Integer userId, List<Integer> roleIds);

    Result<String> allocatePermission(Integer userId, List<Integer> permissionsId);

    List<Users> getUserList(Integer page, Integer limit,String role);

}
