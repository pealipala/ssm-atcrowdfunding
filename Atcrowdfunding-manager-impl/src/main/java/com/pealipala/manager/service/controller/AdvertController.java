package com.pealipala.manager.service.controller;

import com.pealipala.bean.Advertisement;
import com.pealipala.bean.User;
import com.pealipala.manager.service.AdvertService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Const;
import com.pealipala.utils.Page;
import com.pealipala.utils.StringUtil;
import com.pealipala.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    @RequestMapping("/add")
    public String add(){
        return "advert/add";
    }

    @RequestMapping("/index")
    public String index(){
        return "advert/index";
    }

    @RequestMapping("/edit")
    public String edit( Integer id, Model model ) {

        // 根据主键查询资质信息
        Advertisement advert = advertService.queryById(id);
        model.addAttribute("advert", advert);
        return "advert/edit";
    }
    /**
     * 异步分页查询数据
     * @author : yechaoze
     * @date : 2019/8/15 22:26
     * @param pageno :
     * @param pagesize :
     * @param queryText :
     * @return : java.lang.Object
     */
    @RequestMapping("/pageQuery")
    @ResponseBody
    public Object pageQuery(Integer pageno,Integer pagesize,String pagetext){
        AjaxResult result=new AjaxResult();
        try {
            Map<String,Object> paramMap=new HashMap<String,Object>();
            paramMap.put("pageno",pageno);
            paramMap.put("pagesize",pagesize);
            if ( StringUtil.isNotEmpty(pagetext) ) {
                pagetext = pagetext.replaceAll("%", "\\\\%");
            }
            paramMap.put("pagetext",pagetext);
            Page page=advertService.queryAllAdv(paramMap);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setMessage("查询广告列表失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("/doAdd")
    @ResponseBody
    public Object doAdd(HttpSession session, HttpServletRequest request, Advertisement advertisement){
        AjaxResult result=new AjaxResult();
        try {
            MultipartHttpServletRequest mreq= (MultipartHttpServletRequest) request;
            MultipartFile file = mreq.getFile("advpic");
            String filename = file.getOriginalFilename();
            String extname = filename.substring(filename.lastIndexOf("."));
            String iconpath= UUID.randomUUID().toString()+extname;
            ServletContext application = session.getServletContext();
            User user  = (User) session.getAttribute(Const.LOGIN_USER);
            String path = application.getRealPath("pics")+ "\\adv\\"+iconpath;
            file.transferTo(new File(path));//保存文件到该路径
            advertisement.setUserid(user.getId());
            advertisement.setStatus("1");
            advertisement.setIconpath(iconpath);
            int count = advertService.insertAdvert(advertisement);
            result.setSuccess(count==1);
        } catch (Exception e) {
            result.setMessage("添加广告失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除单个广告
     * @author : yechaoze
     * @date : 2019/8/15 23:29
     * @param id :
     * @return : java.lang.Object
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Integer id){
        AjaxResult result=new AjaxResult();
        try {
            int count=advertService.deleteByid(id);
            if (count==1){
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("广告删除失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除多个广告
     * @author : yechaoze
     * @date : 2019/8/15 23:34
     * @param datas :
     * @return : java.lang.Object
     */
    @RequestMapping("/batchDelete")
    @ResponseBody
    public Object batchDelete(Data datas){
        AjaxResult result=new AjaxResult();
        try {
            int count=advertService.batchDeleteByid(datas);
            if (count==datas.getDatas().size()){
                result.setSuccess(true);
            }else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("广告删除失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
