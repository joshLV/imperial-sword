package cn.lovike.spring.boot.single.datasource.controller;


import cn.lovike.spring.boot.single.datasource.entity.DictionaryEntity;
import cn.lovike.spring.boot.single.datasource.mapper.DictionaryEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author lovike
 * @since 2021/4/10
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SingleDatasourceApplicationTests {


    @Autowired
    private DictionaryEntityMapper dictionaryEntityMapper;

    @Test
    public void testList(){
        List<DictionaryEntity> dictionaryEntities = dictionaryEntityMapper.selectAll();
        for (DictionaryEntity dictionaryEntity : dictionaryEntities) {
            System.out.println("*************************************************");
            log.info(dictionaryEntity.toString());
            log.info(dictionaryEntity.getDictValue());
            System.out.println("*************************************************");
        }
    }

}