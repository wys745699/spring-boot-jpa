package com.example.jpa.controller;

import com.example.jpa.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


@Controller
public class TestController {
    @Resource
    private TestService testService;

    @RequestMapping("test")
    public String test(){
        testService.testSave();
        return "test";
    }


}
