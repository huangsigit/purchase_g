package com.egao.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.egao.common.core.Cache;
import com.egao.common.core.utils.OrderUtil;
import com.egao.common.system.mapper.QuoteMapper;
import com.egao.common.system.service.QuoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    public QuoteMapper quoteMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = quoteMapper.select(map);

        return dataList;
    }



    public int selectCount(Map map){
        int quoteCount = quoteMapper.selectCount(map);
        return quoteCount;
    }


    @Override
    public boolean insert(Map map){

        long count = quoteMapper.insert(map);

        if(count > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Map map){

        quoteMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(Long id){
        int count = quoteMapper.deleteById(id);
        return count > 0 ? true : false;
    }

    public Map<String, Object> selectById(Long quoteId){

        Map<String, Object> dataMap = quoteMapper.selectById(quoteId);
        return dataMap;
    }

    public void updatePriceStatusById(Long id, Integer price_status){
        quoteMapper.updatePriceStatusById(id, price_status);
    }

    public Map<String, Object> selectBySupplierIdAndGoogsNo(String supplierId, String goodsNo, String colour, String size, String quote_time){

        Map<String, Object> dataMap = quoteMapper.selectBySupplierIdAndGoogsNo(supplierId, goodsNo, colour, size, quote_time);
        return dataMap;
    }

    public List<Map<String, Object>> selectByQuoteTime(String startTime, String endTime){

        List<Map<String, Object>> dataList = quoteMapper.selectByQuoteTime(startTime, endTime);
        return dataList;
    }

    public String getPurchaseStatusStr(Integer purchase_status){
        String purchase_status_str = "待采购";
        if(purchase_status == 1){
            purchase_status_str = "待发货";
        }else if(purchase_status == 2){
            purchase_status_str = "已发货";
        }else if(purchase_status == 3){
            purchase_status_str = "已作废";
        }
        return purchase_status_str;
    }

    public List<Map<String, Object>> selectOrderSummary(Map map){
        List<Map<String, Object>> dataList = quoteMapper.selectOrderSummary(map);
        for(Map<String, Object> orderSummaryMap : dataList){
            Integer purchase_status_db = (Integer)orderSummaryMap.get("purchase_status");

            orderSummaryMap.put("purchase_status_str", getPurchaseStatusStr(purchase_status_db));

            int price_status2 = (int)orderSummaryMap.get("price_status");
            orderSummaryMap.put("price_status_str", price_status2 == 0 ? "未确认" : "已确认");

            String supplierId = (String)orderSummaryMap.getOrDefault("supplier_id", "");
            orderSummaryMap.put("supplier_name", OrderUtil.getListSupplierName(supplierId));

            String colour = (String)orderSummaryMap.getOrDefault("colour", "");
            orderSummaryMap.put("chinese_colour", OrderUtil.getListColour(colour));
        }


        return dataList;
    }

    public int selectOrderSummaryCount(Map map){
        int orderCount = quoteMapper.selectOrderSummaryCount(map);
        return orderCount;
    }


}
