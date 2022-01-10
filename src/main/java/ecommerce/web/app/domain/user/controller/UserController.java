package ecommerce.web.app.domain.user.controller;

import ecommerce.web.app.domain.user.service.UserService;
import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.dto.UserPostDto;
import ecommerce.web.app.mapper.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MapStructMapper mapStructMapper;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserPostDto userPostDto, BindingResult result) {
        Optional<User> findIfExists = userService.findByUsername(userPostDto.getUsername());
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        } else if (findIfExists.isPresent()) {
            return new ResponseEntity("Username exists", HttpStatus.CONFLICT);
        } else {
            userService.saveUser(mapStructMapper.userPostDtoToUser(userPostDto));
            return new ResponseEntity(userPostDto, HttpStatus.OK);
        }
    }
}
