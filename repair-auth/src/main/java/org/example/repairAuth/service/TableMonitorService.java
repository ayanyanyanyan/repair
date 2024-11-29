package org.example.repairAuth.service;

import org.example.repairAuth.entity.UpdateLog;
import org.example.repairAuth.entity.Users;
import org.example.repairAuth.mapper.UpdateLogMapper;
import org.example.repairAuth.mapper.UserMapper;
import org.example.repairAuth.until.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TableMonitorService {
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.receiver}")
    private String receiver;
    @Value("${spring.mail.nickName}")
    private String nickName;
    @Autowired
    private EmailUtil emailUtil;


    @Scheduled(fixedRate = 360000)
    public void checkForUpdate()
    {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        emailUtil.sendSimpleMail(receiver,"定时任务", "定时任务执行成功！");
    }
}