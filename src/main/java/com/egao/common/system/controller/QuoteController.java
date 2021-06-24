package com.egao.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.Cache;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.QuoteService;
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
@RequestMapping("/sys/quote")
public class QuoteController extends BaseController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private SupplierMarkService supplierMarkService;

    @RequiresPermissions("sys:quote:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/quote.html");

        Map map = new HashMap();
        map.put("page", 0);
        map.put("rows", 1000);
        List<Map<String, Object>> supplierList = supplierMarkService.select(map);
        model.addAttribute("supplierList", JSON.toJSONString(supplierList));

        model.addAttribute("shopList", Cache.getShopList());

        return "system/quote.html";
    }




    @OperLog(value = "报价单", desc = "分页查询")
//    @RequiresPermissions("sys:quote:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "price_status", required = false)Integer price_status, @RequestParam(name = "searchTime", required = false)String searchTime
            , @RequestParam(name = "purchase_status", required = false)Integer purchase_status) {


        System.out.println("报价单 分页查询数据...");



        String startTime = StringUtils.substringBefore(searchTime, " - ");

        System.out.println("startTime1："+startTime);
        System.out.println("startTime11："+(System.currentTimeMillis() - 86400000*6l));
        // 获取7天前日期
//        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000 * 7, "yyyy-MM") : startTime;
        // 获取一年前日期
        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000*6l, "yyyy-MM-dd") : startTime;
        System.out.println("startTime2："+startTime);

        String endTime = StringUtils.substringAfter(searchTime, " - ");
        // 获取昨天日期
        endTime = StringUtils.isEmpty(endTime) ? DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd") : endTime;

        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ; //使用了默认的格式创建了一个日期格式化对象。

                Date startDate = dateFormat.parse(startTime); //注意:指定的字符串格式必须要与SimpleDateFormat的模式要一致。
                Date endDate = dateFormat.parse(endTime); //注意:指定的字符串格式必须要与SimpleDateFormat的模式要一致。

                if(startDate.getTime() > endDate.getTime()){
                    System.out.println("startDate.getTime()："+startDate.getTime());
                    System.out.println("endDate.getTime()："+endDate.getTime());
                    return JsonResult.error("开始日期不能大于结束日期...");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("goods_no", goods_no);
        map.put("supplier_id", supplier_id);
        map.put("price_status", price_status);
        map.put("purchase_status", purchase_status);
        map.put("startTime", startTime+" 00:00:00");
        map.put("endTime", endTime+ " 23:59:59");
        map.put("no_goods_time", null);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = quoteService.selectOrderSummary(map);

        int count = 0;
        if(list.size() > 0){
            count = quoteService.selectOrderSummaryCount(map);
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("报价单 data:"+JSONObject.toJSON(data));
        return data;
    }





    /**
     * 添加数据
     */
    @OperLog(value = "报价单", desc = "添加数据", result = true)
    @RequiresPermissions("sys:quote:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "purchase_price", required = false)String purchase_price, @RequestParam(name = "price_status", required = false)String price_status
            , @RequestParam(name = "quote_time", required = false)String quote_time) {

        System.out.println("报价单 add:"+request);


        try {

            Map map = new HashMap();
            map.put("goods_no", goods_no);
            map.put("supplier_id", supplier_id);
            map.put("purchase_price", purchase_price);
            map.put("price_status", price_status);
            map.put("quote_time", quote_time);
            System.out.println("map："+map);


/*            Map<String, Object> quoteMap = quoteService.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, quote_time);
            if(quoteMap != null){
                return JsonResult.error("该货号已存在");
            }*/


            boolean result = quoteService.insert(map);
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
    @OperLog(value = "报价单", desc = "修改数据", result = true)
    @RequiresPermissions("sys:quote:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Long id, @RequestParam(name = "goods_no", required = false)String goods_no
            , @RequestParam(name = "supplier_id", required = false)String supplier_id, @RequestParam(name = "quote_time", required = false)String quote_time
            , @RequestParam(name = "colour", required = false)String colour, @RequestParam(name = "size", required = false)String size
            , @RequestParam(name = "purchase_price", required = false)String purchase_price, @RequestParam(name = "price_status", required = false)String price_status) {

        System.out.println("报价单 update:"+request);

        try {

            Map map = new HashMap();
            map.put("id", id);
//            map.put("goods_no", goods_no);
//            map.put("supplier_id", supplier_id);
            map.put("purchase_price", purchase_price);
            map.put("price_status", price_status);
//            map.put("quote_time", quote_time);
            System.out.println("map："+map);

/*
            Map<String, Object> quoteMap = quoteService.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, colour, size, quote_time);
            if(quoteMap != null){
                Long idDb = (Long)quoteMap.get("id");
                if(!idDb.equals(id)){
                    return JsonResult.error("该货号已存在");
                }
            }
*/

            boolean result = quoteService.update(map);
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
    @OperLog(value = "报价单", desc = "删除数据", result = true)
    @RequiresPermissions("sys:quote:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(Long id) {

        System.out.println("报价单 删除数据 quote_id："+id);

        boolean result = quoteService.deleteById(id);

        if (result) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }


    /**
     * 批量确认
     */
    @OperLog(value = "采购清单", desc = "批量确认", result = true)
    @RequiresPermissions("sys:quote:update")
    @ResponseBody
    @RequestMapping("/batchConfirm")
    public JsonResult batchConfirm(@RequestBody List<Long> ids) {

        System.out.println("批量确认 ids:"+ids);

        Integer priceStatus = 1; // 单价确认 0未确认 1已确认
        try {
            for(Long id : ids){
//                orderService.updatePurchaseStatus(id, purchaseStatus);
                quoteService.updatePriceStatusById(id, priceStatus);
            }

            return JsonResult.ok("确认成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("确认失败");
    }



}
