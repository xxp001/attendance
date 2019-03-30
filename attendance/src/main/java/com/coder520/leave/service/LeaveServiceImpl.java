package com.coder520.leave.service;


import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.ReadFiles;
import com.coder520.leave.dao.LeaveMapper;
import com.coder520.leave.entity.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/19.
 */
@Service("leaveServiceImpl")
public class LeaveServiceImpl implements LeaveService{

    @Autowired
    private LeaveMapper leaveMapper;
   
    
    
    @SuppressWarnings("all")
	@Override
	public void importLeave(String name, MultipartFile file) {
    	
    	 Leave leave=null;
    	 ReadFiles readFiles=new ReadFiles();
         //解析excel，获取考勤信息集合。
         List<Map> mapList = readFiles.getExcelInfo(name ,file);
         if(mapList != null){
        	 List<Leave> leaveList = new ArrayList<>();
        	 for(Map map : mapList){
            String approveId = (String) map.get("one");
         	String submitDate = (String) map.get("two");
         	String applyName=(String) map.get("three");
         	String applyDepartment=(String) map.get("four");
         	String applyType=(String) map.get("five");
         	String startDate=(String) map.get("six");
         	String endDate=(String) map.get("seven");
         	String leaveTime=(String) map.get("eight");
         	String leaveReason=(String) map.get("nine");
         	String approveStatus=(String) map.get("ten");
         	String approveName=(String) map.get("eleven");
         	String sendName=(String) map.get("twelve");
         	String approveProcess=(String) map.get("thirteen");
         	leave=new Leave();
         	leave.setApproveId(approveId);
         	leave.setSubmitDate(submitDate);
         	leave.setApplyName(applyName);
         	leave.setApplyDepartment(applyDepartment);
         	leave.setApplyType(applyType);
         	leave.setStartDate(startDate);
         	leave.setEndDate(endDate);
         	leave.setLeaveTime(leaveTime);
         	leave.setLeaveReason(leaveReason);
         	leave.setApproveStatus(approveStatus);
         	leave.setApproveName(approveName);
         	leave.setSendName(sendName);
         	leave.setApproveProcess(approveProcess);
         	leaveList.add(leave);
         }
        	 leaveMapper.batchInsert(leaveList);
	}
    }


	@Override
	public PageQueryBean listLeave(QueryCondition condition) {
		 //根据条件查询 count记录数目
        int count = leaveMapper.countLeave(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            @SuppressWarnings("rawtypes")
			List<Map> leaveList = leaveMapper.selectLeave(condition);
            pageResult.setItems(leaveList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
	}



	
}
