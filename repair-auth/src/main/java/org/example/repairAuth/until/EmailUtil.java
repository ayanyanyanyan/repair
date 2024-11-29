package org.example.repairAuth.until;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class EmailUtil {
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.receiver}")
    private String receiver;
    @Value("${spring.mail.nickName}")
    private String nickName;
    @Autowired
    private JavaMailSender mailSender;


    @Async
    public void sendSimpleMail(String to, String title, String content) {
        CompletableFuture.runAsync(()->{
            log.info("发送邮件===》收件人：{}，标题：{},内容：{}", to, title, content);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(nickName + '<' + from + '>');
            message.setTo(StringUtils.hasText(to) ? to : receiver);
            message.setSubject(title);
            message.setText(content);
            try {
                mailSender.send(message);
            } catch (MailException e) {
                log.error("邮件发送失败===>",e);
                return;
            }
            log.info("邮件发送成功===>");
        });
    }

    @Async
    public void sendAttachmentsMail(String to, String title, String cotent, List<File> fileList) {
        if (fileList == null) {
            sendSimpleMail(to, title, cotent);
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(StringUtils.hasText(to) ? to : receiver);
            helper.setSubject(title);
            helper.setText(cotent);
            String fileName = null;
            for (File file : fileList) {
                fileName = MimeUtility.encodeText(file.getName(), "GB2312", "B");
                helper.addAttachment(fileName, file);
            }
            mailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败===>",e);
            return;
        }
        log.info("邮件发送成功");
    }

}
