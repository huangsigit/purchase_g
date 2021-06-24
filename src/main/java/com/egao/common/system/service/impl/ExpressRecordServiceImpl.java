package com.egao.common.system.service.impl;

import com.egao.common.system.mapper.ExpressRecordMapper;
import com.egao.common.system.service.ExpressRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author hs
 * @since 2020-10-10
 */
@Service
public class ExpressRecordServiceImpl implements ExpressRecordService {

    @Autowired
    public ExpressRecordMapper expressRecordMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = expressRecordMapper.select(map);

        return dataList;
    }


    public int selectCount(Map map){
        int expressRecordCount = expressRecordMapper.selectCount(map);
        return expressRecordCount;
    }


    @Override
    public Long insert(Map map){

        long count = expressRecordMapper.insert(map);

        if(count > 0){
            return (Long)map.get("id");
        }

        return null;
    }

    @Override
    public boolean update(Map map){

        expressRecordMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(long id){
        long count = expressRecordMapper.deleteById(id);
        return count > 0 ? true : false;
    }

    public Map<String, Object> selectById(long expressRecordId){

        Map<String, Object> dataMap = expressRecordMapper.selectById(expressRecordId);
        return dataMap;
    }

    @Override
    public void insertOrderExpress(Map map){
        expressRecordMapper.insertOrderExpress(map);
    }

    @Override
    public void deleteOrderExpressByExpressId(Long expressId){
        expressRecordMapper.deleteOrderExpressByExpressId(expressId);
    }



}
