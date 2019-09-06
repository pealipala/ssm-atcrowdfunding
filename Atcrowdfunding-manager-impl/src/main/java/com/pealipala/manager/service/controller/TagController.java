package com.pealipala.manager.service.controller;

import com.pealipala.bean.Tag;
import com.pealipala.manager.service.TagService;
import com.pealipala.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping("/index")
    public String index(){
        return "tag/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "tag/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id, Model model){
        Tag tag=tagService.queryTag(id);
        model.addAttribute("tag",tag);
        return "tag/update";
    }


    /**
     * 使用Map组合父子关系 减少循环
     * @author : yechaoze
     * @date : 2019/9/6 20:00
     * @return : java.lang.Object
     */
    @RequestMapping("/loadData")
    @ResponseBody
    public Object loadData(){
        AjaxResult result=new AjaxResult();
        try {
            //创建根节点
            List<Tag> root=new ArrayList<Tag>();
            //查询所有tag
            List<Tag> tagList = tagService.queryAllTags();
            //存放所有tag的Map
            Map<Integer,Tag> map=new HashMap<Integer, Tag>();
            //遍历查询到的所有tags存放到map
            for (Tag tag : tagList) {
                map.put(tag.getId(),tag);
            }
            //遍历查询到的tags 判断是否有pid
            for (Tag childTag : tagList) {
                //通过子找父，pid为空的为空结点
                Tag child=childTag;
                if (child.getPid()==null){
                    root.add(child);
                }else {
                    Tag parent=map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }
            result.setData(root);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("标签加载失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 增加标签
     * @author : yechaoze
     * @date : 2019/9/6 23:32
     * @param name :
     * @param pid :
     * @param icon :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(String name,Integer pid,String icon){
        AjaxResult result=new AjaxResult();
        try {
            Tag newTag=new Tag();
            newTag.setPid(pid);
            newTag.setName(name);
            newTag.setIcon(icon);
            int line=tagService.addNewTag(newTag);
            if (line==1){
                result.setSuccess(true);
            }else {
                result.setMessage("增加标签数据失败");
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("增加标签数据失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 删除标签
     * @author : yechaoze
     * @date : 2019/9/6 23:43
     * @param id :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/deleteTag")
    public Object deleteTag(Integer id){
        AjaxResult result=new AjaxResult();
        try {
            int line=tagService.deleteTag(id);
            if (line==1){
                result.setSuccess(true);
            }else {
                result.setMessage("删除标签失败");
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("删除标签失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 修改标签
     * @author : yechaoze
     * @date : 2019/9/6 23:51
     * @param id :
     * @param name :
     * @param icon :
     * @return : java.lang.Object
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Integer id,String name,String icon){
        AjaxResult result=new AjaxResult();
        try {
            //为了获取当前tag的pid
            Tag currentTag = tagService.queryTag(id);
            Tag tag=new Tag();
            tag.setId(id);
            tag.setName(name);
            tag.setIcon(icon);
            tag.setPid(currentTag.getPid());
            int line=tagService.updateTag(tag);
            if (line==1){
                result.setSuccess(true);
            }else {
                result.setMessage("修改标签数据失败");
                result.setSuccess(false);
            }
        } catch (Exception e) {
            result.setMessage("修改标签数据失败");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
