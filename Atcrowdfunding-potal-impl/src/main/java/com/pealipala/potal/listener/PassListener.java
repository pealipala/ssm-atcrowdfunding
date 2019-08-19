package com.pealipala.potal.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class PassListener implements ExecutionListener {
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("实名认证审核通过");
    }
}
