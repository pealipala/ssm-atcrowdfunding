package com.pealipala.manager.service.controller;

import com.pealipala.bean.Member;
import com.pealipala.manager.service.AuthCertService;
import com.pealipala.potal.service.MemberService;
import com.pealipala.potal.service.TicketService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
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
@RequestMapping("/authcert")
public class AuthCertController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private MemberService memberService;
    @RequestMapping("/index")
    public String index(){
        return "authcert/index";
    }

    /**
     * 查询验证页面
     * @author : yechaoze
     * @date : 2019/8/20 19:02
     * @param pageno :
     * @param pagesize :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                            @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize){
        AjaxResult result=new AjaxResult();
        try {
            Page page=new Page(pageno,pagesize);
            //1.查询后台backuser委托组的任务
            TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("auth").taskCandidateGroup("backuser");
            List<Task> taskList = taskQuery.listPage(page.getStartIndex(), pagesize);
            List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
            for (Task task : taskList) {
                Map map=new HashMap();
                map.put("taskid", task.getId());
                map.put("taskName", task.getName());
                //2.根据任务查询流程定义(流程定义名称,流程定义版本)
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
                map.put("procDefName", processDefinition.getName());
                map.put("procDefVersion", processDefinition.getVersion());
                //3.根据任务查询流程实例(根据流程实例的id查询流程单,查询用户信息)
                Member member = ticketService.getMemberByPiid(task.getProcessInstanceId());
                map.put("member",member);
                data.add(map);
            }
            page.setTotalsize((int) taskQuery.count());
            page.setData(data);
            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("查询页面失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 显示资质
     * @author : yechaoze
     * @date : 2019/8/20 21:14
     * @param memberid :
     * @param map :
     * @return : java.lang.String
     */
    @RequestMapping("/show")
    public String show(Integer memberid,Map<String,Object> map){

        Member member = memberService.getMemberById(memberid);

        List<Map<String,Object>> list = memberService.queryCertByMemberid(memberid);

        map.put("member", member);
        map.put("certimgs", list);

        return "authcert/show";
    }


    /**
     * 审批通过
     * @author : yechaoze
     * @date : 2019/8/20 21:17
     * @param taskid :
     * @param memberid :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/pass")
    public Object pass(String taskid,Integer memberid){
        AjaxResult result=new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", true);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * 审批拒绝
     * @author : yechaoze
     * @date : 2019/8/20 21:18
     * @param taskid :
     * @param memberid :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(String taskid,Integer memberid){
        AjaxResult result=new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", false);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

}
