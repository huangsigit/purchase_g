package com.egao.common.system.service.impl;

import com.egao.common.system.mapper.IvipdealMapper;
import com.egao.common.system.service.IvipdealService;
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
public class IvipdealServiceImpl implements IvipdealService {

    @Autowired
    public IvipdealMapper ivipdealMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = ivipdealMapper.select(map);
        return dataList;
    }

    public List<Map<String, Object>> selectByLockStatus(Integer lock_status){

        List<Map<String, Object>> dataList = ivipdealMapper.selectByLockStatus(lock_status);
        return dataList;
    }



    public int selectCount(Map map){
        int ivipdealCount = ivipdealMapper.selectCount(map);
        return ivipdealCount;
    }


    @Override
    public boolean insert(Map map){


        String ivipdeal_id = (String)map.get("ivipdeal_id");
        Map<String, Object> dbMap = selectByIvipdealId(ivipdeal_id);
        if(dbMap != null){
            Long id = (Long)dbMap.get("id");

            map.put("id", id);
            System.out.println("insert id:"+id);
            ivipdealMapper.update(map);
            return true;
        }else{
            long count = ivipdealMapper.insert(map);
            if(count > 0){
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean update(Map map){

        ivipdealMapper.update(map);
        return true;
    }

    @Override
    public void updateLockStatus(Long id, Integer lockStatus){

        ivipdealMapper.updateLockStatus(id, lockStatus);
    }

    @Override
    public boolean deleteById(Long id){
        long count = ivipdealMapper.deleteById(id);
        boolean result = count > 0 ? true : false;
        return result;
    }

    @Override
    public void deleteAll(){
        ivipdealMapper.deleteAll();
    }


    public Map<String, Object> selectById(Long id){
        Map<String, Object> dataMap = ivipdealMapper.selectById(id);
        return dataMap;
    }

    public Map<String, Object> selectByIvipdealId(String ivipdealId){
        Map<String, Object> dataMap = ivipdealMapper.selectByIvipdealId(ivipdealId);
        return dataMap;
    }

    public List<Map<String, Object>> selectByItemGroupId(String item_group_id){
        List<Map<String, Object>> dataList = ivipdealMapper.selectByItemGroupId(item_group_id);
        return dataList;
    }




}
