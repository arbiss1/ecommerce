package ecommerce.web.app.domain.controller;

import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserPostDto;
import ecommerce.web.app.exception.UserNotFoundException;
import ecommerce.web.app.i18nConfig.MessageByLocaleImpl;
import ecommerce.web.app.mapper.MapStructMapper;
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

    public final UserService userService;
    public final MapStructMapper mapStructMapper;
    public final MessageByLocaleImpl messageByLocale;

    public UserController(UserService userService,
                          MapStructMapper mapStructMapper,
                          MessageByLocaleImpl messageByLocale){
        this.mapStructMapper = mapStructMapper;
        this.userService = userService;
        this.messageByLocale = messageByLocale;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserPostDto userPostDto,
                                             BindingResult result) throws UserNotFoundException {
        Optional<User> findIfExists = userService.findByUsername(userPostDto.getUsername());
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        } else if (findIfExists.isPresent()) {
            throw new UserNotFoundException(
                    messageByLocale.getMessage("error.409.userExists"));
        } else {
            userService.saveUser(mapStructMapper.userPostDtoToUser(userPostDto));
            return new ResponseEntity(userPostDto, HttpStatus.OK);
        }
    }
}
