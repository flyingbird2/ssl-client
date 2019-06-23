package com.example.sslclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NIO2Controller {


    @GetMapping(value = "/nio2")
    public String hello(){
        return "nio2";
    }
}
