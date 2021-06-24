package com.egao.common.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.egao.common.core.utils.UserAgentGetter;
import com.egao.common.core.web.PageParam;
import com.egao.common.core.web.PageResult;
import com.egao.common.system.entity.EmailRecord;
import com.egao.common.system.entity.LoginRecord;
import com.egao.common.system.mapper.EmailRecordMapper;
import com.egao.common.system.mapper.LoginRecordMapper;
import com.egao.common.system.service.EmailRecordService;
import com.egao.common.system.service.LoginRecordService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 登录日志服务实现类
 * Created by wangfan on 2018-12-24 16:10
 */
@Service
public class EmailRecordServiceImpl extends ServiceImpl<EmailRecordMapper, EmailRecord> implements EmailRecordService {

/*
    @Override
    public PageResult<LoginRecord> listPage(PageParam<LoginRecord> page) {
        List<LoginRecord> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<LoginRecord> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }
*/

    @Override
    public void saveAsync(Integer type, String comments, HttpServletRequest request) {
        EmailRecord emailRecord = new EmailRecord();
//        emailRecord.setUsername(username);
        emailRecord.setOperType(type);
        emailRecord.setComments(comments);
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        emailRecord.setOs(agentGetter.getOS());
        emailRecord.setDevice(agentGetter.getDevice());
        emailRecord.setBrowser(agentGetter.getBrowser());
        emailRecord.setIp(agentGetter.getIp());
        saveAsync(emailRecord);
    }

    @Override
    public void saveAsync(HttpServletRequest request) {
        saveAsync(LoginRecord.TYPE_LOGIN, null, request);
    }

    @Async
    public void saveAsync(EmailRecord emailRecord) {

        baseMapper.insert(emailRecord);
    }

}
