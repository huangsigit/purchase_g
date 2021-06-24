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
public interface OrderMapper {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    long insert(Map map);

    void update(Map map);

    long deleteById(@Param("id") Long id);

    Map<String, Object> selectById(@Param("id") Long id);

    Map<String, Object> selectByItemId(@Param("item_id") String item_id);

    Map<String, Object> selectByOutboundOrderNo(@Param("outbound_order_no") String outbound_order_no);

    Map<String, Object> selectByGoodsId(@Param("outbound_order_id") Long outbound_order_id, @Param("goods_id") Long goods_id);

    List<Map<String, Object>> selectAll(Map map);

    void updatePurchaseStatus(@Param("id") Long id, @Param("purchase_status") Integer purchase_status);

    void updatePurchaseStatusByItemId(@Param("item_id") String item_id, @Param("purchase_status") Integer purchase_status);

//    void updateVoidStatusByItemId(@Param("item_id") String item_id, @Param("void_status") Integer void_status);

    void updatePurchaseStatusByGoodsId(@Param("outbound_order_id") Long outbound_order_id, @Param("goods_id") Long goods_id, @Param("purchase_status") Integer purchase_status);

    List<Map<String, Object>> selectShop(Map map);

    List<Map<String, Object>> selectByOutCreateTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectBySupplierIdAndGoogsNo(@Param("supplier_id") String supplier_id, @Param("goods_no") String goods_no
            , @Param("colour") String colour, @Param("size") String size, @Param("order_time") String order_time);

    List<Map<String, Object>> selectOrderSummary(Map map);

    int selectOrderSummaryCount(Map map);

    void insertOrderExtend(Map map);

    void updateOrderExtend(Map map);

    List<Map<String, Object>> selectFinance(Map map);

    int selectFinanceCount(Map map);

    List<Map<String, Object>> selectSku(Map map);

    int selectSkuCount(Map map);

    List<Map<String, Object>> selectOrderBySku(Map map);

    int selectOrderCountBySku(Map map);

    void updatePickingStatusById(@Param("id") Long id, @Param("picking_status") Integer picking_status);

    List<Map<String, Object>> selectPurchaseOrderByOutboundOrderId(@Param("outbound_order_id") Long outbound_order_id);

}
