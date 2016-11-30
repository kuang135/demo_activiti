package com.ceair.activiti.java.p3_instance;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/*
 * 	act_ru_execution -- 正在执行的执行对象表
 * 		ID_: 执行对象id
 * 		PROC_INST_ID_: 流程实例id
 * 			如果是单例流程(没有分支和聚合)，执行对象id和流程实例id是相同的
 * 			一个流程只有一个流程实例，可以有多个执行对象
 * 		PROC_DEF_ID_: 流程定义id
 * 		ACT_ID_: 当前活动的id
 * 	act_hi_procinst -- 流程实例的历史表
 * 
 * 	act_ru_task -- 正在执行的任务表(节点是UserTask时，该表中才存在数据)
 * 		ID_: 任务id
 * 		EXECUTION_ID_: 执行对象id
 * 		PROC_INST_ID_：流程实例id
 * 		PROC_DEF_ID_: 流程定义id
 * 	act_hi_taskinst -- 任务历史表(节点是UserTask时，该表中才存在数据)
 * 	act_hi_actinst -- 所有活动节点的历史表(包括开始和结束)
 * 		
 */
public class I1_Instance {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process2.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process2.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程2")
					.addInputStream("process2.bpmn", inBpmn)
					.addInputStream("process2.png", inPng)
					.deploy();
	}
	
	
	//启动流程实例，表act_ru_execution
	@Test
	public void start() {
		String processDefinitionKey="process2_id"; //bpmn的properties视图中process的Id
		//根据流程定义的key启动流程(按最新版本启动)
		ProcessInstance processInstance = processEngine.getRuntimeService()
					.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程启动完成！：流程实例id："+processInstance.getProcessInstanceId());
		System.out.println("流程启动完成！：流程定义id："+processInstance.getProcessDefinitionId());
	}
	
	//查询当前办理人的个人任务，表act_ru_task
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
				System.out.println("任务名称："+task.getName()); //task节点的name属性
				System.out.println("任务办理人："+task.getAssignee());
				System.out.println("任务创建时间："+task.getCreateTime());
				System.out.println("任务办理经历的时间："+task.getDueDate());
				System.out.println("流程执行对象ID："+task.getExecutionId());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("流程定义ID："+task.getProcessDefinitionId());
			}
		}
	}
	
	
	//张三完成办理人的个人任务(手动指定一个一个完成)
	@Test
	public void zhangSanCompleteTask() {
		String taskId = "40004";//正在执行的任务ID
		processEngine.getTaskService()
				.complete(taskId);//使用任务ID完成任务
	}
	
	@Test
	public void queryLisiTask() {
		String  assignee = "李四";//指定任务办理人，task节点的assignee属性
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
				.createTaskQuery()//创建任务查询对象
				/**查询条件（where部分）*/
				.taskAssignee(assignee)//指定个人任务查询，指定办理人
//				.taskCandidateUser(candidateUser)//组任务的办理人查询
//				.executionId(executionId)//使用执行对象ID查询
//				.processInstanceId(processInstanceId)//使用流程实例ID查询
//				.processDefinitionId(processDefinitionId)//使用流程定义ID查询
				/**排序*/
				.orderByTaskCreateTime().asc()//使用创建时间的升序排列
				/**返回结果集*/
//				.singleResult()//返回惟一结果集
//				.count()//返回结果集的数量
//				.listPage(firstResult, maxResults);//分页查询
				.list();//返回列表
		if(null != list && list.size() > 0) { //遍历
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
				System.out.println("任务名称："+task.getName()); //task节点的name属性
				System.out.println("任务办理人："+task.getAssignee());
				System.out.println("任务创建时间："+task.getCreateTime());
				System.out.println("任务办理经历的时间："+task.getDueDate());
				System.out.println("流程执行对象ID："+task.getExecutionId());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("流程定义ID："+task.getProcessDefinitionId());
			}
		}
	}
	
	
	
	/*查询流程状态(判断流程正在执行,还是结束)
    1.从act_ru_execution查：如果没有对应的数据，就是流程结束。
    2.从act_hi_procinst中查：查end时间*/
    @Test
    public void queryInstanceStatus() {
    	String processInstanceId="40001";//流程实例ID
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
	
    //李四完成办理人的个人任务(手动指定一个一个完成)
	@Test
	public void liSiCompleteTask() {
		String taskId = "42502";//正在执行的任务ID
		processEngine.getTaskService()
				.complete(taskId);//使用任务ID完成任务
	}
    
}
