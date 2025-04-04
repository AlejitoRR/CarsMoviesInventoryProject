package com.motoscostosasinventory.app.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class hellocontroller{

    @GetMapping
    public String hello(){
        return "hello world!";
    }
}