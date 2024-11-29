package org.example.repairAuth.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UpdateLog {
    private Long id;
    private String tableName;
    private String action;
    private LocalDateTime updatedAt;
}
