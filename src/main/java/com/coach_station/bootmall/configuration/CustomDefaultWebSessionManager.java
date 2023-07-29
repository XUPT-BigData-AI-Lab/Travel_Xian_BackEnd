package com.coach_station.bootmall.configuration;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @Auther: yjw
 * @Date: 2022/01/07/22:56
 * @Description:
 */

public class CustomDefaultWebSessionManager extends DefaultWebSessionManager{

    private final String TOKEN = "SessionId";
    /**
     * 获取session id
     * 前后端分离将从请求头中获取jsesssionid
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从请求头中获取token
        String id = WebUtils.toHttp(request).getHeader(TOKEN);
        System.out.println("会话管理器得到的token是：" + id);
        // 判断是否有值
            // 设置当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;

        // 若header获取不到token则尝试从cookie中获取
    }


}
