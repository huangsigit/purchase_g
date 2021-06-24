package com.egao.common.system.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author hs
 * @since 2019-10-10
 */
public interface QuoteService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    boolean insert(Map map);

    boolean update(Map map);

    boolean deleteById(Long id);

    Map<String, Object> selectById(Long id);

    void updatePriceStatusById(Long id, Integer price_status);

    Map<String, Object> selectBySupplierIdAndGoogsNo(String supplier_id, String goods_no, String colour, String size, String quote_time);

    List<Map<String, Object>> selectByQuoteTime(String startTime, String endTime);

    List<Map<String, Object>> selectOrderSummary(Map map);

    int selectOrderSummaryCount(Map map);

}
