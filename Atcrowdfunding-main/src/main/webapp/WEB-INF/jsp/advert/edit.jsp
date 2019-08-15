<%@page pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" href="${APP_PATH }/css/main.css">
	<link rel="stylesheet" href="${APP_PATH }/css/doc.min.css">
	<style>
	.tree li {
        list-style-type: none;
		cursor:pointer;
	}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 广告维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li style="padding-top:8px;">
				<%@include file="/WEB-INF/jsp/common/userinfo.jsp" %>
			</li>
            <li style="margin-left:10px;padding-top:8px;">
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
				<ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">数据列表</a></li>
				  <li class="active">修改</li>
				</ol>
			<div class="panel panel-default">
              <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
			  <div class="panel-body">
				<form id="updateForm">
				  <div class="form-group">
					<label for="name">广告名称</label>
					<input type="text" class="form-control" id="name" value="${advert.name }" placeholder="请输入广告名称">
				  </div>
				  <div class="form-group">
					<label for="url">广告地址</label>
					<input type="text" class="form-control" id="url" value="${advert.url }" placeholder="请输入广告地址">
				  </div>
				  <button id="updateBtn" type="button" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 修改</button>
				  <button id="resetBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
				</form>
			  </div>
			</div>
        </div>
      </div>
    </div>

    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script src="${APP_PATH }/jquery/layer/layer.js"></script>
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
            });
            $(function(){
            	$("#updateBtn").click(function(){
                	var loadingIndex = -1 ;
                	$.ajax({
                		
                		url : "${APP_PATH}/advert/update.do",
                		type : "POST",
                		data : {
                			name : $("#name").val(),
                			url : $("#url").val(),
                			id : ${advert.id}
                		},
                		beforeSend : function(){
                			loadingIndex = layer.msg('数据正在修改中', {icon: 6});
                			return true ;
                		},
                		success : function(result){
                			layer.close(loadingIndex);
                			if(result.success){
                				layer.msg("广告数据修改成功", {time:1000, icon:6});
                				window.location.href="${APP_PATH}/advert/index.htm?pageno=${param.pageno}";
                			}else{
                				layer.msg("广告数据修改失败", {time:1000, icon:5, shift:6});
                			} 			
                			
                		},
                		error : function(){
                			layer.msg("广告数据修改失败", {time:2000, icon:5, shift:6});
                		}
                		
                	});
                	
                });	
            	
            	
            	$("#resetBtn").click(function(){
            		$("#updateForm")[0].reset();
            	});
            	
            });
            
            
            
        </script>
  </body>
</html>
