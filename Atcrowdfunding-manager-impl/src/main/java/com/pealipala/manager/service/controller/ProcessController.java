package com.pealipala.manager.service.controller;

import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Page;
import com.sun.org.apache.regexp.internal.RE;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @RequestMapping("/showImg")
    public String showImg(){
        return "process/showimg";
    }

    /**
     * 流程定义查询
     * @author : yechaoze
     * @date : 2019/8/18 21:14
     * @param pageno :
     * @param pagesize :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                          @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize){
        AjaxResult result=new AjaxResult();
        try {
            Page page=new Page(pageno,pagesize);
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询流程定义集合数据 可能出现自关联，导致jackson组件无法将集合序列化为json串
            List<ProcessDefinition> list = processDefinitionQuery.listPage(page.getStartIndex(), pagesize);
            List<Map<String,Object>> myList=new ArrayList<Map<String, Object>>();
            for (ProcessDefinition processDefinition:list) {
                Map<String,Object> pd=new HashMap<String,Object>();
                pd.put("id",processDefinition.getId());
                pd.put("name",processDefinition.getName());
                pd.put("key",processDefinition.getKey());
                pd.put("version",processDefinition.getVersion());
                myList.add(pd);
            }
            long count = processDefinitionQuery.count();
            page.setData(myList);
            page.setTotalsize((int) count);
            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("查询流程定义失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 流程部署
     * @author : yechaoze
     * @date : 2019/8/18 21:14
     * @param request :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/deploy")
    public Object deploy(HttpServletRequest request){
        AjaxResult result=new AjaxResult();
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartHttpServletRequest.getFile("processDefFile");
            repositoryService.createDeployment().addInputStream(multipartFile.getOriginalFilename(),multipartFile.getInputStream()).deploy();
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("流程部署失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 流程定义删除
     * @author : yechaoze
     * @date : 2019/8/18 21:14
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(String id){//流程定义 根据流程定义使用部署id删除
        AjaxResult result=new AjaxResult();
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);//true表示级联删除
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("删除流程定义失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 显示流程定义图片
     * @author : yechaoze
     * @date : 2019/8/18 21:31
     * @param id :
     * @param response :
     * @return : null
     */
    @ResponseBody
    @RequestMapping("/showImgProDef")
    public void showImgProDef(String id,HttpServletResponse response) throws IOException {//流程定义 根据流程定义使用部署id删除
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();//根据id查
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());//使用DeploymentId以及DiagramResourceName
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(resourceAsStream,outputStream);
    }



}
