package activity;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActivity {


    ApplicationContext ioc=new ClassPathXmlApplicationContext("spring/spring-*.xml");
    ProcessEngine processEngine= (ProcessEngine) ioc.getBean("processEngine");
    /**
     * 创建23张表
     * @author : yechaoze
     * @date : 2019/8/16 14:41
     * @return : void
     */
    @Test
    public void test01(){
        System.out.println("processEngine="+processEngine);

    }

    /**
     * 部署流程定义
     * @author : yechaoze
     * @date : 2019/8/16 14:42
     * @return : void
     */
    @Test
    public void test02(){
        System.out.println("processEngine="+processEngine);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("Myprocess1.bpmn").deploy();
        System.out.println("deploy="+deploy);
    }

    /**
     * 创建流程实例
     * @author : yechaoze
     * @date : 2019/8/16 17:14
     * @return : void
     */
    @Test
    public void test03(){
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("processInstance="+processInstance);
    }

}
