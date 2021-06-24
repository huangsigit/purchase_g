package com.egao.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.egao.common.core.Cache;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 订单
 * Created by hs on 2017-06-10 10:10
 */
public class OrderUtil {

    public static String getSku(String sku) {
        try {

            if(sku.contains(",")){
                System.out.println("有逗号...");
                return "";
            }

            int occurrenceCount = getOccurrenceCount(sku, "_");
            if(occurrenceCount == 0){ // SOH1794+YWSYCDZSWYXGS
                return sku;
            } else if(occurrenceCount == 1){ // SOH1794+YWSYCDZSWYXGS_SOH1794+YWSYCDZSWYXGS
                int index = sku.lastIndexOf('_', sku.lastIndexOf("_"));

//                System.out.println("index："+index);

                if(index > 0){
                    String sku2 = sku.substring(0, index);
                    int index2 = sku2.lastIndexOf('_', sku2.lastIndexOf("_"));
                    String result = sku2.substring(index2 + 1);
                    return result;
                }

            } else if(occurrenceCount >= 2){ // 620#+YFFWP_620#+010_红色_XL
//                if(sku.contains("_")){
                    int index = sku.lastIndexOf('_', sku.lastIndexOf("_")-2);

//                    System.out.println("index："+index);

                    if(index > 0){
                        String sku2 = sku.substring(0, index);
                        int index2 = sku2.lastIndexOf('_', sku2.lastIndexOf("_"));
                        String result = sku2.substring(index2 + 1);
                        return result;
                    }
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getColour(String sku) {
        try {

            if(sku.contains(",")){
                System.out.println("有逗号...");
                return "";
            }

            int occurrenceCount = getOccurrenceCount(sku, "_");
            if(occurrenceCount == 0){ // SOH1794+YWSYCDZSWYXGS
                return "";
            } else if(occurrenceCount == 1){ // SOH1794+YWSYCDZSWYXGS_SOH1794+YWSYCDZSWYXGS
                return "";

            } else if(occurrenceCount >= 2){ // 620#+YFFWP_620#+010_红色_XL
                String colour = sku.substring(sku.lastIndexOf("_",sku.lastIndexOf("_")-1)+1, sku.lastIndexOf("_"));
                return colour;
            }



        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getColour sku："+sku);
        }
        return "";
    }

    public static String getSize(String sku) {
        try {
            int occurrenceCount = getOccurrenceCount(sku, "_");
            if(occurrenceCount == 0){ // SOH1794+YWSYCDZSWYXGS
                return "";
            } else if(occurrenceCount == 1){ // SOH1794+YWSYCDZSWYXGS_SOH1794+YWSYCDZSWYXGS
                return "";

            } else if(occurrenceCount >= 2){ // 620#+YFFWP_620#+010_红色_XL
                String size = sku.substring(sku.lastIndexOf("_")+1);
                return size;
            }



/*
            if(StringUtils.isNotEmpty(sku) && sku.contains("_")){
                String size = sku.substring(sku.lastIndexOf("_")+1);
                return size;
            }
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getGoodsNo(String sku) {
        try {
            if(StringUtils.isNotEmpty(sku) && sku.contains("+")){
                int index = sku.lastIndexOf("+");
                String goodsNo = sku.substring(0, index);
                return goodsNo;
            }else{
                return sku;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSupplierName(String sku) {
        try {
            if(StringUtils.isNotEmpty(sku) && sku.contains("+")){
                int index = sku.lastIndexOf("+");
                String supplierName = sku.substring(index + 1, sku.length());
                return supplierName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> isNotExist(List<Map<String, Object>> orderList, String outbound_order_id, String goods_id) {
        for(Map<String, Object> orderMap : orderList){
            Long id = (Long)orderMap.get("id");
            Long outbound_order_id_db = (Long)orderMap.get("outbound_order_id");
            Long goods_id_db = (Long)orderMap.get("goods_id");
            Integer lock_status = (Integer)orderMap.get("lock_status"); // 订单锁定 1不锁定可更新 0锁定不可更新
            // 存在可更新
            if(String.valueOf(outbound_order_id_db).equals(outbound_order_id) && String.valueOf(goods_id_db).equals(goods_id)){
                return orderMap;
            }
        }
        return null;
    }

    public static String getListSupplierName(String supplier_id){
        String supplierName = "";
        List<Map<String, Object>> supplierList = Cache.getSupplierList();
        if(supplierList!=null && supplierList.size() > 0){

            for(Map<String, Object> supplierMap : supplierList){
                String supplier_id_db = (String)supplierMap.get("supplier_id");
                String supplier_code_db = (String)supplierMap.get("supplier_code");
                String supplier_name_db = (String)supplierMap.get("supplier_name");
                if(StringUtils.isNotEmpty(supplier_id)){
                    supplierName = supplier_id.equals(supplier_id_db) || supplier_id.equals(supplier_code_db) ? supplier_name_db : supplierName;
                }
            }
        }

        return supplierName;
    }

    public static String getListColour(String colour){
        String colourName = "";
        List<Map<String, Object>> colourList = Cache.getColourList();
        if(colourList!=null && colourList.size() > 0){
            for(Map<String, Object> colourMap : colourList){
                String local_language = (String)colourMap.get("local_language");
                String chinese = (String)colourMap.get("chinese");
                if(StringUtils.isNotEmpty(colour)){
                    colourName = colour.equals(local_language) || colour.equals(chinese) ? chinese : colour;
                }
            }
            return colourName;
        }
        return colourName;

    }

    // 获取字符在字符串中出现次数
    public static int  getOccurrenceCount(String str, String chars){
        //存放每个字符的数组
        String [] strs = new String[str.length()];
        //计数该字符出现了多少次
        int count = 0;
        //先把字符串转换成数组
        for(int i = 0;i<strs.length;i++){
            strs[i] = str.substring(i,i+1);
        }
        //挨个字符进行查找，查找到之后count++
        for(int i = 0;i<strs.length;i++){
            if(strs[i].equals(chars)){
                count++;
            }
        }
        System.out.println("一共有"+count+"个啊");
        return count;
    }


}
