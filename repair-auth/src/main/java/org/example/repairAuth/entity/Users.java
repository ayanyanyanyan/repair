package org.example.repairAuth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (Users)实体类
 *
 * @author makejava
 * @since 2024-11-06 15:42:04
 */

@Data
@TableName("users")
public class Users implements Serializable {
    private static final long serialVersionUID = 136078656743520826L;

    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Integer id;
/**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;
/**
     * 密码
     */
    private String password;
/**
     * 电话
     */
    private String phone;
/**
     * 邮箱
     */
    private String email;
/**
     * 是否启用
     */
    private Integer enabled;


}

