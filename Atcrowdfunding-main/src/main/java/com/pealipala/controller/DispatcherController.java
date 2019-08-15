package com.pealipala.controller;

import com.pealipala.bean.Permission;
import com.pealipala.bean.User;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/reg")
    public String reg(){ return "reg"; }

    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/logout")
    public String logOut(HttpSession session){
        //销毁session对象
        session.invalidate();
        return "redirect:/index.htm";
    }

//    //同步请求
//    @RequestMapping("/doLogin")
//    public String doLogin(String loginacct, String userpswd, String type, HttpSession session){
//        Map<String,Object> paramMap=new HashMap<String,Object>();
//        paramMap.put("loginacct",loginacct);
//        paramMap.put("userpswd",userpswd);
//        paramMap.put("type",type);
//        User user=userService.login(paramMap);
//        session.setAttribute(Const.LOGIN_USER,user);
//        return "redirect:/main.htm";
//    }

    //异步请求
    //将返回结果以json串的形式通过流的方式传给浏览器
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct,String userpswd,String type,HttpSession session){

        AjaxResult result = new AjaxResult();

        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("loginacct", loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type", type);

            User user = userService.login(paramMap);
            //---------------------
            //加载当前登录用户拥有的权限
            List<Permission> list=userService.queryPermissionById(user.getId());
            Permission permissionRoot=null;
            Map<Integer,Permission> map=new HashMap<>();
            Set<String> uris=new HashSet<>();
            for (Permission permissionList:list) {
                map.put(permissionList.getId(),permissionList);
                uris.add("/"+permissionList.getUrl());
            }
            session.setAttribute(Const.MY_URI,uris);
            for (Permission permission:list) {
                Permission child=permission;
                if (permission.getPid()==null){
                    permissionRoot=child;
                }else {
                    Permission parent = map.get(permission.getPid());
                    parent.getChildren().add(permission);
                }
            }
            session.setAttribute("permissionRoot",permissionRoot);
            //---------------------
            session.setAttribute(Const.LOGIN_USER, user);
            result.setSuccess(true);
            // {"success":true}
        } catch (Exception e) {
            result.setMessage("登录失败!");
            e.printStackTrace();
            result.setSuccess(false);
            // {"success":false,"message":"登录失败!"}
            //throw e ;
        }

        return result ;
    }

}
