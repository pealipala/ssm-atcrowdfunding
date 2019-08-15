package com.pealipala.listener;

import com.pealipala.bean.Permission;
import com.pealipala.manager.service.PermissionService;
import com.pealipala.utils.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemStartListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //1 服务器启动将上下文路径放在里面
        ServletContext application = servletContextEvent.getServletContext();
        String path = application.getContextPath();
        application.setAttribute("APP_PATH",path);
        //2 加载所有许可路径
        ApplicationContext ioc= WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        List<Permission> permissions = permissionService.queryAllPermission();
        Set<String> allUris=new HashSet<>();
        for (Permission list:permissions) {
            allUris.add("/"+list.getUrl());
        }
        application.setAttribute(Const.ALL_URI,allUris);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
