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
public interface TranslationMapper {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    long insert(Map map);

    void update(Map map);

    int deleteById(@Param("id") Long id);

    Map<String, Object> selectById(@Param("id") Long id);

    Map<String, Object> selectByLocalLanguage(@Param("local_language") String local_language);

    List<Map<String, Object>> selectAll();

}
