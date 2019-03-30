package com.coder520.common.utils;
/** 
*@Title: leaveCount.java 
*@Package com.coder520.common.utils 
*@Description: 计算每月每人请假总时长单位转换的封装类
*@author xxp 
*@date 2018年12月19日 上午10:15:30 
*@version V1.0 
*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
public class leaveCount {
	public static Map count(List<String> list) {
		double day=0;
		double day1=0;
		double hour=0;
		double hour1=0;
		Map map=new HashMap<>();
         for(String param:list) {
			if(param.contains("天")&&param.contains("小时")) {
				String a=param.substring(0, param.indexOf("天"));
				day+=Double.parseDouble(a);
				String b=param.substring(param.indexOf("天")+1, param.indexOf("小时"));
				hour+=Double.parseDouble(b);
			}
			if(param.contains("天")&&!param.contains("小时")) {
				String c=param.substring(0, param.indexOf("天")); 
				day1+=Double.parseDouble(c);
			}
			if(param.contains("小时")&&!param.contains("天")) {
				String d=param.substring(0, param.indexOf("小时")); 
				hour1+=Double.parseDouble(d);
			}
			
    	}	
         map.put("day", day+day1);
		 map.put("hour",hour+hour1);
		return map;
		
	}
	

}
