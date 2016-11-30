package com.ceair.activiti.java.p7_exclusiveGateway;

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
 * 		排他网关的默认连线的可以设置为某个连线的id
 * 		设置连线的属性：	排他网关设置默认的连线
 * 					${money >= 500 && money <= 1000}
 * 					${money > 1000}					
 */
public class G1_ExclusiveGateway {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process5.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process5.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程5")
					.addInputStream("process5.bpmn", inBpmn)
					.addInputStream("process5.png", inPng)
					.deploy();
	}
	
	//启动流程
	@Test
	public void start() {
		String processDefinitionKey="process5_id"; //bpmn的properties视图中process的Id
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
	
	//张三完成任务,并设置报销的money
	@Test
	public void zhangsanCompleteTask() {
		String taskId = "115004";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("money", 100);
		processEngine.getTaskService().complete(taskId, variables);
	}
	
	//查询李四的任务ID，排他网关走了默认的连线
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
	
	//李四获取流程变量，完成任务 
	@Test
	public void lisiCompleteTask() {
		String taskId = "117504";
		TaskService taskService = processEngine.getTaskService();
		Integer money = (Integer) taskService.getVariable(taskId, "money");
		taskService.complete(taskId);
		System.out.println("money：" + money);
	}
	
}
