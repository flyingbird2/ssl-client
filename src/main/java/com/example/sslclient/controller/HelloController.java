package com.example.sslclient.controller;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {

    @GetMapping(value = "/")
    @ResponseBody
    public String hello(){
        return "hi";
    }
}
