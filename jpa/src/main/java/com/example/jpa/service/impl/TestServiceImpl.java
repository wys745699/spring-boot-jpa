package com.example.jpa.service.impl;

import com.example.jpa.dao.TestDao;
import com.example.jpa.entity.Test;
import com.example.jpa.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService {
    @Resource
    private TestDao testDao;

    @Override
    public void testSave() {
        Test test=new com.example.jpa.entity.Test();
        test.setName("222");
        testDao.save(test);
    }
}
