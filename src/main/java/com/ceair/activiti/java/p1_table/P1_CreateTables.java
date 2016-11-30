package com.ceair.activiti.java.p1_table;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class P1_CreateTables {
	
	//建表初始化
	@Test
	public void createTables() {
		//加载classpath下的 activiti.cfg.xml
		//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();
		System.out.println(processEngine.getName());
	}
	

}
