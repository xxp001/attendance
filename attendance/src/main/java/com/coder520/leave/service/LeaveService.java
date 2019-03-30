package com.coder520.leave.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/16.
 */
public interface LeaveService {
    /**
     * 
     * Title: importLeave
     * Description:  请假数据导入
     * @param name 文件名
     * @param file 需上传的文件
     */
	void importLeave(String name,MultipartFile file);
	/**
	 * 
	 * Title: listLeave
	 * Description: 请假数据展示 
	 * @param condition 查询条件
	 * @return
	 */
    PageQueryBean listLeave(QueryCondition condition);
    
}
