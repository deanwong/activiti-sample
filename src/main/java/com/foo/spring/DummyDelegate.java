package com.foo.spring;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangding on 13/02/2017.
 */
@Transactional(transactionManager = "simpleTxManager", propagation = Propagation.REQUIRES_NEW)
@Service("dummyDelegate")
public class DummyDelegate implements JavaDelegate {

    private static Logger LOGGER = LoggerFactory.getLogger(DummyDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("start execute dummy action [{}] ...", execution.getCurrentActivityName());
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        LOGGER.info("end execute dummy action [{}] ...", execution.getCurrentActivityName());
    }
}