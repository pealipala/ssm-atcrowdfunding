<%--
  Created by IntelliJ IDEA.
  User: 叶朝泽
  Date: 2019/8/13
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet"
          href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/main.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }

        table tbody tr:nth-child(odd) {
            background: #F4F4F4;
        }

        table tbody td:nth-child(even) {
            color: #C00;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div>
                <a class="navbar-brand" style="font-size: 32px;" href="#">众筹平台
                    - 角色维护</a>
            </div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li style="padding-top: 8px;">
                    <%@include file="/WEB-INF/jsp/common/userinfo.jsp"%>
                </li>
                <li style="margin-left: 10px; padding-top: 8px;">
                    <button type="button" class="btn btn-default btn-danger">
                        <span class="glyphicon glyphicon-question-sign"></span> 帮助
                    </button>
                </li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <%@include file="/WEB-INF/jsp/common/menu.jsp" %>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <i class="glyphicon glyphicon-th"></i> 数据列表
                    </h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" name="form" style="float: left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="queryText" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning" onclick="queryRole()">
                            <i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchDelete" type="button" class="btn btn-danger"
                            style="float: right; margin-left: 10px;">
                        <i class="glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary"
                            style="float: right;" onclick="window.location.href='${APP_PATH}/role/add.htm'">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear: both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>name</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <ul class="pagination">


                                    </ul>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script src="${APP_PATH}/jquery/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });

        <c:if test="${empty param.pageno}">
        queryPage(1);
        </c:if>
        <c:if test="${not empty param.pageno}">
        queryPage(${param.pageno});
        </c:if>

//        showMenu();
    });

    function changePageno( pageno ) {
        //window.location.href = "<%=request.getContextPath() %>/role/index.htm?pageno="+pageno;
        queryPage(pageno);
    }

    //使用Ajax异步查询数据
    function queryPage( pageno ){
        var dataObj = {
            "pageno" : pageno ,   //pageno 是属性名称,是否增加双引号无所谓
            "pagesize" : 3
        };
        if(condition){
            dataObj.queryText = $("#queryText").val(); //增加模糊查询条件
        }

        var loadingIndex = -1 ;
        $.ajax({

            url : "${APP_PATH}/role/pageQuery.do",
            type : "post",
            data : dataObj,
            beforeSend : function(){
                loadingIndex = layer.msg('数据查询中', {icon: 6});
                return true ;
            },
            success : function(result){
                //显示结果
                layer.close(loadingIndex);
                if(result.success){
                    //循环遍历,显示数据
                    var pageObj = result.page;
                    var roleList = pageObj.data ;
                    var content = "";

                    $.each(roleList,function(i,n){

                        content+="<tr>";
                        content+="	<td>"+(i+1)+"</td>";
                        content+="	<td><input type='checkbox' value='"+n.id+"'></td>";
                        content+="	<td>"+n.name+"</td>";
                        content+="	<td>";
                        content+="		<button type='button' onclick='window.location.href=\"${APP_PATH}/role/assignPermission.htm?roleid="+n.id+"\"' class='btn btn-success btn-xs'>";
                        content+="			<i class=' glyphicon glyphicon-check'></i>";
                        content+="		</button>";
                        content+="		<button type='button' onclick='window.location.href=\"${APP_PATH}/role/edit.htm?pageno="+pageObj.pageno+"&id="+n.id+"\"' class='btn btn-primary btn-xs'>";
                        content+="			<i class=' glyphicon glyphicon-pencil'></i>";
                        content+="		</button>";
                        content+="		<button type='button' class='btn btn-danger btn-xs' onclick='deleteRole("+n.id+",\""+n.name+"\")'>";
                        content+="			<i class=' glyphicon glyphicon-remove'></i>";
                        content+="		</button>";
                        content+="	</td>";
                        content+="</tr>";
                        //$("tbody").append(content);
                        $("tbody").html(content);
                    });

                    //拼接导航条
                    var pageContent = "";

                    if(pageObj.pageno!=1){
                        pageContent += '<li><a href="#" onclick="changePageno('+(pageObj.pageno-1)+')">上一页</a></li>';
                    }

                    for(var i=1 ; i<=pageObj.totalno ; i++){
                        if(i==pageObj.pageno){
                            pageContent += '<li class="active"><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

                        }else{
                            pageContent += '<li><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

                        }
                    }

                    if(pageObj.pageno!=pageObj.totalno){
                        pageContent += '<li><a href="#" onclick="changePageno('+(pageObj.pageno+1)+')">下一页</a></li>';
                    }

                    $(".pagination").html(pageContent);

                }else{

                    layer.msg("角色分页查询数据失败", {time:1000, icon:5, shift:6});

                }
            },
            error : function(){
                layer.msg("角色分页查询数据错误", {time:1000, icon:5, shift:6});
            }

        });
    }

    var condition = false ;
    //条件查询
    function queryRole(){
        var queryText = $("#queryText");

        if(queryText.val()==""){
            layer.msg("查询条件不能为空", {time:1000, icon:5, shift:6}, function(){
                queryText.focus();
            });
            return ;
        }
        condition = true ;
        queryPage(1);
    }

    function deleteRole(uid,name){

        layer.confirm("是否要删除角色?",  {icon: 3, title:'提示'}, function(cindex){

            $.ajax({
                url : "${APP_PATH}/role/delete.do",
                type : "POST",
                data : {uid:uid},
                success : function(result){
                    if(result.success){
                        layer.msg("删除"+name+"角色成功!", {time:1000, icon:6}, function(){
                            queryPage(1);
                        });
                    }else{
                        layer.msg("删除"+name+"角色失败!", {time:1000, icon:5, shift:6});
                    }
                }
            });

            layer.close(cindex);
        }, function(cindex){
            layer.close(cindex);
        });

    }


    $(".table thead :checkbox").click(function(){
        var checked = this.checked ;
        var checkboxList = $(".table tbody :checkbox");
        $.each(checkboxList,function(i,n){
            n.checked = checked ;
        });
    });
    //传递多个对象的方式
    $("#batchDelete").click(function(){
        var checkedList = $("table tbody input:checked");
        if(checkedList.length>0){
            layer.confirm("确定要删除吗?",  {icon: 3, title:'提示'}, function(cindex){

                var datas = {};

                $.each(checkedList,function(i,n){

                    datas["datas["+i+"].id"]=n.value;
                    datas["datas["+i+"].name"]="111";
                });

                $.ajax({
                    url : "${APP_PATH}/role/batchDelete.do",
                    type : "POST",
                    data : datas ,
                    success : function(result){
                        if(result.success){
                            layer.msg("删除角色成功!", {time:1000, icon:6}, function(){
                                queryPage(1);
                            });
                        }else{
                            layer.msg("删除角色失败!", {time:1000, icon:5});
                        }
                    }
                });
                layer.close(cindex);
            }, function(cindex){
                layer.close(cindex);
            });
        }
    });


</script>
<script type="text/javascript" src="${APP_PATH }/script/menu.js"></script>

</body>

</html>