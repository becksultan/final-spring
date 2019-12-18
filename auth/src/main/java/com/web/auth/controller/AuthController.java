package com.web.auth.controller;

import com.web.auth.entity.Token;
import com.web.auth.entity.User;
import com.web.auth.jwt.JwtUtil;
import com.web.auth.repository.UserRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    JwtUtil jwtUtil;
    UserRepository userRepository;

    public AuthController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Token register(@RequestBody User user) throws Exception {
        userRepository.save(user);
        return new Token(jwtUtil.createToken(user));
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Token authenticate(@RequestBody User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            return new Token(jwtUtil.createToken(user));
        } else {
            return new Token("No user found");
        }
    }

    @RequestMapping(value = "/validate/{token}", method = RequestMethod.GET)
    public String check(@PathVariable String token) {
        if (jwtUtil.validateToken(token)) {
            return jwtUtil.decodeToken(token);
        } else {
            return "Invalid";
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        return userRepository.findById(username).orElseThrow(() -> new ObjectNotFoundException(username, "User"));
    }
}
