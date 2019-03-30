package com.coder520.user.controller;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.entity.Employee;
import com.coder520.user.entity.User;
import com.coder520.user.service.EmployeeService;
import com.coder520.user.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
@Controller

public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    
    @RequestMapping("/employee")
    public String user(){

     return "employee";
    }
    
    /**
     * 
     * Title: listEmployee
     * Description: 员工数据分页
     * @param condition 查询条件
     * @return
     */
    @RequestMapping("/employeeList")
    @ResponseBody
    public PageQueryBean listEmployee(QueryCondition condition){
    	/* User user = (User) session.getAttribute("USER_SESSION");
         condition.setUserId(user.getId());*/
        PageQueryBean result = employeeService.listEmployee(condition);
       
        return result;
    }
   /**
    * 
    * Title: deleteEmployee
    * Description:  删除员工
    * @param id 员工id
    * @return
    */
    @RequestMapping("/deleteEmployee")
    @ResponseBody
    public String deleteEmployee(Long id){
    	employeeService.deleteEmployee(id);;
		return "delete";
    	
    }
    /**
     * 
     * Title: editEmployee
     * Description: 展示要修改的员工信息 
     * @param id 员工id
     * @return
     */
    @RequestMapping("/editEmployee")
    @ResponseBody
    public Employee editEmployee(Long id){
    	return employeeService.editEmployee(id);
    	
    }
    /**
     * 
     * Title: updateEmployee
     * Description:  修改保存员工信息
     * @param employee 已修改的员工实体
     * @return
     */
    @RequestMapping("/updateEmployee")
    @ResponseBody
    public String updateEmployee(@RequestBody Employee employee) {
    	employeeService.updateEmployee(employee);
		return "update";
    	
    }
    /**
     * 
     * Title: addEmployee
     * Description: 添加员工 
     * @param employee 员工实体
     * @return
     */
    @RequestMapping("/addEmployee")
    @ResponseBody
    public String addEmployee(@RequestBody Employee employee) {
    	employeeService.addEmployee(employee);
		return "add";
    	
    }
}
