package com.ceair.activiti.spring.bean;

import java.util.Date;

/**
 * 请假单
 */
public class Leave {
	private int id;//主键ID
	private int days;// 请假天数
	private String content;// 请假内容
	private Date leaveDate = new Date();// 请假时间
	private String remark;// 备注
	private int employeeId;// 请假人
	private int state;// 请假单状态 : 0初始录入,1.开始审批,2为审批完成
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
