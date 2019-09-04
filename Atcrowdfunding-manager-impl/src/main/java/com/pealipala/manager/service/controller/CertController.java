package com.pealipala.manager.service.controller;

import com.pealipala.bean.Cert;
import com.pealipala.manager.service.CertService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Page;
import com.pealipala.utils.StringUtil;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cert")
public class CertController {
    @Autowired
    private CertService certService;

    @RequestMapping("/index")
    public String index(){
        return "cert/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "cert/add";
    }

    @RequestMapping("/edit")
    public String edit(Integer id,Model model){
        // 根据主键查询资质信息
        Cert cert = certService.queryById(id);
        model.addAttribute("cert", cert);
        return "cert/edit";
    }

    /**
     * 异步查询 所有的资质
     * @author : yechaoze
     * @date : 2019/9/4 15:48
     * @param pageno :
     * @param pagesize :
     * @param queryText :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                            @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
                            String queryText){
        AjaxResult result=new AjaxResult();
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("pageno",pageno);
            map.put("pagesize",pagesize);
            if (StringUtil.isNotEmpty(queryText)){
                if (queryText.contains("%")){
                    queryText = queryText.replaceAll("%", "\\\\%");
                }
                map.put("queryText",queryText);
            }
            //执行页面查询
            Page<Cert> page=certService.pageQuery(map);
            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("查询资质失败");
            e.printStackTrace();
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 新增资质
     * @author : yechaoze
     * @date : 2019/9/4 16:17
     * @param name :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/insert")
    public Object insert(String name){
        AjaxResult result=new AjaxResult();
        try {
            Cert cert=new Cert();
            cert.setName(name);
            //执行插入 返回结果等于1即成功
            int line=certService.insert(cert);
            if (line==1){
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("添加资质失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除资质
     * @author : yechaoze
     * @date : 2019/9/4 16:26
     * @param id :
     * @param name :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/delete")
    public  Object delete(Integer id,String name){
        AjaxResult result=new AjaxResult();
        try {
            Cert cert=new Cert();
            cert.setId(id);
            cert.setName(name);
            //返回影响的行数 等于1 为成功
            int line=certService.delete(cert);
            if (line==1){
                result.setSuccess(true);
            }else {
            result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("删除资质失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除多个资质
     * @author : yechaoze
     * @date : 2019/9/4 16:39
     * @param datas :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/deletes")
    public Object deletes(Data datas){
        AjaxResult result=new AjaxResult();
        try {
            //返回结果为数据中的条数为成功
            int line=certService.deletes(datas);
            if (line==datas.getDatas().size()){
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("删除多个资质失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改资质
     * @author : yechaoze
     * @date : 2019/9/4 16:49
     * @param id :
     * @param name :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/update")
    public Object update(Integer id,String name){
        AjaxResult result=new AjaxResult();
        try {
            Cert cert=new Cert();
            cert.setId(id);
            cert.setName(name);
            int line=certService.update(cert);
            if (line==1){
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("修改资质失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
