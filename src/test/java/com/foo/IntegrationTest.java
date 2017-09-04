package com.foo;


import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangding on 17/05/2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class IntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    private static String processDefId;

    @Test(singleThreaded = true)
    public void test001Process() {
        String tenantId = "1";
        String resourceName = "DeadLock-NoTimer" + "." + BpmnDeployer.BPMN_RESOURCE_SUFFIXES[0];
        Deployment deployment = processEngine.getRepositoryService().createDeployment().addClasspathResource("process/" + resourceName).tenantId(tenantId).deploy();
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).processDefinitionTenantId(tenantId).singleResult();
        processDefId = processDefinition.getId();
    }

    @Test(invocationCount = 1000, threadPoolSize = 20, timeOut = 300000)
//    @Test(singleThreaded = true)
    public void test002Run() {
        System.out.printf("Thread Id : %s%n", Thread.currentThread().getId());
        Map<String, Object> variables = new HashMap<>();
        variables.put("contact", "John Snow");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefId, variables);
    }

    @Test(singleThreaded = true)
    public void test003End() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(20));
    }

}
