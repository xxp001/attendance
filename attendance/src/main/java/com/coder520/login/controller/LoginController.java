package com.coder520.login.controller;

import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;




/**
 * Created by xxp[www.aiprogram.top] 2018/9/17.
 */
@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private UserService userService;

    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/18 12:47
     *@Description  登录页面
     */
    @RequestMapping
    public String login(){
        return "login";
    }
    
    /* 获取校验码 */
    @RequestMapping("/getVerifyCode")
    public void generate(HttpServletResponse response, HttpSession session) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String verifyCodeValue = drawImg(output);
        // 将校验码保存到session中
        session.setAttribute("verifyCodeValue", verifyCodeValue);

        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
        } catch (IOException e) {
           //logger.info("<--验证码前端写出.出现异常-->");
            e.printStackTrace();

        }
    }

    /* 绘制验证码 */
    private String drawImg(ByteArrayOutputStream output) {
        String code = "";
        // 随机产生4个字符
        for (int i = 0; i < 4; i++) {
            code += randomChar();
        }
        int width = 70;
        int height = 25;
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        Font font = new Font("Times New Roman", Font.PLAIN, 20);
        // 调用Graphics2D绘画验证码
        Graphics2D g = bi.createGraphics();
        g.setFont(font);
        Color color = new Color(66, 2, 82);
        g.setColor(color);
        g.setBackground(new Color(226, 226, 240));
        g.clearRect(0, 0, width, height);
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(code, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = bounds.getY();
        double baseY = y - ascent;
        g.drawString(code, (int) x, (int) baseY);
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /* 获取随机参数 */
    private char randomChar() {
        Random r = new Random();
        String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
        return s.charAt(r.nextInt(s.length()));
    }
    /**
     *@throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @Author xxp [www.coder520.com]
     *@Date 2018/9/18 12:47
     *@Description 校验登录
     */
    @RequestMapping("/check")
    @ResponseBody
    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException  {


        String username = request.getParameter("username");
        String pwd=request.getParameter("password");
        //查数据库 如果查到数据  调用MD5加密对比密码
        User user = userService.findUserByUsername(username);
        String verifyCode = request.getParameter("verifyCode");
        String sessionVerifyCode = (String) request.getSession().getAttribute("verifyCodeValue");
        if(verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
        	if(user!=null){
           /* if(SecurityUtils.checkPassword(pwd,user.getPassword())&&user.getRole()==0){
                //校验成功  设置session
                request.getSession().setAttribute("userinfo",user);
                return "login_stu";
            }*/
            if(SecurityUtils.checkPassword(pwd,user.getPassword())&&user.getRole()==1){
                //校验成功  设置session
                request.getSession().setAttribute("userinfo",user);
                return "login_admin";
            }else {
            	return "login_fail";
            }
        }else {
            // 校验失败  返回校验失败signal
            return "login_fail";
        }
      }else {
    	  return "img_error";
      }
        
    }

}
