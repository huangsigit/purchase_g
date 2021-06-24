package com.egao.common.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.egao.common.system.entity.Auth;
import com.egao.common.system.mapper.AuthMapper;
import com.egao.common.system.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 * Created by huangsi on 2018-12-24 16:10
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {

}
