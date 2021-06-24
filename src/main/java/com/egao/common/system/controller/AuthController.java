package com.egao.common.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.CoreUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.core.web.PageParam;
import com.egao.common.core.web.PageResult;
import com.egao.common.system.entity.Auth;
import com.egao.common.system.service.AuthService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证管理
 * Created by wangfan on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/sys/auth")
public class AuthController extends BaseController {
    @Autowired
    private AuthService authService;

    @RequiresPermissions("sys:auth:view")
    @RequestMapping()
    public String view() {
        return "system/auth.html";
    }

    /**
     * 分页查询认证
     */
    @OperLog(value = "认证管理", desc = "分页查询")
    @RequiresPermissions("sys:auth:list")
    @ResponseBody
    @RequestMapping("/page")
//    public PageResult<Auth> page(HttpServletRequest request) {
    public JsonResult page(HttpServletRequest request) {
        PageParam<Auth> pageParam = new PageParam<>(request);

        List<Auth> records = authService.page(pageParam, pageParam.getWrapper()).getRecords();

        PageResult<Auth> authPageResult = new PageResult<>(authService.page(pageParam, pageParam.getWrapper()).getRecords(), pageParam.getTotal());

        JsonResult data = JsonResult.ok(0, Integer.valueOf(String.valueOf(pageParam.getTotal()))).put("data", records);

        System.out.println("auth data:"+JSONObject.toJSON(data));
//        return authPageResult;
        return data;
    }

    /**
     * 查询全部认证
     */
    @OperLog(value = "认证管理", desc = "查询全部")
    @RequiresPermissions("sys:auth:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request) {
        PageParam<Auth> pageParam = new PageParam<>(request);
        List<Auth> list = authService.list(pageParam.getOrderWrapper());

        System.out.println("认证管理 list:"+list);
        return JsonResult.ok().setData(list);
    }

    /**
     * 根据id查询认证
     */
    @OperLog(value = "认证管理", desc = "根据id查询")
    @RequiresPermissions("sys:auth:list")
    @ResponseBody
    @RequestMapping("/get")
    public JsonResult get(Integer id) {
        return JsonResult.ok().setData(authService.getById(id));
    }

    /**
     * 添加认证
     */
    @OperLog(value = "认证管理", desc = "添加", param = false, result = true)
    @RequiresPermissions("sys:auth:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult save(Auth auth) {
        if (authService.save(auth)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 修改认证
     */
    @OperLog(value = "认证管理", desc = "修改", param = false, result = true)
    @RequiresPermissions("sys:auth:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(Auth auth) {
        if (authService.updateById(auth)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    /**
     * 删除认证
     */
    @OperLog(value = "认证管理", desc = "删除", result = true)
    @RequiresPermissions("sys:auth:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(Integer id) {
        if (authService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量添加认证
     */
    @OperLog(value = "认证管理", desc = "批量添加", param = false, result = true)
    @RequiresPermissions("sys:auth:save")
    @ResponseBody
    @RequestMapping("/saveBatch")
    public JsonResult saveBatch(@RequestBody List<Auth> list) {
        // 对集合本身进行非空和重复校验
        StringBuilder sb = new StringBuilder();
        sb.append(CoreUtil.listCheckBlank(list, "authCode", "认证标识"));
        sb.append(CoreUtil.listCheckBlank(list, "authName", "认证名称"));
        sb.append(CoreUtil.listCheckRepeat(list, "authCode", "认证标识"));
        sb.append(CoreUtil.listCheckRepeat(list, "authName", "认证名称"));
        if (sb.length() != 0) return JsonResult.error(sb.toString());
        if (authService.saveBatch(list)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    /**
     * 批量删除认证
     */
    @OperLog(value = "认证管理", desc = "批量删除", result = true)
    @RequiresPermissions("sys:auth:remove")
    @ResponseBody
    @RequestMapping("/removeBatch")
    public JsonResult removeBatch(@RequestBody List<Integer> ids) {
        if (authService.removeByIds(ids)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}
