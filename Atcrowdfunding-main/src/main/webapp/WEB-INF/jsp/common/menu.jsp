<%--
  Created by IntelliJ IDEA.
  User: 叶朝泽
  Date: 2019/8/10
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul style="padding-left:0px;" class="list-group">
    <li class="list-group-item tree-closed">
        <span><i class="glyphicon glyphicon glyphicon-tasks"></i> 权限管理 <span class="badge" style="float:right">3</span></span>
        <ul style="margin-top:10px;display:none;">
            <li style="height:30px;">
                <a href="${APP_PATH }/user/index.htm"><i class="glyphicon glyphicon-user"></i> 用户维护</a>
            </li>
            <li style="height:30px;">
                <a href="${APP_PATH }/role/index.htm"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
            </li>
            <li style="height:30px;">
                <a href="${APP_PATH }/permission/index.htm"><i class="glyphicon glyphicon-lock"></i> 许可维护</a>
            </li>
        </ul>
    </li>
    <li class="list-group-item tree-closed">
        <span><i class="glyphicon glyphicon-ok"></i> 业务审核 <span class="badge" style="float:right">3</span></span>
        <ul style="margin-top:10px;display:none;">
            <li style="height:30px;">
                <a href="auth_cert.html"><i class="glyphicon glyphicon-check"></i> 实名认证审核</a>
            </li>
            <li style="height:30px;">
                <a href="auth_adv.html"><i class="glyphicon glyphicon-check"></i> 广告审核</a>
            </li>
            <li style="height:30px;">
                <a href="auth_project.html"><i class="glyphicon glyphicon-check"></i> 项目审核</a>
            </li>
        </ul>
    </li>
    <li class="list-group-item tree-closed">
        <span><i class="glyphicon glyphicon-th-large"></i> 业务管理 <span class="badge" style="float:right">7</span></span>
        <ul style="margin-top:10px;display:none;">
            <li style="height:30px;">
                <a href="${APP_PATH }/cert/index.htm"><i class="glyphicon glyphicon-picture"></i> 资质维护</a>
            </li>
            <li style="height:30px;">
                <a href="${APP_PATH }/type/index.htm"><i class="glyphicon glyphicon-equalizer"></i> 分类管理</a>
            </li>
            <li style="height:30px;">
                <a href="process.html"><i class="glyphicon glyphicon-random"></i> 流程管理</a>
            </li>
            <li style="height:30px;">
                <a href="${APP_PATH }/advert/index.htm"><i class="glyphicon glyphicon-hdd"></i> 广告管理</a>
            </li>
            <li style="height:30px;">
                <a href="message.html"><i class="glyphicon glyphicon-comment"></i> 消息模板</a>
            </li>
            <li style="height:30px;">
                <a href="${APP_PATH }/projectType/index.htm"><i class="glyphicon glyphicon-list"></i> 项目分类</a>
            </li>
            <li style="height:30px;">
                <a href="tag.html"><i class="glyphicon glyphicon-tags"></i> 项目标签</a>
            </li>
        </ul>
    </li>
    <li class="list-group-item tree-closed" >
        <a href="${APP_PATH }/param/index.htm"><i class="glyphicon glyphicon-list-alt"></i> 参数管理</a>
    </li>

</ul>

