package com.egao.common.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.egao.common.core.web.PageParam;
import com.egao.common.system.entity.EmailRecord;
import com.egao.common.system.entity.LoginRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 邮件发送Mapper接口
 * Created by huangsi on 2018-12-24 16:10
 */
public interface EmailRecordMapper extends BaseMapper<EmailRecord> {

}
