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
public interface IvipdealMapper {

    List<Map<String, Object>> select(Map map);

    List<Map<String, Object>> selectByLockStatus(@Param("lock_status") Integer lock_status);

    int selectCount(Map map);

    long insert(Map map);

    void update(Map map);

    void updateLockStatus(@Param("id") Long id, @Param("lock_status") Integer lock_status);

    long deleteById(@Param("id") Long id);

    void deleteAll();

    Map<String, Object> selectById(@Param("id") Long id);

    Map<String, Object> selectByIvipdealId(@Param("ivipdeal_id") String ivipdeal_id);

    List<Map<String, Object>> selectByItemGroupId(@Param("item_group_id") String item_group_id);



}
