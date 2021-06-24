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
public interface TranslationService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    boolean insert(Map map);

    boolean update(Map map);

    boolean deleteById(Long id);

    Map<String, Object> selectById(Long id);

    Map<String, Object> selectByLocalLanguage(String local_language);

    List<Map<String, Object>> selectAll();

}
