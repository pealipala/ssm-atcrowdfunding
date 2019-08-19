package com.pealipala.controller;

import com.pealipala.bean.Member;
import com.pealipala.bean.Permission;
import com.pealipala.bean.User;
import com.pealipala.potal.service.MemberService;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.utils.MD5Util;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 判断是否直接登录
     * @author : yechaoze
     * @date : 2019/8/19 11:51
     * @param session :
     * @param request :
     * @return : java.lang.String
     */
    @RequestMapping("/login")
    public String login(HttpSession session,HttpServletRequest request){
        //判断是否需要自动登录
        //如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证

        boolean needLogin = true;
        String logintype = null ;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){ //如果客户端禁用了Cookie，那么无法获取Cookie信息

            for (Cookie cookie : cookies) {
                if("logincode".equals(cookie.getName())){
                    String logincode = cookie.getValue();
//                    System.out.println("获取到Cookie中的键值对"+cookie.getName()+"===== " + logincode);
//                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                    String[] split = logincode.split("&");
                    if(split.length == 3){
                        String loginacct = split[0].split("=")[1];
                        String userpwd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];
                        Map<String,Object> paramMap = new HashMap<String,Object>();
                        paramMap.put("loginacct", loginacct);
                        paramMap.put("userpswd", userpwd);
                        paramMap.put("type", logintype);

                        if("user".equals(logintype)){


                            User dbLogin = userService.login(paramMap);

                            if(dbLogin!=null){
                                session.setAttribute(Const.LOGIN_USER, dbLogin);
                                needLogin = false ;
                            }


                            //加载当前登录用户的所拥有的许可权限.

                            //User user = (User)session.getAttribute(Const.LOGIN_USER);

                            List<Permission> myPermissions = userService.queryPermissionById(dbLogin.getId()); //当前用户所拥有的许可权限

                            Permission permissionRoot = null;

                            Map<Integer,Permission> map = new HashMap<Integer,Permission>();

                            Set<String> myUris = new HashSet<String>(); //用于拦截器拦截许可权限

                            for (Permission innerpermission : myPermissions) {
                                map.put(innerpermission.getId(), innerpermission);

                                myUris.add("/"+innerpermission.getUrl());
                            }

                            session.setAttribute(Const.MY_URI, myUris);


                            for (Permission permission : myPermissions) {
                                //通过子查找父
                                //子菜单
                                Permission child = permission ; //假设为子菜单
                                if(child.getPid() == null ){
                                    permissionRoot = permission;
                                }else{
                                    //父节点
                                    Permission parent = map.get(child.getPid());
                                    parent.getChildren().add(child);
                                }
                            }


                            session.setAttribute("permissionRoot", permissionRoot);


                        }else if("member".equals(logintype)){


                            Member dbLogin = memberService.login(paramMap);

                            if(dbLogin!=null){
                                session.setAttribute(Const.LOGIN_MENBER, dbLogin);
                                needLogin = false ;
                            }
                        }

                    }
                }
            }
        }
        if(needLogin){
            return "login";
        }else{
            if("user".equals(logintype)){
                return "redirect:/main.htm";
            }else if("member".equals(logintype)){
                return "redirect:/member.htm";
            }
        }
        return "login";
    }

    @RequestMapping("/reg")
    public String reg(){ return "reg"; }

    @RequestMapping("/main")
    public String main(){
        return "main";
    }
    @RequestMapping("/member")
    public String member(){
        return "member/member";
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



    /**
     * 登录--异步请求
     * 将返回结果以json串的形式通过流的方式传给浏览器
     * @author : yechaoze
     * @date : 2019/8/18 23:34
     * @param loginacct :
     * @param userpswd :
     * @param type :
     * @param session :
     * @param rememberme :
     * @param response :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct, String userpswd, String type, HttpSession session, String rememberme, HttpServletResponse response){

        AjaxResult result = new AjaxResult();
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("loginacct", loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type", type);
            if ("user".equals(type)){
                User user = userService.login(paramMap);
                if ("1".equals(rememberme)){
                    String loginCode="\"loginacct="+user.getLoginacct()+"&userpwd="+user.getUserpswd()+"&logintype=user\"";
                    Cookie c=new Cookie("logincode",loginCode);
                    c.setMaxAge(60*60*24*14);
                    c.setPath("/");
                    response.addCookie(c);
                }
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
            }else if ("member".equals(type)){
                Member member = memberService.login(paramMap);
                session.setAttribute(Const.LOGIN_MENBER,member);
                if ("1".equals(rememberme)){
                    String loginCode="\"loginacct="+member.getLoginacct()+"&userpwd="+member.getUserpswd()+"&logintype=member\"";
                    Cookie c=new Cookie("logincode",loginCode);
                    c.setMaxAge(60*60*24*14);
                    c.setPath("/");
                    response.addCookie(c);
                }
            }else {
                return false;
            }

            result.setData(type);
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
