package ecommerce.web.app.controller;


import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {


    @GetMapping( "/hello")
    public String hello() {
        return "Hello World";
    }

}
