package com.ceair.activiti.java.p9_user_group;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


/*
 * 	个人任务及三种分配方式：
    	1：在taskProcess.bpmn中直接写 assignee="张三丰"
    	2：在taskProcess.bpmn中写 assignee="#{userId}"，变量的值要是String的。
    		使用流程变量指定办理人
    	3，使用TaskListener接口，要使类实现该接口，在类中定义：
         	@Override
			public void notify(DelegateTask delegateTask) {
				//指定个人任务的办理人，也可以指定组任务的办理人
				//个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
				String assignee = "张无忌";
				delegateTask.setAssignee(assignee);
			}

    
 	使用任务ID和办理人重新指定办理人：
     	processEngine.getTaskService()
                     .setAssignee(taskId, userId);

 */
public class U1_User {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process7.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process7.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程7")
					.addInputStream("process7.bpmn", inBpmn)
					.addInputStream("process7.png", inPng)
					.deploy();
	}
	
	/*
	 * 	设置流程变量，启动流程实例
	 */
	@Test
	public void start() {
		String processDefinitionKey="process7_id"; //bpmn的properties视图中process的Id
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId", "张三");
		ProcessInstance processInstance = processEngine.getRuntimeService()
					.startProcessInstanceByKey(processDefinitionKey, variables);
		System.out.println("流程启动完成！：流程实例id："+processInstance.getProcessInstanceId());
		System.out.println("流程启动完成！：流程定义id："+processInstance.getProcessDefinitionId());
	}
	
	/**查询当前人的个人任务*/
	@Test
	public void findTaskByUser(){
		String assignee = "张三";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskAssignee(assignee)//指定个人任务查询，指定办理人
						.list();//返回列表
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	//可以分配个人任务从一个人到另一个人（认领任务）
	@Test
	public void setAssigneeTask(){
		String taskId = "150005";
		String userId = "李四";
		processEngine.getTaskService()//
					.setAssignee(taskId, userId);
	}
	
	
	@Test
	public void findTaskByUser2(){
		String assignee = "李四";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskAssignee(assignee)//指定个人任务查询，指定办理人
						.list();//返回列表
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	/**完成任务*/
	@Test
	public void completeTask(){
		String taskId = "150005";
		processEngine.getTaskService()
					.complete(taskId);
	}
	
	
}
