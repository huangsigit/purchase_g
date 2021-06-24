package com.egao.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.*;
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
@RequestMapping("/sys/orderPicking")
public class OrderPickingController extends BaseController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private ShelfService shelfService;



    @RequiresPermissions("sys:orderPicking:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/orderPicking.html");

/*
        Map map = new HashMap();
        map.put("page", 0);
        map.put("rows", 1000);
        List<Map<String, Object>> supplierList = supplierMarkService.select(map);
        model.addAttribute("supplierList", JSON.toJSONString(supplierList));
*/

        return "system/orderPicking.html";
    }




    @OperLog(value = "拣货单", desc = "分页查询")
    @RequiresPermissions("sys:orderPicking:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "price_status", required = false)Integer price_status, @RequestParam(name = "outbound_order_no", required = false)String outbound_order_no
            , @RequestParam(name = "purchase_status", required = false)Integer purchase_status) {

        System.out.println("拣货单 分页查询数据...");

        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("outbound_order_no", outbound_order_no);
        map.put("supplier_id", supplier_id);
        map.put("purchase_status", 1);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = orderService.selectSku(map); // 查询已采购订单


        int count = 0;
        if(list.size() > 0){
            count = orderService.selectSkuCount(map);

            for(Map<String, Object> orderMap : list){
                String goods_no2 = (String)orderMap.get("goods_no");
                String supplier_id2 = (String)orderMap.get("supplier_id");
                String colour = (String)orderMap.get("colour");
                String chinese = (String)orderMap.get("chinese");
                String size = (String)orderMap.get("size");

                orderMap.put("skus", goods_no2 + "+" + supplier_id2 + "," +chinese + "," + size);



            }
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("拣货单 data:"+JSONObject.toJSON(data));
        return data;
    }



    @OperLog(value = "拣货单", desc = "分页查询")
    @RequiresPermissions("sys:orderPicking:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult page(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "platform_order_number", required = false)String platform_order_number, @RequestParam(name = "outbound_order_no", required = false)String outbound_order_no
            , @RequestParam(name = "purchase_status", required = false)Integer purchase_status, @RequestParam(name = "orderId", required = false)Long orderId) {

        System.out.println("拣货单 分页查询数据...");

        Map<String, Object> orderMap2 = orderService.selectById(orderId);
        System.out.println("orderMap2："+orderMap2);
        String goods_no2 = (String)orderMap2.get("goods_no");
        String supplier_id2 = (String)orderMap2.get("supplier_id");
        String colour2 = (String)orderMap2.get("colour");
        String size2 = (String)orderMap2.get("size");


        Map orderMap = new HashMap();
        orderMap.put("page", (page-1)*limit);
        orderMap.put("rows", limit);
        orderMap.put("purchase_status", 1);
        orderMap.put("goods_no", goods_no2);
        orderMap.put("supplier_id", supplier_id2);
        orderMap.put("colour", colour2);
        orderMap.put("size", size2);
        System.out.println("orderMap："+orderMap);

        List<Map<String, Object>> orderList = orderService.selectOrderBySku(orderMap);
        System.out.println("orderList："+orderList);

        int count = 0;
        if(orderList.size() > 0){
            count = orderService.selectOrderCountBySku(orderMap);


        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", orderList);

        System.out.println("拣货单 data:"+JSONObject.toJSON(data));
        return data;
    }



    /**
     * 添加数据
     */
    @OperLog(value = "拣货单", desc = "添加数据", result = true)
    @RequiresPermissions("sys:orderPicking:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "goods_no", required = false)String goods_no, @RequestParam(name = "supplier_id", required = false)String supplier_id
            , @RequestParam(name = "purchase_price", required = false)String purchase_price, @RequestParam(name = "price_status", required = false)String price_status
            , @RequestParam(name = "quote_time", required = false)String quote_time) {

        System.out.println("拣货单 add:"+request);


        try {

            Map map = new HashMap();
            map.put("goods_no", goods_no);
            map.put("supplier_id", supplier_id);
            map.put("purchase_price", purchase_price);
            map.put("price_status", price_status);
            map.put("quote_time", quote_time);
            System.out.println("map："+map);


/*
            Map<String, Object> quoteMap = quoteService.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, quote_time);
            if(quoteMap != null){
                return JsonResult.error("该货号已存在");
            }
*/


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
    @OperLog(value = "拣货单", desc = "修改数据", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Long id, @RequestParam(name = "goods_no", required = false)String goods_no
            , @RequestParam(name = "supplier_id", required = false)String supplier_id, @RequestParam(name = "quote_time", required = false)String quote_time
            , @RequestParam(name = "purchase_price", required = false)String purchase_price, @RequestParam(name = "price_status", required = false)String price_status) {

        System.out.println("拣货单 update:"+request);

        try {

            Map map = new HashMap();
            map.put("id", id);
            map.put("goods_no", goods_no);
            map.put("supplier_id", supplier_id);
            map.put("purchase_price", purchase_price);
            map.put("price_status", price_status);
            map.put("quote_time", quote_time);
            System.out.println("map："+map);

/*
            Map<String, Object> quoteMap = quoteService.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, quote_time);
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
    @OperLog(value = "拣货单", desc = "删除数据", result = true)
    @RequiresPermissions("sys:orderPicking:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(Long id) {

        System.out.println("拣货单 删除数据 quote_id："+id);

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
    @RequiresPermissions("sys:orderPicking:update")
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


    /**
     * 开始拣货
     */
    @OperLog(value = "拣货单", desc = "开始拣货", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/startPicking")
    public JsonResult startPicking(Long id) {

        try {
            System.out.println("拣货单 开始拣货 id："+id);

            Map<String, Object> orderMap = orderService.selectById(id);
            System.out.println("orderMap："+orderMap);
            List<Map<String, Object>> skuList = skuService.selectBySku(orderMap);
            if(skuList.size() > 0){
                return JsonResult.error("正在拣货中");
            }

            orderMap.put("user_id", getLoginUserId()); // 拣货人
            orderMap.put("status", 1); // 0未拣货 1拣货中 2拣货已完成

            skuService.insert(orderMap);

            // 分配货架

            orderMap.put("page", 0);
            orderMap.put("rows", 1000);
            List<Map<String, Object>> orderList = orderService.selectOrderBySku(orderMap);
            List<Map<String, Object>> canAllotShelfList = shelfService.selectCanAllotShelf(new HashMap());
            for(Map<String, Object> order : orderList){
                Long outbound_order_id = (Long)order.get("outbound_order_id");

                // 查询是否已经分配了货架
                List<Map<String, Object>> orderShelfList = shelfService.selectByOutboundOrderId(outbound_order_id);
                // 如果该订单已经分配货架将不再分配货架号码
                if(orderShelfList.size() > 0){
                    continue;
                }

                // 根据系统订单ID查询已采购订单
                List<Map<String, Object>> purchaseOrderList = orderService.selectPurchaseOrderByOutboundOrderId(outbound_order_id);
                // 大于2说明其他SKU也存在该订单
                if(purchaseOrderList.size() >= 2){
                    for(Map<String, Object> purchaseOrderMap : purchaseOrderList){
                        Long outbound_order_id2 = (Long)purchaseOrderMap.get("outbound_order_id");
                        Long goods_id2 = (Long)purchaseOrderMap.get("goods_id");
//                        Long shelf_id2 = (Long)purchaseOrderMap.get("shelf_id");

                        // 必须要有可分配的货架才执行分配操作
                        if(canAllotShelfList.size() > 0){
                            // 分配货架
                            Map orderShelfMap = new HashMap();
                            orderShelfMap.put("outbound_order_id", outbound_order_id2);
                            orderShelfMap.put("goods_id", goods_id2);
                            orderShelfMap.put("shelf_id", canAllotShelfList.get(0).get("shelf_id"));
                            shelfService.insertOrderShelf(orderShelfMap);
                        }
                    }
                }
            }



            return JsonResult.ok("开始拣货");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("拣货失败");
    }


    /**
     * 取消拣货
     */
    @OperLog(value = "拣货单", desc = "取消拣货", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/cancelPicking")
    public JsonResult cancelPicking(Long id) {

        try {
            System.out.println("拣货单 取消拣货 id："+id);

            Map<String, Object> orderMap = orderService.selectById(id);
            if(orderMap == null){
                return JsonResult.error("取消失败");
            }else{


                List<Map<String, Object>> skuList = skuService.selectBySku(orderMap);

                if(skuList.size() > 0){
                    Integer user_id = (Integer)skuList.get(0).get("user_id");
                    System.out.println("user_id："+user_id);
                    System.out.println("getLoginUserId()："+getLoginUserId());
                    if(!getLoginUserId().equals(user_id)){
                        return JsonResult.error("必须拣货人才可执行该操作");
                    }
                }

                skuService.deleteBySku(orderMap);



                // 判断是否删除货架
                orderMap.put("page", 0);
                orderMap.put("rows", 1000);
                List<Map<String, Object>> orderList = orderService.selectOrderBySku(orderMap);
                for(Map<String, Object> orderMap2 : orderList){
                    Long outbound_order_id = (Long)orderMap2.get("outbound_order_id");

                    List<Map<String, Object>> shelfList = shelfService.selectByOutboundOrderId(outbound_order_id);
                    if(shelfList.size() > 1){ // 大于1时说明其他SKU还有临时货架

                        // 正在操作的SKU
                        String goods_no = (String)orderMap.getOrDefault("goods_no", "");
                        String supplier_id = (String)orderMap.getOrDefault("supplier_id", "");
                        String colour = (String)orderMap.getOrDefault("colour", "");
                        String size = (String)orderMap.getOrDefault("size", "");


                        boolean noDeleteShelf = true;
                        Map deleteSkuMap = new HashMap();
                        // 查询所有SKU，判断是否有正在拣货的SKU
                        for(Map<String, Object> shelfMap : shelfList){
                            // 通过outbound_order_id和goods_id找到sku
                            Long outbound_order_id3 = (Long)shelfMap.get("outbound_order_id");
                            Long goods_id3 = (Long)shelfMap.get("goods_id");
                            Map<String, Object> orderMap3 = orderService.selectByGoodsId(outbound_order_id3, goods_id3);
                            deleteSkuMap.putAll(orderMap3);
                            String goods_no3 = (String)orderMap3.getOrDefault("goods_no", "");
                            String supplier_id3 = (String)orderMap3.getOrDefault("supplier_id", "");
                            String colour3 = (String)orderMap3.getOrDefault("colour", "");
                            String size3 = (String)orderMap3.getOrDefault("size", "");

                            // 如果是正在操作的SKU则跳过
                            if(goods_no.equals(goods_no3) && supplier_id.equals(supplier_id3) && colour.equals(colour3) && size.equals(size3)){
                                System.out.println("正在操作的SKU skuMap4："+JSON.toJSONString(shelfMap));
                                continue;
                            }

                            // 判断该SKU是否在拣货
                            List<Map<String, Object>> skuList4 = skuService.selectBySku(orderMap3);
                            if(skuList4.size() > 0){
                                Map<String, Object> skuMap4 = skuList4.get(0);
                                Integer status = (Integer)skuMap4.get("status");
                                // 正在拣货和已完成都不删除货架
                                if(status == 1 || status == 2){ // 1正在拣货 2已完成拣货

                                    System.out.println("正在拣货 skuMap4："+JSON.toJSONString(skuMap4));
                                    noDeleteShelf = false;
                                    break;
                                }
                            }

                        }

                        if(noDeleteShelf){
                            deleteSkuMap.put("page", 0);
                            deleteSkuMap.put("rows", 1000);
                            // 没有拣货
                            // 找到SKU后再查询SKU下所有的订单
                            List<Map<String, Object>> orderList4 = orderService.selectOrderBySku(deleteSkuMap);
                            System.out.println("没有拣货 orderList4："+JSON.toJSONString(orderList4));
                            for(Map<String, Object> orderMap4 : orderList4){
                                Long outbound_order_id4 = (Long)orderMap4.get("outbound_order_id");
                                if(outbound_order_id.equals(outbound_order_id4)){
                                    System.out.println("删除货架"+JSON.toJSONString(orderMap4));
                                    shelfService.deleteByOutboundOrderId(outbound_order_id4);
                                }
                            }
                        }

                    }
                }






                return JsonResult.ok("取消拣货成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("取消失败");
    }

    /**
     * 完成拣货
     */
    @OperLog(value = "拣货单", desc = "完成拣货", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/finishPicking")
    public JsonResult finishPicking(Long id) {

        try {
            System.out.println("拣货单 完成拣货 id："+id);

            Map<String, Object> orderMap = orderService.selectById(id);
            if(orderMap == null){
                return JsonResult.error("操作失败");
            }else{



                List<Map<String, Object>> skuList = skuService.selectBySku(orderMap);
                if(skuList.size() <= 0){
                    return JsonResult.error("还未开始拣货");
                }else{

                    // 判断是否已完成拣货
                    Integer status0 = (Integer)skuList.get(0).get("status");
                    if(status0 == 2){
                        return JsonResult.error("已完成拣货，不可再操作");
                    }

                    Integer user_id = (Integer)skuList.get(0).get("user_id");
                    if(!getLoginUserId().equals(user_id)){
                        return JsonResult.error("必须拣货人才可执行该操作");
                    }

                    orderMap.put("page", 0);
                    orderMap.put("rows", 1000);
                    // 判断是否全部订单都完成了拣货
                    List<Map<String, Object>> orderList0 = orderService.selectOrderBySku(orderMap);
                    for(Map<String, Object> orderMap2 : orderList0){
                        Integer picking_status = (Integer)orderMap2.get("picking_status");
                        if(picking_status == 0){ // 拣货状态 0未拣货 1已拣货
                            return JsonResult.error("操作失败，还有订单未完成拣货");
                        }
                    }


                    orderMap.put("status", 2); // 0未拣货 1拣货中 2拣货已完成

                    System.out.println("完成拣货成功 orderMap："+orderMap);
                    skuService.updateStatusBySku(orderMap);



/*
                    orderMap.put("page", 0);
                    orderMap.put("rows", 1000);
                    // 没有拣货
                    // 找到SKU后再查询SKU下所有的订单
                    List<Map<String, Object>> orderList4 = orderService.selectOrderBySku(orderMap);
                    for(Map<String, Object> orderMap4 : orderList4){
                        Long outbound_order_id4 = (Long)orderMap4.get("outbound_order_id");
                        shelfService.deleteByOutboundOrderId(outbound_order_id4);
                    }
*/



                    // 判断是否删除货架
                    orderMap.put("page", 0);
                    orderMap.put("rows", 1000);
                    List<Map<String, Object>> orderList = orderService.selectOrderBySku(orderMap);
                    for(Map<String, Object> orderMap2 : orderList){
                        Long outbound_order_id = (Long)orderMap2.get("outbound_order_id");

                        List<Map<String, Object>> shelfList = shelfService.selectByOutboundOrderId(outbound_order_id);
                        if(shelfList.size() > 1){ // 大于1时说明其他SKU还有临时货架

                            // 正在操作的SKU
                            String goods_no = (String)orderMap.getOrDefault("goods_no", "");
                            String supplier_id = (String)orderMap.getOrDefault("supplier_id", "");
                            String colour = (String)orderMap.getOrDefault("colour", "");
                            String size = (String)orderMap.getOrDefault("size", "");


                            boolean noDeleteShelf = true;
                            Map deleteSkuMap = new HashMap();
                            // 查询所有SKU，判断是否有正在拣货的SKU
                            for(Map<String, Object> shelfMap : shelfList){
                                // 通过outbound_order_id和goods_id找到sku
                                Long outbound_order_id3 = (Long)shelfMap.get("outbound_order_id");
                                Long goods_id3 = (Long)shelfMap.get("goods_id");
                                Map<String, Object> orderMap3 = orderService.selectByGoodsId(outbound_order_id3, goods_id3);
                                deleteSkuMap.putAll(orderMap3);
                                String goods_no3 = (String)orderMap3.getOrDefault("goods_no", "");
                                String supplier_id3 = (String)orderMap3.getOrDefault("supplier_id", "");
                                String colour3 = (String)orderMap3.getOrDefault("colour", "");
                                String size3 = (String)orderMap3.getOrDefault("size", "");

                                // 如果是正在操作的SKU则跳过
                                if(goods_no.equals(goods_no3) && supplier_id.equals(supplier_id3) && colour.equals(colour3) && size.equals(size3)){
                                    System.out.println("正在操作的SKU skuMap4："+JSON.toJSONString(shelfMap));
                                    continue;
                                }

                                // 判断该SKU是否在拣货
                                List<Map<String, Object>> skuList4 = skuService.selectBySku(orderMap3);
                                if(skuList4.size() > 0){
                                    Map<String, Object> skuMap4 = skuList4.get(0);
                                    Integer status = (Integer)skuMap4.get("status");
                                    // 正在拣货和已完成都不删除货架
                                    if(status == 1 || status == 2){ // 1正在拣货 2已完成拣货

                                        System.out.println("正在拣货 skuMap4："+JSON.toJSONString(skuMap4));
                                        noDeleteShelf = false;
                                        break;
                                    }
                                }

                            }

                            if(noDeleteShelf){
                                deleteSkuMap.put("page", 0);
                                deleteSkuMap.put("rows", 1000);
                                // 没有拣货
                                // 找到SKU后再查询SKU下所有的订单
                                List<Map<String, Object>> orderList4 = orderService.selectOrderBySku(deleteSkuMap);
                                System.out.println("没有拣货 orderList4："+JSON.toJSONString(orderList4));
                                for(Map<String, Object> orderMap4 : orderList4){
                                    Long outbound_order_id4 = (Long)orderMap4.get("outbound_order_id");
                                    if(outbound_order_id.equals(outbound_order_id4)){
                                        System.out.println("删除货架"+JSON.toJSONString(orderMap4));
                                        shelfService.deleteByOutboundOrderId(outbound_order_id4);
                                    }
                                }
                            }

                        }
                    }





                    return JsonResult.ok("完成拣货成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("操作失败");
    }



    /**
     * 完成订单拣货
     */
    @OperLog(value = "拣货单", desc = "完成拣货", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/finishOrderPicking")
    public JsonResult finishOrderPicking(Long id) {

        try {

            Map<String, Object> orderMap = orderService.selectById(id);
            if(orderMap == null){
                return JsonResult.error("操作失败");
            }else{

                List<Map<String, Object>> skuList = skuService.selectBySku(orderMap);
                if(skuList.size() <= 0){
                    return JsonResult.error("还未开始拣货");
                }else{
                    // 判断是否已完成拣货
                    Integer status = (Integer)skuList.get(0).get("status");
                    if(status == 2){
                        return JsonResult.error("已完成拣货，不可再操作");
                    }

                    Integer user_id = (Integer)skuList.get(0).get("user_id");
                    if(!getLoginUserId().equals(user_id)){
                        return JsonResult.error("必须拣货人才可执行该操作");
                    }


                }


            }



            orderService.updatePickingStatusById(id, 1);

            return JsonResult.ok("开始拣货");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("拣货失败");
    }


    /**
     * 取消订单拣货
     */
    @OperLog(value = "拣货单", desc = "取消拣货", result = true)
    @RequiresPermissions("sys:orderPicking:update")
    @ResponseBody
    @RequestMapping("/cancelOrderPicking")
    public JsonResult cancelOrderPicking(Long id) {

        try {

            Map<String, Object> orderMap = orderService.selectById(id);
            if(orderMap == null){
                return JsonResult.error("操作失败");
            }else{

                List<Map<String, Object>> skuList = skuService.selectBySku(orderMap);
                if(skuList.size() <= 0){
                    return JsonResult.error("还未开始拣货");
                }else{
                    // 判断是否已完成拣货
                    Integer status = (Integer)skuList.get(0).get("status");
                    if(status == 2){
                        return JsonResult.error("已完成拣货，不可再操作");
                    }

                    Integer user_id = (Integer)skuList.get(0).get("user_id");
                    if(!getLoginUserId().equals(user_id)){
                        return JsonResult.error("必须拣货人才可执行该操作");
                    }
                }
            }


            orderService.updatePickingStatusById(id, 0);

            return JsonResult.ok("取消拣货成功");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.error("操作失败");
    }




}
