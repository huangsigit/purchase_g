package com.egao.common.system.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU表 服务类
 * </p>
 *
 * @author hs
 * @since 2019-10-10
 */
public interface SkuService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    long insert(Map map);

    void updateStatusBySku(Map map);

    Map<String, Object> selectById(Long id);



    long deleteById(Long id);

    int deleteBySku(Map map);

    List<Map<String, Object>> selectBySku(Map map);



}
