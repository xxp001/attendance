package com.coder520.leave.controller;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.leave.service.LeaveService;
import com.coder520.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
@Controller

public class LeaveController {


    @Autowired
    private LeaveService leaveService;

    @RequestMapping("/leave")
    public String home(){

     return "leave";
    }
    
    //导入请假数据
    @RequestMapping("/leaveImport")
    @ResponseBody
	 public String batchimport(@RequestParam(value="filename") MultipartFile file,
	         HttpServletRequest request,HttpServletResponse response) throws Exception{
	     //判断文件是否为空
	     if(file==null) return null;
	     //获取文件名
	     String name=file.getOriginalFilename();
	     //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
	     long size=file.getSize();
	     if(name==null || ("").equals(name) && size==0) return null;
	     
	     //批量导入。参数：文件名，文件。
	    leaveService.importLeave(name, file);
	    return "importLeave";
	     }
	         
    /**
     * 
    * @Title: listUser
    
    * @Description:请假数据分页查询
    
    * @param @param condition
    * @param @return   
    
    * @return PageQueryBean 
    
    * @throws
     */
    @RequestMapping("/leaveList")
    @ResponseBody
    public PageQueryBean listUser(QueryCondition condition){
    	/* User user = (User) session.getAttribute("USER_SESSION");
         condition.setUserId(user.getId());*/
        PageQueryBean result = leaveService.listLeave(condition);
        return result;
    }
}
