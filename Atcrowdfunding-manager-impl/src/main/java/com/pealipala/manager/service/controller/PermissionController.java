package com.pealipala.manager.service.controller;

import com.pealipala.bean.Permission;
import com.pealipala.manager.service.PermissionService;
import com.pealipala.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        return "permission/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
        Permission permission=permissionService.getPermissionById(id);
        map.put("permission",permission);
        return "permission/update";
    }

    //zTree demo1 数据写死
    /**
     * zTree demo2 数据库查询数据
     * @author : yechaoze
     * @date : 2019/8/12 11:51
     * @return : java.lang.Object
     */
//    @ResponseBody
//    @RequestMapping("/loadData")
//    public Object loadData(){
//        AjaxResult result = new AjaxResult();
//        try {
//            List<Permission> root = new ArrayList<Permission>();
//            //父
//            Permission permission = permissionService.getRootPermission();
//            root.add(permission);
//            //子
//            List<Permission> children = permissionService.getChildByPid(permission.getId());
//            //父子关系
//            permission.setChildren(children);
//            for (Permission child:children) {
//                List<Permission> innerChildren = permissionService.getChildByPid(child.getId());
//                child.setChildren(innerChildren);
//            }
//            result.setData(root);
//            result.setSuccess(true);
//        } catch (Exception e) {
//            result.setSuccess(false);
//            e.printStackTrace();
//            result.setMessage("权限查询失败");
//        }
//        return result;
//    }

    /**
     * zTree demo3 使用递归
     * @author : yechaoze
     * @date : 2019/8/12 15:10
     * @return : java.lang.Object
     */
//    @ResponseBody
//    @RequestMapping("/loadData")
//    public Object loadData(){
//        AjaxResult result = new AjaxResult();
//        try {
//            List<Permission> root = new ArrayList<Permission>();
//            //父
//            Permission permission = permissionService.getRootPermission();
//            root.add(permission);
//            //子
//            queryPermissions(permission);
//
//            result.setSuccess(true);
//            result.setData(root);
//        } catch (Exception e) {
//            result.setSuccess(false);
//            e.printStackTrace();
//            result.setMessage("权限查询失败");
//        }
//        return result;
//    }

//    private void queryPermissions(Permission permission){
//        List<Permission> child = permissionService.getChildByPid(permission.getId());
//        //组合父子关系
//        permission.setChildren(child);
//        for (Permission innerChild:child) {
//            queryPermissions(innerChild);
//        }
//    }

    /**
     * demo4 -- 一次性加载全部数据 减少与数据库的交互次数
     * @author : yechaoze
     * @date : 2019/8/12 15:31
     * @return : java.lang.Object
     */
//    @ResponseBody
//    @RequestMapping("/loadData")
//    public Object loadData(){
//        AjaxResult result = new AjaxResult();
//
//        try {
//            List<Permission> root = new ArrayList<Permission>();
//
//
//            List<Permission> childrendPermissons =  permissionService.queryAllPermission();
//            for (Permission permission : childrendPermissons) {
//                //通过子查找父
//                //子菜单
//                Permission child = permission ; //假设为子菜单
//                if(child.getPid() == null ){
//                    root.add(permission);
//                }else{
//                    //父节点
//                    for (Permission innerPermission : childrendPermissons) {
//                        if(child.getPid() == innerPermission.getId()){
//                            Permission parent = innerPermission;
//                            parent.getChildren().add(child);
//                            break ; //跳出内层循环,如果跳出外层循环,需要使用标签跳出
//                        }
//                    }
//                }
//            }
//
//
//
//            result.setSuccess(true);
//            result.setData(root);
//
//        } catch (Exception e) {
//            result.setSuccess(false);
//            result.setMessage("加载许可树数据失败!");
//        }
//
//
//        return result ;
//    }

    /**
     * Demo5 - 用Map集合来查找父,来组合父子关系.减少循环的次数 ,提高性能.
     * @author : yechaoze
     * @date : 2019/8/12 16:04
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxResult result = new AjaxResult();

        try {
            List<Permission> root = new ArrayList<Permission>();

            List<Permission> childPermission =  permissionService.queryAllPermission();

            Map<Integer,Permission> map=new HashMap<Integer, Permission>();

            for (Permission innerPermission : childPermission) {
                map.put(innerPermission.getId(),innerPermission);
            }

            for (Permission permission : childPermission) {
                //通过子查找父
                //子菜单
                Permission child = permission ; //假设为子菜单
                if(child.getPid() == null ){
                    root.add(permission);
                }else{
                    //父节点
                    Permission parent = map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }

            result.setSuccess(true);
            result.setData(root);

        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("加载许可树数据失败!");
        }


        return result ;
    }

    /**
     * 许可树添加节点
     * @author : yechaoze
     * @date : 2019/8/13 16:14
     * @param permission :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Permission permission){
        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.savePermission(permission);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("保存许可数据失败");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Permission permission){
        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.updatePermission(permission);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("修改许可数据失败");
        }
        return result;
    }

    /**
     * 删除许可树节点
     * @author : yechaoze
     * @date : 2019/8/13 16:47
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/deletePermission")
    public Object deletePermission(Integer id){
        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.deletePermissionById(id);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("删除许可数据失败");
        }
        return result;
    }
}
