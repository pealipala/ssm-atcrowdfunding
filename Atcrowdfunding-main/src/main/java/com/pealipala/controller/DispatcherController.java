package com.pealipala.controller;

import com.pealipala.bean.Member;
import com.pealipala.bean.Permission;
import com.pealipala.bean.User;
import com.pealipala.potal.service.MemberService;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.utils.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/forget")
    public String forget(){
        return "forget";
    }

    //加密密码本
    String key = "123222!@#$%";// 相当于密码本

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
            paramMap.put("userpswd", DesUtil.encrypt(userpswd,key));
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

    /**
     * * 异步请求---注册账号
     * 1 验证用户名是否存在
     * 2 验证密码格式
     * @author : yechaoze
     * @date : 2019/9/4 10:25
     * @param loginacct :
     * @param userpswd :
     * @param type :
     * @param email :
     * @param realname :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doReg")
    public Object doReg(String loginacct, String userpswd, String type, String email,String realname){
        AjaxResult result=new AjaxResult();
        try {
            //根据账户类型 查询是否有同名账户
            if (type.equals("user")){
                User user=userService.seleceLogin(loginacct);
                if (user!=null){

                }
            }else{
                Member member=memberService.selectLogin(loginacct);
                if (member!=null){
                    result.setMessage("账号名已存在请重新输入");
                    result.setSuccess(false);
                    return result;
                }
            }

            //验证用户名 邮箱 真实姓名 密码 格式
            if (!this.verifyLoginacct(loginacct)){
                result.setMessage("账号名格式有误,请重新输入");
                result.setSuccess(false);
                return result;
            }
            if (!this.verifyuserpswd(userpswd)){
                result.setMessage("密码格式有误,请重新输入");
                result.setSuccess(false);
                return result;
            }
            if (!this.verifyEmail(email)){
                result.setMessage("邮箱格式有误,请重新输入");
                result.setSuccess(false);
                return result;
            }
            if (!this.verifyRealname(realname)){
                result.setMessage("真实姓名有误,请重新输入");
                result.setSuccess(false);
                return result;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strdate = format.format(new Date());
            //验证通过
            if (type.equals("user")){
                User regUser=new User();
                regUser.setLoginacct(loginacct);
                regUser.setUserpswd(DesUtil.encrypt(userpswd,key));
                regUser.setEmail(email);
                regUser.setCreatetime(strdate);
                regUser.setUsername(realname);
                int line=userService.regUser(regUser);
                if (line>0){
                    result.setMessage("注册成功");
                    result.setSuccess(true);
                }
            }else {
                Member member=new Member();
                member.setUsername(realname);
                member.setLoginacct(loginacct);
                member.setUserpswd(DesUtil.encrypt(userpswd,key));
                member.setEmail(email);
                member.setRealname(realname);
                member.setAuthstatus("0");
                member.setUsertype("0");
                int line=memberService.insert(member);
                if (line>0){
                    result.setMessage("注册成功");
                    result.setSuccess(true);
                }
            }
        } catch (Exception e) {
            result.setMessage("注册失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 验证账户名格式
     * @author : yechaoze
     * @date : 2019/9/4 10:02
     * @param loginacct :
     * @return : boolean
     */
    private boolean verifyLoginacct(String loginacct){
        Pattern p=Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}");
        Matcher m=p.matcher(loginacct);
        boolean matches = m.matches();
        return  matches;
    }

    /**
     * 验证密码格式
     * @author : yechaoze
     * @date : 2019/9/4 10:05
     * @param userpswd :
     * @return : boolean
     */
    private boolean verifyuserpswd(String userpswd){
        Pattern p=Pattern.compile("[a-zA-Z0-9]{1,16}");
        Matcher m=p.matcher(userpswd);
        boolean matches = m.matches();
        return  matches;
    }

    /**
     * 验证邮箱格式
     * @author : yechaoze
     * @date : 2019/9/4 10:05
     * @param email :
     * @return : boolean
     */
    private boolean verifyEmail(String email){
        Pattern p=Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m=p.matcher(email);
        boolean matches = m.matches();
        return  matches;
    }

    /**
     * 验证真实姓名
     * @author : yechaoze
     * @date : 2019/9/4 10:26
     * @param realname :
     * @return : boolean
     */
    private boolean verifyRealname(String realname){
        Pattern p=Pattern.compile("^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$");
        Matcher m=p.matcher(realname);
        boolean matches = m.matches();
        return  matches;
    }

    /**
     * 忘记密码-->邮箱获取密码
     * @author : yechaoze
     * @date : 2019/9/8 13:11
     * @param loginacct :
     * @param email :
     * @param type :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doForget")
    public Object doForget(String loginacct,String email,String type){
        AjaxResult result=new AjaxResult();
        try {
            //判断账户类型-->验证账号名是否存在
            if (type.equals("user")){
                User user=userService.selectForget(loginacct,email);
                if (user==null){
                    result.setMessage("账户名与邮箱不匹配");
                    result.setSuccess(false);
                    return result;
                }
                String emailMessage=user.getUserpswd();
                this.sendPassword(emailMessage,email);
                result.setSuccess(true);
                return result;
            }else if (type.equals("member")){
                Member member=memberService.selectForget(loginacct,email);
                if (member==null){
                    result.setMessage("账户名与邮箱不匹配");
                    result.setSuccess(false);
                    return result;
                }
                String emailMessage=member.getUserpswd();
                this.sendPassword(emailMessage,email);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            result.setMessage("验证邮箱或者账号名失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return true;
    }

    /**
     *  使用spring邮箱发送器发送密码
     * @author : yechaoze
     * @date : 2019/9/8 13:27
     * @param message :
     * @param toEmailAddress :
     * @return : void
     */
    private void sendPassword(String message,String toEmailAddress){
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setSubject("标题");
            StringBuilder content = new StringBuilder();

            String s = DesUtil.decrypt(message,key);

            content.append("<a href='http://www.atcrowdfunding.com/login.htm'>password:"+s+"</a>");
            helper.setText(content.toString(), true);
            helper.setFrom("admin@pealipala.cn");
            helper.setTo(toEmailAddress);
            javaMailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
