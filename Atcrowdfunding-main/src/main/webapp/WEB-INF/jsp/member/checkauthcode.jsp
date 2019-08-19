<%--
  Created by IntelliJ IDEA.
  User: 叶朝泽
  Date: 2019/8/20
  Time: 0:21
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
    <meta name="author" content="">
    <link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/theme.css">
    <style>
        #footer {
            padding: 15px 0;
            background: #fff;
            border-top: 1px solid #ddd;
            text-align: center;
        }
    </style>
</head>
<body>
<%@include file="/WEB-INF/jsp/common/member-top.jsp"%>
<div class="container theme-showcase" role="main">
    <div class="page-header">
        <h1>实名认证 - 申请</h1>
    </div>

    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" ><a href="#"><span class="badge">1</span> 基本信息</a></li>
        <li role="presentation" ><a href="#"><span class="badge">2</span> 资质文件上传</a></li>
        <li role="presentation" ><a href="#"><span class="badge">3</span> 邮箱确认</a></li>
        <li role="presentation" class="active"><a href="#"><span class="badge">4</span> 申请确认</a></li>
    </ul>

    <form role="form" style="margin-top:20px;">
        <div class="form-group">
            <label for="authcode">验证码</label>
            <input type="text" class="form-control" id="authcode" placeholder="请输入你邮箱中接收到的验证码">
        </div>
        <button type="button" onclick="javascript:;" class="btn btn-primary">重新发送验证码</button>
        <button type="button" id="finishBtn"  class="btn btn-success">申请完成</button>
    </form>
    <hr>
</div> <!-- /container -->
<div class="container" style="margin-top:20px;">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div id="footer">
                <div class="footerNav">
                    <a rel="nofollow" href="http://www.atguigu.com">关于我们</a> | <a rel="nofollow" href="http://www.atguigu.com">服务条款</a> | <a rel="nofollow" href="http://www.atguigu.com">免责声明</a> | <a rel="nofollow" href="http://www.atguigu.com">网站地图</a> | <a rel="nofollow" href="http://www.atguigu.com">联系我们</a>
                </div>
                <div class="copyRight">
                    Copyright ?2017-2017 atguigu.com 版权所有
                </div>
            </div>

        </div>
    </div>
</div>
<script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH }/script/docs.min.js"></script>
<script>
    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    });


    $("#finishBtn").click(function(){
        $.ajax({
            type : "POST",
            url  : "${APP_PATH}/member/finishApply.do",
            data : {
                authcode : $("#authcode").val()
            },
            success : function(result) {
                if ( result.success ) {
                    window.location.href = "${APP_PATH}/member.htm";
                } else {
                    var msg = "实名认证申请失败";
                    if ( result.message ) {
                        msg = result.message;
                    }
                    layer.msg(msg, {time:1000, icon:5, shift:6});
                }
            }
        });
    });


</script>
</body>
</html>
