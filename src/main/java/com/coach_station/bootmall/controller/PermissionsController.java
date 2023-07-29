package com.coach_station.bootmall.controller;

import com.coach_station.bootmall.service.PermissionsService;
import com.coach_station.bootmall.service.UserNewService;
import com.coach_station.bootmall.util.VerifyUtil;
import com.coach_station.bootmall.vo.LoginInfoVo;
import com.coach_station.bootmall.vo.RegisterInfoVo;
import com.coach_station.bootmall.vo.Result;
import com.coach_station.bootmall.enumAndConst.ResultCodeEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @Auther: yjw
 * @Date: 2022/01/08/15:12
 * @Description:
 */
@RequestMapping("/permissions")
@Controller
@CrossOrigin
public class PermissionsController {

    @Autowired
    PermissionsService permissionsService;

    @Autowired
    UserNewService userNewService;

    //TODO 发送短信验证码接口
    @GetMapping("/sendPhoneCode")
    @ResponseBody
    public Result sendPhoneCode(@RequestParam(name = "phone_number") String phoneNumber){
        if (phoneNumber == null || phoneNumber.length() < 11){
            return Result.setResult(ResultCodeEnum.LOGIN_PHONE_PATTARN_ERRROR);
        }
        return Result.setResult(permissionsService.sendPhoneCode(phoneNumber,permissionsService.cacheAndCreateCheckPhoneCode(phoneNumber)));
    }

    // 新用户注册接口
    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestBody RegisterInfoVo registerInfo){
        if (registerInfo == null || registerInfo.getPhoneNumber() == null || registerInfo.getPhoneNumber().length() < 11){
            return Result.setResult(ResultCodeEnum.LOGIN_PHONE_PATTARN_ERRROR);
        }
        return Result.setResult(permissionsService.register(registerInfo,permissionsService.getCacheCheckPhoneCode(registerInfo.getPhoneNumber())));
    }

    // 生成验证码图片接口
    @GetMapping("/getCheckCodePicture")
    @ResponseBody
    protected void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getSession().getId().toString());
        res.addHeader("Session-Id", subject.getSession().getId().toString());
        Object[] objs = VerifyUtil.createImage();
        setImgCode(req,objs);
        setImage(res,objs);
    }

    // 用户登录接口
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody LoginInfoVo loginInfo){
        ResultCodeEnum codeEnum = permissionsService.login(loginInfo);
        return Result.setResult(codeEnum).data("SessionId",getSession().getId());
    }

    // 用户登出接口
    @GetMapping("/logout")
    @ResponseBody
    public Result logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.ok();
    }

    // 用户测试接口
    @GetMapping("/log")
    @ResponseBody
    public Result log(){
        return Result.ok();
    }

    // 用户无权限提示接口
    @GetMapping("/toLogin")
    @ResponseBody
    public Result withOutPermissions(){
        return Result.setResult(ResultCodeEnum.WITHOUT_PERMISSION);
    }

    // 登录时忘记旧密码修改密码接口
    @PostMapping("/modifyPassword")
    @ResponseBody
    public Result modifyPassword(@RequestBody RegisterInfoVo registerInfo){
        if (registerInfo == null || registerInfo.getPhoneNumber() == null || registerInfo.getPhoneNumber().length() < 11){
            return Result.setResult(ResultCodeEnum.LOGIN_PHONE_PATTARN_ERRROR);
        }
        return Result.setResult(permissionsService.modifyPassword(registerInfo,permissionsService.getCacheCheckPhoneCode(registerInfo.getPhoneNumber())));
    }

    public Session getSession(){
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession();
    }

    public void setImgCode(HttpServletRequest req,Object[] objs){
        String imgcode = (String) objs[0];
        HttpSession session = req.getSession();
        session.setAttribute("imgcode", imgcode);
    }

    public void setImage(HttpServletResponse res,Object[] objs) throws IOException {
        BufferedImage img = (BufferedImage) objs[1];
        res.setContentType("image/png");
        OutputStream os = res.getOutputStream();
        ImageIO.write(img, "png", os);
        os.close();
    }

}
