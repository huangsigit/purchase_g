package com.egao.common.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU表 Mapper 接口
 * </p>
 *
 * @author hs
 * @since 2020-11-11
 */
@Component
public interface SkuMapper {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    long insert(Map map);

    Map<String, Object> selectById(@Param("id") Long id);

    void updateStatusBySku(Map map);

    int deleteById(@Param("id") Long id);

    int deleteBySku(Map map);

    List<Map<String, Object>> selectBySku(Map map);


}
