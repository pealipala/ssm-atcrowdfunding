package com.pealipala.interceptor;

import com.pealipala.bean.Permission;
import com.pealipala.manager.service.PermissionService;
import com.pealipala.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Set<String> allUris = (Set<String>) request.getSession().getServletContext().getAttribute(Const.ALL_URI);
        if (allUris.contains(request.getServletPath())){
            //3 取当前用户拥有的权限路径
            Set<String> myUris = (Set<String>) request.getSession().getAttribute(Const.MY_URI);
            if (myUris.contains(request.getServletPath())){
                return true;
            }else {
                response.sendRedirect(request.getContextPath()+"/login.htm");
                return false;
            }
        }else {
            return true;
        }
    }
}
