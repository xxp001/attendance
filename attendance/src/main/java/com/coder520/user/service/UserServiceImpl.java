package com.coder520.user.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.dao.UserMapper;
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
* @ClassName: UserServiceImpl
* @Description: 对用户的增删改查
* @author xxp
* @date 2018年10月20日
*
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    
    @Override
    public User findUserByUsername(String username) {
        User user =userMapper.selectByUsername(username);
        return user;
    }


    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        userMapper.insertSelective(user);
    }
   


	@Override
	public PageQueryBean listUser(QueryCondition condition) {
		 //根据条件查询 count记录数目
        int count = userMapper.countUsers(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            @SuppressWarnings("rawtypes")
			List<Map> userList = userMapper.selectUsers(condition);
            pageResult.setItems(userList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
	}


	@Override
	public void deleteUser(Long id) {
		userMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public User editUser(Long id) {
		
		return userMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void updateUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if(user.getPassword()!=null) {
		String encrypassword = SecurityUtils.encryptPassword(user.getPassword());
		user.setPassword(encrypassword);
			}
		userMapper.updateByPrimaryKeySelective(user);
		
	}


	
}
