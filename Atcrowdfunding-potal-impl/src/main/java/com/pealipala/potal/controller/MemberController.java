package com.pealipala.potal.controller;

import com.pealipala.bean.Cert;
import com.pealipala.bean.Member;
import com.pealipala.bean.MemberCert;
import com.pealipala.bean.Ticket;
import com.pealipala.manager.service.CertService;
import com.pealipala.potal.listener.PassListener;
import com.pealipala.potal.listener.RefuseListener;
import com.pealipala.potal.service.MemberService;
import com.pealipala.potal.service.TicketService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.vo.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CertService certService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @RequestMapping("/accttype")
    public String accttype(){
        return "member/accttype";
    }

    @RequestMapping("/basicinfo")
    public String basicinfo(){
        return "member/basicinfo";
    }

    @RequestMapping("/uploadCert")
    public String uploadCert(){
        return "member/uploadCert";
    }

    @RequestMapping("/checkemail")
    public String checkemail(){
        return "member/checkemail";
    }

    @RequestMapping("/checkauthcode")
    public String checkauthcode(){
        return "member/checkauthcode";
    }

    /**
     * 根据id查询流程步骤判断显示页面
     * @author : yechaoze
     * @date : 2019/8/19 18:00
     * @param session :
     * @return : java.lang.String
     */
    @RequestMapping("/apply")
    public String apply(HttpSession session){
		Member member= (Member) session.getAttribute(Const.LOGIN_MENBER);
		Ticket ticket = ticketService.getTicketByMemberId(member.getId()) ;

		if(ticket == null ){
            ticket  = new Ticket(); //封装数据
            ticket.setMemberid(member.getId());
            ticket.setPstep("apply");
            ticket.setStatus("0");
			ticketService.saveTicket(ticket);

		}else{
			String pstep = ticket.getPstep();

			if("accttype".equals(pstep)){

				return "redirect:/member/basicinfo.htm";
			} else if("basicinfo".equals(pstep)){
                //根据当前用户查询账户类型,然后根据账户类型查找需要上传的资质
                List<Cert> queryCertByAccttype = certService.queryCertByAccttype(member.getAccttype());
                session.setAttribute("queryCertByAccttype", queryCertByAccttype);
				return "redirect:/member/uploadCert.htm";
			} else if("uploadcert".equals(pstep)){

                return "redirect:/member/checkemail.htm";
            }else if("checkemail".equals(pstep)){

                return "redirect:/member/checkauthcode.htm";
            }
		}

        return "member/accttype";
    }

    /**
     * 认证申请
     * @author : yechaoze
     * @date : 2019/8/19 15:29
     * @param session :
     * @param accttype :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/updateAcctType")
    public Object updateAcctType(HttpSession session,String accttype){
        AjaxResult result=new AjaxResult();
        try {
            Member member= (Member) session.getAttribute(Const.LOGIN_MENBER);
            member.setAccttype(accttype);
            //更新账户类型
            memberService.updateAcctType(member);

            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(member.getId()) ;
            ticket.setPstep("accttype");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("认证申请失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 添加认证信息
     * @author : yechaoze
     * @date : 2019/8/19 16:49
     * @param session :
     * @param member :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/updateBasicinfo")
    public Object updateBasicinfo(HttpSession session,Member member){
        AjaxResult result=new AjaxResult();
        try {
            Member loginMember= (Member) session.getAttribute(Const.LOGIN_MENBER);
            //添加信息
            loginMember.setRealname(member.getRealname());
            loginMember.setCardnum(member.getCardnum());
            loginMember.setTel(member.getTel());
            memberService.updateBasicInfo(loginMember);

            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            ticket.setPstep("basicinfo");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("认证信息添加失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 上传资质图片
     * @author : yechaoze
     * @date : 2019/8/19 22:08
     * @param data :
     * @param session :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert(Data data,HttpSession session){
        AjaxResult result=new AjaxResult();
        try {
            Member loginMember= (Member) session.getAttribute(Const.LOGIN_MENBER);
            //添加信息
            List<MemberCert> certimgs = data.getCertimgs();
            String realPath = session.getServletContext().getRealPath("/pics");
            for (MemberCert certimg : certimgs) {
                MultipartFile fileImg = certimg.getFileImg();
                String extName = fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));
                String tmpName= UUID.randomUUID().toString()+extName;
                String fileName=realPath+"/cert" +"/"+tmpName;
                fileImg.transferTo(new File(fileName));
                certimg.setMemberid(loginMember.getId());
                certimg.setIconpath(tmpName);
            }
            // 保存会员与资质关系数据.
            certService.saveMemberCert(certimgs);

            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            ticket.setPstep("uploadcert");
            ticketService.updatePstep(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("资质图片上传失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 邮箱验证
     * @author : yechaoze
     * @date : 2019/8/19 23:15
     * @param email :
     * @param session :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess(String email,HttpSession session){
        AjaxResult result=new AjaxResult();
        try {
            Member loginMember= (Member) session.getAttribute(Const.LOGIN_MENBER);
            //如果用户输入了新的邮箱，将旧的邮箱替换
            if (!loginMember.getEmail().equals(email)){
                loginMember.setEmail(email);
                memberService.updateEmail(loginMember);
            }
            //生成验证码
            StringBuilder authcode=new StringBuilder();
            for (int i=1;i<=4;i++){
                authcode.append(new Random().nextInt(10));
            }
            //启动实名认证流程，系统自动发送邮件 验证是否合法
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();
            //准备变量 toEmail loginacct authcode flag(审核实名认证时提供) passListener refuseListener
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("toEmail","test@pealipala.cn");
            map.put("loginacct",loginMember.getLoginacct());
            map.put("authcode",authcode);
            map.put("passListener",new PassListener());
            map.put("refuseListener",new RefuseListener());

            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), map);
            //记录流程步骤:
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            ticket.setPstep("checkemail");
            ticket.setPiid(processInstance.getId());
            ticket.setAuthcode(authcode.toString());
            ticketService.updatePstepAndPiid(ticket);

            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("获取邮箱失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 验证码提交
     * @author : yechaoze
     * @date : 2019/8/20 0:30
     * @param authcode :
     * @param session :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply(String authcode,HttpSession session){
        AjaxResult result=new AjaxResult();
        try {
            Member loginMember= (Member) session.getAttribute(Const.LOGIN_MENBER);
            Ticket ticket = ticketService.getTicketByMemberId(loginMember.getId()) ;
            if (ticket.getAuthcode().equals(authcode)){
                //让当前系统用户完成验证码审核任务
                Task task = taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
                taskService.complete(task.getId());
                //记录流程步骤:
                ticket.setPstep("finishapply");
                //更新用户申请状态
                loginMember.setAuthstatus("1");
                memberService.updateAuthStatus(loginMember);
            }else {
                result.setSuccess(false);
                result.setMessage("验证码不正确,请重新获取");
            }

            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("验证失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
