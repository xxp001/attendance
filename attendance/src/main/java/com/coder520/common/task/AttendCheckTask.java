package com.coder520.common.task;

import org.springframework.beans.factory.annotation.Autowired;
import com.coder520.attend.service.AttendService;

import java.text.ParseException;
/**
 * Created by xxp[www.aiprogram.top] 2018/9/28.
 */
public class AttendCheckTask {

	@Autowired
    private AttendService attendService;
    public  void checkAttend() throws ParseException{
    	//首先获取 今天没打卡的人  给他插入打卡记录  并且设置为异常 缺勤540分钟

        //如果有打卡记录  检查早晚打卡、迟到早退， 看看考勤是不是正常
    	attendService.checkAttend();
    	
        
    }
}
