package org.example.repairAuth.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (RolePermissions)实体类
 *
 * @author ayan
 * @since 2024-11-15 10:49:20
 */
@Data
@TableName("role_permissions")
public class RolePermissions implements Serializable {
    private static final long serialVersionUID = -47010235308712631L;

    private Integer roleId;

    private Integer permissionId;

}

