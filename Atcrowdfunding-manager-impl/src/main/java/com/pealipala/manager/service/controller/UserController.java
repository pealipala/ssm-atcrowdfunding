package com.pealipala.manager.service.controller;

import com.pealipala.manager.service.UserService;
import com.pealipala.utils.AjaxResult;
import com.pealipala.utils.Page;
import com.pealipala.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "user/index";
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
    @RequestMapping("/index")
    public Object index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
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

}
