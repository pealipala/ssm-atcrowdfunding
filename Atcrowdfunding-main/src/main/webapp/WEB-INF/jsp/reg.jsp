<%--
  Created by IntelliJ IDEA.
  User: 叶朝泽
  Date: 2019/8/5
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/login.css">
    <style>

    </style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="${APP_PATH}/index.htm" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">

    <form id="regForm" action="${APP_PATH}/doReg.do" class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户注册</h2>
        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="floginacct" name="loginacct" placeholder="请输入账号" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="frealname" name="realname" placeholder="请输入真实姓名" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="password" type="text" class="form-control" id="fuserpswd" name="userpswd" placeholder="请输入密码" style="margin-top:10px;">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="femail" name="email" placeholder="请输入邮箱地址" style="margin-top:10px;">
            <span class="glyphicon glyphicon glyphicon-envelope form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <select id="ftype" class="form-control" name="type">
                <option value="member">会员</option>
                <option value="user">管理</option>
            </select>
        </div>
        <div class="checkbox">
            <label>
                忘记密码
            </label>
            <label style="float:right">
                <a href="${APP_PATH }/login.htm">我有账号</a>
            </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="doreg()" > 注册</a>
    </form>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>
<script>
    function doreg() {

        var floginacct = $("#floginacct");
        var fuserpswd = $("#fuserpswd");
        var femail = $("#femail");
        var ftype = $("#ftype");
        var frealname = $("#frealname")


        //对于表单数据而言不能用null进行判断,如果文本框什么都不输入,获取的值是""
        if($.trim(floginacct.val()) == ""){
            //alert("用户账号不能为空,请重新输入!");
            layer.msg("用户账号不能为空,请重新输入!", {time:1000, icon:5, shift:6}, function(){
                floginacct.val("");
                floginacct.focus();
                //return false ;  //不能结束if,只是结束msg函数.
            });
            return false ;
        }

        var loadingIndex = -1 ;
        $.ajax({
            type : "POST",
            data : {
                loginacct : floginacct.val(),
                userpswd : fuserpswd.val(),
                email : femail.val(),
                type : ftype.val(),
                realname : frealname.val()
            },
            url : "${APP_PATH}/doReg.do",
            beforeSend : function(){
                loadingIndex = layer.msg('处理中', {icon: 16});
                //一般做表单数据校验.
                return true ;
            },
            success : function(result){ //{"success":true}  或    {"success":false,"message":"登录失败!"}
                layer.close(loadingIndex);
                if(result.success){
                        window.location.href="${APP_PATH}/login.htm";
                }else{
                    floginacct.val("");
                    femail.val("");
                    fuserpswd.val("");
                    frealname.val("");
                    floginacct.focus();
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            },
            error : function(){
                layer.msg("注册失败!", {time:1000, icon:5, shift:6});
            }
        });
    }
</script>
</body>
</html>