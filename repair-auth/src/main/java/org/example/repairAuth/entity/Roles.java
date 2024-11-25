package org.example.repairAuth.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (Roles)实体类
 *
 * @author ayan
 * @since 2024-11-15 10:47:20
 */
@Data
@TableName("roles")
public class Roles implements Serializable {
    private static final long serialVersionUID = -74440126794828857L;

    private Integer id;
/**
     * 角色名称
     */
    private String roleName;


}

