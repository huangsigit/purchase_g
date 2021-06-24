package com.egao.common.core.scheduler;

import com.alibaba.fastjson.JSON;
import com.egao.common.core.Cache;
import com.egao.common.core.Constants;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.system.service.OrderService;
import com.egao.common.system.service.SupplierMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Component
@EnableScheduling
public class SupplierScheduler {

    Logger logger = Logger.getLogger(SupplierScheduler.class.getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");



    private boolean environment = Constants.DEVELOPMENT_ENVIRONMENT;





}
