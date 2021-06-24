package com.egao.common.system.service;

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
public interface IvipdealService {

    List<Map<String, Object>> select(Map map);

    List<Map<String, Object>> selectByLockStatus(Integer lock_status);

    int selectCount(Map map);

    boolean insert(Map map);

    boolean update(Map map);

    void updateLockStatus(Long id, Integer lock_status);


    boolean deleteById(Long id);

    void deleteAll();

    Map<String, Object> selectById(Long id);

    Map<String, Object> selectByIvipdealId(String ivipdeal_id);

    List<Map<String, Object>> selectByItemGroupId(String item_group_id);



}

