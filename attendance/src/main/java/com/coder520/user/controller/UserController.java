package com.coder520.user.controller;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
@Controller

public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(){

     return "home";
    }
    @RequestMapping("/admin")
    public String admin(){

     return "admin";
    }
    @RequestMapping("/admin2")
    public String admin1(){

     return "admin2";
    }
    @RequestMapping("/user")
    public String user(){

     return "user";
    }
    @RequestMapping("/register")
    public String register(){

     return "register";
    }
    @RequestMapping("/userinfor")
    public String userinfor(){

     return "userinfo";
    }
    @RequestMapping("/password")
    public String password(){

     return "password";
    }
    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/19 21:51
     *@Description  获取用户信息
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUser(HttpSession session){
       User user = (User) session.getAttribute("userinfo");
       return user;
    }


    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/20 20:39
     *@Description 登出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }
    /**
     * 
    
    * @Title: listUser
    
    * @Description:用户数据分页
    
    * @param @param condition
    * @param @return   
    
    * @return PageQueryBean 
    
    * @throws
     */
    @RequestMapping("/userList")
    @ResponseBody
    public PageQueryBean listUser(QueryCondition condition){
    	/* User user = (User) session.getAttribute("USER_SESSION");
         condition.setUserId(user.getId());*/
        PageQueryBean result = userService.listUser(condition);
       
        return result;
    }
   //value="/delete",method=RequestMethod.POST
    @RequestMapping("/delete")
    @ResponseBody
    public String deleteUser(Long id){
    	userService.deleteUser(id);
		return "delete";
    	
    }
    @RequestMapping("/edit")
    @ResponseBody
    public User editUser(Long id){
    	return userService.editUser(id);
    	
    }
    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(@RequestBody User user,HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	request.getSession().removeAttribute("userinfo");
    	request.getSession().setAttribute("userinfo", user);
    	userService.updateUser(user);
		return "update";
    	
    }
}
