package com.pealipala.potal.listener;

import com.pealipala.bean.Member;
import com.pealipala.potal.service.MemberService;
import com.pealipala.potal.service.TicketService;
import com.pealipala.utils.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

public class RefuseListener implements ExecutionListener {
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Integer memberid = (Integer) delegateExecution.getVariable("memberid");
        //获取IOC容器 通过自定义工具类 实现spring接口 以接口注入方式获取ioc容器对象
        ApplicationContext applicationContext = ApplicationContextUtils.applicationContext;
        TicketService ticketService = applicationContext.getBean(TicketService.class);
        MemberService memberService = applicationContext.getBean(MemberService.class);
        //更新会员status 1 -> 0 表示未完成验证
        Member member = memberService.getMemberById(memberid);
        member.setAuthstatus("0");
        memberService.updateAuthStatus(member);
        //更新ticket status 0 -> 1 表示流程结束 直接sql语句写死
        ticketService.updateStatus(member);
    }
}
