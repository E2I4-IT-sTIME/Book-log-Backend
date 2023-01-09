package com.dormammu.BooklogWeb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/beanstalk_test")
    public String beanstalk_test() {
        return "beanstalk test!";
    }
}
