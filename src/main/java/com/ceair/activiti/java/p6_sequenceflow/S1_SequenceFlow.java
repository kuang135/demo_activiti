package com.ceair.activiti.java.p6_sequenceflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


/*
 * 	通过流程变量来设置连线的走向
 * 		设置连线的属性：	#{message=='审核通过'}
 * 					#{message=='审核不通过'}
 */
public class S1_SequenceFlow {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process4.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process4.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程4")
					.addInputStream("process4.bpmn", inBpmn)
					.addInputStream("process4.png", inPng)
					.deploy();
	}
	
	//启动流程
	@Test
	public void start() {
		String processDefinitionKey="process4_id"; //bpmn的properties视图中process的Id
		//根据流程定义的key启动流程(按最新版本启动)
		ProcessInstance processInstance = processEngine.getRuntimeService()
					.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程启动完成！：流程实例id："+processInstance.getProcessInstanceId());
		System.out.println("流程启动完成！：流程定义id："+processInstance.getProcessDefinitionId());
	}
	
	//查询张三的任务ID
	@Test
	public void queryZhangSanTask() {
		String  assignee = "张三";//指定任务办理人，task节点的assignee属性
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定任务办理人
						.list();//查询列表
		if(null != list && list.size() > 0) { //遍历
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//张三完成任务,并设置流程变量
	@Test
	public void zhangsanCompleteTask() {
		String taskId = "82504";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("请假天数", 5);
		variables.put("请假原因", "约会");
		processEngine.getTaskService().complete(taskId, variables);
	}
	
	//查询李四的任务ID
	@Test
	public void queryLisiTask() {
		String  assignee = "李四";//指定任务办理人，task节点的assignee属性
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定任务办理人
						.list();//查询列表
		if(null != list && list.size() > 0) { //遍历
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//李四完成任务, 获取流程变量，通过判断请假天数，来设置流程变量，决定流程的走向
	@Test
	public void lisiCompleteTask() {
		String taskId = "85004";
		TaskService taskService = processEngine.getTaskService();
		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		String resean = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假原因："+resean);
		Map<String, Object> variables = new HashMap<String, Object>();
		if (days < 5) {
			variables.put("message", "审核通过");//#{message=='审核通过'}
		} else {
			variables.put("message", "审核不通过");//#{message=='审核不通过'}
		}
		taskService.complete(taskId, variables);
	}
	
	//查询流程状态
	@Test
    public void queryInstanceStatus() {
    	String processInstanceId="82501";//流程实例ID
    	//从正在执行的执行对象表，获取流程实例对象
    	ProcessInstance processInstance = processEngine.getRuntimeService()//只要查询正在执行的东东，都使用runtimeService
							    		.createProcessInstanceQuery()
							    		.processInstanceId(processInstanceId)
							    		.singleResult();
    	//判断是否有流程实例对象
    	if(processInstance != null) {
    		System.out.println("流程正在执行！");
    	}else{
    		System.out.println("流程已经结束！");
    	}
    }
	
	//查询张三的任务ID
	@Test
	public void queryZhangSan2Task() {
		String  assignee = "张三";//指定任务办理人，task节点的assignee属性
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定任务办理人
						.list();//查询列表
		if(null != list && list.size() > 0) { //遍历
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//张三完成任务,并重新设置流程变量
	@Test
	public void zhangsanCompleteTask2() {
		String taskId = "87503";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("请假天数", 3);
		variables.put("请假原因", "约会");
		processEngine.getTaskService().complete(taskId, variables);
	}
	
	//查询李四的任务ID
	@Test
	public void queryLisiTask2() {
		String  assignee = "李四";//指定任务办理人，task节点的assignee属性
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定任务办理人
						.list();//查询列表
		if(null != list && list.size() > 0) { //遍历
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//李四完成任务, 获取流程变量，通过判断请假天数，来设置流程变量，决定流程的走向
	@Test
	public void lisiCompleteTask2() {
		String taskId = "90002";
		TaskService taskService = processEngine.getTaskService();
		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		String resean = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假原因："+resean);
		Map<String, Object> variables = new HashMap<String, Object>();
		if (days < 5) {
			variables.put("message", "审核通过");//#{message=='审核通过'}
		} else {
			variables.put("message", "审核不通过");//#{message=='审核不通过'}
		}
		taskService.complete(taskId, variables);
	}
	
	//查询流程状态
	@Test
    public void queryInstanceStatus2() {
    	String processInstanceId="82501";//流程实例ID
    	//从正在执行的执行对象表，获取流程实例对象
    	ProcessInstance processInstance = processEngine.getRuntimeService()//只要查询正在执行的东东，都使用runtimeService
							    		.createProcessInstanceQuery()
							    		.processInstanceId(processInstanceId)
							    		.singleResult();
    	//判断是否有流程实例对象
    	if(processInstance != null) {
    		System.out.println("流程正在执行！");
    	}else{
    		System.out.println("流程已经结束！");
    	}
    }
}
