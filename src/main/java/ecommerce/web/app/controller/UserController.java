package ecommerce.web.app.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin()
public class UserController {

    @RequestMapping({ "/hello" })
    public String hello() {
        return "Hello World";
    }

}
