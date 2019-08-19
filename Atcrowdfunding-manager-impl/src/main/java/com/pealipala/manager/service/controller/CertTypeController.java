package com.pealipala.manager.service.controller;

import com.pealipala.bean.AccountTypeCert;
import com.pealipala.bean.Cert;
import com.pealipala.manager.service.CertService;
import com.pealipala.manager.service.CertTypeService;
import com.pealipala.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/certtype")
public class CertTypeController {
    @Autowired
    private CertTypeService certTypeService;
    @Autowired
    private CertService certService;

    /**
     * 分类管理显示
     * @author : yechaoze
     * @date : 2019/8/19 20:40
     * @param map :
     * @return : java.lang.String
     */
    @RequestMapping("/index")
    public String index(Map<String,Object> map){
        List<Cert> alllist=certService.queryAllCert();
        map.put("allCert",alllist);
        //获取资质关系
        List<Map<String,Object>> certAccttypeList=certTypeService.queryAccountTypeCert();
        map.put("certAccttypeList",certAccttypeList);
        return "certtype/index";
    }

    /**
     * 勾选复选框完成添加操作
     * @author : yechaoze
     * @date : 2019/8/19 20:41
     * @param certid :
     * @param accttype :
     * @return : java.lang.Object
     */
    @RequestMapping("/insertAcctTypeCert")
    @ResponseBody
    public Object insertAcctTypeCert(Integer certid,String accttype){
        AjaxResult result=new AjaxResult();
        try {
            AccountTypeCert accountTypeCert=new AccountTypeCert();
            accountTypeCert.setCertid(certid);
            accountTypeCert.setAccttype(accttype);
            int count = certTypeService.insertAcctTypeCert(accountTypeCert);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setMessage("添加失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 取消复选框表示删除
     * @author : yechaoze
     * @date : 2019/8/19 20:47
     * @param certid :
     * @param accttype :
     * @return : java.lang.Object
     */
    @RequestMapping("/deleteAcctTypeCert")
    @ResponseBody
    public Object deleteAcctTypeCert(Integer certid,String accttype){
        AjaxResult result=new AjaxResult();
        try {
            int count = certTypeService.deleteAcctTypeCert(certid,accttype);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setMessage("删除失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
