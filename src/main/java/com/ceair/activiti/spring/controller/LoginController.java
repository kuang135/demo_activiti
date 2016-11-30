package com.ceair.activiti.spring.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ceair.activiti.spring.bean.Employee;
import com.ceair.activiti.spring.dao.DbUtil;


@Controller
public class LoginController {
	
	
	@RequestMapping(value = "login")
	public ModelAndView login(String name, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		Employee user = DbUtil.getPojo("select * from t_employee where name = '" + name + "'", Employee.class);
		if (user != null) {
			mv.setViewName("main");
			session.setAttribute("globle_user", user);
		} else {
			mv.setViewName("../../login");
		}
		return mv;
	}
	
	/**
	 * 标题
	 * @return
	 */
	@RequestMapping(value = "top")
	public String top() {
		return "top";
	}
	
	/**
	 * 左侧菜单
	 * @return
	 */
	@RequestMapping(value = "left")
	public String left() {
		return "left";
	}
	
	/**
	 * 主页显示
	 * @return
	 */
	@RequestMapping(value = "welcome")
	public String welcome() {
		return "welcome";
	}
	
	@RequestMapping(value = "logout")
	public String logout(HttpSession session) {
		session.removeAttribute("globle_user");
		return "redirect:login.jsp";
	}

}
