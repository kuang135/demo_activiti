package com.ceair.activiti.spring.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ceair.activiti.spring.bean.Leave;
import com.ceair.activiti.spring.dao.DbUtil;

@Service
public class LeaveService {


	public List<Leave> findLeaveListById(int employeeId) throws SQLException {
		return DbUtil.getPojoList("select * from t_leave where employeeId = ?", Leave.class, employeeId);
	}

	public Leave findLeaveById(int leaveId) throws SQLException {
		return DbUtil.getPojo("select * from t_leave where id = ?", Leave.class, leaveId);
	}

	public void saveOrUpdateLeave(Leave leave) throws SQLException {
		if (leave != null && leave.getId() != 0) {
			String sql =  "UPDATE t_leave SET days=?,content=?,remark=?,leaveDate=?,state=?,employeeId=? WHERE id =?";
			DbUtil.update(sql, leave.getDays(),leave.getContent(),leave.getRemark(),
					leave.getLeaveDate(),leave.getState(),leave.getEmployeeId(),leave.getId());
		} else {
			String sql =  "INSERT INTO t_leave(days,content,remark,leaveDate,state,employeeId)"
					+ "VALUES(?,?,?,?,?,?)";
			DbUtil.update(sql, leave.getDays(),leave.getContent(),leave.getRemark(),
					leave.getLeaveDate(),leave.getState(),leave.getEmployeeId());
		}
		
	}
	
	public void deleteById(int id) throws SQLException {
		String sql = "DELETE FROM t_leave where id = ?";
		DbUtil.update(sql, id);
	}
	
	/**保存请假单*//*
	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		//获取请假单ID
		Long id = leaveBill.getId();
		*//**新增保存*//*
		if(id==null){
			//1：从Session中获取当前用户对象，将LeaveBill对象中user与Session中获取的用户对象进行关联
			leaveBill.setUser(SessionContext.get());//建立管理关系
			//2：保存请假单表，添加一条数据
			leaveBillDao.saveLeaveBill(leaveBill);
		}
		*//**更新保存*//*
		else{
			//1：执行update的操作，完成更新
			leaveBillDao.updateLeaveBill(leaveBill);
		}
		
	}
	
	*//**使用请假单ID，查询请假单的对象*//*
	@Override
	public LeaveBill findLeaveBillById(Long id) {
		LeaveBill bill = leaveBillDao.findLeaveBillById(id);
		return bill;
	}
	
	*//**使用请假单ID，删除请假单*//*
	@Override
	public void deleteLeaveBillById(Long id) {
		leaveBillDao.deleteLeaveBillById(id);
	}*/

}
