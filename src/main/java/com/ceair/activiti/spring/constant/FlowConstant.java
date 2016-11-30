package com.ceair.activiti.spring.constant;

public class FlowConstant {

	//流程变量名，指定当前任务的办理人
	public static final String TASK_ASSIGNEE = "inputUser";
	//流程变量名，指定当前连线走向的变量名
	public static final String SEQUENCEFLOW_CONDITION = "outcome";
	//流程变量名，用来关联业务的变量名
	public static final String BUSINESS_KEY = "BusinessKey";
	//连线的名称,申请
	public static final String SEQUENCEFLOW_APPLY = "提交";
	//连线的名称,批准
	public static final String SEQUENCEFLOW_APPROVE = "批准";
	//连线的名称，驳回
	public static final String SEQUENCEFLOW_REJECT = "驳回";
}
