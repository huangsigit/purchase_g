package com.egao.common.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author hs
 * @since 2020-11-11
 */
@Component
public interface QuoteMapper {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    long insert(Map map);

    void update(Map map);

    int deleteById(@Param("id") Long id);

    Map<String, Object> selectById(@Param("id") Long id);

    void updatePriceStatusById(@Param("id") Long id, @Param("price_status") Integer price_status);

    Map<String, Object> selectBySupplierIdAndGoogsNo(@Param("supplier_id") String supplier_id, @Param("goods_no") String goods_no
            , @Param("colour") String colour, @Param("size") String size, @Param("quote_time") String quote_time);

    List<Map<String, Object>> selectByQuoteTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectOrderSummary(Map map);

    int selectOrderSummaryCount(Map map);

}
