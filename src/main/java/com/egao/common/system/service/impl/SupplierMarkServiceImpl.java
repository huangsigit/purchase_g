package com.egao.common.system.service.impl;

import com.egao.common.core.Cache;
import com.egao.common.system.mapper.SupplierMarkMapper;
import com.egao.common.system.service.SupplierMarkService;
import org.apache.commons.lang3.StringUtils;
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
public class SupplierMarkServiceImpl implements SupplierMarkService {

    @Autowired
    public SupplierMarkMapper supplierMarkdMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = supplierMarkdMapper.select(map);


        return dataList;
    }



    public int selectCount(Map map){
        int supplierMarkdCount = supplierMarkdMapper.selectCount(map);
        return supplierMarkdCount;
    }


    @Override
    public boolean insert(Map map){

        long count = supplierMarkdMapper.insert(map);

        if(count > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Map map){

        supplierMarkdMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(long id){
        long count = supplierMarkdMapper.deleteById(id);
        return count > 0 ? true : false;
    }

    public Map<String, Object> selectById(long supplierMarkdId){

        Map<String, Object> dataMap = supplierMarkdMapper.selectById(supplierMarkdId);
        return dataMap;
    }

    public List<Map<String, Object>> selectAll(){
        List<Map<String, Object>> dataList = supplierMarkdMapper.selectAll();
        return dataList;
    }


}
