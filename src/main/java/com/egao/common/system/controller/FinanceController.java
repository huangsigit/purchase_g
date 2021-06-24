package com.egao.common.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.Cache;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.utils.ExcelUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.ExpressRecordService;
import com.egao.common.system.service.OrderService;
import com.egao.common.system.service.QuoteService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/sys/finance")
public class FinanceController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private ExpressRecordService expressRecordService;

    @RequiresPermissions("sys:finance:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/finance.html");

        model.addAttribute("shopList", Cache.getShopList());

        return "system/finance.html";
    }


    @OperLog(value = "财务单", desc = "分页查询")
//    @RequiresPermissions("sys:finance:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "platform_order_number", required = false)String platform_order_number
            , @RequestParam(name = "logist_num", required = false)String logist_num, @RequestParam(name = "searchTime", required = false)String searchTime
            , @RequestParam(name = "purchase_status", required = false)String purchase_status_search, @RequestParam(name = "outbound_order_no", required = false)String outbound_order_no
            , @RequestParam(name = "shop_id", required = false)String shop_id, @RequestParam(name = "goods_no", required = false)String goods_no) {

        System.out.println("财务单 分页查询数据...");

        String startTime = getStartTime(searchTime);
        String endTime = getEndTime(searchTime);

        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("platform_order_number", platform_order_number);
        map.put("logist_num", logist_num);
        map.put("purchase_status", purchase_status_search);

        map.put("outbound_order_no", outbound_order_no);
//        map.put("shop_name", shop_name);
        map.put("shop_id", shop_id);
        map.put("goods_no", goods_no);

        map.put("startTime", startTime+" 00:00:00");
        map.put("endTime", endTime+ " 23:59:59");

        System.out.println("map:"+map);

        List<Map<String, Object>> list = orderService.selectFinance(map);

        int count = 0;
        if(list.size() > 0){
            count = orderService.selectFinanceCount(map);
            for(Map<String, Object> orderMap : list){
                Date outCreate_time = (Date)orderMap.get("outCreate_time");
                Integer price_status = (Integer)orderMap.get("price_status");
                Integer purchase_status = (Integer)orderMap.get("purchase_status");
                String express_number = (String)orderMap.getOrDefault("express_number", "");

                orderMap.put("outCreate_time", DateUtil.formatDate(outCreate_time, "yyyy/MM/dd"));

                orderMap.put("priceStatusStr", price_status!= null && price_status == 1 ? "已确认" : "未确认");

                orderMap.put("purchase_status_str", StringUtils.isNotEmpty(express_number) ? "已发货" : getPurchaseStatusStr(purchase_status));

            }
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("财务单 data:"+JSONObject.toJSON(data));
        return data;
    }




    /**
     * 添加数据
     */
    @OperLog(value = "财务单", desc = "添加数据", result = true)
    @RequiresPermissions("sys:finance:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_name", required = false)String supplier_name
            , @RequestParam(name = "purchase_price", required = false)String purchase_price, @RequestParam(name = "price_status", required = false)String price_status) {

        System.out.println("财务单 add:"+request);


        try {

            Map map = new HashMap();
            map.put("goods_no", goods_no);
            map.put("supplier_name", supplier_name);
            map.put("purchase_price", purchase_price);
            map.put("price_status", price_status);
            System.out.println("map："+map);

            boolean result = orderService.insert(map);
            if(result){
                return JsonResult.ok("添加成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }



    /**
     * 修改数据
     */
    @OperLog(value = "财务单", desc = "修改数据", result = true)
    @RequiresPermissions("sys:finance:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Long id, @RequestParam(name = "outCreateTime", required = false)String outCreateTime
            , @RequestParam(name = "outboundOrderNo", required = false)String outboundOrderNo, @RequestParam(name = "lock_status", required = false)Integer lock_status
            , @RequestParam(name = "shopName", required = false)String shopName, @RequestParam(name = "goods_no", required = false)String goods_no
            , @RequestParam(name = "supplier_name", required = false)String supplier_name, @RequestParam(name = "colour", required = false)String colour
            , @RequestParam(name = "size", required = false)String size, @RequestParam(name = "num", required = false)String num
            , @RequestParam(name = "platformOrderNumber", required = false)String platformOrderNumber, @RequestParam(name = "logistNum", required = false)String logistNum
            , @RequestParam(name = "outbound_order_id", required = false)String outbound_order_id, @RequestParam(name = "goods_id", required = false)String goods_id) {

        System.out.println("财务单 update:"+request);

        try {


            Map map = new HashMap<>();
            map.put("id", id); // 订单ID
            map.put("outCreate_time", outCreateTime); // 订单日期
            map.put("outbound_order_no", outboundOrderNo); // Ezsale系统订单id
            map.put("shop_name", shopName); // 站点
            map.put("goods_no", goods_no); // 货号
            map.put("supplier_name", supplier_name); // 供应商名称
            map.put("colour", colour); // 颜色
            map.put("size", size); // 尺码
            map.put("num", num); // 数量
            map.put("platform_order_number", platformOrderNumber); // 虾皮订单号
            map.put("logist_num", logistNum); // 运单号
            map.put("lock_status", lock_status); // 运单号
            map.put("outbound_order_id", outbound_order_id); // 运单号
            map.put("goods_id", goods_id); // 运单号
            System.out.println("map："+map);

            boolean result = orderService.update(map);
            System.out.println("result："+result);
            if(result){
                return JsonResult.ok("修改成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("修改失败");
    }


    /**
     * 删除数据
     */
    @OperLog(value = "财务单", desc = "删除数据", result = true)
    @RequiresPermissions("sys:finance:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(int id) {

        System.out.println("财务单 删除数据 order_id："+id);

        boolean result = orderService.deleteById(id);

        if (result) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }


    /**
     * 同步数据
     */
    @OperLog(value = "财务单", desc = "同步数据", result = true)
    @RequiresPermissions("sys:finance:add")
    @ResponseBody
    @RequestMapping("/syncData")
    public JsonResult syncData(HttpServletRequest request, @RequestParam(name = "platform_order_number", required = false)String platform_order_number
            , @RequestParam(name = "logist_num", required = false)String logist_num) {

        System.out.println("财务单 同步数据开始：" + DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd HH;mm:ss:SSS"));

        try {

/*
            if(true){
                System.out.println("失败了");
//                return JsonResult.error("同步失败");
                return JsonResult.ok("同步成功");
            }
*/


            boolean orderResult = orderService.syncOrder(platform_order_number, logist_num);
            if(!orderResult){
                return JsonResult.error("订单同步失败");
            }

            boolean quoteResult = orderService.syncQuote();
            if(!quoteResult){
                return JsonResult.error("报价单同步失败");
            }


            System.out.println("财务单 同步数据结束：" + DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd HH;mm:ss:SSS"));
            return JsonResult.ok("同步成功");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("同步失败");
    }



    /**
     * 批量删除清单
     */
    @OperLog(value = "财务单", desc = "批量删除", result = true)
    @RequiresPermissions("sys:finance:delete")
    @ResponseBody
    @RequestMapping("/batchDelete")
    public JsonResult batchDelete(@RequestBody List<Long> ids) {

        System.out.println("批量删除 ids:"+ids);

        try {
            for(Long id : ids){
                orderService.deleteById(id);
            }

            return JsonResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 批量采购
     */
    @OperLog(value = "财务单", desc = "批量采购", result = true)
    @RequiresPermissions("sys:finance:update")
    @ResponseBody
    @RequestMapping("/batchPurchase")
    public JsonResult batchPurchase(@RequestBody List<String> goodsIds) {

        System.out.println("批量采购 orderIds:"+goodsIds);

        for(String goodsId : goodsIds){
            String order_id = StringUtils.substringBefore(goodsId, ",");
            String goods_id = StringUtils.substringAfterLast(goodsId, ",");
            System.out.println("Long.valueOf(order_id):"+Long.valueOf(order_id));
            System.out.println("Long.valueOf(goods_id):"+Long.valueOf(goods_id));
            Map<String, Object> orderMap = orderService.selectByGoodsId(Long.valueOf(order_id), Long.valueOf(goods_id));
            System.out.println("orderMap："+orderMap);
            Integer purchase_status = (Integer)orderMap.get("purchase_status");
            System.out.println("purchase_status："+purchase_status);
            if(purchase_status == null || purchase_status != 0){
                return JsonResult.error("有订单不是待采购状态");
            }
        }




        Integer purchaseStatus = 1;
        try {

            for(String goodsId : goodsIds){


                String order_id = StringUtils.substringBefore(goodsId, ",");
                String goods_id = StringUtils.substringAfterLast(goodsId, ",");
                // 修改为采购状态
                orderService.updatePurchaseStatusByGoodsId(Long.valueOf(order_id), Long.valueOf(goods_id), purchaseStatus);

            }



//            return JsonResult.error("采购失败");
            return JsonResult.ok("采购成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("采购失败");
    }


    /**
     * 批量发货
     */
    @OperLog(value = "财务单", desc = "批量发货", result = true)
    @RequiresPermissions("sys:finance:update")
    @ResponseBody
    @RequestMapping("/batchExpress")
    public JsonResult batchExpress(HttpServletRequest request, @RequestParam(name = "itemIds", required = false)  List<String> itemIds, @RequestParam(name = "company", required = false)String company
            , @RequestParam(name = "express_number", required = false)String express_number, @RequestParam(name = "fees", required = false)String fees
            , @RequestParam(name = "express_time", required = false)String express_time) {

        System.out.println("批量发货 itemIds:"+itemIds);

        String field = request.getParameter("itemIds");
        System.out.println("field："+field);


        for(String itemId : itemIds){

            Map<String, Object> orderMap = orderService.selectByItemId(itemId);
            Integer purchase_status = (Integer)orderMap.get("purchase_status");
            if(purchase_status != 1){
                return JsonResult.error("有订单不是待发货状态");
            }

        }

        Map map = new HashMap();
        map.put("itemIds", itemIds);
        map.put("company", company);
        map.put("express_number", express_number);
        map.put("fees", fees);
        map.put("express_time", express_time);

        System.out.println("batchExpress map:"+map);


        Long expressRecordId = expressRecordService.insert(map);// 生成发货记录
        Integer purchaseStatus = 2;
        try {

            for(String itemId : itemIds){
                map.put("item_id", itemId);

                orderService.updatePurchaseStatusByItemId(itemId, purchaseStatus); // 修改采购状态

                Map orderExpressMap = new HashMap();
                orderExpressMap.put("item_id", itemId);
                orderExpressMap.put("express_id", expressRecordId);
                System.out.println("orderExpressMap:"+orderExpressMap);
                expressRecordService.insertOrderExpress(orderExpressMap);
            }

            return JsonResult.ok("发货成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("发货失败");
    }



    /**
     * 批量作废
     */
    @OperLog(value = "财务单", desc = "批量作废", result = true)
    @RequiresPermissions("sys:finance:update")
    @ResponseBody
    @RequestMapping("/batchVoid")
    public JsonResult batchVoid(@RequestBody List<String> goodsIds) {

        System.out.println("批量作废 goodsIds:"+goodsIds);

        Integer purchaseStatus = 3;
        try {

            for(String goodsId : goodsIds){

                String order_id = StringUtils.substringBefore(goodsId, ",");
                String goods_id = StringUtils.substringAfterLast(goodsId, ",");
                // 修改为作废状态
                orderService.updatePurchaseStatusByGoodsId(Long.valueOf(order_id), Long.valueOf(goods_id), purchaseStatus);

            }

            return JsonResult.ok("作废成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("作废失败");
    }


    @OperLog(value = "财务单", desc = "导出数据到excel")
    @RequiresPermissions("sys:finance:list")
    @ResponseBody
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "platform_order_number", required = false)String platform_order_number
            , @RequestParam(name = "logist_num", required = false)String logist_num, @RequestParam(name = "searchTime", required = false)String searchTime
            , @RequestParam(name = "purchase_status", required = false)String purchase_status_search, @RequestParam(name = "outbound_order_no", required = false)String outbound_order_no
            , @RequestParam(name = "shop_id", required = false)String shop_id, @RequestParam(name = "goods_no", required = false)String goods_no) {

        System.out.println("导出数据到excel start");

        String startTime = getStartTime(searchTime);
        String endTime = getEndTime(searchTime);
        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("platform_order_number", platform_order_number);
        map.put("logist_num", logist_num);
        map.put("purchase_status", purchase_status_search);

        map.put("outbound_order_no", outbound_order_no);
//        map.put("shop_name", shop_name);
        map.put("shop_id", shop_id);
        map.put("goods_no", goods_no);

        map.put("startTime", startTime+" 00:00:00");
        map.put("endTime", endTime+ " 23:59:59");

        System.out.println("map:"+map);

        List<Map<String, Object>> list = orderService.select(map);

        //把要导出到excel的数据的LinkedHashMap装载到这个List里面,这是导出工具类要求封装格式.
        List<Map<String, Object>> exportData = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            Map<String, Object> orderMap = list.get(i);
            Date outCreate_time = (Date)orderMap.get("outCreate_time");
//            String shopId = (String)orderMap.get("shop_id");
            String shop_name = (String)orderMap.getOrDefault("shop_name", "");
            Integer price_status = (Integer)orderMap.get("price_status");
            Integer purchase_status = (Integer)orderMap.get("purchase_status");
            String express_number = (String)orderMap.getOrDefault("express_number", "");


//            orderMap.put("priceStatusStr", price_status!= null && price_status == 1 ? "已确认" : "未确认");
            String priceStatusStr = price_status!= null && price_status == 1 ? "已确认" : "未确认";

//            orderMap.put("purchase_status_str", StringUtils.isNotEmpty(express_number) ? "已发货" : getPurchaseStatusStr(purchase_status));
            String purchase_status_str = StringUtils.isNotEmpty(express_number) ? "已发货" : getPurchaseStatusStr(purchase_status);

            //使用LinkedHashMap,因为这个是有序的map
            LinkedHashMap<String,Object> reportData = new LinkedHashMap<>();

            //装载数据,就是要导出到excel的数据
            reportData.put("outCreate_time_str",DateUtil.formatDate(outCreate_time, "yyyy/MM/dd"));
            reportData.put("outbound_order_no",orderMap.get("outbound_order_no"));

            reportData.put("shop_name",shop_name);
            reportData.put("goods_no",orderMap.get("goods_no"));
            reportData.put("supplier_name",orderMap.getOrDefault("supplier_name", ""));

            reportData.put("chinese_colour",orderMap.getOrDefault("chinese_colour", ""));
            reportData.put("size",orderMap.getOrDefault("size", ""));
            reportData.put("num",orderMap.getOrDefault("num", ""));
            reportData.put("platform_order_number",orderMap.getOrDefault("platform_order_number", ""));
            reportData.put("logist_num",orderMap.getOrDefault("logist_num", ""));
            reportData.put("purchase_price",orderMap.getOrDefault("purchase_price", ""));
            reportData.put("purchase_status_str", StringUtils.isNotEmpty(express_number) ? "已发货" : getPurchaseStatusStr(purchase_status));


            reportData.put("express_time",orderMap.getOrDefault("express_time", ""));
            reportData.put("express_number",orderMap.getOrDefault("express_number", ""));
            reportData.put("fee",orderMap.getOrDefault("fee", ""));
            reportData.put("total_cost",orderMap.getOrDefault("total_cost", ""));
//            reportData.put("lock_status",(Integer)orderMap.get("lock_status") == 0 ? "已锁定" : "可更新");


            exportData.add(reportData);
        }


        //表格列名用ArrayList装载
        List<String> columns = new ArrayList<>();
        //设置excel表格中的列名
        columns.add("订单日期");
        columns.add("Ezsale系统订单id");
        columns.add("站点");
        columns.add("货号");
        columns.add("供应商");

        columns.add("颜色");
        columns.add("尺码");
        columns.add("数量");
        columns.add("虾皮订单号");
        columns.add("运单号");
        columns.add("采购单价");
        columns.add("采购状态");

        columns.add("发货日期");
        columns.add("快递单号");
        columns.add("运费");
        columns.add("总成本");
//        columns.add("更新状态");



        //点击导出按钮的时候,页面上显示的标题,同时也是sheet的名称
        String filename ="finance";
        try {
            //处理一下中文乱码问题
            response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gb2312"), "ISO8859-1")+".xls");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //以上均为数据准备,下面开始调用导出excel工具类
        ExcelUtil.exportToExcel(response, exportData, filename, columns);
        System.out.println("导出数据到excel end");
//            renderState(true);
        return;
    }


}
