package org.example.repairAuth.entity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (Modules)实体类
 *
 * @author ayan
 * @since 2024-11-15 10:49:20
 */
@Data
@TableName("modules")
public class Modules implements Serializable {
    private static final long serialVersionUID = 481124215248998269L;

    private Integer id;

    private String name;

    private String path;

}

