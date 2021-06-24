package com.egao.common.system.service.impl;

import com.egao.common.system.mapper.ShelfMapper;
import com.egao.common.system.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 货架表 服务实现类
 * </p>
 *
 * @author hs
 * @since 2020-10-10
 */
@Service
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    public ShelfMapper shelfdMapper;

    public List<Map<String, Object>> select(Map map){
        List<Map<String, Object>> dataList = shelfdMapper.select(map);
        return dataList;
    }

    @Override
    public int selectCount(Map map){
        int shelfdCount = shelfdMapper.selectCount(map);
        return shelfdCount;
    }

    @Override
    public boolean insert(Map map){

        long count = shelfdMapper.insert(map);

        if(count > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Map map){

        shelfdMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(long id){
        long count = shelfdMapper.deleteById(id);
        return count > 0 ? true : false;
    }

    @Override
    public Map<String, Object> selectById(long shelfdId){
        Map<String, Object> dataMap = shelfdMapper.selectById(shelfdId);
        return dataMap;
    }

    @Override
    public void insertOrderShelf(Map map){
        shelfdMapper.insertOrderShelf(map);
    }

    public List<Map<String, Object>> selectCanAllotShelf(Map map){
        List<Map<String, Object>> dataList = shelfdMapper.selectCanAllotShelf(map);
        return dataList;
    }

    public List<Map<String, Object>> selectByOutboundOrderId(long outbound_order_id){
        List<Map<String, Object>> dataList = shelfdMapper.selectByOutboundOrderId(outbound_order_id);
        return dataList;
    }

    @Override
    public long deleteByOutboundOrderId(long outbound_order_id){
        long count = shelfdMapper.deleteByOutboundOrderId(outbound_order_id);
        return count;
    }



}
