package com.pealipala.interceptor;

import com.pealipala.bean.User;
import com.pealipala.utils.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Set<String> uri=new HashSet<>();
        //设置不需要拦截的uri
        uri.add("/login.htm");
        uri.add("/reg.htm");
        uri.add("/doReg.do");
        uri.add("/logout.do");
        uri.add("/doLogin.do");
        uri.add("/index.htm");
        String servletPath = request.getServletPath();
        if (uri.contains(servletPath)){
            return true;
        }
        //取session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        if (user!=null){
            return true;
        }else{
            response.sendRedirect(request.getContextPath()+"/login.htm");
            return false;
        }
    }
}
