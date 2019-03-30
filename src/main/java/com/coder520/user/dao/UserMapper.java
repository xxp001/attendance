package com.coder520.user.dao;

import java.util.List;
import java.util.Map;

import com.coder520.attend.vo.QueryCondition;
import com.coder520.user.entity.User;

public interface UserMapper {
    void deleteByPrimaryKey(Long id);
    
    

    int insert(User record);
   //注册
    int insertSelective(User record);
    //修改保存
    User selectByPrimaryKey(Long id);
    void updateByPrimaryKey(User user);
    
    
    void updateByPrimaryKeySelective(User record);

    //int updateByPrimaryKey(User record);
    //登陆校验
    User selectByUsername(String username);
    //用户分页
    List<Map> selectUsers(QueryCondition condition);
    
    int countUsers(QueryCondition condition);
}