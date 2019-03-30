package com.coder520.user.dao;

import java.util.List;
import java.util.Map;

import com.coder520.attend.vo.QueryCondition;
import com.coder520.user.entity.Employee;

public interface EmployeeMapper {
   
	
	
	void deleteByPrimaryKey(Long id);
    
    

    int insert(Employee record);
   //注册
    int insertSelective(Employee record);
    //修改保存
    Employee selectByPrimaryKey(Long id);
    void updateByPrimaryKey(Employee employee);
    
    
    //int updateByPrimaryKeySelective(User record);

    //用户分页
    List<Map> selectEmployee(QueryCondition condition);
    
    int countEmployee(QueryCondition condition);
}