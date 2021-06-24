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
public interface ExpressRecordService {

    List<Map<String, Object>> select(Map map);

    int selectCount(Map map);

    Long insert(Map map);

    boolean update(Map map);

    boolean deleteById(long id);

    Map<String, Object> selectById(long id);

    void insertOrderExpress(Map map);

    void deleteOrderExpressByExpressId(Long express_id);

}
