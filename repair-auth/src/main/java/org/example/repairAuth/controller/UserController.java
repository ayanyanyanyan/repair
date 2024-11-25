package org.example.repairAuth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.example.repairAuth.entity.Users;
import org.example.repairAuth.mapper.RolesMapper;
import org.example.repairAuth.mapper.UserMapper;
import org.example.repairAuth.service.Impl.UserDetailsServiceImpl;
import org.example.repairAuth.service.UserService;
import org.example.repairAuth.until.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.example.repaircommon.untils.Result;
import org.example.repairAuth.entity.entity.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ayan
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<String> add(@RequestBody Users user){
        Result<String> rs = new Result<>();
        rs.setCode(-1);
        rs.setCount(0);
        rs.setCode(null);
        if(
                user.getUserName()!=null&&
                        user.getPassword()!=null&&
                        user.getPhone()!=null&&
                        user.getEmail()!=null
        ){
            if(userService.queryUser(user.getUserName()) == null){

                log.info("saveUser:"+user);
                //密码加密
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.saveUser(user);
                if(userService.queryUser(user.getUserName()) == null){
                    rs.setMsg("添加用户失败！添加新分组失败！");
                }else{
                    rs.setCode(0);
                    rs.setMsg("添加用户成功！");
                    rs.setCount(0);
                    rs.setCode(null);
                }
            }else{
                rs.setMsg("添加用户失败！已有该用户名的用户！");
            }
        }else {
            rs.setMsg("添加用户失败！请检查用户名、密码、手机号、邮箱是否填写完整！");
        }

        return rs;
    }
    @Operation(summary = "分配角色")
    @PostMapping("/allocateRole")
    public Result<String> allocateRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") List<Integer> roleIds){
        log.info("allocateRole:"+userId+" "+roleIds);
        List<String> S = new ArrayList<>();
        Result<String> rs = new Result<>();
        boolean admin = false;
        boolean repair = false;
        boolean report = false;
        //获取当前登录用户角色
        UserDetailsVO currentUserInfo =userDetailsService.getCurrentUser();
        List<String>authoritiesList = currentUserInfo.getAuthoritiesList();
        if (authoritiesList.isEmpty())
        {
            rs.setCode(-1);
            rs.setMsg("当前用户无角色，请联系管理员！");
            return rs;
        }
        for (String i:currentUserInfo.getAuthoritiesList())
        {
            if ("ROLE_ADMIN".equals(i))
            {
                admin = true;
                repair = true;
                report = true;
                break;
            }if ("ROLE_RepairAdmin".equals(i)){
            repair = true;
        }if ("ROLE_ReportAdmin".equals(i)){
            report = true;

        }
        }

        boolean finalAdmin = admin;
        boolean finalRepair = repair;
        boolean finalReport = report;
        roleIds = roleIds.stream()
                .filter(roleId -> {
                    Roles role = rolesMapper.selectById(roleId);
                    String roleName = role.getRoleName();
                    boolean keep = true;

                    if ("ROLE_ADMIN".equals(roleName) && !finalAdmin) {
                        S.add("权限不足，无法分配超级管理员\n");
                        keep = false;
                    }
                    if ("ROLE_RepairAdmin".equals(roleName) && !finalAdmin) {
                        S.add("权限不足，无法分配维修管理员\n");
                        keep = false;
                    }
                    if ("ROLE_ReportAdmin".equals(roleName) && !finalAdmin) {
                        S.add("权限不足，无法分配报修管理员\n");
                        keep = false;
                    }
                    if ("ROLE_Repair".equals(roleName)&&!finalRepair){
                        S.add("权限不足，无法分配维修人员\n");
                        keep = false;
                    }
                    if ("ROLE_Report".equals(roleName)&&!finalReport){
                        S.add("权限不足，无法分配报修人员\n");
                        keep = false;
                    }
                    return keep;
                })
                .collect(Collectors.toList());
        if (roleIds.isEmpty())
        {
            S.add("角色分配失败，权限不足！\n");
            return rs;
        }else {
            String data= userService.allocateAuthority(userId,roleIds);
            log.info("roleIds:"+roleIds);
            S.add(data);
        }
        rs.setMsg(S.toString());
        return rs;
    }
    @Operation(summary = "分配权限")
    @PostMapping("/allocatePermission")
    public Result<String> allocatePermission(@RequestParam("userId") Integer userId, @RequestParam("permissionIds") List<Integer> permissionIds){
        Result<String> rs = new Result<>();
        UserDetailsVO currentUserInfo =userDetailsService.getCurrentUser();
        List<String>authoritiesList = currentUserInfo.getAuthoritiesList();
        if (authoritiesList.isEmpty())
        {
            rs.setCode(-1);
            rs.setMsg("权限不足，请联系管理员！");
            return rs;
        }
        for (String i:currentUserInfo.getAuthoritiesList())
        {
            if(i.equals("ROLE_ADMIN")){
                rs =userService.allocatePermission(userId,permissionIds);
                return rs;
            }else {
                rs.setCode(-1);
                rs.setMsg("权限不足，请联系管理员！");
                return rs;
            }
        }
        return rs;
    }
    @Operation(summary = "获取用户列表")
    @PostMapping("/getUserList")
    public Result<List<Users>> getUserList(@RequestParam("page")Integer page,@RequestParam("limit") Integer limit){
        Result<List<Users>> rs = new Result<>();
        UserDetailsVO currentUserInfo =userDetailsService.getCurrentUser();
        List<Users> list = new ArrayList<>();
        try {
            for (String i:currentUserInfo.getAuthoritiesList())
            {
                if("ROLE_ADMIN".equals(i)){
                    list = userService.getUserList(page, limit,i) ;
                    rs.setCount(list.size());
                    rs.setMsg("获取用户列表成功！");
                    rs.setData(list);
                    return rs;
                }
                if ("ROLE_ReportAdmin".equals(i)){
                    list.addAll(userService.getUserList(page, limit,i));
                }
                if ("ROLE_RepairAdmin".equals(i)){
                    list.addAll(userService.getUserList(page, limit,i));
                }
            }
            rs.setData(list);
            rs.setCount(list.size());
            rs.setMsg("获取用户列表成功！");
        }catch (Exception e){
            rs.setMsg("获取用户列表失败！");
            rs.setCode(-1);
            log.error(e.getMessage());
        }

        return rs;
    }
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public Result<String> delete(@RequestParam("userId") List<Integer> userIds){
        Result<String> rs = new Result<>();
        //获取当前登录用户角色
        UserDetailsVO currentUserInfo = userDetailsService.getCurrentUser();
        List<String>authoritiesList = currentUserInfo.getAuthoritiesList();
        List<String> S = new ArrayList<>();
        if (authoritiesList.isEmpty())
        {
            rs.setCode(-1);
            rs.setMsg("权限不足，请联系管理员！");
            return rs;
        }
        boolean admin = false;
        boolean repair = false;
        boolean report = false;
        for (String i:currentUserInfo.getAuthoritiesList())
        {
            if ("ROLE_ADMIN".equals(i))
            {
                admin = true;
                repair = true;
                report = true;
                break;
            }if ("ROLE_RepairAdmin".equals(i)){
            repair = true;
        }if ("ROLE_ReportAdmin".equals(i)){
            report = true;

        }
        }

        boolean finalAdmin = admin;
        boolean finalRepair = repair;
        boolean finalReport = report;
        userIds = userIds.stream().filter(userId -> {
           boolean keep = true;
           String role = userService.QueryUserRoleByUserId(userId).get(0);
           log.info("role:"+role);
           log.info("finalAdmin:"+finalAdmin);
           if (!finalAdmin && "ROLE_ADMIN".equals(role)){
               keep = false;
               S.add("权限不足，无法删除超级管理员\n");
           }
           if ("ROLE_RepairAdmin".equals(role) && !finalAdmin) {
               S.add("权限不足，无法删除维修管理员\n");
               keep = false;
           }
           if ("ROLE_ReportAdmin".equals(role)&&!finalAdmin){
               S.add("权限不足，无法删除报修管理员\n");
               keep = false;
           }
           if ("ROLE_Repair".equals(role)&&!finalRepair){
               S.add("权限不足，无法删除维修人员\n");
               keep = false;
           }
           if ("ROLE_Report".equals(role)&&!finalReport){
               S.add("权限不足，无法删除报修人员\n");
               keep = false;
           }
           return keep;
        })
                .collect(Collectors.toList());
        List<String> usernames = new ArrayList<>();
        if (!userIds.isEmpty())
        {
            for (Integer userId:userIds)
            {
                Users user = userMapper.selectById(userId);
                usernames.add(user.getUserName());
                userService.removeById(userId);
            }
            S.add("删除用户："+usernames+"\n");
            S.add("删除成功！");
        }else {
            S.add("删除失败！");
        }
        rs.setMsg(S.toString());
        rs.setCount(userIds.size());
        rs.setCode(0);
        return rs;
    }
    @Operation(summary = "测试")
    @PostMapping("/test")
    public String test(){

        UserDetailsVO currentUserInfo = userDetailsService.getCurrentUser();
        currentUserInfo.getAuthorities().forEach(System.out::println);
        List<String>authoritiesList = currentUserInfo.getAuthoritiesList();
        if (authoritiesList.isEmpty())
        {
            log.info("当前用户无任何权限！");
            return "当前用户无任何权限！";
        }
        for (String i:currentUserInfo.getAuthoritiesList())
        {
            if(i.equals("ROLE_ADMIN")){

              log.info("当前用户是管理员");
            }else {
                log.info("当前用户不是管理员");
            }
        }
        return "test";
    }
}
