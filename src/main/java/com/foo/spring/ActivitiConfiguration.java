package com.foo.spring;

import com.foo.spring.upgrade.UpgradeConfiguration;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by wangding on 23/02/2017.
 */
@Configuration
@DependsOn(UpgradeConfiguration.FLYWAY)
public class ActivitiConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${activiti.asyncExecutor.corePoolSize}")
    private int asyncExecutorCorePoolSize;

    @Value("${activiti.asyncExecutor.maxPoolSize}")
    private int asyncExecutorMaxPoolSize;

    @Value("${activiti.asyncExecutor.threadPoolQueueSize}")
    private int asyncExecutorThreadPoolQueueSize;

    @Bean
    ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, @Qualifier("simpleJpaTxManager") PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        processEngineConfiguration.setJobExecutorActivate(false);
        processEngineConfiguration.setAsyncExecutorEnabled(true);
        processEngineConfiguration.setAsyncExecutorActivate(true);
        processEngineConfiguration.setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode.MIXED);
        processEngineConfiguration.setAsyncExecutorCorePoolSize(asyncExecutorCorePoolSize);
        processEngineConfiguration.setAsyncExecutorMaxPoolSize(asyncExecutorMaxPoolSize);
        processEngineConfiguration.setAsyncExecutorThreadPoolQueueSize(asyncExecutorThreadPoolQueueSize);
        processEngineConfiguration.setAsyncExecutorDefaultTimerJobAcquireWaitTime(1 * 1000);
        return processEngineConfiguration;
    }

    @Bean
    ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    FormattingConversionService spelConversionService() {
        FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    SpelExpressionParser spelExpressionParser() {
        return new SpelExpressionParser();
    }

}
