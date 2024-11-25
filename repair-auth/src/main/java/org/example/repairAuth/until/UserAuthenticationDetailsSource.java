package org.example.repairAuth.until;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component("authenticationDetailsSource")
/**
 * 自定义认证详情源
 * 用于替换SpringSecurity默认的WebAuthenticationDetails
 * 用于获取用户输入的验证码
 * 继承WebAuthenticationDetails
 *author:liuzeyang
 * date:2024-11-13
 */
public class UserAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationDetailsSource.class);

    static {
        // 静态初始化块
        log.info("UserAuthenticationDetailsSource initialized");
    }

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        log.info("开始替换SpringSecurity原生的WebAuthenticationDetails");
        return new UserWebAuthenticationDetails(request);
    }
}
