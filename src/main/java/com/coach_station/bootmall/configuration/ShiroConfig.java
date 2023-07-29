package com.coach_station.bootmall.configuration;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yjw
 * @Date: 2020/04/11/10:36
 * @Description:
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManger") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> filterMap = new HashMap<>();
        filterMap.put("/userCenter/**","authc");    //个人中心需要权限
        filterMap.put("/order/**","authc");         //订单中心需要权限
        filterMap.put("/manage/**", "perms[manager]");
//        filterMap.put("/query/rideCode/sendRideCode","authc");
        bean.setFilterChainDefinitionMap(filterMap);
        bean.setUnauthorizedUrl("/permissions/toLogin");
        bean.setLoginUrl("/permissions/toLogin");
        return bean;

    }

    @Bean(name = "securityManger")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager()
    {
        CustomDefaultWebSessionManager customSessionManager = new CustomDefaultWebSessionManager();
        customSessionManager.setGlobalSessionTimeout(1000 * 60 * 60 * 24);

        return  customSessionManager;
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
