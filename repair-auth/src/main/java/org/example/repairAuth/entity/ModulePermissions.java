package org.example.repairAuth.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (ModulePermissions)实体类
 *
 * @author ayan
 * @since 2024-11-15 10:49:20
 */
@Data
@TableName("module_permissions")
public class ModulePermissions implements Serializable {
    private static final long serialVersionUID = -38808238268062849L;

    private Integer moduleId;

    private Integer permissionId;

}

