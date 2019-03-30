package com.coder520.attend.vo;

import com.coder520.common.page.PageQueryBean;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/25.
 */
public class QueryCondition  extends PageQueryBean{

    private Long userId;

    private String startDate ;

    private String endDate ;

    private String rangeDate;

    private String attendStatus;
    
    private String dataFrom;
    
    private String userName;
    
    private String applyName;
    
    private String name;
    

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAttendStatus() {
		return attendStatus;
	}

	public void setAttendStatus(String attendStatus) {
		this.attendStatus = attendStatus;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getRangeDate() {
        return rangeDate;
    }

    public void setRangeDate(String rangeDate) {
        this.rangeDate = rangeDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

	
	
}
