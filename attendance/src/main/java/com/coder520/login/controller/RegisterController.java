package com.coder520.login.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;

@Controller
public class RegisterController {
	 @Autowired
	    private UserService userService;
	
    @RequestMapping(value="/register",method=RequestMethod.GET)
    public String register() {
		return "register";
    	
    }
    
    
    //注册
    @RequestMapping(value="saveRegister",method=RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        userService.createUser(user);

        return "register_succ";
    }
    
    
    
}
