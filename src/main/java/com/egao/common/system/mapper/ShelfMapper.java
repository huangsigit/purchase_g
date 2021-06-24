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
public interface ShelfMapper {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    Map<String, Object> selectById(@Param("id") long id);

    long insert(Map map);

    void update(Map map);

    long deleteById(@Param("id") long id);

    void insertOrderShelf(Map map);

    List<Map<String, Object>> selectCanAllotShelf(Map map);

    List<Map<String, Object>> selectByOutboundOrderId(@Param("outbound_order_id") long outbound_order_id);

    long deleteByOutboundOrderId(@Param("outbound_order_id") long outbound_order_id);

}
