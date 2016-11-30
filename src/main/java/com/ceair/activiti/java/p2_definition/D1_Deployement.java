package com.ceair.activiti.java.p2_definition;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

/*
 * act_re_deployment -- 部署对象表
 * act_re_procdef	-- 流程定义表
 * act_ge_bytearray -- 资源文件表(.bpmn和.png)
 * act_ge_property -- 主键生成策略表
 */
public class D1_Deployement {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	//流程部署，通过.bpmn部署，还可以通过.zip部署
	//act_re_deployment,act_re_procdef
	@Test
	public void deployment() {
		InputStream inBpmn = this.getClass().getResourceAsStream("process1.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("process1.png");
		processEngine.getRepositoryService()
					.createDeployment()
					.name("流程1")
					.addInputStream("process1.bpmn", inBpmn)
					.addInputStream("process1.png", inPng)
					.deploy();
	}
	
	//流程部署查询，act_re_deployment 表
	@Test
	public void queryDeployment() {
		List<Deployment> list = processEngine.getRepositoryService()
								.createDeploymentQuery()
								.list();
		if(list!=null && list.size() > 0) {
			for (Deployment dm : list) {
				System.out.println("部署id：" + dm.getId());
				System.out.println("部署名称：" + dm.getName());
				System.out.println("部署时间：" + dm.getDeploymentTime());
				System.out.println("-----------------------------------");
			}
		}
	}

	
	//删除流程部署实例
	@Test
	public void delete() {
		String deploymentId="2501";
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.deleteDeployment(deploymentId);//根据部署id删除部署对象数据,只能删除没有启动或者已经结束的流程
		//repositoryService.deleteDeployment(deploymentId, true);//删除部署对象数据,级联删除,可以删除正则执行的流程
	}
	
}
