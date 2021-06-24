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
public interface ShelfService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    boolean insert(Map map);

    boolean update(Map map);

    boolean deleteById(long id);

    Map<String, Object> selectById(long id);

    void insertOrderShelf(Map map);

    List<Map<String, Object>> selectCanAllotShelf(Map map);

    List<Map<String, Object>> selectByOutboundOrderId(long outbound_order_id);

    long deleteByOutboundOrderId(long outbound_order_id);

}
