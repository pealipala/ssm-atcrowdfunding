package com.pealipala.manager.service.controller;

import com.pealipala.bean.Role;
import com.pealipala.bean.User;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.utils.Page;
import com.pealipala.utils.StringUtil;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(){
        return "user/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
        User user=userService.queryUserById(id);
        map.put("user",user);
        return "user/update";
    }

    /**
     * 显示用户已经分配的角色
     * @author : yechaoze
     * @date : 2019/8/11 13:45
     * @param id :
     * @param paramMap :
     * @return : java.lang.String
     */
    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map paramMap){
        List<Role> allRole=userService.queryAllRole();//全部的权限
        List<Integer> userRole=userService.queryRoleByUserId(id);//拥有的权限
        List leftRole=new ArrayList();//左侧权限 未拥有
        List rightRole=new ArrayList();//右侧权限 已拥有
        for (Role list:allRole) {
            if (userRole.contains(list.getId())){
                rightRole.add(list);
            }else {
                leftRole.add(list);
            }
        }
        paramMap.put("leftRole",leftRole);
        paramMap.put("rightRole",rightRole);

        return "user/assignrole";
    }

    /**
     * 异步请求方式 --- 查询所有用户
     * @author : yechaoze
     * @date : 2019/8/8 18:08
     * @param pageno :
     * @param pagesize :
     * @return : java.lang.String
     */
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                        @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
                        String queryText){

        AjaxResult result=new AjaxResult();

        try {
            Map<String,Object> paramMap=new HashMap<String,Object>();
            paramMap.put("pageno",pageno);
            paramMap.put("pagesize",pagesize);
            if (StringUtil.isNotEmpty(queryText)){
                if (queryText.contains("%")){
                    queryText = queryText.replaceAll("%", "\\\\%");
                }
                paramMap.put("queryText",queryText);
            }
            Page page = userService.queryPage(paramMap);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询失败");
        }

        return result;
    }

//    同步请求方式
//    @RequestMapping("/index")
//    public String index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageNo,
//                        @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pageSize, Map map){
//
//        Page page = userService.queryPage(pageNo,pageSize);
//        map.put("page", page);
//        return "user/index";
//    }

    /**
     * 异步请求方式 ---- 添加用户
     * @author : yechaoze
     * @date : 2019/8/9 11:30
     * @param user :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object index(User user){

        AjaxResult result=new AjaxResult();

        try {
            int count = userService.saveUser(user);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("添加用户失败");
        }

        return result;
    }

    /**
     * 异步请求方式 ----- 修改用户
     * @author : yechaoze
     * @date : 2019/8/9 13:01
     * @param user :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(User user,HttpSession session){

        AjaxResult result=new AjaxResult();
        try {
            int count = userService.upDateUser(user);
            session.setAttribute(Const.LOGIN_USER, user);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("修改用户失败");
        }

        return result;
    }

    /**
     * 异步请求 ---- 删除操作
     * @author : yechaoze
     * @date : 2019/8/9 13:55
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id,HttpSession session){

        AjaxResult result=new AjaxResult();
        try {
            User sessionUser = (User) session.getAttribute(Const.LOGIN_USER);
            Integer sessionUserId = sessionUser.getId();
            if (id.equals(sessionUserId)){
                result.setSuccess(false);
                result.setMessage("不可删除当前登录用户");
                return result;
            }
            int count = userService.deleteUser(id);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除用户失败");
        }
        return result;
    }


//    /**
//     * 异步请求 --- 批量删除用户
//     * @author : yechaoze
//     * @date : 2019/8/9 15:16
//     * @param id :
//     * @return : java.lang.Object
//     */
//    @ResponseBody
//    @RequestMapping("/doDeleteBatch")
//    public Object doDeleteBatch(Integer[] id,HttpSession session){
//
//        AjaxResult result=new AjaxResult();
//        try {
//            User sessionUser = (User) session.getAttribute(Const.LOGIN_USER);
//            Integer sessionUserId = sessionUser.getId();
//            for (Integer selectid:id) {
//                if (selectid.equals(sessionUserId)){
//                    result.setSuccess(false);
//                    result.setMessage("不可删除当前登录用户");
//                    return result;
//                }
//            }
//            int count = userService.deleteUserBatch(id);
//            result.setSuccess(count==id.length);
//        } catch (Exception e) {
//            result.setSuccess(false);
//            e.printStackTrace();
//            result.setMessage("删除用户失败");
//        }
//        return result;
//    }
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Data data, HttpSession session){

        AjaxResult result=new AjaxResult();
        try {
            User sessionUser = (User) session.getAttribute(Const.LOGIN_USER);
            Integer sessionUserId = sessionUser.getId();
            List<User> userList = data.getDatas();
            for (User list:userList) {
                if (list.getId().equals(sessionUserId)){
                    result.setSuccess(false);
                    result.setMessage("不可删除当前登录用户");
                    return result;
                }
            }
            int count = userService.deleteUserBatchByVO(data);
            result.setSuccess(count==data.getDatas().size());
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除用户失败");
        }
        return result;
    }

    /**
     * 异步请求 --- 用户角色添加
     * @author : yechaoze
     * @date : 2019/8/11 15:23
     * @param userid :
     * @param data :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid,Data data){

        AjaxResult result=new AjaxResult();
        try {
            userService.saveUserRoleRelationship(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("添加用户角色失败");
        }
        return result;
    }

    /**
     * 异步请求 --- 用户角色删除
     * @author : yechaoze
     * @date : 2019/8/11 15:27
     * @param userid :
     * @param data :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object unAssignRole(Integer userid,Data data){

        AjaxResult result=new AjaxResult();
        try {
            userService.deleteUserRoleRelationship(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除用户角色失败");
        }
        return result;
    }


}
