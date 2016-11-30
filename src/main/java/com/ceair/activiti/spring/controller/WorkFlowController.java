package com.ceair.activiti.spring.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ceair.activiti.spring.bean.Employee;
import com.ceair.activiti.spring.bean.Leave;
import com.ceair.activiti.spring.service.LeaveService;
import com.ceair.activiti.spring.service.WorkflowService;


@Controller
@RequestMapping(value = "workflow")
public class WorkFlowController {
	
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private LeaveService leaveService;
	
	/**
	 * 部署管理首页显示
	 */
	@RequestMapping(value = "listDeployment")
	public ModelAndView listDeployment(){
		//1:查询部署对象信息，对应表（act_re_deployment）
		List<Deployment> depList = workflowService.findDeploymentList();
		//2:查询流程定义的信息，对应表（act_re_procdef）
		List<ProcessDefinition> pdList = workflowService.findProcessDefinitionList();
		ModelAndView mv = new ModelAndView("workflow/deployment_list");
		mv.addObject("depList", depList);
		mv.addObject("pdList", pdList);
		//放置到上下文对象中
		return mv;
	}
	
	/**
	 * 通过zip文件部署流程
	 */
	@RequestMapping(value = "addDeployment")
	public String addDeployment(@RequestParam("deployName")String deployName, 
			@RequestParam("file") MultipartFile multipartFile) throws IOException{
		//1：获取页面上传递的zip格式的文件，格式是File类型
		InputStream in = multipartFile.getInputStream();
		//完成部署
		workflowService.deployment(in, deployName);
		return "redirect:listDeployment.do";
	}

	
	/**
	 * 查看流程图
	 */
	@RequestMapping(value = "viewImage")
	public void viewImage(@RequestParam("deploymentId")String deploymentId, 
				@RequestParam("imageName")String imageName, 
				HttpServletResponse response) throws Exception{
		//1：获取页面传递的部署对象ID和资源图片名称
		//2：获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
		InputStream in = workflowService.findImageInputStream(deploymentId,imageName);
		//3：从response对象获取输出流
		OutputStream out = response.getOutputStream();
		//4：将输入流中的数据读取出来，写到输出流中
		for(int b=-1;(b=in.read())!=-1;){
			//将图写到页面上，用输出流写
			out.write(b);
		}
		out.close();
		in.close();
	}
	
	
	/**
	 * 删除部署信息
	 */
	@RequestMapping(value = "deleteDeployment")
	public String deleteDeployment(String deploymentId){
		//使用部署对象ID，删除流程定义
		workflowService.deleteDeployment(deploymentId);
		return "redirect:listDeployment.do";
	}
	
	
	
	/**
	 * 启动流程
	 */
	@RequestMapping(value = "startProcess")
	public String startProcess(Integer leaveId, HttpSession session) throws SQLException{
		Employee employee = (Employee) session.getAttribute("globle_user");
		//更新请假状态，启动流程实例，让启动的流程实例关联业务
		workflowService.startProcess(leaveId, employee.getName());
		return "redirect:listTask.do";
	}
	
	
	/**
	 * 任务管理首页显示
	 */
	 @RequestMapping(value = "listTask")
	public ModelAndView listTask(HttpSession session){
		//1：从Session中获取当前用户名
		Employee employee = (Employee) session.getAttribute("globle_user");
		//2：使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>
		List<Task> taskList = workflowService.findTaskListByAssignee(employee.getName()); 
		ModelAndView mv = new ModelAndView("workflow/task_list");
		mv.addObject("taskList", taskList);
		return mv;
	}
	 
	 /**
	 * 查看当前任务
	 */
	@RequestMapping(value = "detailTask")
	public ModelAndView detailTask(String taskId) throws SQLException{
		//**一：使用任务ID，查找请假单ID，从而获取请假单信息*//*
		Leave leave = workflowService.findLeaveByTaskId(taskId);
		//**二：已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中*//*
		List<String> outSequenceFlowList = workflowService.findOutSequenceFlowListByTaskId(taskId);
		//**三：查询所有历史审核人的审核信息，帮助当前人完成审核，返回List<Comment>*//*
		List<Comment> commentList = workflowService.findCommentByTaskId(taskId);
		ModelAndView mv = new ModelAndView("workflow/task_detail");
		mv.addObject("taskId", taskId);
		mv.addObject("leave", leave);
		mv.addObject("outSequenceFlowList", outSequenceFlowList);
		mv.addObject("commentList", commentList);
		return mv;
	}
	
	/**
	 * 完成当前任务
	 * @throws SQLException 
	 */
	@RequestMapping(value = "completeTask")
	public String completeTask(String taskId, Leave leave, 
					String comment, String outSequenceFlow,
					HttpSession session) throws SQLException{
		Employee employee = (Employee) session.getAttribute("globle_user");
		workflowService.completeTask(taskId, leave.getId(), employee.getName(),comment, outSequenceFlow);
		return "redirect:listTask.do";
	}
	
	// 查看历史的批注信息
	@RequestMapping(value = "viewRecord")
	public ModelAndView viewRecord(int leaveId) throws SQLException{
		//1：使用请假单ID，查询请假单对象，将对象放置到栈顶，支持表单回显
		Leave leave = leaveService.findLeaveById(leaveId);
		//2：使用请假单ID，查询历史的批注信息
		List<Comment> commentList = workflowService.findCommentByLeaveId(leaveId);
		ModelAndView mv = new ModelAndView("workflow/task_record");
		mv.addObject("leave", leave);
		mv.addObject("commentList", commentList);
		return mv;
	}
	
	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 */
	@RequestMapping(value = "viewCurrentImage")
	public ModelAndView viewCurrentImage(String taskId){
		/**一：查看流程图*/
		//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = workflowService.findProcessDefinitionByTaskId(taskId);
		/**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
		Map<String, Object> map = workflowService.findCoordingByTask(taskId);
		ModelAndView mv = new ModelAndView("workflow/task_image");
		mv.addObject("pd", pd);
		mv.addObject("acs", map);
		return mv;
	}
}
