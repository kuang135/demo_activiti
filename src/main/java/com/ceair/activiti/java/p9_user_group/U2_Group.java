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
 * 	组任务及三种分配方式：
	    1：在taskProcess.bpmn中直接写 candidate-users=“小A,小B,小C,小D"
	    2：在taskProcess.bpmn中写 candidate-users =“#{userIDs}”，变量的值要是String的。
 			使用流程变量指定办理人
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("userIDs", "大大,小小,中中");
    	3，使用TaskListener接口，使用类实现该接口，在类中定义：
            //添加组任务的用户
			delegateTask.addCandidateUser(userId1);
			delegateTask.addCandidateUser(userId2);
			
		组任务分配给个人任务（认领任务）：
		     processEngine.getTaskService().claim(taskId, userId);
		个人任务分配给组任务：
		     processEngine.getTaskService().setAssignee(taskId, null);
		向组任务添加人员：
		     processEngine.getTaskService().addCandidateUser(taskId, userId);
		向组任务删除人员：
		     processEngine.getTaskService().deleteCandidateUser(taskId, userId);
		个人任务和组任务存放办理人对应的表：
		act_ru_identitylink表存放任务的办理人，包括个人任务和组任务，表示正在执行的任务
		act_hi_identitylink表存放任务的办理人，包括个人任务和组任务，表示历史任务
		区别在于：如果是个人任务TYPE的类型表示participant（参与者）
				 如果是组任务TYPE的类型表示candidate（候选者）和participant（参与者）

 */
public class U2_Group {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//部署流程
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process8.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process8.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程8")
					.addInputStream("process8.bpmn", inBpmn)
					.addInputStream("process8.png", inPng)
					.deploy();
	}
	
	/*
	 * 	设置流程变量，启动流程实例
	 */
	@Test
	public void start() {
		String processDefinitionKey="process8_id"; //bpmn的properties视图中process的Id
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userIds", "张三,李四");
		ProcessInstance processInstance = processEngine.getRuntimeService()
					.startProcessInstanceByKey(processDefinitionKey, variables);
		System.out.println("流程启动完成！：流程实例id："+processInstance.getProcessInstanceId());
		System.out.println("流程启动完成！：流程定义id："+processInstance.getProcessDefinitionId());
	}
	
	
	/**查询当前人的组任务*/
	@Test
	public void findGroupTask(){
		String candidateUser = "张三";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskCandidateUser(candidateUser)
						.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("------------------------------------------------------");
			}
		}
	}
	
	//**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
	@Test
	public void claim(){
		String taskId = "162505";
		//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		String userId = "张三";
		processEngine.getTaskService()
					.claim(taskId, userId);
	}
	
	
	@Test
	public void findUserTask(){
		String assignee = "张三";
		List<Task> list = processEngine.getTaskService()
						.createTaskQuery()
						.taskAssignee(assignee)
						.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("-------------------------------------------");
			}
		}
	}
	
	/*
	 * 将个人任务回退到组任务，前提，之前一定是个组任
	 * 回退之后，组中的其他成员才能查	 
	 */
	@Test
	public void setAssigee(){
		String taskId = "162505";
		processEngine.getTaskService()//
					.setAssignee(taskId, null);
	}
	
	//**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
	@Test
	public void claim2(){
		String taskId = "162505";
		//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		String userId = "张阿三";
		processEngine.getTaskService()
					.claim(taskId, userId);
	}
	
	/**完成任务*/
	@Test
	public void completeTask(){
		String taskId = "162505";
		processEngine.getTaskService()
					.complete(taskId);
	}
	
	/* -------------------- 另外，在流程未完成时，可以添加和删除组 ------------------------------ */
	
	/**向组任务中添加成员*/
	@Test
	public void addGroupUser(){
		String taskId = "162505";
		String userId = "新增";
		processEngine.getTaskService()//
					.addCandidateUser(taskId, userId);
	}
	/**从组任务中删除成员*/
	@Test
	public void deleteGroupUser(){
		String taskId = "162505";
		String userId = "新增";
		processEngine.getTaskService()//
					.deleteCandidateUser(taskId, userId);
	}
	
	
}
