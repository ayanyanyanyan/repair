package org.example.repairAuth.until;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.logging.Logger;
/**
 * 自定义认证信息类，可用于获取用户输入的验证码
 * @author ayan
 * @date 2024.11.13
 */

public class UserWebAuthenticationDetails extends WebAuthenticationDetails {

    private static Logger log = (Logger) LoggerFactory.getLogger(UserWebAuthenticationDetails.class);

    private static final long serialVersionUID = 6975601077710753878L;

    private final String code;

    /**
     * @param request that the authentication request was received from
     */

    public UserWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        code=request.getParameter("code");
        log.info("进入到了获取验证码的过滤器-->WebAuthenticationDetails,获取到的验证码-->"+code);

    }
//new,runnable,blocked,waiting,timed_waiting,terminated


    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; code: ").append(this.getCode());
        return sb.toString();
    }
}