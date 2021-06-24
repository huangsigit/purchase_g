package com.egao.common.system.test;

import com.egao.common.core.utils.OrderUtil;

import java.util.HashMap;
import java.util.Map;

public class Test19 {

    public static void main(String[] args) {

//        String sku = "05095#+ANNZWG_05095#+ANNZWG_สีเทาเข้ม_XXL";
//        String sku = "SOH1794+YWSYCDZSWYXGS_SOH1794+YWSYCDZSWYXGS";
        String sku = "102+052_102+052_黑灰*_2XL";
        Map map = new HashMap<>();
//        map.put("outCreate_time", outCreateTime); // 订单日期
//        map.put("order_time", outCreateTime); // 订单日期
//        map.put("outbound_order_no", outboundOrderNo); // Ezsale系统订单id
//        map.put("shop_id", shopId); // 站点
//        map.put("shop_name", shopName); // 站点
        map.put("sku", sku); // 站点
        System.out.println("sku1:"+sku);
        map.put("colour", OrderUtil.getColour(sku)); // 颜色
        map.put("size", OrderUtil.getSize(sku)); // 尺码
        sku = OrderUtil.getSku(sku); // 截取符号_倒数第三个字符串
        System.out.println("sku2:"+sku);
        map.put("goods_no", OrderUtil.getGoodsNo(sku)); // 货号
        map.put("supplier_id", OrderUtil.getSupplierName(sku)); // 供应商名称
        System.out.println("skuskusku："+sku);
        System.out.println("map:："+map);

//        String sku3 = "620#+YFFWP_620#+010_红色_XL";
//        String sku3 = "XTnYG5rUtryqVIWSD80f/8oo+3t0SFFI25L+jopGemg=_Orang,L";
//        String sku3 = "luckybox_luckybox_2XL";
//        String sku3 = "102+052_102+052_黑灰*_2XL";
        String sku3 = "1041#+SYWP_1041#+045_黑色_XL";
        System.out.println("出现次数："+OrderUtil.getOccurrenceCount(sku3, "_"));
        sku3 = OrderUtil.getSku(sku3); // 截取符号_倒数第三个字符串
        System.out.println("sku3:"+sku3);
        map.put("goods_no", OrderUtil.getGoodsNo(sku3)); // 货号
        map.put("supplier_id", OrderUtil.getSupplierName(sku3)); // 供应商名称
        System.out.println("map3:"+map);


/*
        String sku = "05095#+ANNZWG_05095#+ANNZWG_สีเทาเข้ม_XXL";
        sku = OrderUtil.getSku(sku);
        System.out.println("sku:"+sku);
        String goodsNo = OrderUtil.getGoodsNo(sku);
        System.out.println("goodsNo:"+goodsNo);
        String supplierName = OrderUtil.getSupplierName(sku);
        System.out.println("supplierName:"+supplierName);
*/

    }

    public static int test02(String str2){
        String str = "SOH1794+YWSYCDZSWYXGSSOH1794+YWSYCDZSWYXGS";
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
            if(strs[i].equals(str2)){
                count++;
            }
        }

        System.out.println("一共有"+count+"个啊");
        return count;
    }

}
