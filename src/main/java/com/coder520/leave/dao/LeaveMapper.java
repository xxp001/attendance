package com.coder520.leave.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.coder520.attend.vo.QueryCondition;
import com.coder520.leave.entity.Leave;

public interface LeaveMapper {
   
    //分页
    @SuppressWarnings("rawtypes")
	List<Map> selectLeave(QueryCondition condition);
    
    void batchInsert(@Param("list") List<Leave> leaveList);
    
    int countLeave(QueryCondition condition);
}