package com.coder520.attend.dao;

import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AttendMapper {
	// 汇总数据
	
	int selectByName();
	
	List<String> selectName(@Param("startRow")int start,@Param("pageSize")int size);

	int countName();
	
	int selectAllDate(String name);

	int selectLate(String name);
	
	int selectAbsence(String name);

	List<String> selectcasualLeave(String name);

	List<String> selectsickLeave(String name);

	List<String> selectyearLeave(String name);

	List<String> selectrelaxLeave(String name);
	
	
	
	
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);
    
    List<Long> selectTodayAbsence(String atendDate);
    
    List<String> selectAttendDate();
    
    String selectNameById(Long id);
    int selectIdByName(String userName);
    String selectDataFromById(Long id);
    
   
    
    List<Map> selectLeave(@Param("name")String name);
    
    List<Attend> selectTodayEveningAbsence(String eveDate);
    
    List<Attend> selectTodayMorningAbsence(String morDate);

    void batchInsert(@Param("list") List<Attend> attendList);
    
    void wechatInsert(@Param("list") List<Attend> attendList);
    
    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

    Attend selectTodaySignRecord(@Param("userId")Long userId,@Param("attendDate")String attendDate);
    //查询所有考勤数据
    List<Attend> selectAttend();

    int countByCondition(QueryCondition condition);

    List<Map> selectAttendPage(QueryCondition condition);
    
    List<Attend> selectExportAttend(QueryCondition condition1);
}