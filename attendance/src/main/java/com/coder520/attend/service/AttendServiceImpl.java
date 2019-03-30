package com.coder520.attend.service;

import com.coder520.attend.dao.AttendMapper;
import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.DateUtils;
import com.coder520.common.utils.ExportExcel;
import com.coder520.common.utils.ReadExcel;
import com.coder520.common.utils.ReadWeChat;
import com.coder520.common.utils.leaveCount;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: AttendServiceImpl
* @Description: 实现每月考勤数据汇总展示、指纹数据导入、微信数据导入，每日考勤数据展示、每日考勤数据状态判断
* @author xxp
* @date 2018年10月15日
*
 */

@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService{

    /**
     * 中午十二点 判定上下午
     */
    private static final int NOON_HOUR = 12 ;
    private static final int MORNING_HOUR = 9 ;
    private static final int AFTERNOON_HOUR =17 ;
    private static final int MORNING_MINUTE = 30 ;
    private static final int NOON_MINUTE = 00 ;
    private static final int NOON_SECOND = 00 ;
    //设置状态变量
    private static final int UNNORMAL_STATE =2;
    private static final int NORMAL_STATE =1;
    //设置缺勤时长
    private static final int ABSENCE_DAY=540;
    private static final int ABSENCE_HALF=270;

    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat format3= new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat format2= new SimpleDateFormat("yyyy-MM-dd");
       
    @Autowired
    private AttendMapper attendMapper;

    @SuppressWarnings("all")
	public PageQueryBean listAll(QueryCondition condition) {
    	 PageQueryBean pageResult = new PageQueryBean();
    	String like = condition.getUserName();
    	//根据条件查询 count记录数目
    	if(null==like||like=="") {
    		int count=attendMapper.countName();
            Map map;
            List<Map> allList=new ArrayList<Map>();
            if(count>0){
             	 pageResult.setCurrentPage(condition.getCurrentPage());
                 pageResult.setPageSize(condition.getPageSize());
            	 pageResult.setTotalRows(count);
            	 Integer startRow = pageResult.getStartRow();
            	 Integer pageSize = condition.getPageSize();
            	 pageResult.setStartRow(startRow);
            	 List<String> userName = attendMapper.selectName(startRow,pageSize);
                for(String name:userName) {
                	map=new HashMap<>();
                	//int allDate=attendMapper.selectAllDate(name);
                	int late=attendMapper.selectLate(name);
                	int absence= attendMapper.selectAbsence(name);
                	
                	double dayRelax=0;
                	double hourRelax=0;
                	double dayYear=0;
                	double hourYear=0;
                	double daySick=0;
                	double hourSick=0;
                	double dayCasual=0;
                	double hourCasual=0;
                	List<String> relax=attendMapper.selectrelaxLeave(name);
                	if(relax.size()!=0) {
	                	Map map4=leaveCount.count(relax);
	                	dayRelax=(double) map4.get("day");
	                	hourRelax=(double) map4.get("hour");
	                	}
                	else {
                    		map.put("relax", null);
                	}
                	List<String> year= attendMapper.selectyearLeave(name);
                	if(year.size()!=0) {
	                	Map map1=leaveCount.count(year);
	                	dayYear= (double) map1.get("day");
	                	hourYear=(double) map1.get("hour");
                	} else {
                    		map.put("year", null);
                	}
                	
                	List<String> sick= attendMapper.selectsickLeave(name);
                	if(sick.size()!=0) {
	                	Map map2=leaveCount.count(sick);
	                	daySick=(double) map2.get("day");
	                	hourSick=(double) map2.get("hour");
                	} else {
                		map.put("sick", null);
                	}
                	
                	List<String> casual=attendMapper.selectcasualLeave(name);
                	if(casual.size()!=0) {
	                	Map map3=leaveCount.count(casual);
	                	dayCasual=(double) map3.get("day");
	                	hourCasual=(double) map3.get("hour");
                	} else {
                		map.put("casual", null);
                	}
                	
                	map.put("name", name);
                	map.put("totalDate", "22天");
                	double allHour=hourRelax+hourYear+hourSick+hourCasual;
                	int allDay=(int) (dayRelax+dayYear+daySick+dayCasual);
                	int actuallyDay=0;
                	double actuallyHour=0;
                	actuallyHour=((22-allDay)*7.5-allHour)%7.5;
                	actuallyDay=(int) (22-allDay-absence-Math.ceil(allHour/7.5));
                	
                	if(actuallyHour!=0) {
                	map.put("actuallyDate", actuallyDay+"天"+actuallyHour+"小时");
                	}else {
                		map.put("actuallyDate", actuallyDay+"天");
                	}
                	if(late!=0) {
                		map.put("late", late+"次");
                	}else {
                		map.put("late", null);
                	}
                	if(absence!=0) {
                		map.put("absence", absence+"次");
                	}else {
                		map.put("absence", null);
                	}
                	
                	//每月调休假统计
                	if(dayRelax!=0&&hourRelax!=0) {
                		if(hourRelax<7.5) {
                			map.put("relax", (int)dayRelax+"天"+hourRelax+"小时");
                		}else {
                			double rest=hourRelax%7.5;
                    		int relaxDay=(int) (dayRelax+((hourRelax-rest)/7.5));
                    		map.put("relax", relaxDay+"天"+rest+"小时");
                		}
                		
                	}
                	if(dayRelax==0&&hourRelax!=0) {
                		if(hourRelax<7.5) {
                			map.put("relax", hourRelax+"小时");
                		}else {
                			double relaxHour=hourRelax%7.5;
                			int relaxDay=(int) ((hourRelax-relaxHour)/7.5);
                			if(relaxHour!=0) {
                				map.put("relax",relaxDay+"天"+relaxHour+"小时");
                			}else {
                				map.put("relax",relaxDay+"天");
                			}
                			
                		}
                		
                	}if(dayRelax!=0&&hourRelax==0){
                		map.put("relax",(int) dayRelax+"天");
                	}
                	
                	//每月年假统计
                	if(dayYear!=0&&hourYear!=0) {
                		if(hourYear<7.5) {
                			map.put("year", (int)dayYear+"天"+hourYear+"小时");
                		}else {
                			double rest=hourYear%7.5;
                    		int yearDay=(int) (dayYear+((hourYear-rest)/7.5));
                    		map.put("year", yearDay+"天"+rest+"小时");
                		}
                	}
                	if(dayYear==0&&hourYear!=0) {
                		if(hourYear<7.5) {
                			map.put("year", hourYear+"小时");
                		}else {
                			double yearHour=hourYear%7.5;
                			int yearDay=(int) ((hourYear-yearHour)/7.5);
                			if(yearHour!=0) {
                				map.put("year",yearDay+"天"+yearHour+"小时");
                			}else {
                				map.put("year",yearDay+"天");
                			}
                		}
                		
                	}if(dayYear!=0&&hourYear==0) {
                		map.put("year", (int)dayYear+"天");
                	}
                	//每月病假统计
                	if(daySick!=0&&hourSick!=0) {
                		if(hourSick<7.5) {
                			map.put("sick",(int) daySick+"天"+hourSick+"小时");
                		}else {
                			double rest=hourSick%7.5;
                    		int sickDay= (int) (daySick+((hourSick-rest)/7.5));
                    		map.put("sick",sickDay+"天"+rest+"小时");
                		}
                	}
                	if(daySick==0&&hourSick!=0) {
                		if(hourSick<7.5) {
                			map.put("sick", hourSick+"小时");
                		}else {
                			double sickHour=hourSick%7.5;
                			int sickDay=(int) ((hourSick-sickHour)/7.5);
                			if(sickHour!=0) {
                				map.put("sick",sickDay+"天"+sickHour+"小时");
                			}else {
                				map.put("sick",sickDay+"天");
                			}
                			
                		}
                		
                	}if(daySick!=0&&hourSick==0) {
                		map.put("sick", (int)daySick+"天");
                	}
                	//每月事假统计
                	if(dayCasual!=0&&hourCasual!=0) {
                		if(hourCasual<7.5) {
                			map.put("casual", (int)dayCasual+"天"+hourCasual+"小时");
                		}else {
                			double rest=hourCasual%7.5;
                    		int casualDay=(int) (dayCasual+((hourCasual-rest)/7.5));
                    		map.put("casual", casualDay+"天"+rest+"小时");
                		}
                	}
                	if(dayCasual==0&&hourCasual!=0) {
                		if(hourCasual<7.5) {
                			map.put("casual", hourCasual+"小时");
                		}else {
                			double casualHour=hourCasual%7.5;
                			int casualDay=(int) ((hourCasual-casualHour)/7.5);
                			if(casualHour!=0) {
                				map.put("casual",casualDay+"天"+casualHour+"小时");
                			}else {
                				map.put("casual",casualDay+"天");
                			}
                			
                		}
                	}
                	if(dayCasual!=0&&hourCasual==0){
                		map.put("casual", (int)dayCasual+"天");
                	}
                     allList.add(map);
                }
                pageResult.setItems(allList);
            }
    	}else {
    		int selectByName = attendMapper.selectByName();
    		Map map1;
    	        List<Map> singleList=new ArrayList<Map>();
    	        if(selectByName>0){
    	         	//pageResult.setCurrentPage(condition.getCurrentPage());
    	             pageResult.setPageSize(condition.getPageSize());
    	        	 pageResult.setTotalRows(1);
    	            	map1=new HashMap<>();
    	            	//int allDate=attendMapper.selectAllDate(name);
    	            	int late=attendMapper.selectLate(like);
    	            	int absence= attendMapper.selectAbsence(like);
    	            	int actuallyDate=22-absence;
    	            	double dayRelax=0;
                    	double hourRelax=0;
                    	double dayYear=0;
                    	double hourYear=0;
                    	double daySick=0;
                    	double hourSick=0;
                    	double dayCasual=0;
                    	double hourCasual=0;
    	            	
    	            	
    	            	List<String> relax=attendMapper.selectrelaxLeave(like);
                    	if(relax.size()!=0) {
    	                	Map mapr=leaveCount.count(relax);
    	                	dayRelax=(double) mapr.get("day");
    	                	hourRelax=(double) mapr.get("hour");
    	                	}
                    	else {
                        		map1.put("relax", null);
                    	}
                    	List<String> year= attendMapper.selectyearLeave(like);
                    	if(year.size()!=0) {
    	                	Map mapy=leaveCount.count(year);
    	                	dayYear= (double) mapy.get("day");
    	                	hourYear=(double) mapy.get("hour");
                    	} else {
                        		map1.put("year", null);
                    	}
                    	
                    	List<String> sick= attendMapper.selectsickLeave(like);
                    	if(sick.size()!=0) {
    	                	Map sickMap=leaveCount.count(sick);
    	                	daySick=(double) sickMap.get("day");
    	                	hourSick=(double) sickMap.get("hour");
                    	} else {
                    		map1.put("sick", null);
                    	}
                    	
                    	List<String> casual=attendMapper.selectcasualLeave(like);
                    	if(casual.size()!=0) {
    	                	Map casualMap=leaveCount.count(casual);
    	                	dayCasual=(double) casualMap.get("day");
    	                	hourCasual=(double) casualMap.get("hour");
                    	} else {
                    		map1.put("casual", null);
                    	}
                    	
    	            	map1.put("name", like);
    	            	map1.put("totalDate", "22天");
    	            	double allHour=hourRelax+hourYear+hourSick+hourCasual;
                    	double hour=0;
                    	int actuallyDay=0;
                    	double actuallyHour=0;
                    	if(allHour>7.5) {
                    		actuallyHour=allHour%7.5;
                    		int day=(int) ((allHour-actuallyHour)/7.5);
                    		actuallyDay=(int) (22-dayRelax-dayYear-daySick-dayCasual-absence-1-day);
                    	}else if(allHour>0){
                    		actuallyHour=7.5-allHour;
                    		actuallyDay=(int) (22-dayRelax-dayYear-daySick-dayCasual-absence-1);
                    	}else {
                    		actuallyHour=allHour;
                    		actuallyDay=(int) (22-dayRelax-dayYear-daySick-dayCasual-absence);
                    	}
                    	if(actuallyHour!=0) {
                    	map1.put("actuallyDate", actuallyDay+"天"+actuallyHour+"小时");
                    	}else {
                    		map1.put("actuallyDate", actuallyDay+"天");
                    	}
    	            	
    	            	if(late!=0) {
    	            		map1.put("late", late+"次");
    	            	}else {
    	            		map1.put("late", null);
    	            	}
    	            	if(absence!=0) {
    	            		map1.put("absence", absence+"次");
    	            	}else {
    	            		map1.put("absence", null);
    	            	}
    	            	
    	            	
    	            	//每月调休假统计
                    	if(dayRelax!=0&&hourRelax!=0) {
                    		if(hourRelax<7.5) {
                    			map1.put("relax", (int)dayRelax+"天"+hourRelax+"小时");
                    		}else {
                    			double rest=hourRelax%7.5;
                        		int relaxDay=(int) (dayRelax+((hourRelax-rest)/7.5));
                        		map1.put("relax", relaxDay+"天"+rest+"小时");
                    		}
                    		
                    	}
                    	if(dayRelax==0&&hourRelax!=0) {
                    		if(hourRelax<7.5) {
                    			map1.put("relax", hourRelax+"小时");
                    		}else {
                    			double relaxHour=hourRelax%7.5;
                    			int relaxDay=(int) ((hourRelax-relaxHour)/7.5);
                    			if(relaxHour!=0) {
                    				map1.put("relax",relaxDay+"天"+relaxHour+"小时");
                    			}else {
                    				map1.put("relax",relaxDay+"天");
                    			}
                    			
                    		}
                    		
                    	}if(dayRelax!=0&&hourRelax==0){
                    		map1.put("relax",(int) dayRelax+"天");
                    	}
                    	
                    	//每月年假统计
                    	if(dayYear!=0&&hourYear!=0) {
                    		if(hourYear<7.5) {
                    			map1.put("year", (int)dayYear+"天"+hourYear+"小时");
                    		}else {
                    			double rest=hourYear%7.5;
                        		int yearDay=(int) (dayYear+((hourYear-rest)/7.5));
                        		map1.put("year", yearDay+"天"+rest+"小时");
                    		}
                    	}
                    	if(dayYear==0&&hourYear!=0) {
                    		if(hourYear<7.5) {
                    			map1.put("year", hourYear+"小时");
                    		}else {
                    			double yearHour=hourYear%7.5;
                    			int yearDay=(int) ((hourYear-yearHour)/7.5);
                    			if(yearHour!=0) {
                    				map1.put("year",yearDay+"天"+yearHour+"小时");
                    			}else {
                    				map1.put("year",yearDay+"天");
                    			}
                    		}
                    		
                    	}if(dayYear!=0&&hourYear==0) {
                    		map1.put("year", (int)dayYear+"天");
                    	}
                    	//每月病假统计
                    	if(daySick!=0&&hourSick!=0) {
                    		if(hourSick<7.5) {
                    			map1.put("sick",(int) daySick+"天"+hourSick+"小时");
                    		}else {
                    			double rest=hourSick%7.5;
                        		int sickDay= (int) (daySick+((hourSick-rest)/7.5));
                        		map1.put("sick",sickDay+"天"+rest+"小时");
                    		}
                    	}
                    	if(daySick==0&&hourSick!=0) {
                    		if(hourSick<7.5) {
                    			map1.put("sick", hourSick+"小时");
                    		}else {
                    			double sickHour=hourSick%7.5;
                    			int sickDay=(int) ((hourSick-sickHour)/7.5);
                    			if(sickHour!=0) {
                    				map1.put("sick",sickDay+"天"+sickHour+"小时");
                    			}else {
                    				map1.put("sick",sickDay+"天");
                    			}
                    			
                    		}
                    		
                    	}if(daySick!=0&&hourSick==0) {
                    		map1.put("sick", (int)daySick+"天");
                    	}
                    	//每月事假统计
                    	if(dayCasual!=0&&hourCasual!=0) {
                    		if(hourCasual<7.5) {
                    			map1.put("casual", (int)dayCasual+"天"+hourCasual+"小时");
                    		}else {
                    			double rest=hourCasual%7.5;
                        		int casualDay=(int) (dayCasual+((hourCasual-rest)/7.5));
                        		map1.put("casual", casualDay+"天"+rest+"小时");
                    		}
                    	}
                    	if(dayCasual==0&&hourCasual!=0) {
                    		if(hourCasual<7.5) {
                    			map1.put("casual", hourCasual+"小时");
                    		}else {
                    			double casualHour=hourCasual%7.5;
                    			int casualDay=(int) ((hourCasual-casualHour)/7.5);
                    			if(casualHour!=0) {
                    				map1.put("casual",casualDay+"天"+casualHour+"小时");
                    			}else {
                    				map1.put("casual",casualDay+"天");
                    			}
                    			
                    		}
                    	}
                    	if(dayCasual!=0&&hourCasual==0){
                    		map1.put("casual", (int)dayCasual+"天");
                    	}
    	            	
    	            	singleList.add(map1);
    	                
    	            	
    	            }
    	            pageResult.setItems(singleList);
    	        }
    	return pageResult;
    	}
   
    @SuppressWarnings("rawtypes")
	@Override
    public void fingerprintImport(String name,MultipartFile file) throws Exception {
        try {
        	//User user = (User) session.getAttribute("userinfo");
        	//List<Attend> attendList = new ArrayList<>();
        	Date noon = DateUtils.getDate(NOON_HOUR,NOON_MINUTE,NOON_SECOND);
        	Date noon1=format1.parse(format1.format(noon));
    	    Date late = DateUtils.getDate(MORNING_HOUR,MORNING_MINUTE,NOON_SECOND);
            Date morning=format1.parse(format1.format(late));
            Date early = DateUtils.getDate(AFTERNOON_HOUR,MORNING_MINUTE,NOON_SECOND);
            Date evening=format1.parse(format1.format(early));
        	Attend attend=null;
        	 //创建处理EXCEL
            ReadExcel readExcel=new ReadExcel();
            //解析excel，获取考勤信息集合。
            List<Map> mapList = readExcel.getExcelInfo(name ,file);
            if(mapList != null){
            for(Map map : mapList){
            	String stime = (String) map.get("attendTime");
            	String userName = (String) map.get("userName");
            	String attendDate=(String) map.get("attendDate");
            	Date date=format.parse(attendDate);
            	int attendWeek = DateUtils.getTodayWeek(date);
            	attend=new Attend();
            	Long userId = (long) attendMapper.selectIdByName(userName);
            	attend.setUserId(userId);
            	attend.setAttendDate(format2.format(date));
            	attend.setAttendWeek((byte) attendWeek);
            	attend.setUserName(userName);
            	attend.setDataFrom("指纹数据");
            	Date time=format1.parse(stime);//1970
            	Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId(),attend.getAttendDate());
            	if(todayRecord==null){
            	if(time.compareTo(noon1)<=0){
                    //打卡时间 早于12点 上午打卡
                    attend.setAttendMorning(time);
                }else {
                    //晚上打卡
                    attend.setAttendEvening(time);
                }
            	 attendMapper.insertSelective(attend);
            	//attendList.add(attend);
              }else {
            	  if(time.compareTo(noon1)<=0){
                      //打卡时间 早于12点 上午打卡
                  }else {
            	  todayRecord.setAttendEvening(time);
                 }
            	  attendMapper.updateByPrimaryKeySelective(todayRecord);
               }
            	  
              }
            	
              }
            //attendMapper.batchInsert(attendList);
        }catch (Exception e){
            log.error("上传数据异常",e);
            throw e;
        }

    }
    
    @SuppressWarnings("all")
	public void wechatImport(String name,MultipartFile file) throws Exception{
    	try {
    	List<Attend> wechatList=new ArrayList<Attend>();
    	Date noon = DateUtils.getDate1(NOON_HOUR,NOON_MINUTE);
    	Date noon1=format3.parse(format1.format(noon));//12:00
    	Date late = DateUtils.getDate1(MORNING_HOUR,MORNING_MINUTE);
        Date morning=format3.parse(format1.format(late));//9:30
        Date early = DateUtils.getDate1(AFTERNOON_HOUR,MORNING_MINUTE);
        Date evening=format3.parse(format1.format(early));//17:30
    	ReadWeChat readWeChat=new ReadWeChat();
    	Attend attend=null;
         //解析excel，获取考勤信息集合。
         List<Map> mapList = readWeChat.getExcelInfo(name ,file);
         if(mapList!=null) {
        	 for(Map map:mapList) {
        		 String wechatDate= (String) map.get("one");
        		 String employeeName= (String) map.get("two");
        		 String wechatMorning= (String) map.get("five");
        		 String wechatEvening= (String) map.get("six");
        		 attend=new Attend();
        		 Date date=format2.parse(wechatDate);
        		 int attendWeek = DateUtils.getTodayWeek(date);
             	Long employeeId = (long) attendMapper.selectIdByName(employeeName);
             	attend.setUserName(employeeName);
             	attend.setAttendDate(wechatDate);
             	attend.setAttendWeek((byte) attendWeek);
             	attend.setUserId(employeeId);
             	attend.setDataFrom("微信数据");
             	Date morning1=format3.parse(wechatMorning);//1970
             	Date evening1=format3.parse(wechatEvening);
             	if(morning1.compareTo(noon1)<=0){
                     //打卡时间 早于12点 上午打卡
                     attend.setAttendMorning(morning1);
                 }else {
                     //晚上打卡
                	attend.setAttendEvening(morning1);
                 }
             	if(evening1.compareTo(noon1)<=0){
             		attend.setAttendMorning(morning1);
                }else {
                    //晚上打卡
                 	attend.setAttendEvening(evening1);
                }
             	wechatList.add(attend);
               }
        	 attendMapper.batchInsert(wechatList);
         }
    	}catch (Exception e){
            log.error("上传数据异常",e);
            throw e;
        }
    
    }
    
    @Override
    public PageQueryBean listAttend(QueryCondition condition) {
        //根据条件查询 count记录数目
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            @SuppressWarnings("rawtypes")
			List<Map> attendList = attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
    }
    
    
    
      //中午十二点之前打卡 都算早晨打卡  如果9.30以后 直接异常 算迟到
  	  // 十二点以后都算下午打卡
      // 下午打卡 检查与上午打卡时间差 17点30之前  算异常
      // 不足九小时都算异常 并且 缺席多长时间 要存进去
  	@SuppressWarnings("all")
  	@Override
      @Transactional
      public void checkAttend() throws ParseException {
          //查询缺勤用户ID 插入打卡记录  并且设置为异常 缺勤540分钟
      	 Date late = DateUtils.getDate(MORNING_HOUR,MORNING_MINUTE,NOON_SECOND);
           Date morning=format1.parse(format1.format(late));
           Date early = DateUtils.getDate(AFTERNOON_HOUR,MORNING_MINUTE,NOON_SECOND);
           Date evening=format1.parse(format1.format(early));
      	Attend attend1=null;
      	List<String> dateList = attendMapper.selectAttendDate();
      	for(String date:dateList) {
      	List<Long> userIdList =attendMapper.selectTodayAbsence(date);
          if(org.apache.commons.collections.CollectionUtils.isNotEmpty(userIdList)){
              List<Attend> attendList = new ArrayList<>();
              for(Long userId:userIdList){
              	  attend1= new Attend();
                  attend1.setUserId(userId);
                  String userName=attendMapper.selectNameById(userId);
                  attend1.setUserName(userName);
                  attend1.setAttendDate(date);
                  attend1.setAttendWeek((byte)DateUtils.getTodayWeek(format2.parse(date)));
                  attend1.setAbsence(ABSENCE_DAY);
                  attend1.setAttendStatus((byte) UNNORMAL_STATE);
                  List<Map> selectLeave = attendMapper.selectLeave(userName);
                  String date1=format.format(format2.parse(date));
                  if(org.apache.commons.collections.CollectionUtils.isNotEmpty(selectLeave)){
                   for(Map leave:selectLeave) {
                   	String start = (String) leave.get("start_date");
                   	String start1 = start.substring(0, 10);
                   	String type = (String) leave.get("apply_type");
                   	if(date1.equals(start1)) {
                   		attend1.setLeaveStatus('是');
                   		attend1.setApplyType(type);
                   		attend1.setAttendStatus((byte) NORMAL_STATE);
                   	}
                   	
              }
                   
          }
                  attendMapper.insertSelective(attend1);
              }
          }
          // 检查早打卡 将上班未打卡记录设置为异常
          List<Attend> morMiss = attendMapper.selectTodayMorningAbsence(date);
          if(org.apache.commons.collections.CollectionUtils.isNotEmpty(morMiss)){
              for(Attend attend : morMiss){
                attend.setAbsence(ABSENCE_HALF);
                attend.setAttendStatus((byte) UNNORMAL_STATE);
              	String userName=attend.getUserName();
              	String attendDate=attend.getAttendDate();
              	String date2=format.format(format2.parse(date));
              	List<Map> selectLeave = attendMapper.selectLeave(userName);
              	if(org.apache.commons.collections.CollectionUtils.isNotEmpty(selectLeave)) {
              	for(Map leave:selectLeave) {
                  	String start = (String) leave.get("start_date");
                  	String start1 = start.substring(0, 10);
                  	String type = (String) leave.get("apply_type");
                  	if(date2.equals(start1)) {
                  		attend.setLeaveStatus('是');
                  		attend.setApplyType(type);
                  		attend.setAttendStatus((byte) NORMAL_STATE);
                  	}
                  
              }
          }
                  attendMapper.updateByPrimaryKeySelective(attend);
              }
          }
          // 检查晚打卡 将下班未打卡记录设置为异常
          List<Attend> afterMiss = attendMapper.selectTodayEveningAbsence(date);
          if(org.apache.commons.collections.CollectionUtils.isNotEmpty(afterMiss)){
              for(Attend attend : afterMiss){
        	    attend.setAbsence(ABSENCE_HALF);
                attend.setAttendStatus((byte) UNNORMAL_STATE);
              	String userName=attend.getUserName();
              	String attendDate=attend.getAttendDate();
              	String date3=format.format(format2.parse(date));
              	List<Map> selectLeave = attendMapper.selectLeave(userName);
              if(org.apache.commons.collections.CollectionUtils.isNotEmpty(selectLeave)) {
                	for(Map leave:selectLeave) {
                    	String start = (String) leave.get("start_date");
                    	String start1 = start.substring(0, 10);
                    	String type = (String) leave.get("apply_type");
                    	if(date3.equals(start1)) {
                    		attend.setLeaveStatus('是');
                    		attend.setApplyType(type);
                    		attend.setAttendStatus((byte) NORMAL_STATE);
                    	}
                  
              }
          }
                   attendMapper.updateByPrimaryKeySelective(attend);
        }   
     }
      	
      	}   	
      
          List<Attend> allAttends=attendMapper.selectAttend();
          for(Attend attend:allAttends){
          	Date attendMorning = attend.getAttendMorning();
          	Date attendEvening = attend.getAttendEvening();
          	if(attendMorning!=null&&attendEvening!=null) {
          		int munite = DateUtils.getMunite(attendMorning, attendEvening);
              	if(munite<540) {
              		int lack=540-munite;
              		attend.setAbsence(lack);
              		attend.setAttendStatus((byte) UNNORMAL_STATE);
              		attendMapper.updateByPrimaryKeySelective(attend);
              	}else if(attendMorning.compareTo(morning)>0||attendEvening.compareTo(evening)<0){
              		attend.setAttendStatus((byte) UNNORMAL_STATE);
              		attendMapper.updateByPrimaryKeySelective(attend);
              	}else {
              		attend.setAttendStatus((byte)NORMAL_STATE);
              		attendMapper.updateByPrimaryKeySelective(attend);
              	}
          	
          	}
          	
          
          }
      	
      }

	/* (非 Javadoc)
	* <p>Title: export</p>
	* <p>Description: </p>
	* @return
	* @see com.coder520.attend.service.AttendService#export()
	*/
	@Override
	public List<Attend> export(QueryCondition condition1) {
		List<Attend> export = attendMapper.selectExportAttend(condition1);
		
		    return export;
	}
  
    
}
