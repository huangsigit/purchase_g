package com.egao.common.system.service.impl;

import com.egao.common.system.mapper.SkuMapper;
import com.egao.common.system.service.SkuService;
import com.egao.common.system.service.SupplierMarkService;
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
public class SkuServiceImpl implements SkuService {

    @Autowired
    public SkuMapper skuMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = skuMapper.select(map);

        return dataList;
    }


    public int selectCount(Map map){
        int supplierMarkdCount = skuMapper.selectCount(map);
        return supplierMarkdCount;
    }


    @Override
    public long insert(Map map){

        long count = skuMapper.insert(map);

        return count;
    }

    @Override
    public void updateStatusBySku(Map map){

        skuMapper.updateStatusBySku(map);
    }

    @Override
    public Map<String, Object> selectById(Long id){
        Map<String, Object> dataMap = skuMapper.selectById(id);
        return dataMap;
    }


    @Override
    public long deleteById(Long id){
        long count = skuMapper.deleteById(id);
        return count;
    }

    @Override
    public int deleteBySku(Map map){
        int count = skuMapper.deleteBySku(map);
        return count;
    }

    @Override
    public List<Map<String, Object>> selectBySku(Map map){
        List<Map<String, Object>> dataList = skuMapper.selectBySku(map);
        return dataList;
    }


}
