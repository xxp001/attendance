package com.coder520.user.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.dao.EmployeeMapper;
import com.coder520.user.dao.UserMapper;
import com.coder520.user.entity.Employee;
import com.coder520.user.entity.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: EmployeeServiceImpl
* @Description: 对员工的增删改查
* @author xxp
* @date 2018年10月19日
*
 */
@Service("emploeeServiceImpl")

public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeMapper employeeMapper;
	@Override
	
	public PageQueryBean listEmployee(QueryCondition condition) {
		 //根据条件查询 count记录数目
        int count = employeeMapper.countEmployee(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            @SuppressWarnings("rawtypes")
			List<Map> employeeList = employeeMapper.selectEmployee(condition);
            pageResult.setItems(employeeList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
	}
	
	@Override
	    public void addEmployee(Employee employee) {
	        employeeMapper.insertSelective(employee);
	    }
	   
	@Override
	public void deleteEmployee(Long id) {
		employeeMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public Employee editEmployee(Long id) {
		
		return employeeMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void updateEmployee(Employee employee) {
		employeeMapper.updateByPrimaryKey(employee);
		
	}


	
}
