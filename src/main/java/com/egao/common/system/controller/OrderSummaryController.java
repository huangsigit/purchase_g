package com.egao.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.OrderService;
import com.egao.common.system.service.SupplierMarkService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/orderSummary")
public class OrderSummaryController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SupplierMarkService supplierMarkService;


    @RequiresPermissions("sys:orderSummary:view")
    @RequestMapping()
    public String orderSummary(Model model) {

        System.out.println("system/orderSummary.html");

        return "system/orderSummary.html";
    }


    @OperLog(value = "采购单", desc = "分页查询")
//    @RequiresPermissions("sys:orderSummary:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "purchase_status", required = false)Integer purchase_status) {

        System.out.println("采购汇总 分页查询数据...");

        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("goods_no", goods_no);
        map.put("supplier_id", supplier_id);
        map.put("purchase_status", purchase_status);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = orderService.selectOrderSummary(map);

        int count = 0;
        if(list.size() > 0){
            count = orderService.selectOrderSummaryCount(map);
            for(Map<String, Object> orderSummaryMap : list){
                Integer purchase_status_db = (Integer)orderSummaryMap.get("purchase_status");

                orderSummaryMap.put("purchase_status_str", getPurchaseStatusStr(purchase_status_db));

                int price_status2 = (int)orderSummaryMap.get("price_status");

                orderSummaryMap.put("price_status_str", price_status2 == 0 ? "未确认" : "已确认");


            }
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("采购汇总 data:"+JSONObject.toJSON(data));
        return data;
    }

}
