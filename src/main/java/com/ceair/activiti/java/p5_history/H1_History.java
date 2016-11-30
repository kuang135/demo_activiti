package com.ceair.activiti.java.p5_history;


import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class H1_History {
	
	
	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	/**查询历史流程实例*/
	@Test
	public void findHistoryProcessInstance(){
		String processInstanceId = "40001";
		HistoricProcessInstance hpi = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
						.createHistoricProcessInstanceQuery()//创建历史流程实例查询
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.orderByProcessInstanceStartTime().asc()
						.singleResult();
		System.out.println(hpi.getId()+"    "+hpi.getProcessDefinitionId()+"    "+hpi.getStartTime()+"    "+hpi.getEndTime()+"     "+hpi.getDurationInMillis());
	}
	
	/**查询历史活动*/
	@Test
	public void findHistoryActiviti(){
		String processInstanceId = "40001";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()//
						.createHistoricActivityInstanceQuery()//创建历史活动实例的查询
						.processInstanceId(processInstanceId)//
						.orderByHistoricActivityInstanceStartTime().asc()//
						.list();
		if(list!=null && list.size()>0){
			for(HistoricActivityInstance hai:list){
				System.out.println(hai.getId()+"   "+hai.getProcessInstanceId()+"   "+hai.getActivityType()+"  "+hai.getStartTime()+"   "+hai.getEndTime()+"   "+hai.getDurationInMillis());
				System.out.println("#####################");
			}
		}
	}
	
	/**查询历史任务*/
	@Test
	public void findHistoryTask(){
		String processInstanceId = "40001";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()//与历史数据（历史表）相关的Service
						.createHistoricTaskInstanceQuery()//创建历史任务实例查询
						.processInstanceId(processInstanceId)//
						.orderByTaskCreateTime().asc()
						.list();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				System.out.println(hti.getId()+"    "+hti.getName()+"    "+hti.getProcessInstanceId()+"   "+hti.getStartTime()+"   "+hti.getEndTime()+"   "+hti.getDurationInMillis());
				System.out.println("################################");
			}
		}
	}
	
	/**查询历史流程变量*/
	@Test
	public void findHistoryProcessVariables(){
		//String processInstanceId = "40001";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()//
						.createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
						//.processInstanceId(processInstanceId)//
						.variableName("请假天数")
						.list();
		if(list!=null && list.size()>0){
			for(HistoricVariableInstance hvi:list){
				System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());
				System.out.println("###############################################");
			}
		}
	}
	
}
