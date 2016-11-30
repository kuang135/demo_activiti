package com.ceair.activiti.java.p8_parrallelGateway;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


/*
 * 		分支流程必须汇聚后,才能继续向下走
 * 			分支(fork)
 *	  		汇聚(join)
 */
public class G1_ParrallelGateway {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process6.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process6.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程6")
					.addInputStream("process6.bpmn", inBpmn)
					.addInputStream("process6.png", inPng)
					.deploy();
	}
	
	/*
	 * 	启动流程
	 * 		启动后 act_ru_execution表有三条数据
	 * 			一个流程实例，两个执行对象
	 * 		act_ru_task中有两条记录
	 * 			相同的流程实例id，不同的执行对象id
	 */
	@Test
	public void start() {
		String processDefinitionKey="process6_id"; //bpmn的properties视图中process的Id
		//根据流程定义的key启动流程(按最新版本启动)
		ProcessInstance processInstance = processEngine.getRuntimeService()
					.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程启动完成！：流程实例id："+processInstance.getProcessInstanceId());
		System.out.println("流程启动完成！：流程定义id："+processInstance.getProcessDefinitionId());
	}
	
	//查看付款的任务id
	@Test
	public void queryTask1() {
		String  name = "付款";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskName(name)
						.list();
		if(null != list && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//完成付款
	@Test
	public void completeTask1() {
		String taskId = "125007";
		processEngine.getTaskService().complete(taskId);
	}
	
	//查看发货的任务id
	@Test
	public void queryTask2() {
		String  name = "发货";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskName(name)
						.list();
		if(null != list && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//完成发货
	@Test
	public void completeTask2() {
		String taskId = "125010";
		processEngine.getTaskService().complete(taskId);
	}
	
	//查看收款的任务id
	@Test
	public void queryTask3() {
		String  name = "收款";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskName(name)
						.list();//查询列表
		if(null != list && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//完成收款
	@Test
	public void completeTask3() {
		String taskId = "127502";
		processEngine.getTaskService().complete(taskId);
	}
	
	//查看收货的任务id
	@Test
	public void queryTask4() {
		String  name = "收货";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskName(name)
						.list();//查询列表
		if(null != list && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID："+task.getId());
			}
		}
	}
	
	//完成收货
	@Test
	public void completeTask4() {
		String taskId = "130002";
		processEngine.getTaskService().complete(taskId);
	}
	
	
}
