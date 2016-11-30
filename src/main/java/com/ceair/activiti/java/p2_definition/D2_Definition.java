package com.ceair.activiti.java.p2_definition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/*
 * act_re_deployment -- 部署对象表
 * act_re_procdef	-- 流程定义表
 * act_ge_bytearray -- 资源文件表(.bpmn和.png)
 * act_ge_property -- 主键生成策略表
 */
public class D2_Definition {

	//ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private ProcessEngine processEngine=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("java/activiti.cfg.xml").buildProcessEngine();

	
	//将流程部署两次
	@Test
	public void deploy2time() {
		D1_Deployement dp = new D1_Deployement();
		dp.deployment();
		dp.deployment();
	}
	

	//流程定义信息查询，act_re_procdef 表
	@Test
	public void queryProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()
				.createProcessDefinitionQuery()
				/**指定查询条件,where条件*/
//				.deploymentId(deploymentId)//使用部署对象ID查询
//				.processDefinitionId(processDefinitionId)//使用流程定义ID查询
//				.processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
//				.processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
				/**排序*/
				.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
//				.orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列
				/**返回的结果集*/
				.list();//返回一个集合列表，封装流程定义
//				.singleResult();//返回惟一结果集
//				.count();//返回结果集数量
//				.listPage(firstResult, maxResults);//分页查询
			
		if(list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				System.out.println("流程定义的ID：" + pd.getId()); //key：版本：随机生成数
				System.out.println("流程定义的key：" + pd.getKey()); //bpmn的properties视图中process的Id
				System.out.println("流程定义的名称：" + pd.getName()); //bpmn的properties视图中process的Name
				System.out.println("流程定义的版本：" + pd.getVersion());
				System.out.println("流程定义的bpmn：" + pd.getResourceName());
				System.out.println("流程定义的图片：" + pd.getDiagramResourceName());
				System.out.println("流程部署id：" + pd.getDeploymentId());
				System.out.println("---------------------------------------");
			}
		}
	}
	
	
	//查看流程图片
	@Test
	public void viewPic() throws IOException {
		String deploymentId = "7506";
		List<String> list = processEngine.getRepositoryService()
										.getDeploymentResourceNames(deploymentId);
		String resourceName = "";
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf(".png") > 0) {
					resourceName = name;
				}
			}
		}
		InputStream in = processEngine.getRepositoryService()
									.getResourceAsStream(deploymentId, resourceName);
		File file = new File("D:\\" + resourceName);
		FileUtils.copyInputStreamToFile(in, file);
	}
	
	//查询最新版本的流程定义
	@Test
	public void findLastVersionProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
						.createProcessDefinitionQuery()//
						.orderByProcessDefinitionVersion().asc()//使用流程定义的版本升序排列
						.list();
		/**
		 * Map<String,ProcessDefinition>
		  map集合的key：流程定义的key
		  map集合的value：流程定义的对象
		  map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
		 */
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		if (pdList != null && pdList.size() > 0) {
			for(ProcessDefinition pd:pdList){
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称:"+pd.getName());//对应bpmn文件中的name属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应bpmn文件中的id属性值
				System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:"+pd.getResourceName());
				System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID："+pd.getDeploymentId());
				System.out.println("-----------------------------------------------");
			}
		}	
	}
	
	//删除流程定义（删除key相同的所有不同版本的流程定义）
	@Test
	public void deleteProcessDefinitionByKey(){
		//流程定义的key
		String processDefinitionKey = "process1_id";
		//先使用流程定义的key查询流程定义，查询出所有的版本
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
						.createProcessDefinitionQuery()//
						.processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
						.list();
		//遍历，获取每个流程定义的部署ID
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				//获取部署ID
				String deploymentId = pd.getDeploymentId();
				processEngine.getRepositoryService()//
							.deleteDeployment(deploymentId, true);
			}
		}
	}
	
	
}
