package com.egao.common.system.service.impl;

import com.egao.common.system.mapper.TranslationMapper;
import com.egao.common.system.service.TranslationService;
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
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    public TranslationMapper translationMapper;


    public List<Map<String, Object>> select(Map map){

        List<Map<String, Object>> dataList = translationMapper.select(map);

        return dataList;
    }


    public int selectCount(Map map){
        int translationCount = translationMapper.selectCount(map);
        return translationCount;
    }


    @Override
    public boolean insert(Map map){

        long count = translationMapper.insert(map);

        if(count > 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean update(Map map){

        translationMapper.update(map);
        return true;
    }


    @Override
    public boolean deleteById(Long id){
        long count = translationMapper.deleteById(id);
        return count > 0 ? true : false;
    }

    public Map<String, Object> selectById(Long translationId){

        Map<String, Object> dataMap = translationMapper.selectById(translationId);
        return dataMap;
    }

    public Map<String, Object> selectByLocalLanguage(String local_language){
        Map<String, Object> dataMap = translationMapper.selectByLocalLanguage(local_language);
        return dataMap;
    }

    public List<Map<String, Object>> selectAll(){
        List<Map<String, Object>> dataList = translationMapper.selectAll();
        return dataList;
    }

}
