package com.egao.common.core.scheduler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.Cache;
import com.egao.common.core.Constants;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.utils.OrderUtil;
import com.egao.common.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Component
@EnableScheduling
public class OrderScheduler {

    Logger logger = Logger.getLogger(OrderScheduler.class.getName());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private OrderService orderService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private SupplierMarkService supplierMarkService;

    private boolean environment = Constants.DEVELOPMENT_ENVIRONMENT;

    // 同步报价单 每隔1小时执行一次
    @Scheduled(fixedRate = 35*60*1000)
    public void quoteTasks() {
        try {

            if (environment) {
                return;
            }

            orderService.syncQuote();

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("quoteTasks error:" + e.getMessage());
        }
    }





    // 同步订单 每隔1小时执行一次
    @Scheduled(cron = "1 0 * * * ?", zone = "Asia/Shanghai")
    public void orderTasks() {

        if (environment) {
            return;
        }

        System.out.println("采购清单 同步数据开始：" + DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd HH;mm:ss:SSS"));
        try {
            String platform_order_number = "";
            String logist_num = "";
            orderService.syncOrder(platform_order_number, logist_num);
            System.out.println("采购清单 同步数据结束：" + DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd HH;mm:ss:SSS"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("orderTasks error:" + e.getMessage());
        }
    }


    // 同步站点 每隔1小时执行一次
    @Scheduled(fixedRate = 30*60*1000)
    public void shopTasks() {
        try {

            // 查询全部站点并存入缓存
            List<Map<String, Object>> shopList = orderService.selectShop(new HashMap());
            Cache.setShopList(shopList);

            System.out.println("supplierTasks shopList："+ JSON.toJSON(shopList));

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("shopTasks error:" + e.getMessage());
        }
    }

    // 同步颜色 每隔半小时执行一次
    @Scheduled(fixedRate = 31*60*1000)
    public void colourTasks() {
        try {

            // 查询全部颜色并且存入缓存
            List<Map<String, Object>> colourList = translationService.selectAll();
            Cache.setColourList(colourList);
            System.out.println("supplierTasks colourTasks："+ JSON.toJSON(colourList));

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("colourList error:" + e.getMessage());
        }
    }

    // 同步供应商标识 每隔半小时执行一次
    @Scheduled(fixedRate = 33*60*1000)
    public void supplierTasks() {
        try {

            // 查询全部供应商
            List<Map<String, Object>> supplierList = supplierMarkService.selectAll();
            // 存入缓存
            Cache.setSupplierList(supplierList);

            System.out.println("supplierTasks supplierList："+ JSON.toJSON(supplierList));

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("supplierTasks error:" + e.getMessage());
        }
    }


}
