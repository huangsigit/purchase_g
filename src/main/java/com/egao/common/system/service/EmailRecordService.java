package com.egao.common.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.egao.common.core.web.PageParam;
import com.egao.common.core.web.PageResult;
import com.egao.common.system.entity.EmailRecord;
import com.egao.common.system.entity.LoginRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 登录日志服务类
 * Created by wangfan on 2018-12-24 16:10
 */
public interface EmailRecordService extends IService<EmailRecord> {

/*
    */
/**
     * 关联分页查询
     *//*

    PageResult<LoginRecord> listPage(PageParam<LoginRecord> page);

    */
/**
     * 关联查询所有
     *//*

    List<LoginRecord> listAll(Map<String, Object> page);
*/

    /**
     * 添加登录日志
     *
     * @param type     操作类型
     * @param comments 备注
     * @param request  HttpServletRequest
     */
    void saveAsync(Integer type, String comments, HttpServletRequest request);

    /**
     * 添加登录成功的登录日志
     *
     * @param request  HttpServletRequest
     */
    void saveAsync(HttpServletRequest request);

}
