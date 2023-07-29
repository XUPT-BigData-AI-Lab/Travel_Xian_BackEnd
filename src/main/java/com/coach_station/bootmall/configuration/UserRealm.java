package com.coach_station.bootmall.configuration;

import com.coach_station.bootmall.dao.UserNewDao;
import com.coach_station.bootmall.entity.User;
import com.coach_station.bootmall.service.UserNewService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: yjw
 * @Date: 2020/04/11/10:33
 * @Description:
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserNewService userNewService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权");
        //给资源进行授权
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        /*//添加资源的授权字符串
        info.addStringPermission("user:add");*/

        //获取当前用户
        Subject subject= SecurityUtils.getSubject();
        User user=(User)subject.getPrincipal();
        //到数据库查询当前登录用户的授权字符串
        User dbUser=userNewService.findByUserId(user.getUserId());//通过当前登录用户id查找的数据库用户

        info.addStringPermission(dbUser.getPerms());
        System.out.println("授权perms:" + dbUser.getPerms());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        User user = userNewService.findByPhoneNumber(userToken.getUsername());
        if (user == null){
            return null;
        }

        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
