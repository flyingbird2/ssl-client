package com.example.sslclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Controller
public class HelloController {

    @GetMapping(value = "/s2")
    @ResponseBody
    public String hello(){
        AtomicInteger a=new AtomicInteger(2);
        int andIncrement = a.getAndIncrement();

        return "his2";
    }
    @GetMapping(value = "/stack")
    @ResponseBody
    public String helloStack(@RequestParam String p){

        System.out.println(p);

        return "hi-stack";
    }

    public static void main(String[] args) {
        Integer a = 1534236469;
        int reverse = reverse(a);
        System.out.println(reverse);
        System.out.println(Integer.MIN_VALUE);



    }
    public static int reverse(int x) {
        int rev =0;
        while(x!=0){
            int pop = x%10;
            x = x/10;
            if(x>214748364 ||(x==214748364 && pop>7)){
                return 0;
            }
            if(x<-214748364 ||(x==-214748364 && pop<-8)){
                return 0;
            }
            rev = rev*10+pop;
        }
        return rev;

    }

}
