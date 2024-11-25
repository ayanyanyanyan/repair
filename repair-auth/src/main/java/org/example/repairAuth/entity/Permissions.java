package org.example.repairAuth.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (Permissions)实体类
 *
 * @author ayan
 * @since 2024-11-15 10:49:20
 */
@Data
@TableName("permissions")
public class Permissions implements Serializable {
    private static final long serialVersionUID = -97377185969541134L;

    private Integer id;
/**
     * 权限
     */
    private String name;


}

