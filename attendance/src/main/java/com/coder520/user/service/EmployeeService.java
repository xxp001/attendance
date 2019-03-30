package com.coder520.user.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.user.entity.Employee;
import com.coder520.user.entity.User;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
public interface EmployeeService {
    /**
     * 
     * Title: addEmployee
     * Description:添加员工  
     * @param employee 员工实体
     */
    void addEmployee(Employee employee);
    /**
     * 
     * Title: listEmployee
     * Description: 员工分页查询 
     * @param condition 查询条件
     * @return
     */
    PageQueryBean listEmployee(QueryCondition condition);
    /**
     * 
     * Title: deleteEmployee
     * Description:删除员工  
     * @param id 员工id
     */
    void deleteEmployee(Long id);
    /**
     * 
     * Title: editEmployee
     * Description:展示要修改的员工信息
     * @param id 员工id
     * @return
     */
    Employee editEmployee(Long id);
    /**
     * 
     * Title: updateEmployee
     * Description: 修改保存员工信息 
     * @param employee 已修改的员工实体
     */
    void updateEmployee(Employee employee);
}
