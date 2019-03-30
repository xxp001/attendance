package com.coder520.user.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.user.entity.User;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
public interface UserService {
    /**
     * 
     * Title: findUserByUsername
     * Description: 根据用户名查询用户 
     * @param username 用户名
     * @return
     */
    User findUserByUsername(String username);
    /**
     * 
     * Title: createUser
     * Description:  用户注册
     * @param user 用户实体
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    /**
     * 
     * Title: listUser
     * Description:用户分页查询
     * @param condition 查询条件
     * @return
     */
    PageQueryBean listUser(QueryCondition condition);
    /**
     * 
     * Title: deleteUser
     * Description:删除用户
     * @param id 用户id
     */
    void deleteUser(Long id);
    /**
     * 
     * Title: editUser
     * Description: 展示要修改的用户信息
     * @param id 用户id
     * @return
     */
    User editUser(Long id);
    /**
     * 
     * Title: updateUser
     * Description:  修改保存用户信息
     * @param user 已修改的用户实体
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    void updateUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
