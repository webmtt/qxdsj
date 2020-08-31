<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>
    <title>后台管理迁移</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.css">
	<style type="text/css">
	.liebiao_1{
	 background: none repeat scroll 0 0 #fafafa;
	}
	.liebiao_2{
 	background: none repeat scroll 0 0 #e0e8ff; 
	}
	.head{
	background: none repeat scroll 0 0 #e0e8ff;
	}
	#content{
		width:1000px;
		height:auto;
		margin-left: 15%;
		margin-right: 15%;
	}
	.list{
		margin: auto;
		padding: auto;
	}
	</style>
  </head>
  
  <body>
  <div id="main" >
		<div id="header" class="navbar navbar-fixed-top">
		<div class="navbar-inner-head" id="div1" >            
  </div>
  <div id="content" style="margin-top: 10px;">
  <center style="margin-bottom: 10px;"><font style="font-weight: bold;font-size: 20px;">业务内网3.0后台管理业务模块迁移</font></center>
    <table class="list">
    	<thead class="head">
    	<th>项目名</th>
    	<th>功能名称</th>
    	<th>链接</th>
    	</thead>
    	<tbody>
    		<tr class="liebiao_1">
    			<td>nmps</td>
    			<td>菜单管理</td>
    			<td><a href="http://10.1.64.154/nmps/mpsnadmin/mpsn/login.do?returnUrl=/mpsnadmin/mpsn/index.do" target="_black">http://10.1.64.154/nmps/mpsnadmin/mpsn/login.do?returnUrl=/mpsnadmin/mpsn/index.do</a>
    			</td>   			
    		</tr>
    		<tr class="liebiao_2">
    			<td>nmsn</td>
    			<td>后台管理</td>
    			<td><a href="http://10.1.64.154/nmsn/manage.action" target="_black">http://10.1.64.154/nmsn/manage.action</a></td>
    		</tr>
    		<tr class="liebiao_1">
    			<td>mdms</td>
    			<td>系统菜单</td>
    			<td><a href="http://10.1.64.154/mdms/jsp_manage.index " target="_black">http://10.1.64.154/mdms/jsp_manage.index </a></td>
    		</tr>
    		<tr class="liebiao_2">
    			<td>home</td>
    			<td>CMACast节目表</td>
    			<td><a href="http://10.1.64.154/home/jsp-CMAmanage.index " target="_black">http://10.1.64.154/home/jsp-CMAmanage.index </a></td>
    		</tr>
    		<tr class="liebiao_1" >
    			<td>home</td>
    			<td>业务通报产品上传</td>
    			<td><a href="http://10.1.64.154/home/upload.jsp" target="_black">http://10.1.64.154/home/upload.jsp</a></td>
    		</tr>
    		<tr class="liebiao_2">
    			<td>home</td>
    			<td>系统菜单</td>
    			<td><a href="http://10.1.64.154/home/jsp-homeManage.index" target="_black">http://10.1.64.154/home/jsp-homeManage.index</a></td>
    		</tr>
    		<tr class="liebiao_2">
    			<td>BM</td>
    			<td>BM管理</td>
    			<td><a href="http://10.1.64.154/bm/web-manage.index" target="_black">http://10.1.64.154/bm/web-manage.index</a></td>
    		</tr> 
    	</tbody>
    </table>
    </div>
  </body>
</html>
