package com.coder520.attend.controller;

import com.coder520.attend.entity.Attend;
import com.coder520.attend.service.AttendService;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.ExportExcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xxp[www.aiprogram.top] 2018/10/3.
 */
@Controller
public class AttendController {

    @Autowired
    private AttendService attendService;
	
    @RequestMapping("/index_v1")
    public String toIndex(){

        return "index_v1";
    }
    @RequestMapping("/attend")
    public String toAttend(){

        return "attend";
    }
    @RequestMapping("/month")
    public String toMonth(){

        return "month";
    }
    /**
     * 
     * Title: listAll
     * Description:  每月考勤汇总
     * @param condition 查询条件
     * @return
     */
    @RequestMapping("/allList")
    @ResponseBody
    public PageQueryBean listAll(QueryCondition condition){
        PageQueryBean result = attendService.listAll(condition);
        return result;
    }
   /**
    * 
    * Title: batchimport
    * Description: 指纹数据导入
    * @param file 需上传的文件
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
    @RequestMapping("/excelImport")
    @ResponseBody
	 public String batchimport(@RequestParam(value="filename") MultipartFile file,
	         HttpServletRequest request,HttpServletResponse response) throws Exception{
	     //判断文件是否为空
	     if(file==null) return null;
	     //获取文件名
	     String name=file.getOriginalFilename();
	     //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
	     long size=file.getSize();
	     if(name==null || ("").equals(name) && size==0) return null;
	     
	     //批量导入。参数：文件名，文件。
	    attendService.fingerprintImport(name, file);
	    return "importSuccess";
	     }
	     
    /**
     * 
     * Title: wechatImport
     * Description:微信数据导入
     * @param file 需上传的文件
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/wechatImport")
    @ResponseBody
	 public String wechatImport(@RequestParam(value="filename") MultipartFile file,
	         HttpServletRequest request,HttpServletResponse response) throws Exception{
	     //判断文件是否为空
	     if(file==null) return null;
	     //获取文件名
	     String name=file.getOriginalFilename();
	     //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
	     long size=file.getSize();
	     if(name==null || ("").equals(name) && size==0) return null;
	     
	     //批量导入。参数：文件名，文件。
	    attendService.wechatImport(name, file);
	    return "importSuccess";
	     }
	

    /**
     * 
     * Title: listAttend
     * Description: 考勤数据分页查询
     * @param condition 查询条件
     * @return
     */
    @RequestMapping("/attendList")
    @ResponseBody
    public PageQueryBean listAttend(QueryCondition condition){
    	/* User user = (User) session.getAttribute("USER_SESSION");
         condition.setUserId(user.getId());*/
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        PageQueryBean result = attendService.listAttend(condition);
       
        return result;
    }
    
    
    @RequestMapping(value = "/export")
    //@ResponseBody
    public void export(QueryCondition condition1,HttpServletRequest request,HttpServletResponse response) throws Exception {
           //获取数据
    	//String [] rangeDate =condition1.getRangeDate().split("/") ;
    	String startTime = request.getParameter("startTime");
    	String endTime = request.getParameter("endTime");
    	String attendStatus =request.getParameter("attendStatus");
    	String userName = request.getParameter("userName");
        condition1.setStartDate(startTime);
        condition1.setEndDate(endTime);
        condition1.setAttendStatus(attendStatus);
        condition1.setUserName(userName);
      
       List<Attend> result = attendService.export(condition1);

           //excel标题
            String[] title = {"姓名","考勤日期","星期","早打卡","晚打卡","数据来源","是否请假","请假类型","缺勤时长","考勤状态"};

          //excel文件名
            String fileName = "员工考勤数据记录表"+System.currentTimeMillis()+".xls";

          //sheet名
            String sheetName = "员工考勤表";
            int max= result.size();
            String[][] content=new String[max][];
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
         for (int i = 0; i <max; i++) {
        	content[i] = new String[title.length];
            Attend attend = result.get(i);
            content[i][0] = attend.getUserName().toString();
            content[i][1] = attend.getAttendDate().toString();
            content[i][2] = attend.getAttendWeek().toString();
             Date attendMorning = attend.getAttendMorning();
            if(attendMorning!=null) {
            	String attendMorning1 = format.format(attend.getAttendMorning());
            	content[i][3] =attendMorning1;
            }
            Date attendEvening = attend.getAttendEvening();
            if(attendEvening!=null) {
            	String attendEvening1 = format.format(attendEvening);
            	content[i][4] = attendEvening1;
            }
            String dataFrom =String.valueOf(attend.getDataFrom());
            if(dataFrom!=null) {
            	content[i][5] =dataFrom;
            }
            String leaveStatus = String.valueOf(attend.getLeaveStatus());
            if(leaveStatus!=null||leaveStatus!="null") {
            	 content[i][6] =leaveStatus;
            }
            String applyType = String.valueOf(attend.getApplyType());
            if(applyType!=null) {
            	content[i][7] =applyType ;
            }
            String absence =  String.valueOf(attend.getAbsence());
            if(absence!=null) {
            	content[i][8] =absence;
            }
            
            if(attend.getAttendStatus()==1) {
            	content[i][9] = "正常";
            }else {
            	content[i][9] = "异常";
            }
            
         }

         //创建HSSFWorkbook 
         	HSSFWorkbook wb = ExportExcel.getHSSFWorkbook(sheetName, title, content, null);
         	//响应到客户端
         	try {
         		this.setResponseHeader(response, fileName);
         		OutputStream os = response.getOutputStream();
         		wb.write(os);
         		os.flush();
         		os.close();
         	}catch (Exception e) {
				e.printStackTrace();
			}
    }
   
    
    @SuppressWarnings("all")
	@RequestMapping(value = "/export1")
    //@ResponseBody
    public void exportMonth(QueryCondition condition2,HttpServletRequest request,HttpServletResponse response) throws Exception {
           //获取数据
    	String userName = request.getParameter("userName");
        condition2.setUserName(userName);
        condition2.setPageSize(100);
       PageQueryBean result = attendService.listAll(condition2);
       		List<Map> items = (List<Map>) result.getItems();
           //excel标题
            String[] title = {"姓名","应出勤天数","实际出勤天数","迟到","旷工","调休","年假","病假","事假"};

          //excel文件名
            String fileName = "员工每月考勤汇总表"+System.currentTimeMillis()+".xls";

          //sheet名
            String sheetName = "员工考勤表";
            int max= items.size();
            String[][] content=new String[max][];
         for (int i = 0; i <max; i++) {
        	content[i] = new String[title.length];
            Map map= items.get(i);
            content[i][0] = (String) map.get("name");
            content[i][1] = (String) map.get("totalDate");
            content[i][2] = (String) map.get("actuallyDate");
            String late = (String) map.get("late");
            if(late!=null) {
            	content[i][3] =late;
            }
            String absence = (String) map.get("absence");
            if(absence!=null) {
            	content[i][4] = absence;
            }
            String relax =(String) map.get("relax");
            if(relax!=null) {
            	content[i][5] =relax;
            }
            String year = (String) map.get("year");
            if(year!=null) {
            	 content[i][6] =year;
            }
            String sick = (String) map.get("sick");
            if(sick!=null) {
            	content[i][7] =sick ;
            }
            String casual = (String) map.get("casual");
            if(casual!=null) {
            	content[i][7] =casual ;
            }
         }

         //创建HSSFWorkbook 
         	HSSFWorkbook wb = ExportExcel.getHSSFWorkbook(sheetName, title, content, null);
         	//响应到客户端
         	try {
         		this.setResponseHeader(response, fileName);
         		OutputStream os = response.getOutputStream();
         		wb.write(os);
         		os.flush();
         		os.close();
         	}catch (Exception e) {
				e.printStackTrace();
			}
  
    } 
    
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    
    
  
  
}
