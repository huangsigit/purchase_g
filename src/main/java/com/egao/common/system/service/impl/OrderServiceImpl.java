package com.egao.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.Constants;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.utils.OrderUtil;
import com.egao.common.system.entity.Auth;
import com.egao.common.system.mapper.AuthMapper;
import com.egao.common.system.mapper.OrderMapper;
import com.egao.common.system.mapper.QuoteMapper;
import com.egao.common.system.mapper.UserMapper;
import com.egao.common.system.service.AuthService;
import com.egao.common.system.service.OrderService;
import com.egao.common.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author hs
 * @since 2020-10-10
 */
@Service
public class OrderServiceImpl implements OrderService {

    String ezsaleURL = "https://api-cn.ezsale.com/ezsale-order/seller-outbound-order/page/outboundOrder";
    String ezsaleAuth = "bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJ0ZW5hbnRfaWQiOiIwMDAwMDAwMSIsInJvbGVfbmFtZSI6IiIsInVzZXJfdHlwZSI6MSwidXNlcl9pZCI6MTM0OTczOTU5ODUwNjQxNDA5Miwic3lzdGVtX3R5cGUiOjIsInJvbGVfaWQiOiIiLCJ1c2VyX25hbWUiOiJZb29zYnV5IiwidG9rZW5fdHlwZSI6ImFjY2Vzc190b2tlbiIsImFjY291bnQiOiJZb29zYnV5IiwiY2xpZW50X2lkIjoiMjAyMTAxMTIxMjI0NTI2MjE2IiwiZXhwIjoxNjE3MjE2MTA4LCJuYmYiOjE2MTYyMjQxMDh9.2439jsY8Z4WpC3OoZ-HBBCPlLZ3i48CpegSBlsCPqoo";


    @Autowired
    public OrderMapper orderMapper;

    @Autowired
    public QuoteMapper quoteMapper;

    @Autowired
    public AuthService authService;


    public Integer getTotalNum(List<Map<String, Object>> dataList, String express_number){
        Integer totalNum = 0;
        for(Map<String, Object> dataMap : dataList){
            String express_number_db = (String)dataMap.getOrDefault("express_number", "");

            if(StringUtils.isNotEmpty(express_number) && express_number.equals(express_number_db)){
                totalNum = totalNum + (Integer)dataMap.getOrDefault("num", 0); // 每单件数
            }
        }
        return totalNum;
    }

    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = orderMapper.select(map);


        for(Map<String, Object> dataMap : dataList){
            BigDecimal zero = new BigDecimal(0.00);
            BigDecimal one = new BigDecimal(1.00);
            BigDecimal purchase_price = (BigDecimal)dataMap.getOrDefault("purchase_price", zero); // 总费用
            BigDecimal fees = (BigDecimal)dataMap.getOrDefault("fees", zero); // 总费用
            Integer number = (Integer)dataMap.getOrDefault("number", 0); // 件数
            Integer num = (Integer)dataMap.getOrDefault("num", 0); // 每单件数
            String express_number_db = (String)dataMap.getOrDefault("express_number", "");
            Long outbound_order_id = (Long)dataMap.get("outbound_order_id"); // 订单ID
            Long goods_id = (Long)dataMap.get("goods_id"); // 订单ID
            String supplier_id = (String)dataMap.getOrDefault("supplier_id", "");

            dataMap.put("outbound_order_id", String.valueOf(outbound_order_id));
            dataMap.put("goods_id", String.valueOf(goods_id));

            Integer totalNum = getTotalNum(dataList, express_number_db); // 获取总件数
            Integer scale = totalNum - number;
//            单笔订单运费=快递总费用/对应勾选订单所含件数*每单所含件数 40/(2+1)*1=20
            BigDecimal fee = new BigDecimal(0.00);
//            if(number > 1){

/*
                System.out.println("-+----------");
                System.out.println("fees:"+fees);
                System.out.println("number:"+number);
                System.out.println("num:"+num);
                System.out.println("scale:"+scale);
*/

                if(number > 0 || scale > 0){
                    fee = fees.divide(new BigDecimal(number).add(new BigDecimal(scale)), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(num));
                }

            dataMap.put("fee", fee);
            dataMap.put("total_cost", purchase_price.add(fee).setScale(2));

            dataMap.put("supplier_name", OrderUtil.getListSupplierName(supplier_id));

            String colour = (String)dataMap.getOrDefault("colour", "");
            dataMap.put("chinese_colour", OrderUtil.getListColour(colour));

        }


        return dataList;
    }


    public int selectCount(Map map){
        int orderCount = orderMapper.selectCount(map);
        return orderCount;
    }


    @Override
    public boolean insert(Map map){

        long count = orderMapper.insert(map);

        if(count > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Map map){

        orderMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(long id){
        long count = orderMapper.deleteById(id);
        return count > 0 ? true : false;
    }


    public Map<String, Object> selectById(long orderId){

        Map<String, Object> dataMap = orderMapper.selectById(orderId);
        return dataMap;
    }

    public Map<String, Object> selectByItemId(String itemId){

        Map<String, Object> dataMap = orderMapper.selectByItemId(itemId);
        return dataMap;
    }

    public Map<String, Object> selectByOutboundOrderNo(String outbound_order_no){
        Map<String, Object> dataMap = orderMapper.selectByOutboundOrderNo(outbound_order_no);
        return dataMap;
    }

    public List<Map<String, Object>> selectAll(Map allMap){

        List<Map<String, Object>> dataList = orderMapper.selectAll(allMap);
        return dataList;
    }

    public Map<String, Object> selectByGoodsId(Long outbound_order_id, Long goods_id){
        Map<String, Object> dataMap = orderMapper.selectByGoodsId(outbound_order_id, goods_id);
        return dataMap;
    }


    public JSONObject selectOutboundOrder(String platformOrderId, String logistNum){

        try {


            String ezsaleURL = "";
            String ezsaleAuth = "";
            Integer size = 10;
            List<Auth> authList = authService.list();
            if(authList.size() > 0){
                Auth auth = authList.get(0);
                ezsaleURL = auth.getUrl();
                ezsaleAuth = auth.getAuth();
                size = auth.getSize();
            }
            System.out.println("selectOutboundOrder authList:"+authList);


            StringBuffer sbf = new StringBuffer();
            String strRead = null;
            URL url = new URL(ezsaleURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");//请求post方式
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //header内的的参数在这里set。||connection.setRequestProperty("健, "值");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//        connection.setRequestProperty("Authorization", "Bearer 59e0-9fcc-c3faea0e2a6c");
            connection.setRequestProperty("ezsale-auth", ezsaleAuth);
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
            //body参数在这里put到JSONObject中
            JSONObject parm = new JSONObject();
            parm.put("current", 1);
            parm.put("size", size);
            parm.put("warehouseType", "virtual");

            parm.put("platformOrderId", platformOrderId);
            parm.put("logistNum", logistNum);
            writer.write(parm.toString());
            writer.flush();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                //sbf.append("\r\n");
            }
            reader.close();
            connection.disconnect();
            String results = sbf.toString();
            JSONObject resultObject = JSONObject.parseObject(results);
//            System.out.println("selectOutboundOrder resultObject:"+resultObject);

            return resultObject;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updatePurchaseStatus(Long id, Integer purchaseStatus){
        orderMapper.updatePurchaseStatus(id, purchaseStatus);
    }

    @Override
    public void updatePurchaseStatusByItemId(String item_id, Integer purchaseStatus){
        orderMapper.updatePurchaseStatusByItemId(item_id, purchaseStatus);
    }

    @Override
    public void updatePurchaseStatusByGoodsId(Long outbound_order_id, Long goods_id, Integer purchase_status){
        orderMapper.updatePurchaseStatusByGoodsId(outbound_order_id, goods_id, purchase_status);
    }

    public List<Map<String, Object>> selectShop(Map map){
        List<Map<String, Object>> dataList = orderMapper.selectShop(map);
        return dataList;
    }

    public List<Map<String, Object>> selectByOutCreateTime(String startTime, String endTime){

        List<Map<String, Object>> dataList = orderMapper.selectByOutCreateTime(startTime, endTime);
        return dataList;
    }


    public List<Map<String, Object>> selectBySupplierIdAndGoogsNo(String supplier_id, String goods_no, String colour, String size, String order_time){

        List<Map<String, Object>> dataList = orderMapper.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, colour, size, order_time);
        return dataList;
    }


    @Override
    public void insertOrderExtend(Map map){
        orderMapper.insertOrderExtend(map);

    }


    @Override
    public void updateOrderExtend(Map map){
        orderMapper.updateOrderExtend(map);
    }



    public BigDecimal getPurchasePrice(List<Map<String, Object>> quoteList, String supplier_id, String goods_no){
        System.out.println("getPurchasePrice："+quoteList);
        for(Map<String, Object> quoteMap : quoteList){

            String supplier_id_db = (String)quoteMap.getOrDefault("supplier_id","");
            String goods_no_db = (String)quoteMap.getOrDefault("goods_no","");
            BigDecimal purchase_price_db = (BigDecimal)quoteMap.get("purchase_price");
            if(supplier_id_db.equals(supplier_id) && goods_no_db.equals(goods_no)){
                return purchase_price_db;

            }
        }
        return new BigDecimal(0.00);
    }


    public boolean syncQuote(){

        try {
            // 如果检测到采购清单里的SPU有增加或者减少，都应该同步在报价单里，如果有增加的，则该条记录在报价单里的状态为未确认
            long startTime = DateUtil.getTodayZero(System.currentTimeMillis());
            long endTime = DateUtil.getTodayTwelve(System.currentTimeMillis());

            List<Map<String, Object>> orderList = orderMapper.selectByOutCreateTime(DateUtil.timestampToTime(startTime-(86400000*7)), DateUtil.timestampToTime(endTime));
            List<Map<String, Object>> quoteList = quoteMapper.selectByQuoteTime(DateUtil.timestampToTime(startTime-(86400000*7), "yyyy-MM-dd"), DateUtil.timestampToTime(endTime, "yyyy-MM-dd"));
            System.out.println("orderList1:"+ JSON.toJSONString(orderList));
            System.out.println("quoteList1:"+ JSON.toJSONString(quoteList));
            for(Map<String, Object> orderMap : orderList){
                String supplier_id = (String)orderMap.getOrDefault("supplier_id","");
                String goods_no = (String)orderMap.getOrDefault("goods_no","");
                String colour = (String)orderMap.getOrDefault("colour","");
                String size = (String)orderMap.getOrDefault("size","");
                Date order_time = (Date)orderMap.get("order_time");
                String order_time_str = DateUtil.formatDate(order_time, "yyyy-MM-dd");
                Map<String, Object> quoteMap = quoteMapper.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, colour, size, order_time_str);
                if(quoteMap == null){ // 如果报价单中没有则插入

                    if(StringUtils.isEmpty(supplier_id) && StringUtils.isEmpty(goods_no)){
                        continue;
                    }
                    Map map = new HashMap();
                    map.put("supplier_id", supplier_id);
                    map.put("goods_no", goods_no);
                    map.put("colour", colour);
                    map.put("size", size);
                    map.put("quote_time", order_time_str);
//                    map.put("purchase_price", 0);
                    map.put("purchase_price", getPurchasePrice(quoteList, supplier_id, goods_no));
                    map.put("price_status", 0);
                    quoteMapper.insert(map);
                    System.out.println("......报价单中没有则插入："+map);
                }
            }


            System.out.println("quoteList:"+JSON.toJSONString(quoteList));
            for(Map<String, Object> quoteMap : quoteList){
                String supplier_id = (String)quoteMap.getOrDefault("supplier_id","");
                String goods_no = (String)quoteMap.getOrDefault("goods_no","");
                String colour = (String)quoteMap.getOrDefault("colour","");
                String size = (String)quoteMap.getOrDefault("size","");
                Date quote_time = (Date)quoteMap.get("quote_time");
                Long id = (Long)quoteMap.get("id");
                List<Map<String, Object>> orderList2 = orderMapper.selectBySupplierIdAndGoogsNo(supplier_id, goods_no, colour, size, DateUtil.formatDate(quote_time, "yyyy-MM-dd"));
                if(orderList2.size() <= 0){ // 如果检测到采购清单里的SPU有减少，则报价单也要减少
                    quoteMapper.deleteById(id);
                    System.out.println("......采购清单中没有则删除："+quoteMap);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean syncOrder(String platform_order_number, String logist_num){

        try {

            System.out.println("syncOrder start-------------------");
            Map allMap = new HashMap();
            allMap.put("page", 0);
            allMap.put("rows", 300);
            allMap.put("platform_order_number", platform_order_number);
            allMap.put("logist_num", logist_num);

            List<Map<String, Object>> orderList = orderMapper.selectAll(allMap);
            System.out.println("orderList:"+JSON.toJSONString(orderList));

            JSONObject result = selectOutboundOrder(platform_order_number, logist_num);
            System.out.println("selectOutboundOrder result："+result);
            JSONObject data = result.getJSONObject("data");
            JSONArray records = data.getJSONArray("records");
            for(int i = 0; i < records.size(); i++){
                // 订单
                JSONObject record = records.getJSONObject(i);
                String outCreateTime = record.getString("outCreateTime");
                String outboundOrderNo = record.getString("outboundOrderNo");
                String shopId = record.getString("shopId");
                String shopName = record.getString("shopName");
                String platformOrderNumber = record.getString("platformOrderNumber");
                String logistNum = record.getString("logistNum");
                String outbound_order_id = record.getString("id");


                JSONArray items = record.getJSONArray("items");
                for(int j = 0; j < items.size(); j++){

                    // 产品信息
                    JSONObject item = items.getJSONObject(j);

                    int num = item.getInteger("num"); // 数量
                    String sku = item.getString("sku"); // sku 截取货号和供应商名称

                    String variationName = item.getString("variationName"); // 属性 截取颜色和尺码
                    String goods_id = item.getString("id"); // 货物ID
                    String itemId = item.getString("itemId"); // 货物ID


                    System.out.println("-----------------------");

                    String colour = "";
                    String size = "";
                    if(StringUtils.isNotEmpty(variationName)){
                        int index = variationName.indexOf(",");
                        System.out.println("index："+index);

                        if(index > 0){
                            colour = variationName.substring(0, index);
                            size = variationName.substring(index+1);
                        }
                    }

                    Map map = new HashMap<>();
                    map.put("outCreate_time", outCreateTime); // 订单日期
                    map.put("order_time", outCreateTime); // 订单日期
                    map.put("outbound_order_no", outboundOrderNo); // Ezsale系统订单id
                    map.put("shop_id", shopId); // 站点
                    map.put("shop_name", shopName); // 站点
                    map.put("sku", sku); // 站点
                    System.out.println("sku1:"+sku);
                    map.put("colour", OrderUtil.getColour(sku)); // 颜色
                    map.put("size", OrderUtil.getSize(sku)); // 尺码
                    sku = OrderUtil.getSku(sku); // 截取符号_倒数第三个字符串
                    System.out.println("sku2:"+sku);
                    map.put("goods_no", OrderUtil.getGoodsNo(sku)); // 货号
                    map.put("supplier_id", OrderUtil.getSupplierName(sku)); // 供应商名称
                    System.out.println("skuskusku："+sku);

                    map.put("total_num", items.size()); // 订单总数量
                    map.put("num", num); // 数量
                    map.put("platform_order_number", platformOrderNumber); // 虾皮订单号
                    map.put("logist_num", logistNum); // 运单号
                    map.put("lock_status", Constants.LOCK_STATUS_OPER); // 运单号
                    map.put("outbound_order_id", outbound_order_id); // 运单号
                    map.put("goods_id", goods_id); // 运单号
                    map.put("item_id", itemId); // 运单号

                    Map<String, Object> orderMap = OrderUtil.isNotExist(orderList, outbound_order_id, goods_id);
                    System.out.println("---outbound_order_id:"+outbound_order_id+" goods_id:"+goods_id+" orderMap:"+orderMap);
                    if(orderMap != null) { // 存在
                        Integer lock_status = (Integer) orderMap.get("lock_status");
                        // 订单锁定 1不锁定可更新 0锁定不可更新
                        if (lock_status == 1) {
                            System.out.println("存在但可更新："+goods_id);
                            Long id = (Long) orderMap.get("id");
                            orderMapper.deleteById(id);
                            orderMapper.insert(map);
                            continue;
                        }else{
                            continue;
                        }
                    }
                    orderMapper.insert(map);

                }
            }


            System.out.println("syncOrder end-------------------");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Map<String, Object>> selectOrderSummary(Map map){
        List<Map<String, Object>> dataList = orderMapper.selectOrderSummary(map);
        return dataList;
    }

    public int selectOrderSummaryCount(Map map){
        int orderCount = orderMapper.selectOrderSummaryCount(map);
        return orderCount;
    }


    public List<Map<String, Object>> selectFinance(Map map){

        List<Map<String, Object>> dataList = orderMapper.selectFinance(map);

        for(Map<String, Object> dataMap : dataList){
            BigDecimal zero = new BigDecimal(0.00);
            BigDecimal one = new BigDecimal(1.00);
            BigDecimal purchase_price = (BigDecimal)dataMap.getOrDefault("purchase_price", zero); // 总费用
            BigDecimal fees = (BigDecimal)dataMap.getOrDefault("fees", zero); // 总费用
            Integer number = (Integer)dataMap.getOrDefault("number", 0); // 件数
            Integer num = (Integer)dataMap.getOrDefault("num", 0); // 每单件数
            String express_number_db = (String)dataMap.getOrDefault("express_number", "");
            Long outbound_order_id = (Long)dataMap.get("outbound_order_id"); // 订单ID
            Long goods_id = (Long)dataMap.get("goods_id"); // 订单ID

            dataMap.put("outbound_order_id", String.valueOf(outbound_order_id));
            dataMap.put("goods_id", String.valueOf(goods_id));

            Integer totalNum = getTotalNum(dataList, express_number_db); // 获取总件数
            Integer scale = totalNum - number;
//            单笔订单运费=快递总费用/对应勾选订单所含件数*每单所含件数 40/(2+1)*1=20
            BigDecimal fee = new BigDecimal(0.00);
//            if(number > 1){

            if(number > 0 || scale > 0){
                fee = fees.divide(new BigDecimal(number).add(new BigDecimal(scale)), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(num));
            }

            dataMap.put("fee", fee);
            dataMap.put("total_cost", purchase_price.add(fee).setScale(2));

            String supplierId = (String)dataMap.getOrDefault("supplier_id", "");
            dataMap.put("supplier_name", OrderUtil.getListSupplierName(supplierId));

            String colour = (String)dataMap.getOrDefault("colour", "");
            dataMap.put("chinese_colour", OrderUtil.getListColour(colour));

        }


        return dataList;
    }


    public int selectFinanceCount(Map map){
        int orderCount = orderMapper.selectFinanceCount(map);
        return orderCount;
    }


    public List<Map<String, Object>> selectSku(Map map){
        List<Map<String, Object>> dataList = orderMapper.selectSku(map);
        return dataList;
    }

    public int selectSkuCount(Map map){
        int orderCount = orderMapper.selectSkuCount(map);
        return orderCount;
    }

    public List<Map<String, Object>> selectOrderBySku(Map map){
        List<Map<String, Object>> dataList = orderMapper.selectOrderBySku(map);
        return dataList;
    }

    public int selectOrderCountBySku(Map map){
        int orderCount = orderMapper.selectOrderCountBySku(map);
        return orderCount;
    }

    public void updatePickingStatusById(Long id, Integer picking_status){
        orderMapper.updatePickingStatusById(id, picking_status);
    }

    public List<Map<String, Object>> selectPurchaseOrderByOutboundOrderId(Long outbound_order_id){
        List<Map<String, Object>> dataList = orderMapper.selectPurchaseOrderByOutboundOrderId(outbound_order_id);
        return dataList;
    }

}
