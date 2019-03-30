package com.coder520.attend.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;

/**
 * Created by xxp[www.aiprogram.top] 2018/10/5.
 */
public interface AttendService {
	/**
	 * 
	 * Title: fingerprintImport
	 * Description:  指纹数据导入
	 * @param name 文件名
	 * @param file 需上传的文件
	 * @throws Exception
	 */
    void fingerprintImport(String name,MultipartFile file) throws Exception;
    /**
     * 
     * Title: wechatImport
     * Description: 微信数据导入 
     * @param name 文件名
	 * @param file 需上传的文件
     * @throws Exception
     */
    void wechatImport(String name,MultipartFile file) throws Exception;
    /**
     * 
     * Title: listAttend
     * Description:  每日考勤数据展示
     * @param condition 查询条件
     * @return
     */
    PageQueryBean listAttend(QueryCondition condition);
    /**
     * 
     * Title: listAll
     * Description:  每月考勤汇总数据展示
     * @param condition 查询条件
     * @return
     */
    PageQueryBean listAll(QueryCondition condition);
    /**
     * 
     * Title: checkAttend
     * Description: quartz定时器检查员工每日考勤状态 
     * @throws ParseException
     */
    void checkAttend() throws ParseException;
    
    
    
    public List<Attend> export(QueryCondition condition1);
}
