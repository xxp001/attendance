package com.coder520.attend.entity;

import java.util.Date;

public class Attend {
    private Long id;

    private Long userId;
    
    private String userName;

    private String attendDate;

    private Byte attendWeek;

    private Date attendMorning;

    private Date attendEvening;
    
	private String dataFrom;
    
    private Character leaveStatus;
    
    private String applyType;
    
    private Integer absence;

    private Byte attendStatus;


	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAttendDate() {
		return attendDate;
	}

	public void setAttendDate(String attendDate) {
		this.attendDate = attendDate;
	}

	public Byte getAttendWeek() {
        return attendWeek;
    }

    public void setAttendWeek(Byte attendWeek) {
        this.attendWeek = attendWeek;
    }
	
	public Date getAttendMorning() {
		return attendMorning;
	}

	public void setAttendMorning(Date attendMorning) {
		this.attendMorning = attendMorning;
	}

	public Date getAttendEvening() {
		return attendEvening;
	}

	public void setAttendEvening(Date attendEvening) {
		this.attendEvening = attendEvening;
	}
	
	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	
	public Character getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(Character leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public Integer getAbsence() {
        return absence;
    }

    public void setAbsence(Integer absence) {
        this.absence = absence;
    }

    public Byte getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(Byte attendStatus) {
        this.attendStatus = attendStatus;
    }
}