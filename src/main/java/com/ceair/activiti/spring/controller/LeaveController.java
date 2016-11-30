package com.ceair.activiti.spring.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ceair.activiti.spring.bean.Employee;
import com.ceair.activiti.spring.bean.Leave;
import com.ceair.activiti.spring.service.LeaveService;

@Controller
@RequestMapping(value = "leave")
public class LeaveController {
	
	@Autowired
	private LeaveService leaveService;
	
	/**
	 *  获取当前登录用户的请假信息
	 */
	@RequestMapping(value = "listLeave")
	public ModelAndView listLeave(HttpSession session) throws SQLException{
		Employee employee = (Employee) session.getAttribute("globle_user");
		List<Leave> list = leaveService.findLeaveListById(employee.getId()); 
		ModelAndView mv = new ModelAndView("leave/list");
		mv.addObject("leaveList", list);
		return mv;
	}

	 
	/**
	 * 到添加或者编辑页面
	 */
	@RequestMapping(value = "toInput")
	public ModelAndView toInput(Integer leaveId) throws SQLException{
		ModelAndView mv = new ModelAndView("leave/input");
		if(leaveId != null){
			Leave leave = leaveService.findLeaveById(leaveId);
			mv.addObject("leave", leave);
		}
		return mv;
	}
	
	/**
	 * 保存/更新，请假申请
	 * @throws SQLException 
	 * */
	@RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
	public String addOrUpdate(Integer id, Integer days, String content, Date leaveDate, String remark, Integer employeeId, Integer state) throws SQLException {
		Leave leave = new Leave();
		if (id != null) {
			leave.setId(id);
		}
		leave.setDays(days);
		leave.setContent(content);
		if (leaveDate == null) {
			leave.setLeaveDate(new Date());
		} else {
			leave.setLeaveDate(leaveDate);
		}
		leave.setRemark(remark);
		leave.setEmployeeId(employeeId);
		if (state == null) {
			leave.setState(0);
		} else {
			leave.setState(state);
		}
		leaveService.saveOrUpdateLeave(leave);
		return "redirect:listLeave.do";
	}
	
	/**
	 * 删除，请假申请
	 * @throws SQLException 
	 */
	@RequestMapping(value = "delete")
	public String delete(Integer leaveId) throws SQLException{
		leaveService.deleteById(leaveId);
		return "redirect:listLeave.do";
	}
}
