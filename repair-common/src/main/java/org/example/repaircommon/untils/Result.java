package org.example.repaircommon.untils;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;//状态码
    private String msg;//返回信息
    private long count; //总数
    private T data;//返回数据
}
