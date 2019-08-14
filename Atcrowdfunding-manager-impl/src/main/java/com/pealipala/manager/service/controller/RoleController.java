package com.pealipala.manager.service.controller;

import com.pealipala.bean.Permission;
import com.pealipala.bean.Role;
import com.pealipala.controller.BaseController;
import com.pealipala.manager.service.PermissionService;
import com.pealipala.manager.service.RoleService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Page;
import com.pealipala.utils.StringUtil;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        return "role/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "role/add";
    }

    @RequestMapping("/edit")
    public String edit(Integer id,Map map){
        Role role = roleService.selectRoleById(id);
        map.put("role",role);
        return "role/edit";
    }

    @RequestMapping("/assignPermission")
    public String assignPermission(){
        return "role/assignPermission";
    }
    /**
     * 异步分页查询
     * @author : yechaoze
     * @date : 2019/8/13 22:18
     * @param queryText :
     * @param pageno :
     * @param pagesize :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(String queryText,@RequestParam(required = false, defaultValue = "1") Integer pageno,
                            @RequestParam(required = false, defaultValue = "2") Integer pagesize){
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pageno", pageno); // 空指针异常
            paramMap.put("pagesize", pagesize);

            if(StringUtil.isNotEmpty(queryText)){
                queryText = queryText.replaceAll("%", "\\\\%"); //斜线本身需要转译
                System.out.println("--------------"+queryText);
            }

            paramMap.put("queryText", queryText);

            // 分页查询数据
            Page<Role> rolePage = roleService.pageQuery(paramMap);

            result.setPage(rolePage);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }

    /**
     * 添加角色
     * @author : yechaoze
     * @date : 2019/8/13 22:21
     * @param role :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Role role){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.saveRole(role);
            result.setSuccess(count==1);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }

    /**
     * 更新role
     * @author : yechaoze
     * @date : 2019/8/13 22:28
     * @param role :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doEdit")
    public Object doEdit(Role role){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.updateRole(role);
            result.setSuccess(count==1);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }

    /**
     * 删除role
     * @author : yechaoze
     * @date : 2019/8/13 22:30
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(Integer uid) {
        AjaxResult result = new AjaxResult();
        try {
            System.out.println(uid);
            int count = roleService.deleteRole(uid);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除多个role
     * @author : yechaoze
     * @date : 2019/8/13 23:32
     * @param datas :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/batchDelete")
    public Object batchDelete(Data datas) {
        AjaxResult result = new AjaxResult();
        try {
            int count=roleService.batchDeleteRole(datas);
            result.setSuccess(count==datas.getDatas().size());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 查询角色获得的权限
     * @author : yechaoze
     * @date : 2019/8/14 15:40
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/loadDataAsync")
    public Object loadDataAsync(Integer roleid) {
        List<Permission> root = new ArrayList<Permission>();

        List<Permission> childredPermissons =  permissionService.queryAllPermission();


        //根据角色id查询该角色之前所分配过的许可.
        List<Integer> permissonIdsForRoleid = permissionService.queryPermissionidsByRoleid(roleid);

        Map<Integer,Permission> map = new HashMap<Integer,Permission>();//100

        for (Permission innerpermission : childredPermissons) {
            map.put(innerpermission.getId(), innerpermission);

            if(permissonIdsForRoleid.contains(innerpermission.getId())){
                innerpermission.setChecked(true);
            }
        }


        for (Permission permission : childredPermissons) { //100
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


        return root ;
    }

    /**
     * 分配许可
     * @author : yechaoze
     * @date : 2019/8/14 22:50
     * @param roleid :
     * @param ids :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAssignPermission")
    public Object doAssignPermission(Integer roleid,Data ids) {
        AjaxResult result = new AjaxResult();
        try {
            int count=roleService.savePermissionRelationship(roleid,ids);
            result.setSuccess(count==ids.getIds().size());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

}
