package com.ceair.activiti.java.p4_variable;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


/* 
 *	流程变量的作用： act_ru_variable
 *		1.传递业务参数 
 *	流程变量类型：
 *		1.基本类型
 *		2.对象类型，序列化
 */
public class V1_Variable {
	
	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	
	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process3.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process3.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程3")
					.addInputStream("process3.bpmn", inBpmn)
					.addInputStream("process3.png", inPng)
					.deploy();
	}
	
	//启动流程
	@Test
	public void start() {
		String processDefinitionKey="process3_id"; //bpmn的properties视图中process的Id
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
	
	
	/*
	 * 张三设置流程变量
	 * setVariableLocal是和当前的任务绑定,当任务完成,该变量就在正在执行的变量表中就消失了
	 * setVariable是和流程实例绑定,在全流程运行过程中都管用,一般用这个
	 */
	@Test
	public void setVariables(){
		String taskId = "50004";
		TaskService taskService = processEngine.getTaskService();
		taskService.setVariableLocal(taskId, "请假天数", 5);//与任务ID绑定
		taskService.setVariable(taskId, "请假日期", new Date());
		taskService.setVariable(taskId, "请假原因", "约会");
		System.out.println("设置流程变量成功！");
	}
	
	//张三完成任务
	@Test
	public void zhangsanCompleteTask() {
		String taskId = "50004";
		processEngine.getTaskService().complete(taskId);
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
	
	//李四获取流程变量
	@Test
	public void getVariables(){
		String taskId = "55002";
		TaskService taskService = processEngine.getTaskService();
		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");//null，因为绑定任务
		Date date = (Date) taskService.getVariable(taskId, "请假日期");
		String resean = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+resean);
	}
	
	//李四完成任务
	@Test
	public void lisiCompleteTask() {
		String taskId = "55002";
		processEngine.getTaskService().complete(taskId);
	}
	
	
	
	/**
	 * 模拟设置和获取流程变量的场景
	 */
	@SuppressWarnings("unused")
	@Test
	public void setAndGetVariables(){
		/**与流程实例，执行对象（正在执行）*/
		RuntimeService runtimeService = processEngine.getRuntimeService();
		/**与任务（正在执行）*/
		TaskService taskService = processEngine.getTaskService();
		
		/**设置流程变量*/
//		runtimeService.setVariable(executionId, variableName, value)//表示使用执行对象ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
//		runtimeService.setVariables(executionId, variables)//表示使用执行对象ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
		
//		taskService.setVariable(taskId, variableName, value)//表示使用任务ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
//		taskService.setVariables(taskId, variables)//表示使用任务ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
		
//		runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时，可以设置流程变量，用Map集合
//		taskService.complete(taskId, variables)//完成任务的同时，设置流程变量，用Map集合
		
		/**获取流程变量*/
//		runtimeService.getVariable(executionId, variableName);//使用执行对象ID和流程变量的名称，获取流程变量的值
//		runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//		runtimeService.getVariables(executionId, variableNames);//使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
		
//		taskService.getVariable(taskId, variableName);//使用任务ID和流程变量的名称，获取流程变量的值
//		taskService.getVariables(taskId);//使用任务ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//		taskService.getVariables(taskId, variableNames);//使用任务ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
		
	}
	
	
	/**查询流程变量的历史表*/
	@Test
	public void findHistoryProcessVariables(){
		List<HistoricVariableInstance> list = processEngine.getHistoryService()//
						.createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
						.variableName("请假天数")
						.list();
		if(list != null && list.size() > 0) {
			for(HistoricVariableInstance hvi:list) {
				System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());
				System.out.println("-----------------------------------------------------------");
			}
		}
	}

}
