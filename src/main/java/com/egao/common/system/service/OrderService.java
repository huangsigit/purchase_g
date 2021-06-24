package com.egao.common.system.service;

import com.alibaba.fastjson.JSONObject;
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
public interface OrderService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    boolean insert(Map map);

    boolean update(Map map);

    boolean deleteById(long id);

    Map<String, Object> selectById(long id);

    Map<String, Object> selectByItemId(String item_id);

    Map<String, Object> selectByOutboundOrderNo(String outbound_order_no);

    Map<String, Object> selectByGoodsId(Long outbound_order_id, Long goods_id);

    JSONObject selectOutboundOrder(String platformOrderId, String logistNum);

    List<Map<String, Object>> selectAll(Map allMap);

    void updatePurchaseStatus(Long id, Integer purchase_status);

    void updatePurchaseStatusByItemId(String item_id, Integer purchase_status);

    void updatePurchaseStatusByGoodsId(Long outbound_order_id, Long goods_id, Integer purchase_status);

    List<Map<String, Object>> selectShop(Map map);

    List<Map<String, Object>> selectByOutCreateTime(String startTime, String endTime);

    List<Map<String, Object>> selectBySupplierIdAndGoogsNo(String supplier_id, String goods_no, String colour, String size, String quote_time);

    public boolean syncOrder(String platform_order_number, String logist_num);

    public boolean syncQuote();

    void insertOrderExtend(Map map);

    void updateOrderExtend(Map map);

    List<Map<String, Object>> selectOrderSummary(Map map);

    int selectOrderSummaryCount(Map map);

    List<Map<String, Object>> selectFinance(Map map);

    int selectFinanceCount(Map map);

    List<Map<String, Object>> selectSku(Map map);

    int selectSkuCount(Map map);

    List<Map<String, Object>> selectOrderBySku(Map map);

    int selectOrderCountBySku(Map map);

    void updatePickingStatusById(Long id, Integer picking_status);

    List<Map<String, Object>> selectPurchaseOrderByOutboundOrderId(@Param("outbound_order_id") Long outbound_order_id);

}
